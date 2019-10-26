package com.example.cognetivtask;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import javax.inject.Inject;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class LocationService {

    private static final long UPDATE_INTERVAL = 30 * 1000;  /* 2m */
    private static final long FASTEST_INTERVAL = 2000; /* 2 sec */

    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationListener locationListener;


    @Inject
    public LocationService(Context context) {
        this.context = context;
        this.mFusedLocationClient = getFusedLocationProviderClient(context);


    }


    public void setLocationCallBacks(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public void requestLocation(Boolean isRealTime) {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (isRealTime) {
            mLocationRequest.setSmallestDisplacement(500); // 500 m from the last location
        } else
            mLocationRequest.setNumUpdates(1);  // single update mode

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();


        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);


        createLocationCallback();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationListener != null)
                    locationListener.onLocationChanged(locationResult.getLastLocation());
            }
        };
    }


    public Boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);

        }

    }

    public void removeCurrentUpdate() {
        if (mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public interface LocationListener {

        void onLocationChanged(Location location); // real time mode

    }


}
