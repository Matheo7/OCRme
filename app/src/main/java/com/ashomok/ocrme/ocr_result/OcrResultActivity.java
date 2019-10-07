package com.ashomok.ocrme.ocr_result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.ashomok.ocrme.R;
import com.ashomok.ocrme.ocr.ocr_task.OcrResponse;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import com.ashomok.ocrme.utils.LogHelper;

/**
 * Created by iuliia on 5/30/17.
 */
public class OcrResultActivity
        extends DaggerAppCompatActivity implements OcrResultContract.View {
    public static final String EXTRA_OCR_RESPONSE = "com.ashomokdev.imagetotext.OCR_RESPONCE";
    public static final String EXTRA_ERROR_MESSAGE = "com.ashomokdev.imagetotext.ERROR_MESSAGE";
    private static final String TAG = LogHelper.makeLogTag(OcrResultActivity.class);

    @Inject
    OcrResultContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result);
        initToolbar();

        Intent intent = getIntent();
        String errorMessage = intent.getStringExtra(EXTRA_ERROR_MESSAGE);

        if (errorMessage != null && errorMessage.length() > 0) {
            showError(errorMessage);
        } else {
            OcrResponse ocrData = null;
            if (intent.getExtras() != null) {
                ocrData = (OcrResponse) intent.getExtras()
                        .getSerializable(EXTRA_OCR_RESPONSE);
            }
            if (ocrData != null) {
                if (ocrData.getStatus().equals(OcrResponse.Status.OK)) {
                    initTabLayout(ocrData);
                } else {
                    showError(ocrData.getStatus());
                }
            }

        }

        mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();  //prevent leaking activity in
        // case presenter is orchestrating a long running task
    }

    private void showError(OcrResponse.Status status) {
        String errorMessage = getString(R.string.unknown_error);
        switch (status) {
            case UNKNOWN_ERROR:
                break;
            case PDF_CAN_NOT_BE_CREATED_LANGUAGE_NOT_SUPPORTED:
                errorMessage = getString(R.string.language_not_supported);
                break;
            case TEXT_NOT_FOUND:
                errorMessage = getString(R.string.text_not_found);
                break;
            case INVALID_LANGUAGE_HINTS:
                errorMessage = getString(R.string.invalid_language);
                break;
            default:
                break;
        }
        showError(errorMessage);
    }

    @Override
    public void showError(String errorMessage) {
        View emptyResult = findViewById(R.id.empty_result_layout);
        emptyResult.setVisibility(View.VISIBLE);

        View resultView = findViewById(R.id.pager);
        resultView.setVisibility(View.GONE);

        TextView errorMessageView = emptyResult.findViewById(R.id.error_message);
        errorMessageView.setText(errorMessage);
    }

    @Override
    public void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd) {
        LogHelper.d(TAG, "on populateUnifiedNativeAdView");
        View adView = findViewById(R.id.native_ad);
        adView.setVisibility(View.VISIBLE);
        NativeAdViewHolder nativeAdViewHolder = new NativeAdViewHolder(adView);

//         The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) nativeAdViewHolder.adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getCallToAction() == null) {
            nativeAdViewHolder.adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            nativeAdViewHolder.adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdViewHolder.adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            nativeAdViewHolder.adView.getBodyView().setVisibility(View.GONE);
        } else {
            nativeAdViewHolder.adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdViewHolder.adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getIcon() == null) {
            nativeAdViewHolder.adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdViewHolder.adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            nativeAdViewHolder.adView.getIconView().setVisibility(View.VISIBLE);
        }

        nativeAdViewHolder.adView.setNativeAd(nativeAd);
    }


    private void initTabLayout(OcrResponse ocrData) {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.text)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.PDF)));

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.searchable_PDF)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), ocrData);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            //back btn pressed

            //save data if you need here
            finish();
        });
    }

    private static class NativeAdViewHolder {
        UnifiedNativeAdView adView;

        NativeAdViewHolder(View view) {
            adView = view.findViewById(R.id.native_ad);
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        }
    }
}
