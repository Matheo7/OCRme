package com.ashomok.ocrme.crop_image.tab_fragments.simple;

import com.ashomok.ocrme.di_dagger.BasePresenter;

public interface SimpleCropContract {
    interface View { }
    interface Presenter extends BasePresenter<SimpleCropContract.View> { }
}
