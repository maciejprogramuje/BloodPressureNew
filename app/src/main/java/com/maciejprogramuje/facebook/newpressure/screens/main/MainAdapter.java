package com.maciejprogramuje.facebook.newpressure.screens.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciejprogramuje.facebook.newpressure.R;
import com.maciejprogramuje.facebook.newpressure.dbsql.DbAdapter;
import com.maciejprogramuje.facebook.newpressure.dbsql.DbHelper;

public class MainAdapter extends RecyclerView.Adapter {
    private Cursor cursor;

    private class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView sysTextView;
        public TextView diaTextView;
        public TextView pulseTextView;
        public TextView sysLabel;
        public TextView diaLabel;
        public TextView pulseLabel;

        MainViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            sysTextView = itemView.findViewById(R.id.sysTextView);
            diaTextView = itemView.findViewById(R.id.diaTextView);
            pulseTextView = itemView.findViewById(R.id.pulseTextView);
            sysLabel = itemView.findViewById(R.id.sysLabel);
            diaLabel = itemView.findViewById(R.id.diaLabel);
            pulseLabel = itemView.findViewById(R.id.pulseLabel);
        }
    }

    MainAdapter(Cursor cursor) {
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
        int sys = cursor.getInt(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_SYS));
        int dia = cursor.getInt(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_DIA));
        int pulse = cursor.getInt(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_PULSE));

        ((MainViewHolder) holder).dateTextView.setText(date);
        ((MainViewHolder) holder).sysTextView.setText(String.valueOf(sys));
        ((MainViewHolder) holder).diaTextView.setText(String.valueOf(dia));
        ((MainViewHolder) holder).pulseTextView.setText(String.valueOf(pulse));

        setColorSysLabel(holder, sys);
        setColorDiaLabel(holder, dia);
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
        notifyDataSetChanged();
    }

    public void removeAllMeasurements(final Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(R.string.delete_all_lines)
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbAdapter.deleteAllMesurementsFromDb(context);
                        swapCursor(DbAdapter.getAllMeasurements(context));
                        DbAdapter.closeDb(context, new DbHelper(context));
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alertBuilder.create().show();
        notifyDataSetChanged();
    }

    private void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    private void setColorSysLabel(RecyclerView.ViewHolder holder, int sys) {
        /*120 optymalne ciśnienie - zielony
        120–129 normalne ciśnienie - zielony
        130–139 prawidłowe wysokie ciśnienie - zółty
        140-159 łagodne nadciśnienie - jasny czerwony
        160-179 umiarkowane nadciśnienie - czerwony
        180 i powyżejostre nadciśnienie - ceimny czerwony*/

        int colorBackground;
        int colorText = R.color.secondaryTextColor;

        if (sys < 130) {
            colorBackground = R.color.green;
        } else if (sys < 140) {
            colorBackground = R.color.yellow;
        } else if (sys < 160) {
            colorBackground = R.color.red_light;
        } else if (sys < 180) {
            colorBackground = R.color.red;
            colorText = R.color.whiteText;
        } else {
            colorBackground = R.color.black;
            colorText = R.color.whiteText;
        }

        ((MainViewHolder) holder).sysLabel.setBackgroundResource(colorBackground);
        ((MainViewHolder) holder).sysTextView.setBackgroundResource(colorBackground);

        Context context = ((MainViewHolder) holder).sysLabel.getContext();
        ((MainViewHolder) holder).sysLabel.setTextColor(ContextCompat.getColor(context, colorText));
        ((MainViewHolder) holder).sysTextView.setTextColor(ContextCompat.getColor(context, colorText));
    }

    private void setColorDiaLabel(RecyclerView.ViewHolder holder, int dia) {
        /*80 optymalne ciśnienie
        80-84 normalne ciśnienie
        85-89 prawidłowe wysokie ciśnienie
        90-99 łagodne nadciśnienie
        100-109 umiarkowane nadciśnienie
        110 i powyżejostre nadciśnienie*/

        int colorBackground;
        int colorText = R.color.secondaryTextColor;

        if (dia < 85) {
            colorBackground = R.color.green;
        } else if (dia < 90) {
            colorBackground = R.color.yellow;
        } else if (dia < 100) {
            colorBackground = R.color.red_light;
        } else if (dia < 110) {
            colorBackground = R.color.red;
            colorText = R.color.whiteText;
        } else {
            colorBackground = R.color.black;
            colorText = R.color.whiteText;
        }

        ((MainViewHolder) holder).diaLabel.setBackgroundResource(colorBackground);
        ((MainViewHolder) holder).diaTextView.setBackgroundResource(colorBackground);

        Context context = ((MainViewHolder) holder).sysLabel.getContext();
        ((MainViewHolder) holder).diaLabel.setTextColor(ContextCompat.getColor(context, colorText));
        ((MainViewHolder) holder).diaTextView.setTextColor(ContextCompat.getColor(context, colorText));
    }
}
