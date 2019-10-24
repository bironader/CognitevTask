package com.example.cognetivtask.features.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cognetivtask.data.models.Place;
import com.example.cognetivtask.data.models.Response;
import com.example.cognetivtask.data.remote.PlacesApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends ViewModel implements OnSuccessListener<Location>, OnFailureListener {


    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPermissionGranted = new MutableLiveData<>();
    private Location location;
    private FusedLocationProviderClient mFusedLocationClient;
    private CompositeDisposable disposable;

    @Inject
    PlacesApi placesApi;
    @Inject
    Context context;


    @Inject
    MainViewModel() {
        disposable = new CompositeDisposable();
        mFusedLocationClient = LocationServices
                .getFusedLocationProviderClient(context);

    }


    private void fetchPlaces(String locationParm) {
        loading.setValue(true);
        disposable.add(placesApi.getPlaces(locationParm, "asd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError));


    }

    private void onError(Throwable throwable) {
        Timber.d(throwable);
        loading.setValue(false);
        repoLoadError.setValue(true);
    }

    private void onSuccess(Response response) {
        loading.setValue(false);
        repoLoadError.setValue(false);
        places.setValue(response.places);


    }

    LiveData<List<Place>> getPlaces() {
        return places;
    }

    LiveData<Boolean> getError() {
        return repoLoadError;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }

    LiveData<Boolean> getPermissionStatus() {
        return isPermissionGranted;
    }


    void checkLocationPermission(RxPermissions rxPermissions) {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted)
                        isPermissionGranted.setValue(true);

                    else
                        isPermissionGranted.setValue(false);
                });
    }

    void getLastKnownUserLocation() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);
        } else
            isPermissionGranted.setValue(false);
    }


    @Override
    public void onSuccess(Location userLocation) {

        fetchPlaces(userLocation.getLatitude() + "," + userLocation.getLongitude());

    }

    @Override
    public void onFailure(@NonNull Exception e) {

        Timber.e(e);
    }
}
