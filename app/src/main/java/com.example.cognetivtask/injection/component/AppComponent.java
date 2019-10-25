package com.example.cognetivtask.injection.component;

import android.app.Application;

import com.example.cognetivtask.BaseApplication;
import com.example.cognetivtask.injection.modules.ActivityBuilderModule;
import com.example.cognetivtask.injection.modules.ContextModule;
import com.example.cognetivtask.injection.modules.NetworkModule;
import com.example.cognetivtask.injection.modules.SharedPrefModule;
import com.example.cognetivtask.injection.modules.ViewModelFactoryModule;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        ActivityBuilderModule.class,
        NetworkModule.class,
        ViewModelFactoryModule.class,
        SharedPrefModule.class, ContextModule.class})

public interface AppComponent extends AndroidInjector<BaseApplication> {


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

}
