package com.example.sparktrials.exp.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.example.sparktrials.MapsFragment;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    View view;
    Experiment experiment;

    SupportMapFragment map;
    GoogleMap testMap;

    public LocationFragment(Experiment experiment){
        this.experiment = experiment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        map = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment_experiment_activity);

        map.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        testMap = googleMap;

        double[] uOfACoords = {53.5232, -113.5263};
        LatLng test = new LatLng(uOfACoords[0], uOfACoords[1]);

        testMap.addMarker(new MarkerOptions()
                .position(test)
                .title("Marker of UofA"));

        float zoomLevel = 13f;
        testMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, zoomLevel));
    }
}
