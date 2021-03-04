package com.myapp.weatherreport.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.weatherreport.Data.Data;
import com.myapp.weatherreport.Model.ForecastModel;
import com.myapp.weatherreport.R;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;
    ForecastModel model;

    public ForecastAdapter(Context context, ForecastModel model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dateTime.setText(new StringBuilder(Data.convertToDate(model.list.get(position).dt)));

        int temp = (int) Math.round(model.list.get(position).main.getTemp());
        int tempFeelsLike = (int) Math.round(model.list.get(position).main.getFeels_like());
        int maxTemp = (int) Math.round(model.list.get(position).main.getTemp_max());
        int minTemp = (int) Math.round(model.list.get(position).main.getTemp_min());

        holder.temperature.setText(new StringBuilder(String.valueOf(temp)).append("°C"));
        holder.feelsLike.setText(new StringBuilder("Feels like ").append(String.valueOf(tempFeelsLike)).append("°C | ")
                .append(String.valueOf(model.list.get(position).weather.get(0).getMain())).toString());
        holder.maxMinTemp.setText(new StringBuilder("Max temp: ").append(String.valueOf(maxTemp)).append("°C | Min temp: ")
                .append(String.valueOf(minTemp)).append("°C").toString());
    }

    @Override
    public int getItemCount() {
        return model.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime, temperature, feelsLike, maxMinTemp;
        public MyViewHolder(View view) {
            super(view);

            dateTime = view.findViewById(R.id.forecast_datetime);
            temperature = view.findViewById(R.id.forecast_temperature);
            feelsLike = view.findViewById(R.id.forecast_feels_like);
            maxMinTemp = view.findViewById(R.id.forecast_max_min_temp);
        }
    }
}
