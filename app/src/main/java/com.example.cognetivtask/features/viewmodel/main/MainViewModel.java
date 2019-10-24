package com.example.cognetivtask.features.viewmodel.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cognetivtask.data.models.Place;
import com.example.cognetivtask.data.models.Response;
import com.example.cognetivtask.data.remote.PlacesApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends ViewModel {


    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private CompositeDisposable disposable;
    @Inject
    PlacesApi placesApi;


    @Inject
    MainViewModel() {
        disposable = new CompositeDisposable();
        fetchPlaces();
    }


    private void fetchPlaces() {
        loading.setValue(true);
        disposable.add(placesApi.getPlaces("dsa", "asd")
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


    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public LiveData<Boolean> getError() {
        return repoLoadError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

}
