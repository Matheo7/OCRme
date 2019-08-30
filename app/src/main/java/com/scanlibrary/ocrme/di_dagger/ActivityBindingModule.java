package com.scanlibrary.ocrme.di_dagger;

import com.scanlibrary.ocrme.get_more_requests.GetMoreRequestsActivity;
import com.scanlibrary.ocrme.get_more_requests.GetMoreRequestsModule;
import com.scanlibrary.ocrme.language_choser.LanguageOcrActivity;
import com.scanlibrary.ocrme.language_choser.LanguageOcrModule;
import com.scanlibrary.ocrme.main.MainActivity;
import com.scanlibrary.ocrme.main.MainModule;
import com.scanlibrary.ocrme.my_docs.MyDocsActivity;
import com.scanlibrary.ocrme.my_docs.MyDocsModule;
import com.scanlibrary.ocrme.ocr_result.OcrResultActivity;
import com.scanlibrary.ocrme.ocr_result.OcrResultModule;
import com.scanlibrary.ocrme.ocr_result.translate.TranslateActivity;
import com.scanlibrary.ocrme.ocr_result.translate.TranslateModule;
import com.scanlibrary.ocrme.update_to_premium.UpdateToPremiumActivity;
import com.scanlibrary.ocrme.update_to_premium.UpdateToPremiumModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector(modules = LanguageOcrModule.class)
    abstract LanguageOcrActivity languageOcrActivity();

    @ContributesAndroidInjector(modules = MyDocsModule.class)
    abstract MyDocsActivity myDocsActivity();

    @ContributesAndroidInjector(modules = UpdateToPremiumModule.class)
    abstract UpdateToPremiumActivity updateToPremiumActivity();

    @ContributesAndroidInjector(modules = TranslateModule.class)
    abstract TranslateActivity translateActivity();

    @ContributesAndroidInjector(modules = GetMoreRequestsModule.class)
    abstract GetMoreRequestsActivity getMoreRequestsActivity();

    @ContributesAndroidInjector(modules = OcrResultModule.class)
    abstract OcrResultActivity getOcrResultActivity();
}
