package com.example.testweatherproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testweatherproject.Model.Place;
import com.example.testweatherproject.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Place> data;
    private OnSearchResultClickListener onSearchResultClickListener;

    public RecyclerViewAdapter(Context context, List data, OnSearchResultClickListener onSearchResultClickListener){
        this.mContext = context;
        this.data = data;
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.place_row_item, parent, false);

        return new MyViewHolder(view, onSearchResultClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.pText.setText(data.get(position).getText());
            holder.place_name.setText(data.get(position).getPlace_name());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView pText;
        TextView place_name;
        OnSearchResultClickListener onSearchResultClickListener;

        public MyViewHolder(View itemView, OnSearchResultClickListener onSearchResultClickListener) {
            super(itemView);
            pText = itemView.findViewById(R.id.ptext);
            place_name = itemView.findViewById(R.id.place_name);

            itemView.setOnClickListener(this);
            this.onSearchResultClickListener = onSearchResultClickListener;
        }

        @Override
        public void onClick(View v) {
            onSearchResultClickListener.onSearchResultClick(getAdapterPosition());
        }
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(int position);
    }

}
