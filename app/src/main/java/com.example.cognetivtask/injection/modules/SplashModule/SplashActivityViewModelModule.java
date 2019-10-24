package com.example.cognetivtask.injection.modules.SplashModule;

import androidx.lifecycle.ViewModel;


import com.example.cognetivtask.features.splash.SplashViewModel;
import com.example.cognetivtask.injection.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SplashActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    public abstract ViewModel bindMainViewModel(SplashViewModel viewModel);


}
