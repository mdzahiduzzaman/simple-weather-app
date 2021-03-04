package com.myapp.weatherreport.Model;

import java.util.List;

public class Forecast {
    public int dt;
    public Main main;
    public List<Weather> weather;
    public Clouds clouds;
    public Wind wind;
    public int visibility;
    public int pop;
    public Sys sys;
    public String dt_txt;
}
