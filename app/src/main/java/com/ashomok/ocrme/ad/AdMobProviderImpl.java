package com.ashomok.ocrme.ad;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.StringRes;

import com.ashomok.ocrme.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import javax.inject.Inject;

import io.reactivex.Observable;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

/**
 * Created by iuliia on 7/26/16.
 */


public class AdMobProviderImpl implements AdProvider {

    private static final String TAG = DEV_TAG + AdMobProviderImpl.class.getSimpleName();
    private final Context context;
    private final int adid;

    @Inject
    public AdMobProviderImpl(Context context, @StringRes int adid) {
        this.context = context;
        this.adid = adid;
        String appId = context.getResources().getString(R.string.app_id);
        MobileAds.initialize(context, appId);
    }


//    /**
//     * async upload photo to firebase storage if not yet uploaded
//     */
//    public Single<String> uploadPhoto() {
//        if (imageUrl != null) {
//            return Single.just(imageUrl);
//        } else {
//
//            return Single.create(emitter -> {
//                String uuid = UUID.randomUUID().toString();
//
//                //generate image ref
//                String lastPathSegment = imageUri.getLastPathSegment();
//                mImageRef = FirebaseStorage.getInstance().getReference().child(
//                        "ocr_request_images/" + uuid + lastPathSegment);
//
//
//                //scale bitmap if needed and upload
//                InputStream imageStream = null;
//                try {
//                    imageStream = getContentResolver().openInputStream(imageUri);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                Bitmap.CompressFormat compressFormat = getCompressFormat(lastPathSegment);
//                if (compressFormat == null){
//                    emitter.onError(new Throwable("Unknown image extension"));
//                }else {
//
//                    byte[] byteArray = toBytes(BitmapFactory.decodeStream(imageStream), compressFormat);
//                    mImageRef
//                            .putBytes(byteArray)
//                            .addOnSuccessListener(taskSnapshot -> {
//                                String path = taskSnapshot.getMetadata().getReference().getPath();
//                                Log.d(TAG, "uploadPhoto:onSuccess:" + path);
//                                String gcsImageUrl = BuildConfig.FIREBASE_FOLDER_URL + path;
//                                emitter.onSuccess(gcsImageUrl);
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.e(TAG, "uploadPhoto:onError", e);
//                                emitter.onError(e);
//                            });
//                }
//            });
//        }
//    }

    @Override
    public Observable<UnifiedNativeAd> loadNativeAdsAsync() {

        String appId = context.getResources().getString(R.string.app_id);
        MobileAds.initialize(context, appId);

        return Observable.create(emitter -> {

            AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(adid));

            builder.forUnifiedNativeAd(unifiedNativeAd -> {
                emitter.onNext(unifiedNativeAd);
                Log.d(TAG, "native add loaded: " + unifiedNativeAd.toString());
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

        });

//        if (adLoader.isLoading()) {
//            // The AdLoader is still loading ads.
//            // Expect more adLoaded or onAdFailedToLoad callbacks.
//        } else {
//            // The AdLoader has finished loading ads.
//        }


        // Dispose the subscription when not interested in the emitted data any more
//        disposable.dispose();


    }


//    public Set<UnifiedNativeAd> loadNativeAdsAsync() {
//
//        Set<UnifiedNativeAd> result = new HashSet<>();
//
//        String appId = context.getResources().getString(R.string.app_id);
//        MobileAds.initialize(context, appId);
//
//        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(adid));
//
//        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//            @Override
//            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                result.add(unifiedNativeAd);
//                Log.d(TAG, "native add loaded, total set size = " + result.size());
//            }
//        });
//
//        VideoOptions videoOptions = new VideoOptions.Builder()
//                .setStartMuted(true)
//                .build();
//
//        NativeAdOptions adOptions = new NativeAdOptions.Builder()
//                .setVideoOptions(videoOptions)
//                .build();
//
//        builder.withNativeAdOptions(adOptions);
//        builder.withAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//
//                Log.d(TAG, "onAdFailedToLoad,error code " + errorCode);
//            }
//        });
//        AdLoader adLoader = builder.build();
//        adLoader.loadAds(new AdRequest.Builder().build(), 5);
//        return result;
//    }


    @Override
    public UnifiedNativeAd loadNativeAdAsync() {
        //todo
        return null;
    }

    /**
     * add bottom banner on the parent view. Note: It may overlay some views.
     *
     * @param parent
     */
    private void addBottomBanner(ViewGroup parent) {
        if (parent instanceof RelativeLayout || parent instanceof LinearLayout) {
            AdView adView = new AdView(context);
            if (parent instanceof RelativeLayout) {

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                adView.setLayoutParams(lp);
            } else if (parent instanceof LinearLayout) {
                adView.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f));

            }

            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(context.getResources().getString(adid));
            adView.setId(R.id.ad_banner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            parent.addView(adView);
        } else {
            Log.e(TAG, "Ads can not been loaded programmaticaly. " +
                    "RelativeLayout and LinearLayout are supported as parent.");
        }
    }

    /**
     * init ad with bottom banner. Note: It may overlay some view.
     *
     * @param parentLayout
     */
    @Override
    public void initBottomBannerAd(ViewGroup parentLayout) {
        if (context.getResources().getConfiguration().orientation ==
                android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            //init banner
            addBottomBanner(parentLayout);
        }
    }
}
