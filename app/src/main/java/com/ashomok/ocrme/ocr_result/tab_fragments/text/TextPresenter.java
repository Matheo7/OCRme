package com.ashomok.ocrme.ocr_result.tab_fragments.text;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import com.ashomok.ocrme.utils.LogHelper;


//currently redundant - exist for clean architecture
public class TextPresenter implements TextContract.Presenter {

    public static final String TAG = LogHelper.makeLogTag(TextPresenter.class);

    @Nullable
    private TextContract.View view;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    TextPresenter() { }

    @Override
    public void takeView(TextContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}