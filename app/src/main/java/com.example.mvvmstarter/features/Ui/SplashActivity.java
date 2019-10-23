package com.example.mvvmstarter.features.Ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.mvvmstarter.R;
import com.example.mvvmstarter.features.ViewModel.MainViewModel;
import com.example.mvvmstarter.features.ViewModel.ViewModelProviderFactory;
import com.foursquare.android.nativeoauth.FoursquareOAuth;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;

public class SplashActivity extends DaggerAppCompatActivity {


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    MainViewModel mainViewModel;
    private static final String CLIENT_ID = "IGCDGFTCLXOGSGGQ2IKFCISLQSHYUM44QKFQAYQ4IVNBLVXD";
    private static final String CLIENT_SECRET = "NIHW4ZEY4WXPHYTGIKTBEG0RRE2MH2ULYLCUCUGIT54XLXKQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);

        Intent intent = FoursquareOAuth.getConnectIntent(SplashActivity.this, CLIENT_ID);

        // If the device does not have the Foursquare app installed, we'd
        // get an intent back that would open the Play Store for download.
        // Otherwise we start the auth flow.
        if (FoursquareOAuth.isPlayStoreIntent(intent)) {
            toastMessage(SplashActivity.this, getString(R.string.app_not_installed_message));
            startActivity(intent);
        } else {
            startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
    }

//    private void ensureUi() {
//        boolean isAuthorized = !TextUtils.isEmpty(ExampleTokenStore.get().getToken());
//
//        TextView tvMessage = findViewById(R.id.tvMessage);
//        tvMessage.setVisibility(isAuthorized ? View.VISIBLE : View.GONE);
//
//        Button btnLogin = findViewById(R.id.btnLogin);
//        btnLogin.setVisibility(isAuthorized ? View.GONE : View.VISIBLE);
//        btnLogin.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start the native auth flow.
//
//                }
//            }
//        });
//    }
}
