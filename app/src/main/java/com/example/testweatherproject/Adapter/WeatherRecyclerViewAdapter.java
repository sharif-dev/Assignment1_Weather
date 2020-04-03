package com.example.testweatherproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testweatherproject.Model.Weather;
import com.example.testweatherproject.R;

import java.util.List;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Weather> data;

    public WeatherRecyclerViewAdapter(Context mContext, List<Weather> data){
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.weather_row_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String day = determineTheDayTextView(position);
        holder.dayTextView.setText(day);
        holder.temperatureMin.setText(data.get(position).getTemperatureMin());
        holder.temperatureMax.setText(data.get(position).getTemperatureMax());
//        holder.weatherIcon.setImageDrawable(Drawable.createFromXml());


        }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private String determineTheDayTextView(int position){
        String day = "";
        switch(position){
            case 0: {
                day = "Sunday";
                break;
            }
            case 1:{
                day = "Monday";
                break;
            }
            case 2:{
                day = "Tuesday";
                break;
            }
            case 3:{
                day = "Wednesday";
                break;
            }
            case 4:{
                day = "Thursday";
                break;
            }
            case 5:{
                day = "Friday";
                break;
            }
            case 6:{
                day = "Saturday";
                break;
            }
        }
        return day;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureMin;
        TextView temperatureMax;
        TextView dayTextView;
        ImageView weatherIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            temperatureMax = itemView.findViewById(R.id.temperatureMAX);
            temperatureMin = itemView.findViewById(R.id.temperatureMIN);
            dayTextView =  itemView.findViewById(R.id.dayTextView);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }

}
