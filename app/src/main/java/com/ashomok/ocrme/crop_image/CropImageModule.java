package com.ashomok.ocrme.crop_image;

import com.ashomok.ocrme.crop_image.tab_fragments.advanced.AdvacedCropContract;
import com.ashomok.ocrme.crop_image.tab_fragments.advanced.AdvancedCropFragment;
import com.ashomok.ocrme.crop_image.tab_fragments.advanced.AdvancedCropPresenter;
import com.ashomok.ocrme.crop_image.tab_fragments.simple.SimpleCropContract;
import com.ashomok.ocrme.crop_image.tab_fragments.simple.SimpleCropFragment;
import com.ashomok.ocrme.crop_image.tab_fragments.simple.SimpleCropPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CropImageModule {

    @Binds
    abstract CropImageContract.Presenter cropImagePresenter(CropImagePresenter presenter);

    @Binds
    abstract SimpleCropContract.Presenter simpleCropPresenter(SimpleCropPresenter presenter);

    @Binds
    abstract AdvacedCropContract.Presenter advancedCropPresenter(AdvancedCropPresenter presenter);

    @ContributesAndroidInjector
    abstract SimpleCropFragment simpleCropFragment();

    @ContributesAndroidInjector
    abstract AdvancedCropFragment advancedCropFragment();
}
