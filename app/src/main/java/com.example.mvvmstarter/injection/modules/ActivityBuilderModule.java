package com.example.mvvmstarter.injection.modules;

import com.example.mvvmstarter.features.Ui.MainActivity;
import com.example.mvvmstarter.injection.modules.MainViewModelModule.MainActivityViewModelModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = {MainActivityViewModelModule.class})
    abstract MainActivity mainActivity();

}
