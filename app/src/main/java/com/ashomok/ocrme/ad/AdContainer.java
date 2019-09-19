package com.ashomok.ocrme.ad;

import android.view.ViewGroup;

import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.Set;

/**
 * Created by iuliia on 7/25/16.
 */
public interface AdContainer {
    Set<UnifiedNativeAd> loadNativeAdAsync();
    void initBottomBannerAd(ViewGroup parentLayout);
}
