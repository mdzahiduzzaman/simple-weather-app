package com.myapp.weatherreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.myapp.weatherreport.Adapter.PagesAdapter;
import com.myapp.weatherreport.Data.Data;
import com.myapp.weatherreport.Model.WeatherModel;
import com.myapp.weatherreport.Retrofit.OpenWeatherMap;
import com.myapp.weatherreport.Retrofit.RetrofitRequest;

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private TabLayout myTabs;
    private ViewPager myPages;
    private CoordinatorLayout myLayout;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback callback;
    private LocationRequest request;
    private WeatherModel model;

    private CompositeDisposable compositeDisposable;
    private OpenWeatherMap weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLayout = findViewById(R.id.root_layout);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitRequest.getRetrofitInstance();
        weatherService = retrofit.create(OpenWeatherMap.class);

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallback();

                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(request, callback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        Snackbar.make(myLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                    }
                }).check();
    }

    private void buildLocationCallback() {
        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(4000);
        request.setFastestInterval(2000);
        request.setSmallestDisplacement(10.0f);
    }

    private void buildLocationRequest() {
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Data.currentLocation = locationResult.getLastLocation();
                myPages = findViewById(R.id.my_pages);
                setupPages(myPages);
                myTabs = findViewById(R.id.my_tabs);
                myTabs.setupWithViewPager(myPages);
            }
        };
    }

    private void setupPages(ViewPager myPages) {
        PagesAdapter adapter = new PagesAdapter(getSupportFragmentManager());
        adapter.addFragment(WeatherFragment.getInstance(), "Today");
        adapter.addFragment(ForecastFragment.getInstance(), "5 Days");
        myPages.setAdapter(adapter);
    }

}