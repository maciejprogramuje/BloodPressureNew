package com.maciejprogramuje.facebook.bloodpressurenew.screens.input;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.maciejprogramuje.facebook.bloodpressurenew.R;
import com.maciejprogramuje.facebook.bloodpressurenew.dbsql.DbAdapter;
import com.maciejprogramuje.facebook.bloodpressurenew.screens.main.MainActivity;
import com.maciejprogramuje.facebook.bloodpressurenew.screens.main.OneMeasurement;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDataActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.done_fab)
    FloatingActionButton fab;
    @BindView(R.id.sysNumberPicker)
    NumberPicker sysNumberPicker;
    @BindView(R.id.diaNumberPicker)
    NumberPicker diaNumberPicker;
    @BindView(R.id.pulseNumberPicker)
    NumberPicker pulseNumberPicker;
    @BindView(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar.setTitle(getString(R.string.enter_mesurement));
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSysNumberPickerLayout(sysNumberPicker);
        setDiaNumberPickerLayout(diaNumberPicker);
        setPulseNumberPickerLayout(pulseNumberPicker);
    }

    private void setSysNumberPickerLayout(NumberPicker numberPicker) {
        setCommonNumberPickerLayout(numberPicker);
        numberPicker.setValue(120);
    }

    private void setDiaNumberPickerLayout(NumberPicker numberPicker) {
        setCommonNumberPickerLayout(numberPicker);
        numberPicker.setValue(80);
    }

    private void setPulseNumberPickerLayout(NumberPicker numberPicker) {
        setCommonNumberPickerLayout(numberPicker);
        numberPicker.setValue(60);
    }

    private void setCommonNumberPickerLayout(NumberPicker numberPicker) {
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);
        setDividerColor(numberPicker, R.color.dividerColor);
    }

    @OnClick(R.id.done_fab)
    public void onViewClicked(View view) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        String localTime = dateFormat.format(date);
        int sys = sysNumberPicker.getValue();
        int dia = diaNumberPicker.getValue();
        int pulse = pulseNumberPicker.getValue();

        OneMeasurement oneMeasurement = new OneMeasurement(localTime, sys, dia, pulse);
        DbAdapter.addOneMeasurementToDb(this, oneMeasurement);

        startActivity(new Intent(this, MainActivity.class));
    }

    private void setDividerColor(NumberPicker picker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | IllegalAccessException | Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
