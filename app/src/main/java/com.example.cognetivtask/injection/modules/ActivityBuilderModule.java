package com.example.cognetivtask.injection.modules;



import com.example.cognetivtask.features.main.MainActivity;
import com.example.cognetivtask.features.splash.SplashActivity;
import com.example.cognetivtask.injection.modules.MainModule.MainViewModelModule;
import com.example.cognetivtask.injection.modules.MainModule.PlacesApiModule;
import com.example.cognetivtask.injection.modules.SplashModule.SplashActivityViewModelModule;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {


    @ContributesAndroidInjector(modules = {SplashActivityViewModelModule.class})
    abstract SplashActivity splashActivity();

    @ContributesAndroidInjector(modules = {MainViewModelModule.class, PlacesApiModule.class})
    abstract MainActivity mainActivity();

}
