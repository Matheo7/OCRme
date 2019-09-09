package com.ashomok.ocrme.crop_image.tab_fragments.advanced;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;


//currently redundant - exist for clean architecture
public class AdvancedCropPresenter implements AdvacedCropContract.Presenter {

    public static final String TAG = DEV_TAG + AdvancedCropPresenter.class.getSimpleName();

    @Nullable
    private AdvacedCropContract.View view;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    AdvancedCropPresenter() { }

    @Override
    public void takeView(AdvacedCropContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}