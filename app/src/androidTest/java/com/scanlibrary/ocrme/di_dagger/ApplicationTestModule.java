package com.scanlibrary.ocrme.di_dagger;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.InstrumentationRegistry;

import com.scanlibrary.ocrme.R;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationTestModule {

    @Provides
    static Context provideContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    @Provides
    static SharedPreferences provideSharedPrefs(Context context) {
        return context.getSharedPreferences(
                context.getString(R.string.preferences), Context.MODE_PRIVATE);
    }
}

