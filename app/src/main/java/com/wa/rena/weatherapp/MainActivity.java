package com.wa.rena.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    final String appid = "18c51bde0a28d9196fef71382fcbd4d8";
    Button searchBtn;
    EditText cityTextView;
    TextView weatherTextView;
    TextView weatherTemp;
    TextView morning;
    TextView afternoon;
    TextView night;
    TextView cityNameTextView;
    TextView conditionOther;
    LocationManager locationManager;
    LocationListener locationListener;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result ="";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);

                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

                String weatherText = "";
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String weatherCondition = jsonObj.getString("weather");
                    String cityName = jsonObj.getString("name");

                    JSONObject sysObject = jsonObj.getJSONObject("sys");
                    String countyName = sysObject.getString("country");

                    JSONObject tempObj = jsonObj.getJSONObject("main");
                    String temp = tempObj.getString("temp");
                    double tempDouble = Double.parseDouble(temp);
                    temp = String.format("%,.1f", tempDouble);

                    String humidity = tempObj.getString("humidity");

                    JSONObject windObj = jsonObj.getJSONObject("wind");
                    String windSpeed = windObj.getString("speed");

                    JSONArray arr = new JSONArray(weatherCondition);


                    for (int i=0; i < arr.length(); i++){
                        JSONObject infoJson = arr.getJSONObject(i);
                        String description = infoJson.getString("description").toUpperCase();
                        weatherText += description + "\r\n";
                    }

                    weatherTextView.setText(weatherText);
                    cityNameTextView.setText(cityName + ", " + countyName);
                    weatherTemp.setText(temp + " °C");
                    conditionOther.setText("Wind: " + windSpeed + " km/s\r\nHumidity: " + humidity + " %");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Something went wrong :(", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
        }
    }

    public void findWeather(){
        String city = cityTextView.getText().toString();
        String[] splittedCity = city.trim().split("\\s*,\\s*");
        String encodedCityName = URLEncoder.encode(splittedCity[0]);

        if (splittedCity.length == 2){
            encodedCityName = encodedCityName + "," + splittedCity[1];
        }

        // gets the input method that is currently being using, which is keyboard
        InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // hide keyboard from the window
        inputMethod.hideSoftInputFromWindow(cityTextView.getWindowToken(),0);
        cityTextView.setText("");
        DownloadTask task = new DownloadTask();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&units=metric&APPID=" + appid;
        task.execute(urlString);
    }

    public void findMyCityWeather(Location location){

        String lat = location.getLatitude() +"";
        String lng = location.getLongitude() +"";
        DownloadTask task = new DownloadTask();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat+ "&lon=" + lng + "&units=metric&APPID=" + appid;
        // String foreCastUrlString = "http://api.openweathermap.org/data/2.5/forecast?q=" + encodedCityName + "&units=metric&appid=" + appid;
        task.execute(urlString);
    }

    @SuppressLint("MissingPermission")
    public void getCurrentCity(View view){
        if (Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        } else{

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.searchBtn);
        cityTextView = findViewById(R.id.cityEditText);
        weatherTextView = findViewById(R.id.weatherDescriptionTextView);
        weatherTemp = findViewById(R.id.weatherTemp);
        morning = findViewById(R.id.morning);
        afternoon = findViewById(R.id.afternoon);
        night = findViewById(R.id.night);
        cityNameTextView = findViewById(R.id.today);
        conditionOther = findViewById(R.id.conditionEtc);
        cityTextView.setText("");
        searchBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
            }
        });
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_SHORT).show();
                findMyCityWeather(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

    }
}
