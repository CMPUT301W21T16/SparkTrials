package com.example.sparktrials.exp.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

public class LocationFragment extends Fragment {
    View view;
    Experiment experiment;
    public LocationFragment(Experiment experiment){
        this.experiment = experiment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        return view;
    }
}
