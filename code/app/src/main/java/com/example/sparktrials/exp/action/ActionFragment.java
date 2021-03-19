package com.example.sparktrials.exp.action;

import android.app.AlertDialog;
import android.content.Context;
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

import com.example.sparktrials.ExperimentActivity;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.MainActivity;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;

import org.jetbrains.annotations.NotNull;

public class ActionFragment extends Fragment {
    View view;
    TextView trialsNumber;
    TextView trialsCount;
    int count;
    private ActionFragmentManager manager;
    private IdManager idManager;
    String id;
    public ActionFragment(Experiment experiment){
        this.manager= new ActionFragmentManager(experiment);
    }

    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        idManager= new IdManager(context);
        id= idManager.getUserId();
        manager.setProfile(id);
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
        updateView();
        if (manager.getType().equals("binomial trials".toLowerCase())){
            leftButton.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addBinomialTrial(true);
                    updateView();
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addBinomialTrial(false);
                    updateView();
                }
            });
        }
        else if(manager.getType().equals("Non-Negative Integer Counts".toLowerCase())){
            recordNumButton.setVisibility(View.VISIBLE);
            valueEditText.setVisibility(View.VISIBLE);
            recordNumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String valueString = valueEditText.getText().toString();
                    Integer result;
                    try{
                        result= Integer.parseInt(valueString);
                        manager.addNonNegIntTrial(result);
                        updateView();
                    }catch (NumberFormatException e) {
                        AlertDialog builder = new AlertDialog.Builder(getContext())
                                .setTitle("ERROR")
                                .setMessage("You must enter a number")
                                .setPositiveButton("OK",null)
                                .show();
                    }
                }
            });
        }
        else if(manager.getType().equals("Measurement Trials".toLowerCase())){
            recordNumButton.setVisibility(View.VISIBLE);
            valueEditText.setVisibility(View.VISIBLE);
            valueEditText.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
            recordNumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double result;
                    String valueString = valueEditText.getText().toString();
                    try{
                        result= Double.parseDouble(valueString);
                        manager.addMeasurementTrial(result);
                        updateView();
                    }catch (NumberFormatException e) {
                        AlertDialog builder = new AlertDialog.Builder(getContext())
                                .setTitle("ERROR")
                                .setMessage("You must enter a number")
                                .setPositiveButton("OK",null)
                                .show();
                    }
                }
            });
        }
        else if(manager.getType().equals("Counts".toLowerCase())){
            leftButton.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setText("Add Count");
            rightButton.setText("Commit Trial");
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count+=1;
                    updateView();
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addCountTrial(count);
                    count=0;
                    updateView();
                }
            });
        }
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.uploadTrials();
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
                manager.deleteTrials();
                updateView();
            }
        });
        return view;
    }
    public void updateView(){
        int trials=manager.getNTrials();
        Log.d("NUM Is", String.valueOf(trials));
        int minimumNumberTrials = manager.getMinNTrials();
        trialsNumber=view.findViewById(R.id.trials_completed);
        trialsCount=view.findViewById(R.id.trials_count);
        if(manager.getType().equals("Counts".toLowerCase())){
            trialsCount.setText("Trial count: "+count);
        }
        if (minimumNumberTrials>0)
            trialsNumber.setText("Trials completed: "+trials+"/"+minimumNumberTrials);
        else
            trialsNumber.setText("Trials completed: "+trials);
    }
}