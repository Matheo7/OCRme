package com.ashomok.ocrme.crop_image;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

public class CropImagePresenter implements CropImageContract.Presenter {
    public static final String TAG = DEV_TAG + CropImagePresenter.class.getSimpleName();
    @Nullable
    private CropImageContract.View view;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    CropImagePresenter() {
    }

    @Override
    public void takeView(CropImageContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void dropView() {
        view = null;
    }

    private void init() {
    }
}
