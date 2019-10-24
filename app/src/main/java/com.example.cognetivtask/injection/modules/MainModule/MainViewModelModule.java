package com.example.cognetivtask.injection.modules.MainModule;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;


import com.example.cognetivtask.LocationService;
import com.example.cognetivtask.features.main.MainViewModel;
import com.example.cognetivtask.features.splash.SplashViewModel;
import com.example.cognetivtask.injection.ViewModelKey;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.tbruyelle.rxpermissions.RxPermissions;



import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);


    @Provides
    static LocationService provideLocationService(Context context) {
        return new LocationService(context);
    }




}
