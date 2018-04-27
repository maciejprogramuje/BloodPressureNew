package com.maciejprogramuje.facebook.bloodpressurenew.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maciejprogramuje.facebook.bloodpressurenew.R;
import com.maciejprogramuje.facebook.bloodpressurenew.screens.input.InputDataActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainTextView)
    TextView mainTextView;
    @BindView(R.id.mainRecyclerView)
    RecyclerView mainRecyclerView;
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.blood_pressure_diary));
        setSupportActionBar(toolbar);

        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<OneMeasurement> measurements = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            measurements.add(new OneMeasurement("2018-04-26", "" + i, "" + (20 - i), "60"));
        }

        mainRecyclerView.setAdapter(new MainAdapter(measurements, mainRecyclerView));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.main_fab)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_fab:
                startActivity(new Intent(this, InputDataActivity.class));
                break;
        }
    }
}
