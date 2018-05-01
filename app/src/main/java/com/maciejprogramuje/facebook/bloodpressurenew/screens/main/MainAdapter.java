package com.maciejprogramuje.facebook.bloodpressurenew.screens.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciejprogramuje.facebook.bloodpressurenew.R;
import com.maciejprogramuje.facebook.bloodpressurenew.dbsql.DbAdapter;
import com.maciejprogramuje.facebook.bloodpressurenew.dbsql.DbHelper;

public class MainAdapter extends RecyclerView.Adapter {
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

    public MainAdapter(Cursor cursor) {
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
                removeSpecyficMeasurement(id, view);
                notifyDataSetChanged();
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

    private void removeSpecyficMeasurement(final long id, final View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
        alertBuilder.setMessage(R.string.delete_this_line)
                .setPositiveButton(view.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbAdapter.deleteOneMeasurementFromDb(view.getContext(), id);
                        swapCursor(DbAdapter.getAllMeasurements(view.getContext()));
                        DbAdapter.closeDb(view.getContext(), new DbHelper(view.getContext()));
                    }
                })
                .setNegativeButton(view.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alertBuilder.create().show();
    }

    private void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }
}
