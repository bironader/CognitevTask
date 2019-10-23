package com.example.mvvmstarter.features.Ui;

import android.os.Bundle;

import com.example.mvvmstarter.R;
import com.example.mvvmstarter.features.ViewModel.MainViewModel;
import com.example.mvvmstarter.features.ViewModel.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    MainViewModel mainViewModel;
    private static final String CLIENT_ID = "IGCDGFTCLXOGSGGQ2IKFCISLQSHYUM44QKFQAYQ4IVNBLVXD";
    private static final String CLIENT_SECRET = "NIHW4ZEY4WXPHYTGIKTBEG0RRE2MH2ULYLCUCUGIT54XLXKQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
    }
}
