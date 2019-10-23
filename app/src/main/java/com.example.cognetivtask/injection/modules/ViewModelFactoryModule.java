package com.example.cognetivtask.injection.modules;

import com.example.cognetivtask.features.viewmodel.ViewModelProviderFactory;


import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);

}