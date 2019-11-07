package com.ashomok.ocrme.ocr_result;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.ashomok.ocrme.R;
import com.ashomok.ocrme.Settings;
import com.ashomok.ocrme.ad.AdMobProvider;
import com.ashomok.ocrme.rate_app.RateAppAsker;
import com.ashomok.ocrme.rate_app.RateAppAskerCallback;

import javax.inject.Inject;

import com.ashomok.ocrme.utils.LogHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class OcrResultPresenter implements OcrResultContract.Presenter, RateAppAskerCallback {
    public static final String TAG = LogHelper.makeLogTag(OcrResultPresenter.class);
    private final AdMobProvider adMobProvider;
    @Nullable
    private OcrResultContract.View view;
    private RateAppAsker rateAppAsker;


    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    OcrResultPresenter(RateAppAsker rateAppAsker, AdMobProvider adMobProvider) {
        this.rateAppAsker = rateAppAsker;
        this.adMobProvider = adMobProvider;
    }

    @Override
    public void takeView(OcrResultContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void dropView() {
        view = null;
    }

    private void init() {
        rateAppAsker.init(this);
        showAdsIfNeeded();

    }

    private void showAdsIfNeeded() {
        if (Settings.isAdsActive) {
            if (view != null) {
                view.showAds(adMobProvider);
            }
        }
    }

    @Override
    public void showRateAppDialog(DialogFragment rateAppDialogFragment) {

        // Show after 3 seconds
        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            Activity activity = (Activity) view;
            if (activity != null) {
                rateAppDialogFragment.show(activity.getFragmentManager(), "rate_app_dialog");
            }
        };

        handler.postDelayed(runnable, 3000);
    }
}
