package com.wa.rena.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String appid = "18c51bde0a28d9196fef71382fcbd4d8";
    Button searchBtn;
    EditText cityTextView;
    TextView weatherTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.searchBtn);
        cityTextView = findViewById(R.id.cityEditText);
        weatherTextView = findViewById(R.id.weatherDescriptionTextView);

        searchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.i("test", "test");
            }
        });

    }
}


//example api call
//http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=18c51bde0a28d9196fef71382fcbd4d8
