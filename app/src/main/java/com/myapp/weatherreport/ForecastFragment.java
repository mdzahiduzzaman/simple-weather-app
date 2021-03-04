package com.myapp.weatherreport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.weatherreport.Adapter.ForecastAdapter;
import com.myapp.weatherreport.Data.Data;
import com.myapp.weatherreport.Model.ForecastModel;
import com.myapp.weatherreport.Retrofit.OpenWeatherMap;
import com.myapp.weatherreport.Retrofit.RetrofitRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    OpenWeatherMap service;

    TextView location;
    RecyclerView recyclerView;

    static ForecastFragment fragmentInstance;

    public static ForecastFragment getInstance() {
        if(fragmentInstance == null) {
            fragmentInstance = new ForecastFragment();
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

    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitRequest.getRetrofitInstance();
        service = retrofit.create(OpenWeatherMap.class);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
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
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        location = view.findViewById(R.id.location);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getForecastInfo();

        return view;
    }

    private void getForecastInfo() {
        compositeDisposable.add(service.getForecastByGeoCoord(String.valueOf(Data.currentLocation.getLatitude()),
                String.valueOf(Data.currentLocation.getLongitude()),
                Data.appID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForecastModel>() {
                    @Override
                    public void accept(ForecastModel forecastModel) throws Exception {
                        displayForecast(forecastModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", ""+throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecast(ForecastModel forecastModel) {
        location.setText(new StringBuilder("For ").append(forecastModel.city.name));
        ForecastAdapter forecastAdapter = new ForecastAdapter(getContext(), forecastModel);
        recyclerView.setAdapter(forecastAdapter);
    }
}