package com.maciejprogramuje.facebook.bloodpressurenew.screens.input;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;

import com.maciejprogramuje.facebook.bloodpressurenew.R;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputDataActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.sysNumberPicker)
    NumberPicker sysNumberPicker;
    @BindView(R.id.diaNumberPicker)
    NumberPicker diaNumberPicker;
    @BindView(R.id.pulseNumberPicker)
    NumberPicker pulseNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setNumberPickerLayout(sysNumberPicker);
        setNumberPickerLayout(diaNumberPicker);
        setNumberPickerLayout(pulseNumberPicker);
    }

    private void setNumberPickerLayout(NumberPicker numberPicker) {
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);
        numberPicker.setValue(100);
        setDividerColor(numberPicker, R.color.colorPrimaryDark);
    }

    @OnClick(R.id.fab)
    public void onViewClicked(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
