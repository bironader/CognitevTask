package com.example.mvvmstarter.injection.component;

import android.app.Application;

import com.example.mvvmstarter.BaseApplication;
import com.example.mvvmstarter.injection.modules.ActivityBuilderModule;
import com.example.mvvmstarter.injection.modules.GlideModule;
import com.example.mvvmstarter.injection.modules.NetworkModule;
import com.example.mvvmstarter.injection.modules.ViewModelFactoryModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {AndroidSupportInjectionModule.class,
        ActivityBuilderModule.class,
        NetworkModule.class,
        GlideModule.class,
        ViewModelFactoryModule.class})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}
