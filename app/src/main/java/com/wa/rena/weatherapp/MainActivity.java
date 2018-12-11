package com.wa.rena.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String appid = "18c51bde0a28d9196fef71382fcbd4d8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


//example api call
//http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=18c51bde0a28d9196fef71382fcbd4d8
