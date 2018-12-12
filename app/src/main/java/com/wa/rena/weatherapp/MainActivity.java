package com.wa.rena.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String appid = "18c51bde0a28d9196fef71382fcbd4d8";
    Button searchBtn;
    EditText cityTextView;
    TextView weatherTextView;

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

            try {
                JSONObject jasonObj = new JSONObject(result);
                String info = jasonObj.getString("weather");
                JSONArray arr = new JSONArray(info);

                for (int i=0; i < arr.length(); i++){
                    JSONObject infoJson = arr.getJSONObject(i);
                    Log.i("info", infoJson.getString("main"));
                    Log.i("info", infoJson.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public void findWeather(){
        String city = cityTextView.getText().toString();

        DownloadTask task = new DownloadTask();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=" + appid;
        task.execute(urlString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.searchBtn);
        cityTextView = findViewById(R.id.cityEditText);
        weatherTextView = findViewById(R.id.weatherDescriptionTextView);

        searchBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
            }
        });
    }
}


//example api call
//http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=18c51bde0a28d9196fef71382fcbd4d8
