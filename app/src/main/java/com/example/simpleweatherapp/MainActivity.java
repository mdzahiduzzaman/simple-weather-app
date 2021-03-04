package com.example.simpleweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    class WeatherClass extends AsyncTask<String, Void, String> {

        String result;
        @Override
        protected String doInBackground(String... urls) {
            result = "";
            URL link;
            HttpURLConnection connection = null;

            try {
                link = new URL(urls[0]);
                connection = (HttpURLConnection) link.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(in);
                int data = streamReader.read();
                while(data!=-1) {
                    char current = (char)data;
                    result = result + current;
                    data = streamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(result);
                JSONObject main = new JSONObject(object.getString(result));
                String temperature = main.getString("temp");
                String city = object.getString("name");

                MainActivity.temperature.setText(temperature);
                MainActivity.city.setText(city);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static TextView temperature;
    static TextView city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperature = (TextView) findViewById(R.id.temperature);
        city = (TextView) findViewById(R.id.city);

        String result;

        WeatherClass getWeatherData = new WeatherClass();
        try {
            result = getWeatherData.execute("http://api.openweathermap.org/data/2.5/weather?q=Dhaka&appid=4567261870a8d87dd139828dab082338").get();
            Log.i("contentData", result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}