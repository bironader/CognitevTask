package com.example.mvvmstarter.injection.modules.MainViewModelModule;

import com.example.mvvmstarter.features.ViewModel.MainViewModel;
import com.example.mvvmstarter.injection.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);
}
