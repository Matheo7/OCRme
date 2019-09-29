package com.ashomok.ocrme.ocr_result;

import com.ashomok.ocrme.di_dagger.BasePresenter;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public interface OcrResultContract {
    interface View {
        void showError(String message);
        void populateUnifiedNativeAdView(UnifiedNativeAd ad);
    }

    interface Presenter extends BasePresenter<View> {
    }
}

