package com.example.sparktrials.exp.action;


import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.example.sparktrials.models.TrialCount;
import com.example.sparktrials.models.TrialIntCount;
import com.example.sparktrials.models.TrialMeasurement;

import java.util.ArrayList;

/**
 * When a user decides to upload a trial or generate a qr code or delete a trial action
 * fragment manager deals with that
 */
public class ActionFragmentManager {
    String expType;
    Experiment experiment;
    ArrayList<Trial> trials = new ArrayList<>();
    public ActionFragmentManager(Experiment experiment) {
        this.experiment=experiment;
        this.expType=expType;
    }
    public void addBinomialTrial(Boolean result){
        TrialBinomial trial = new TrialBinomial(result);
        trials.add(trial);
    }
    public void addNonNegIntTrial(Integer result){
        TrialIntCount trial = new TrialIntCount(result);
        trials.add(trial);
    }
    public void addMeasurmentTrial(Double result){
        TrialMeasurement trial = new TrialMeasurement(result);
        trials.add(trial);
    }
    public void addCountTrial(){
        TrialCount trial = new TrialCount();
        trials.add(trial);
    }

    public String getNTrials(){
        return experiment.getNumTrials();
    }
    public int getMinNTrials(){
        return experiment.getMinNTrials();
    }
    public String getType(){
        return experiment.getType();
    }

    public void uploadTrial(){};

}
