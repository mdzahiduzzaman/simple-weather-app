package com.myapp.weatherreport.Retrofit;

import com.myapp.weatherreport.Model.ForecastModel;
import com.myapp.weatherreport.Model.WeatherModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMap {
    @GET("weather")
    Observable<WeatherModel> getWeatherByGeoCoord(@Query("lat") String lat,
                                                  @Query("lon") String lon,
                                                  @Query("appid") String appid,
                                                  @Query("units") String unit);

    @GET("forecast")
    Observable<ForecastModel> getForecastByGeoCoord(@Query("lat") String lat,
                                                   @Query("lon") String lon,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);
}
