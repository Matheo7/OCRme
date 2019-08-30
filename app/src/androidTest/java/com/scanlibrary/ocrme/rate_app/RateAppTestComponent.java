package com.scanlibrary.ocrme.rate_app;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RateAppTestModule.class})
public interface RateAppTestComponent {
    void inject(RateAppAskerTest test);
}
