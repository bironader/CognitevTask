package com.example.mvvmstarter.injection.modules;

import android.app.Application;
import android.content.Context;

import com.example.mvvmstarter.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public abstract class NetworkModule {

    private static String getBaseUrl() {
        return BuildConfig.APPLICATION_ID;
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(
            HttpLoggingInterceptor httpLoggingInterceptor, StethoInterceptor stethoInterceptor,
            ChuckInterceptor chuckInterceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(chuckInterceptor);
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
            httpClientBuilder.addNetworkInterceptor(stethoInterceptor);
        }
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
   static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Timber.d(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
   static  StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
   static ChuckInterceptor provideChuckInterceptor(Application application) {
        return new ChuckInterceptor(application.getApplicationContext());
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }


    @Provides
    @Singleton
   static Interceptor provideHeaderInterceptor() {
        return chain -> {
            Request request = chain.request();

            request = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Requested-With", " XMLHttpRequest")
                    .build();
            return chain.proceed(request);

        };
    }
}
