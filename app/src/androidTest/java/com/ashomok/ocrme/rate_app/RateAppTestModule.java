package com.ashomok.ocrme.rate_app;



import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.ashomok.ocrme.di_dagger.ApplicationTestModule;

import dagger.Module;


@Module(includes = ApplicationTestModule.class)
public class RateAppTestModule {

//    @Provides
//    RateAppAsker provideRateAppAsker(SharedPreferences sharedPreferences, Context context) {
//        return new RateAppAsker(sharedPreferences, context);
//
//    }
}
