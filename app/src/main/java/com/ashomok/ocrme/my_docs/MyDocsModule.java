package com.ashomok.ocrme.my_docs;

/**
 * Created by iuliia on 12/27/17.
 */

import android.content.Context;

import androidx.annotation.StringRes;

import com.ashomok.ocrme.BuildConfig;
import com.ashomok.ocrme.R;
import com.ashomok.ocrme.ad.AdProvider;
import com.ashomok.ocrme.ad.AdMobProviderImpl;
import com.ashomok.ocrme.ad.NativeAdProviderImpl;
import com.ashomok.ocrme.my_docs.get_my_docs_task.MyDocsHttpClient;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link MyDocsPresenter}.
 */
@Module
public abstract class MyDocsModule {

    @Provides
    static MyDocsHttpClient provideMyDocsHttpClient() {
        return MyDocsHttpClient.getInstance();
    }

    @Provides
    static @StringRes
    int provideNativeAdId() {
        if (BuildConfig.DEBUG) {
            return R.string.test_native;
        } else {
            return R.string.my_docs_native;
        }
    }

    @Provides
    static NativeAdProviderImpl provideNativeAdProviderImpl(Context context, @StringRes int adMobId) {
        return new NativeAdProviderImpl(context, adMobId);
    }

    @Binds
    abstract MyDocsContract.Presenter myDocsPresenter(MyDocsPresenter presenter);
}
