package com.ashomok.ocrme.crop_image;

import com.ashomok.ocrme.crop_image.tab_fragments.simple.SimpleCropContract;
import com.ashomok.ocrme.crop_image.tab_fragments.simple.SimpleCropFragment;
import com.ashomok.ocrme.crop_image.tab_fragments.simple.SimpleCropPresenter;
import com.ashomok.ocrme.ocr_result.tab_fragments.image_pdf.ImagePdfContract;
import com.ashomok.ocrme.ocr_result.tab_fragments.image_pdf.ImagePdfFragment;
import com.ashomok.ocrme.ocr_result.tab_fragments.image_pdf.ImagePdfPresenter;
import com.ashomok.ocrme.ocr_result.tab_fragments.searchable_pdf.SearchablePdfContract;
import com.ashomok.ocrme.ocr_result.tab_fragments.searchable_pdf.SearchablePdfFragment;
import com.ashomok.ocrme.ocr_result.tab_fragments.searchable_pdf.SearchablePdfPresenter;
import com.ashomok.ocrme.ocr_result.tab_fragments.text.TextContract;
import com.ashomok.ocrme.ocr_result.tab_fragments.text.TextFragment;
import com.ashomok.ocrme.ocr_result.tab_fragments.text.TextPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CropImageModule {

    @Binds
    abstract CropImageContract.Presenter cropImagePresenter(CropImagePresenter presenter);

    @Binds
    abstract SimpleCropContract.Presenter searchablePdfPresenter(SimpleCropPresenter presenter);

    @ContributesAndroidInjector
    abstract SimpleCropFragment simpleCropFragment();
}
