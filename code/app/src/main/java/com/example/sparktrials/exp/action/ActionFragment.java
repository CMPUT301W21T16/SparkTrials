package com.example.sparktrials.exp.action;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private ActionFragmentManager manager;
    int numPasses;
    int numFailures;
    public ActionFragment(Experiment experiment){
        Log.d("TYPE=",experiment.getType());
        this.manager= new ActionFragmentManager(experiment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_action, container, false);
        Button leftButton = view.findViewById(R.id.action_bar_pass);
        Button rightButton = view.findViewById(R.id.action_bar_fail);
        Button uploadButton = view.findViewById(R.id.action_bar_upload_trials);
        Button recordNumButton = view.findViewById(R.id.action_bar_recordnum);
        Button generateQR = view.findViewById(R.id.action_bar_generateQR);
        Button deleteTrials = view.findViewById(R.id.action_bar_delete_trials);
        EditText valueEditText = view.findViewById(R.id.countvalue_editText);
        Button incrementCount = view.findViewById(R.id.action_bar_incrementCount);
        String trials=manager.getNTrials();
        int minimumNumberTrials = manager.getMinNTrials();
        trialsNumber=view.findViewById(R.id.trials_completed);
        if (minimumNumberTrials>0)
            trialsNumber.setText("Trials completed: "+trials+"/"+minimumNumberTrials);
        else
            trialsNumber.setText("Trials completed: "+trials);
        if (manager.getType().equals("binomial trials".toLowerCase())){
            leftButton.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addBinomialTrial(true);
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addBinomialTrial(false);
                }
            });
        }
        else if(manager.getType().equals("Non-Negative Integer Counts".toLowerCase())){
            recordNumButton.setVisibility(View.VISIBLE);
            valueEditText.setVisibility(View.VISIBLE);
        }
        else if(manager.getType().equals("Measurement Trials".toLowerCase())){
            recordNumButton.setVisibility(View.VISIBLE);
            valueEditText.setVisibility(View.VISIBLE);
            valueEditText.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
        else if(manager.getType().equals("Counts".toLowerCase())){
            incrementCount.setVisibility(View.VISIBLE);
        }
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deleteTrials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}