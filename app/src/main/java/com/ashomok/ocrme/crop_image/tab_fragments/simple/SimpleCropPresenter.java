package com.ashomok.ocrme.crop_image.tab_fragments.simple;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;


//currently redundant - exist for clean architecture
public class SimpleCropPresenter implements SimpleCropContract.Presenter {

    public static final String TAG = DEV_TAG + SimpleCropPresenter.class.getSimpleName();

    @Nullable
    private SimpleCropContract.View view;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    SimpleCropPresenter() { }

    @Override
    public void takeView(SimpleCropContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}