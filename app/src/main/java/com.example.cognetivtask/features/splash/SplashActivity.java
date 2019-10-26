package com.example.cognetivtask.features.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.cognetivtask.Constants;

import com.example.cognetivtask.features.ViewModelProviderFactory;
import com.example.cognetivtask.features.main.MainActivity;
import com.example.congnitevtask.R;


import com.example.congnitevtask.databinding.SplashMainBinding;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.iamhabib.easy_preference.EasyPreference;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SplashActivity extends DaggerAppCompatActivity implements SplashViewModel.SplashListener {


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    @Inject
    EasyPreference.Builder prefBuilder;

    SplashViewModel splashViewModel;

    private static final int REQUEST_CODE_FSQ_CONNECT = 200;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_main);
        SplashMainBinding splashMainBinding = DataBindingUtil.setContentView(this, R.layout.splash_main);
        splashViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(SplashViewModel.class);
        splashMainBinding.setSplashViewModel(splashViewModel);
        splashViewModel.setListener(this);
        updateUi();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                splashViewModel.onCompleteConnect(resultCode, data);
                break;

            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                splashViewModel.onCompleteTokenExchange(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void attemptLogin() {
        Intent intent = FoursquareOAuth.getConnectIntent(SplashActivity.this, getString(R.string.client_id));
        if (FoursquareOAuth.isPlayStoreIntent(intent)) {
            startActivity(intent);
            Toast.makeText(this,getResources(). getString(R.string.foursquare_app), Toast.LENGTH_LONG).show();
        } else {
            startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);

        }


    }

    @Override
    public void performTokenExchange(String code) {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(this, getString(R.string.client_id), getString(R.string.client_secret), code);
        startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
    }

    @Override
    public void saveToken(String token) {

        prefBuilder.addString(Constants.TOKEN_TAG, token).save();
    }

    @Override
    public void updateUi() {

        Boolean isLoggedIn = !prefBuilder.getString(Constants.TOKEN_TAG, "").isEmpty();
        String token = prefBuilder.getString(Constants.TOKEN_TAG,"");
        if (isLoggedIn)
            startActivity(new Intent(this, MainActivity.class));

    }


}
