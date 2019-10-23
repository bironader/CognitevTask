package com.example.cognetivtask.features.viewmodel.splash;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class SplashViewModel extends ViewModel {



    private SplashListener listener ;
    private CompositeSubscription subscription;


    @Inject
    SplashViewModel() {

    }



    public void onLoginClicked(View view) {

        listener.attemptLogin();
    }

    public void setListener(SplashListener listener) {
        this.listener = listener;
    }
    public void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            // Success.
            String code = codeResponse.getCode();
            listener.performTokenExchange(code);

        } else {
            if (exception instanceof FoursquareCancelException) {
                // Cancel.
                Timber.e(exception);


            } else if (exception instanceof FoursquareDenyException) {
                // Deny.
                Timber.e(exception);


            } else if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
//                String errorMessage = exception.getMessage();
//                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
//                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else if (exception instanceof FoursquareUnsupportedVersionException) {
                // Unsupported Fourquare app version on the device.
                Timber.e(exception);


            } else if (exception instanceof FoursquareInvalidRequestException) {
                // Invalid request.
                Timber.e(exception);

            } else {
                // Error.
                Timber.e(exception);

            }
        }
    }


    public void onCompleteTokenExchange(int resultCode, Intent data) {
        AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
        Exception exception = tokenResponse.getException();

        if (exception == null) {
            String accessToken = tokenResponse.getAccessToken();
            // Success.
            Timber.d("Access token: " + accessToken);

            // Persist the token for later use. In this example, we save
            // it to shared prefs.
            listener.saveToken(accessToken);
            listener.updateUi();


        } else {
            if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = exception.getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
//                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else {
                // Other exception type.
                Timber.d( exception);
            }
        }
    }

    public  interface SplashListener {
        void attemptLogin();
        void performTokenExchange(String token);
        void saveToken(String token);
        void updateUi();
    }
}
