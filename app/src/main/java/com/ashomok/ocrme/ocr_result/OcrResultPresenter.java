package com.ashomok.ocrme.ocr_result;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ashomok.ocrme.Settings;
import com.ashomok.ocrme.ad.NativeAdProviderImpl;
import com.ashomok.ocrme.rate_app.RateAppAsker;
import com.ashomok.ocrme.rate_app.RateAppAskerCallback;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

public class OcrResultPresenter implements OcrResultContract.Presenter, RateAppAskerCallback {
    public static final String TAG = DEV_TAG + OcrResultPresenter.class.getSimpleName();
    @Nullable
    private OcrResultContract.View view;
    private RateAppAsker rateAppAsker;
    private NativeAdProviderImpl adProvider;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    OcrResultPresenter(RateAppAsker rateAppAsker, NativeAdProviderImpl adProvider) {
        this.rateAppAsker = rateAppAsker;
        this.adProvider = adProvider;
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
            adProvider.loadNativeAd()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            ad -> {
                                if (view != null) {
                                    view.populateUnifiedNativeAdView(ad);
                                }
                            }, throwable -> {
                                Log.e(TAG, throwable.getMessage());
                            });
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
