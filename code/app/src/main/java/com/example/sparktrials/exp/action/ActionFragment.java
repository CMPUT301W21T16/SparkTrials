package com.example.sparktrials.exp.action;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;

public class ActionFragment extends Fragment {
    View view;
    TextView trialsNumber;
    private Experiment experiment;
    int numPasses;
    int numFailures;
    public ActionFragment(Experiment experiment){
        Profile profile = new Profile("lol");
        GeoLocation geoLocation = new GeoLocation(1.1,1.1);
        Experiment experiment = new Experiment("aaaa","Binomial Trials",profile,"lol","aaaa",geoLocation,43);
        Log.d("TYPE=",experiment.getType());
        this.experiment = experiment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action, container, false);
        Button passButton = view.findViewById(R.id.action_bar_pass);
        Button failButton = view.findViewById(R.id.action_bar_fail);
        if (experiment.getType().equals("binomial trials".toLowerCase())){
            passButton.setVisibility(View.VISIBLE);
            failButton.setVisibility(View.VISIBLE);
            trialsNumber=view.findViewById(R.id.trials_completed);
            String trials=experiment.getNumTrials();
            trialsNumber.setText("Trials completed:"+trials+"/"+experiment.getMinNTrials().toString());
            passButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numPasses+=1;
                }
            });
            failButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numFailures+=1;
                }
            });
        }
        return view;
    }
}