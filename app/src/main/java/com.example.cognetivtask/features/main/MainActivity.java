package com.example.cognetivtask.features.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    @Inject
    PlacesAdapter adapter;

    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        populateRecycleView(activityMainBinding);


        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);
        mainViewModel.checkLocationPermission(new RxPermissions(this));
        mainViewModel.setRequestParam(prefBuilder.getString(Constants.TOKEN_TAG, ""), getToday());


        mainViewModel.getPermissionStatus().observe(this, isPermissionGranted -> {

            if (isPermissionGranted)
                mainViewModel.initLocationService(locationServices);
            else
                showToast(getResources().getString(R.string.no_permissions));

        });

        mainViewModel.getLocationProviderStatus().observe(this, enabled -> {
            if (!enabled)
                Toast.makeText(this, getResources().getString(R.string.location_provider_check), Toast.LENGTH_LONG).show();
        });


        mainViewModel.getPlaces().observe(this, places -> {
            if (places != null) {
                adapter.setPlaces(places);
                adapter.notifyDataSetChanged();
            }

        });
        mainViewModel.getUpdatedPlace().observe(this, item -> {
            adapter.updatePlace(item);
        });

    }

    String getToday() {
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(todayDate).replace("-", "");
    }

    void populateRecycleView(ActivityMainBinding activityMainBinding) {
        RecyclerView recyclerView = activityMainBinding.placesList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


}
