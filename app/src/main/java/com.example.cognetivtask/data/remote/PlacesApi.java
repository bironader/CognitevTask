package com.example.cognetivtask.data.remote;

import com.example.cognetivtask.data.responses.photos.PhotoRespone;
import com.example.cognetivtask.data.responses.places.PlacesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlacesApi {


    @GET("venues/{id}/photos")
    Observable<PhotoRespone> getPhotos(@Path("id") String id, @Query("oauth_token") String access_token
            , @Query("v") String version);

    @GET("venues/explore")
    Observable<PlacesResponse> getPlaces(@Query("near") String location, @Query("limit") int limit, @Query("oauth_token") String access_token
            , @Query("v") String version);
}
