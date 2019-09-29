package com.ashomok.ocrme.ad;

import android.content.Context;
import android.util.Log;

import androidx.annotation.StringRes;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

public class NativeAdProviderImpl extends AdMobProviderImpl {
    private Set<UnifiedNativeAd> nativeAds;
    private final Context context;
    private final int adid;

    private static final String TAG = DEV_TAG + NativeAdProviderImpl.class.getSimpleName();

    public Set<UnifiedNativeAd> getNativeAds() {
        return nativeAds;
    }

    @Inject
    public NativeAdProviderImpl(Context context, @StringRes int adid) {
        super(context, adid);
        this.context = context;
        this.adid = adid;
        nativeAds = loadNativeAdsAsync();
    }

    private Set<UnifiedNativeAd> loadNativeAdsAsync() {

        Set<UnifiedNativeAd> result = new HashSet<>();

        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(adid));

        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            result.add(unifiedNativeAd);
            Log.d(TAG, "native add loaded, total set size = " + result.size());
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.e(TAG, "onAdFailedToLoad,error code " + errorCode);
            }
        });
        AdLoader adLoader = builder.build();
        adLoader.loadAds(new AdRequest.Builder().build(), 5);
        return result;
    }

    public Single<UnifiedNativeAd> loadNativeAd() {

        return Single.create(emitter -> {

            AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(adid));

            builder.forUnifiedNativeAd(ad -> {
                        emitter.onSuccess(ad);
                        Log.d(TAG, "Native ad loaded: " + ad.toString());
                    }
            );

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    emitter.onError(new Throwable("onAdFailedToLoad,error code " + errorCode));
                    Log.e(TAG, "onAdFailedToLoad,error code " + errorCode);
                }
            });
            AdLoader adLoader = builder.build();
            adLoader.loadAd(new AdRequest.Builder().build());
        });
    }
}
