package com.ashomok.ocrme.ad;

import android.view.ViewGroup;

import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.Set;

import io.reactivex.Observable;

/**
 * Created by iuliia on 7/25/16.
 */
public interface AdProvider {

    Observable<UnifiedNativeAd> loadNativeAdsAsync();

    UnifiedNativeAd loadNativeAdAsync();
    void initBottomBannerAd(ViewGroup parentLayout);
}
