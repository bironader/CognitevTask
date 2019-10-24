package com.example.cognetivtask.injection.modules.MainModule;

import androidx.lifecycle.ViewModel;

import com.example.cognetivtask.features.viewmodel.splash.SplashViewModel;
import com.example.cognetivtask.injection.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    public abstract ViewModel bindMainViewModel(SplashViewModel viewModel);

}