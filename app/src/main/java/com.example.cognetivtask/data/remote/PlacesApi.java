package com.example.cognetivtask.data.remote;

import com.example.cognetivtask.data.models.Response;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    // /users/id
    @GET("venues/search")
    Observable<Response> getPlaces(@Query("near") String location, @Query("oauth_token") String access_token);
}
