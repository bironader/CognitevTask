package com.example.cognetivtask.injection.modules.MainModule;



import com.example.cognetivtask.data.remote.PlacesApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public abstract class PlacesApiModule {

    @Provides
    static PlacesApi provideSessionApi(Retrofit retrofit) {
        return retrofit.create(PlacesApi.class);
    }
}
