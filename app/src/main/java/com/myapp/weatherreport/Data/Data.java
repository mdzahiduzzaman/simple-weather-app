package com.myapp.weatherreport.Data;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    public static final String appID = "4567261870a8d87dd139828dab082338";
    public static final String latitude = "23.7104";
    public static final String longitude = "90.4074";
    public static Location currentLocation = null;

    public static String convertToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, d MMM yyyy");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}
