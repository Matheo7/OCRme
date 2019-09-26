package com.ashomok.ocrme.ocr_result;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ashomok.ocrme.Settings;
import com.ashomok.ocrme.rate_app.RateAppAsker;
import com.ashomok.ocrme.rate_app.RateAppAskerCallback;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import javax.inject.Inject;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

public class OcrResultPresenter implements OcrResultContract.Presenter, RateAppAskerCallback {
    public static final String TAG = DEV_TAG + OcrResultPresenter.class.getSimpleName();
    @Nullable
    private OcrResultContract.View view;
    private RateAppAsker rateAppAsker;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    OcrResultPresenter(RateAppAsker rateAppAsker) {
        this.rateAppAsker = rateAppAsker;
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

//    @Override
//    public void showAdsIfNeeded() {
//        if (Settings.isAdsActive) {
//            if (view != null) {
//                view.showAds();
//            }
//        }
//    }


    private void showAdsIfNeeded() {
        if (Settings.isAdsActive) {

            int adsPresentedCount = 0;
            for (Object item : dataList) {
                if (item instanceof UnifiedNativeAd) {
                    adsPresentedCount++;
                }
            }

            if (adsPresentedCount * 10 < dataList.size()){
                //add ads
                int random = (int )(Math.random() * 2 + 1); //1 or 2 random
                int position = dataList.size() - random;
                Log.d(TAG, "Native ad added to position: " + position+ " adsPresentedCount: "
                        + adsPresentedCount + " dataList size: " + dataList.size());

                UnifiedNativeAd adItem = nativeAdSet.iterator().next();
                dataList.add(position, adItem);
                nativeAdSet.remove(adItem);
                adapter.notifyDataSetChanged();
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
