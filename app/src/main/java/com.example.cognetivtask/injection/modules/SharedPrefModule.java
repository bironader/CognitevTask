package com.example.cognetivtask.injection.modules;

import android.app.Application;

import com.iamhabib.easy_preference.EasyPreference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SharedPrefModule {


    @Singleton
    @Provides
    static EasyPreference.Builder provideSharedPresences(Application application) {
        return EasyPreference.with(application.getApplicationContext());
    }
}
