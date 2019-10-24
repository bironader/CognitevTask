package com.example.cognetivtask.features.main;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.example.cognetivtask.features.ViewModelProviderFactory;
import com.example.congnitevtask.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {


    private RxPermissions rxPermissions;


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxPermissions = new RxPermissions(this);
        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);

        mainViewModel.checkLocationPermission(rxPermissions);

        mainViewModel.getPermissionStatus().observe(this, isPermissionGranted -> {

            if (isPermissionGranted)
                mainViewModel.getLastKnownUserLocation();

        });


        mainViewModel.getPlaces().observe(this, places -> {

        });

        mainViewModel.getError().observe(this, isError -> {

        });


        mainViewModel.getLoading().observe(this, isLoading -> {

        });
    }


}
