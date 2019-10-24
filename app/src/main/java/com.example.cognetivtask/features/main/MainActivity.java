package com.example.cognetivtask.features.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.cognetivtask.Constants;
import com.example.cognetivtask.LocationService;
import com.example.cognetivtask.data.remote.PlacesApi;
import com.example.cognetivtask.features.ViewModelProviderFactory;
import com.example.congnitevtask.R;
import com.example.congnitevtask.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationServices;
import com.iamhabib.easy_preference.EasyPreference;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Inject
    EasyPreference.Builder prefBuilder;
    @Inject
    PlacesApi placesApi;
    @Inject
    LocationService locationServices;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);
        mainViewModel.checkLocationPermission(new RxPermissions(this));
        mainViewModel.setRequestParm(prefBuilder.getString(Constants.TOKEN_TAG, ""), getToday());
        mainViewModel.getPermissionStatus().observe(this, isPermissionGranted -> {
//
            if (isPermissionGranted)
                mainViewModel.initLocationService(locationServices);
            else
                requestPermission();

        });

        mainViewModel.getLocationStatus().observe(this, enabled -> {
            if (!enabled)
                Toast.makeText(this, "Make sure your location is enabled", Toast.LENGTH_LONG).show();
        });


        mainViewModel.getPlaces().observe(this, places -> {

        });

        mainViewModel.getError().observe(this, isError -> {

        });


        mainViewModel.getLoading().observe(this, isLoading -> {

        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainViewModel.initLocationService(locationServices);
            } else {
                Toast.makeText(this, "App is closing", Toast.LENGTH_LONG).show();
                finish();
            }


        }
    }

    String getToday() {
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(todayDate).replace("-","");
    }

}
