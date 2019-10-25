package com.example.cognetivtask.features.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cognetivtask.LocationService;
import com.example.cognetivtask.data.responses.photos.PhotoRespone;
import com.example.cognetivtask.data.responses.places.Place;
import com.example.cognetivtask.data.responses.places.PlacesResponse;
import com.example.cognetivtask.data.responses.places.Item;
import com.example.cognetivtask.data.remote.PlacesApi;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends ViewModel implements LocationService.LocationChanged {


    private final MutableLiveData<List<Item>> places = new MutableLiveData<>();
    private final MutableLiveData<Throwable> loadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFirstRequest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPermissionGranted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLocationEnabled = new MutableLiveData<>();
    private final MutableLiveData<Item> updatedPlace = new MutableLiveData<>();
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


    }

    void setRequestParam(String token, String version) {

        this.token = token;
        this.version = version;
    }


    void initLocationService(LocationService locationService) {
        locationService.setLocationChanged(this);
        locationService.requestLocation();
    }


    /**
     * Complex situation , we need to fetch photo for each place
     * flatmap operator solve the problem when we have  request depend on the result of the anther one
     */
    private void fetchPlaces(String locationParam) {


        disposable.add(getPlacesObservable(locationParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<Item, ObservableSource<Item>>) this::getPhotoObservable)
                .subscribe(this::onSuccess, this::onError));

    }

    private void onSuccess(Item placeWithPic) {
        updatedPlace.setValue(placeWithPic);
        isFirstRequest.setValue(false);
    }

    private void onError(Throwable throwable) {
        loadError.postValue(throwable);
        isFirstRequest.postValue(false);

    }


    private Observable<Item> getPhotoObservable(Item place) {
        return placesApi.getPhotos(place.getPlace().getId(), token, version)
                .map(photoRespone -> {
                    place.getPlace().setPhotoRespone(photoRespone);
                    return place;
                })
                .subscribeOn(Schedulers.io());

    }

    private Observable<Item> getPlacesObservable(String locationParam) {

        return placesApi.getPlaces(locationParam, 10, token, version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<PlacesResponse, ObservableSource<Item>>) placesResponse -> {
                    showPlaces(placesResponse);
                    return Observable.fromIterable(placesResponse.getItems())
                            .subscribeOn(Schedulers.io());
                });
    }


    private void showPlaces(PlacesResponse response) {

        places.setValue(response.getResponse().getGroups().get(0).getItems());

    }


    LiveData<List<Item>> getPlaces() {
        return places;
    }

    LiveData<Throwable> getError() {
        return loadError;
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

    @Override
    public void onLocationChangedListener(Location location) {

        fetchPlaces(location.getLatitude() + "," + location.getLongitude());


    }


}
