package com.maciejprogramuje.facebook.bloodpressurenew.screens.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciejprogramuje.facebook.bloodpressurenew.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter {
    private ArrayList<OneMeasurement> measurements = new ArrayList<>();
    private RecyclerView mainRecyclerView;

    private class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView sysTextView;
        public TextView diaTextView;
        public TextView pulseTextView;

        public MainViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            sysTextView = itemView.findViewById(R.id.sysTextView);
            diaTextView = itemView.findViewById(R.id.diaTextView);
            pulseTextView = itemView.findViewById(R.id.pulseTextView);
        }
    }

    public MainAdapter(ArrayList<OneMeasurement> measurements, RecyclerView mainRecyclerView) {
        this.measurements = measurements;
        this.mainRecyclerView = mainRecyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_measurement_layout, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionToDelete = mainRecyclerView.getChildAdapterPosition(view);
                measurements.remove(positionToDelete);
                notifyItemRemoved(positionToDelete);
            }
        });
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OneMeasurement oneMeasurement = measurements.get(position);
        ((MainViewHolder) holder).dateTextView.setText(oneMeasurement.getDate());
        ((MainViewHolder) holder).sysTextView.setText(oneMeasurement.getSys());
        ((MainViewHolder) holder).diaTextView.setText(oneMeasurement.getDia());
        ((MainViewHolder) holder).pulseTextView.setText(oneMeasurement.getPulse());
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }
}
