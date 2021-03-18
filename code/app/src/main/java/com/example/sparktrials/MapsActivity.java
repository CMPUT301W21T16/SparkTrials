package com.example.sparktrials;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sparktrials.main_ui.publish.PublishFragment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.GeoMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends AppCompatActivity {

    private GeoMap map;

    private Button applyLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        displayBackButton();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment_map_activity);

        map = new GeoMap(true);

        mapFragment.getMapAsync(map);

        applyLocation = findViewById(R.id.choose_location_button);
        applyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackCoords(true);
            }
        });

    }

    private void displayBackButton() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.map_activity_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendBackCoords(false);
    }

    private void sendBackCoords(boolean hasPickedLocation) {
        //Intent goBackToPublishFragment = new Intent(this, MainActivity.class);
        if (hasPickedLocation) {
            Intent sendLocationBack = new Intent(this, MainActivity.class);
            GeoLocation markedLocation = map.getGeoLocation();
            sendLocationBack.putExtra("Latitude", markedLocation.getLat());
            sendLocationBack.putExtra("Longitude", markedLocation.getLon());

            int didPickLocation = (int) getIntent().getExtras().get("LOCATION_PICKED");
            setResult(didPickLocation, sendLocationBack);
        } else {
            int didNotPickLocation = (int) getIntent().getExtras().get("NO_LOCATION_PICKED");
            setResult(didNotPickLocation);
        }
        finish();
    }
}