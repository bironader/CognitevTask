package com.example.cognetivtask.features.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.example.cognetivtask.Constants;
import com.example.cognetivtask.LocationService;
import com.example.cognetivtask.data.remote.PlacesApi;
import com.example.cognetivtask.features.ViewModelProviderFactory;
import com.example.congnitevtask.R;
import com.example.congnitevtask.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationServices;
import com.iamhabib.easy_preference.EasyPreference;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import retrofit2.HttpException;

public class MainActivity extends DaggerAppCompatActivity {


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
    private int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        populateRecycleView(activityMainBinding);


        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);
        mainViewModel.setStoredMode(prefBuilder.getBoolean(Constants.IS_REAL_TIME, true)); // by default enabled real time
        mainViewModel.checkLocationPermission(new RxPermissions(this));
        mainViewModel.setRequestParam(prefBuilder.getString(Constants.TOKEN_TAG, ""), getToday());


        mainViewModel.getPermissionStatus().observe(this, isPermissionGranted -> {

            if (isPermissionGranted)
                mainViewModel.initLocationService(locationServices);
            else
                showToast(getResources().getString(R.string.no_permissions));

        });

        mainViewModel.getIsRealTime().observe(this, isRealTimeMode -> {

            prefBuilder.addBoolean(Constants.IS_REAL_TIME, isRealTimeMode).save();
            if (flag != 0) {
                showToast(getResources().getString(R.string.app_is_restrating));
                restartApp();
            }
            flag++;
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

        mainViewModel.getError().observe(this, this::handleErrors);

    }

    String getToday() {
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(todayDate).replace("-", "");
    }

    private void populateRecycleView(ActivityMainBinding activityMainBinding) {
        RecyclerView recyclerView = activityMainBinding.placesList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void handleErrors(Throwable throwable) {

        if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            if (exception.code() == Constants.FOUR_SQUARE_PHOTO_END_POINT_LIMIT_EXCEED)
                showToast(getResources().getString(R.string.photo_error));
            else if (exception.code() == Constants.FOUR_SQUARE_VENUE_SEARCH_LIMIT_EXCEED)
                showToast(getResources().getString(R.string.limit_veunue_exceed));
        } else if (throwable instanceof SocketTimeoutException)
            showToast(getResources().getString(R.string.time_out));
    }

    private void restartApp() {
        Intent intent = getPackageManager().
                getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
