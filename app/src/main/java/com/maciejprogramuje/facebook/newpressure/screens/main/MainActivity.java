package com.maciejprogramuje.facebook.newpressure.screens.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.maciejprogramuje.facebook.newpressure.R;
import com.maciejprogramuje.facebook.newpressure.dbsql.DbAdapter;
import com.maciejprogramuje.facebook.newpressure.screens.input.InputDataActivity;

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
    @BindView(R.id.adView)
    AdView adView;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MobileAds.initialize(this, "ca-app-pub-9139309714448232~5571873263");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar.setTitle(getString(R.string.blood_pressure_diary));
        setSupportActionBar(toolbar);

        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Cursor cursor = DbAdapter.getAllMeasurements(this);
        mainAdapter = new MainAdapter(cursor);
        mainRecyclerView.setAdapter(mainAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            mainAdapter.removeAllMeasurements(MainActivity.this);
            return true;
        } else if (id == R.id.action_share) {
            Cursor cursor = DbAdapter.getAllMeasurements(this);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getString(R.string.blood_pressure_diary))
                    .append(getString(R.string.end_line)).append(getString(R.string.end_line))
                    .append(getString(R.string.date)).append(getString(R.string.pause))
                    .append(getString(R.string.sys)).append(getString(R.string.pause))
                    .append(getString(R.string.dia)).append(getString(R.string.pause))
                    .append(getString(R.string.pulse)).append(getString(R.string.end_line));

            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_DATE));
                String sys = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_SYS));
                String dia = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_DIA));
                String pulse = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME_PULSE));

                stringBuilder.append(date).append(getString(R.string.pause))
                        .append(sys).append(getString(R.string.pause))
                        .append(dia).append(getString(R.string.pause))
                        .append(pulse).append(getString(R.string.end_line));
            }

            Log.w("UWAGA", stringBuilder.toString());

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.blood_pressure_diary));
            intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
            startActivity(intent);

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
