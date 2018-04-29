package com.maciejprogramuje.facebook.bloodpressurenew.screens.main;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciejprogramuje.facebook.bloodpressurenew.R;
import com.maciejprogramuje.facebook.bloodpressurenew.sql.DbAdapter;
import com.maciejprogramuje.facebook.bloodpressurenew.sql.DbHelper;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerView mainRecyclerView;
    private Cursor cursor;

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

    public MainAdapter(Context context, RecyclerView mainRecyclerView, Cursor cursor) {
        this.context = context;
        this.mainRecyclerView = mainRecyclerView;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_measurement_layout, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) view.getTag();
            }
        });

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        long id = cursor.getLong(cursor.getColumnIndex(DbAdapter.DbEntry._ID));
        holder.itemView.setTag(id);

        String date = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_DATE));
        String sys = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_SYS));
        String dia = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_DIA));
        String pulse = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_PULSE));

        ((MainViewHolder) holder).dateTextView.setText(date);
        ((MainViewHolder) holder).sysTextView.setText(sys);
        ((MainViewHolder) holder).diaTextView.setText(dia);
        ((MainViewHolder) holder).pulseTextView.setText(pulse);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    void removeSpecyficMeasurement(long id) {
        DbAdapter.deleteOneMeasurementFromDb(context, id);
        swapCursor(DbAdapter.getAllMeasurements(context));
        DbAdapter.closeDb(context, new DbHelper(context));
    }

    private void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }
}
