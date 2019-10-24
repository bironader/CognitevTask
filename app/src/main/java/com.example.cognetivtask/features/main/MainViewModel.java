package com.example.cognetivtask.features.main;

import android.Manifest;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cognetivtask.LocationService;
import com.example.cognetivtask.data.models.FourSquarResponse;
import com.example.cognetivtask.data.models.Place;
import com.example.cognetivtask.data.models.Response;
import com.example.cognetivtask.data.remote.PlacesApi;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends ViewModel implements LocationService.LocationChanged {


    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPermissionGranted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLocationEnabled = new MutableLiveData<>();
    private CompositeDisposable disposable;
    private PlacesApi placesApi;
    private LocationService locationService;
    private String token;
    private String version;

    @Inject
    MainViewModel(PlacesApi placesApi, LocationService locationService) {
        disposable = new CompositeDisposable();
        this.placesApi = placesApi;
        this.locationService = locationService;

    }

    void setRequestParm(String token, String version) {

        this.token = token;
        this.version = version;
    }


    void initLocationService(LocationService locationService) {
        locationService.setLocationChanged(this);
        locationService.requestLocation();
    }

    private void fetchPlaces(String locationParm) {
        loading.setValue(true);
        disposable.add(placesApi.getPlaces(locationParm, token, version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError));


    }

    private void onError(Throwable throwable) {
        Timber.d(throwable);
        loading.setValue(false);
        repoLoadError.setValue(true);
    }

    private void onSuccess(FourSquarResponse response) {
        loading.setValue(false);
        repoLoadError.setValue(false);
        places.setValue(response.getResponse().getPlaces());


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

    LiveData<Boolean> getLocationStatus() {
        return isLocationEnabled;
    }

    ;

    void checkLocationPermission(RxPermissions rxPermissions) {

        isLocationEnabled.setValue(locationService.isLocationEnabled());
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(isPermissionGranted::setValue);
    }

    @Override
    public void onLocationChangedListener(Location location) {

        fetchPlaces(location.getLatitude() + "," + location.getLongitude());


    }


}
