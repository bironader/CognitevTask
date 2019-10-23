package com.example.cognetivtask.injection.modules;


import com.example.cognetivtask.features.Ui.main.MainActivity;
import com.example.cognetivtask.features.Ui.splash.SplashActivity;
import com.example.cognetivtask.injection.modules.SplashViewModelModule.SplashActivityViewModelModule;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {


    @ContributesAndroidInjector(modules = {SplashActivityViewModelModule.class})
    abstract SplashActivity splashActivity();


    abstract MainActivity mainActivity();

}
