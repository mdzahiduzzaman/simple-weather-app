package com.myapp.weatherreport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.weatherreport.Data.Data;
import com.myapp.weatherreport.Model.Main;
import com.myapp.weatherreport.Model.WeatherModel;
import com.myapp.weatherreport.Retrofit.OpenWeatherMap;
import com.myapp.weatherreport.Retrofit.RetrofitRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    TextView locatiom, temperature, feelsLike, maxMinTemp, windSpeed, humidity, pressure, visibility, weatherDetailsText;
    LinearLayout temperatureDetails;
    GridLayout weatherDetails;
    ProgressBar progress;

    CompositeDisposable compositeDisposable;
    OpenWeatherMap weatherService;

    static WeatherFragment fragmentInstance;

    public static WeatherFragment getInstance() {
        if(fragmentInstance == null) {
            fragmentInstance = new WeatherFragment();
        }
        return fragmentInstance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitRequest.getRetrofitInstance();
        weatherService = retrofit.create(OpenWeatherMap.class);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_weather, container, false);

        locatiom = view.findViewById(R.id.location);
        temperature = view.findViewById(R.id.temperature);
        feelsLike = view.findViewById(R.id.feels_like);
        maxMinTemp = view.findViewById(R.id.max_min_temp);

        windSpeed = view.findViewById(R.id.wind_speed);
        humidity = view.findViewById(R.id.humidity);
        pressure = view.findViewById(R.id.pressure);
        visibility = view.findViewById(R.id.visibility);

        temperatureDetails = view.findViewById(R.id.temperature_details);
        weatherDetailsText = view.findViewById(R.id.weather_details_text);
        weatherDetails = view.findViewById(R.id.weather_details);
        progress = view.findViewById(R.id.progress);

        getWeatherInfo();

        return view;
    }

    private void getWeatherInfo() {
        compositeDisposable.add(weatherService.getWeatherByGeoCoord(String.valueOf(Data.currentLocation.getLatitude()),
                String.valueOf(Data.currentLocation.getLongitude()),
                Data.appID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherModel>() {
                    @Override
                    public void accept(WeatherModel weatherModel) throws Exception {
                        locatiom.setText(new StringBuilder("For ").append(weatherModel.getName()).toString());

                        int temp = (int) Math.round(weatherModel.getMain().getTemp());
                        int tempFeelsLike = (int) Math.round(weatherModel.getMain().getFeels_like());
                        int maxTemp = (int) Math.round(weatherModel.getMain().getTemp_max());
                        int minTemp = (int) Math.round(weatherModel.getMain().getTemp_min());
                        temperature.setText(new StringBuilder(
                                String.valueOf(temp)).append("°C").toString());
                        feelsLike.setText(new StringBuilder("Feels like ").append(String.valueOf(tempFeelsLike)).append("°C | ").
                                append(String.valueOf(weatherModel.getWeather().get(0).getMain())).toString());
                        maxMinTemp.setText(new StringBuilder("Max. temp: ").append(String.valueOf(maxTemp)).append("°C | Min. temp: ")
                                .append(String.valueOf(minTemp)).append("°C").toString());

                        windSpeed.setText(new StringBuilder(String.valueOf(weatherModel.getWind().getSpeed())).toString());
                        pressure.setText(new StringBuilder(String.valueOf(weatherModel.getMain().getPressure())).toString());
                        humidity.setText(new StringBuilder(String.valueOf(weatherModel.getMain().getHumidity())).toString());
                        visibility.setText(new StringBuilder(String.valueOf(weatherModel.getVisibility())).toString());

                        locatiom.setVisibility(View.VISIBLE);
                        temperatureDetails.setVisibility(View.VISIBLE);
                        weatherDetailsText.setVisibility(View.VISIBLE);
                        weatherDetails.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }
}