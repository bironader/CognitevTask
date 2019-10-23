package com.example.mvvmstarter.injection.modules;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvmstarter.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class GlideModule {

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions
                .placeholderOf(R.drawable.chuck_ic_delete_white_24dp)
                .error(R.drawable.chuck_ic_https_grey_24dp);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }


}
