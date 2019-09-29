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

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;

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
