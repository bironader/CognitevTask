package com.example.cognetivtask.features.main;

import android.Manifest;
import android.location.Location;
import android.widget.CompoundButton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.cognetivtask.Constants;
import com.example.cognetivtask.LocationService;
import com.example.cognetivtask.data.responses.places.PlacesResponse;
import com.example.cognetivtask.data.responses.places.Item;
import com.example.cognetivtask.data.remote.PlacesApi;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

public class MainViewModel extends ViewModel implements LocationService.LocationListener {


    private final MutableLiveData<List<Item>> places = new MutableLiveData<>();
    private final MutableLiveData<Throwable> loadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFirstRequest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPermissionGranted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLocationEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRealTime = new MutableLiveData<>();
    private final MutableLiveData<Item> updatedPlace = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
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
        isFirstRequest.setValue(true);
        isConnected.setValue(true);


    }

    void setRequestParam(String token, String version) {

        this.token = token;
        this.version = version;
    }


    void initLocationService(LocationService locationService) {
        locationService.setLocationCallBacks(this);
        locationService.requestLocation(isRealTime.getValue());
    }

    void setStoredMode(Boolean storedMode) {
        isRealTime.setValue(storedMode);
    }

    /**
     * Complex situation , we need to fetch photo for each place
     * flatmap operator solve the problem when we have  request depend on the result of the anther one
     * //
     */

    private void fetchPlaces(String locationParam) {
        disposable.add(placesResponseObservable(locationParam)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(PlacesResponse::getItems)
                .flatMap(this::getPhotoObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError));
    }

    private void onError(Throwable throwable) {
        Timber.d(throwable);
        isConnected.postValue(((HttpException) throwable).code() != Constants.FOUR_SQUARE_VENUE_SEARCH_LIMIT_EXCEED);
        loadError.postValue(throwable);
        isFirstRequest.postValue(false);

    }

    private void onSuccess(Item item) {
        updatedPlace.setValue(item);
    }


    private Observable<Item> getPhotoObservable(Item place) {
        return placesApi.getPhotos(place.getPlace().getId(), token, version)
                .map(photoRespone -> {
                    place.getPlace().setPhotoRespone(photoRespone);
                    return place;
                })
                .subscribeOn(Schedulers.io());

    }

    private void showPlaces(PlacesResponse response) {

        places.postValue(response.getResponse().getGroups().get(0).getItems());
        isFirstRequest.postValue(false);

    }


    private Observable<PlacesResponse> placesResponseObservable(String locationParam) {
        return placesApi.getPlaces(locationParam, 10, token, version)
                .map(placesResponse -> {
                    showPlaces(placesResponse);
                    return placesResponse;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    LiveData<List<Item>> getPlaces() {
        return places;
    }

    LiveData<Throwable> getError() {
        return loadError;
    }

    public LiveData<Boolean> getIsRealTime() {
        return isRealTime;
    }

    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }


    LiveData<Boolean> getPermissionStatus() {
        return isPermissionGranted;
    }

    LiveData<Boolean> getLocationProviderStatus() {
        return isLocationEnabled;
    }

    LiveData<Item> getUpdatedPlace() {
        return updatedPlace;
    }

    public LiveData<Boolean> isFirstRequest() {
        return isFirstRequest;
    }

    void checkLocationPermission(RxPermissions rxPermissions) {

        isLocationEnabled.setValue(locationService.isLocationEnabled());
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(isPermissionGranted::setValue);
    }


    public void onChangeModeListener(CompoundButton btn, Boolean isChecked) {

        isRealTime.setValue(isChecked);
        locationService.removeCurrentUpdate();
        locationService.requestLocation(isRealTime.getValue());

    }


    @Override
    public void onLocationChanged(Location location) {
        isConnected.setValue(NetworkUtils.isConnected());
        if (isConnected.getValue())
            fetchPlaces(location.getLatitude() + "," + location.getLongitude());
        else
            isFirstRequest.setValue(false);

    }


}
