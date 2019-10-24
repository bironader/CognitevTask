package com.example.cognetivtask.injection.modules;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ContextModule {

    @Singleton
    @Provides
    @NonNull
    static Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}
