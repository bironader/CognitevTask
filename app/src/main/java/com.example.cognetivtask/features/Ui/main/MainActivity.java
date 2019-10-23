package com.example.cognetivtask.features.Ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.cognetivtask.features.viewmodel.ViewModelProviderFactory;
import com.example.cognetivtask.features.viewmodel.main.MainViewModel;
import com.example.congnitevtask.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
    }
}
