package com.scanlibrary.ocrme.my_docs;

/**
 * Created by iuliia on 12/27/17.
 */

import androidx.annotation.StringRes;

import com.scanlibrary.ocrme.BuildConfig;
import com.scanlibrary.ocrme.R;
import com.scanlibrary.ocrme.my_docs.get_my_docs_task.MyDocsHttpClient;

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
    int provideAdBannerId() {
        if (BuildConfig.DEBUG) {
            return R.string.test_banner;
        } else {
            return R.string.my_docs_banner;
        }
    }

    @Binds
    abstract MyDocsContract.Presenter myDocsPresenter(MyDocsPresenter presenter);
}
