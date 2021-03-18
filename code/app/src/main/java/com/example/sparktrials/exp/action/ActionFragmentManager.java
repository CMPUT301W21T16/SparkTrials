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
    int originalNTrials;
    public ActionFragmentManager(Experiment experiment) {
        this.experiment=experiment;
        this.expType=expType;
        this.originalNTrials=Integer.parseInt(experiment.getNumTrials());
    }
    public void addBinomialTrial(Boolean result){
        TrialBinomial trial = new TrialBinomial(result);
        experiment.addTrial(trial);
    }
    public void addNonNegIntTrial(Integer result){
        TrialIntCount trial = new TrialIntCount(result);
        experiment.addTrial(trial);
    }
    public void addMeasurmentTrial(Double result){
        TrialMeasurement trial = new TrialMeasurement(result);
        experiment.addTrial(trial);
    }
    public void addCountTrial(){
        TrialCount trial = new TrialCount();
        experiment.addTrial(trial);
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

//    public void deleteTrial(){
//        int elementsToRemove=(Integer.parseInt(experiment.getNumTrials()) - originalNTrials);
//        for (int i=0;i<elementsToRemove;i++){
//            //Waiting for pop attribute in experiment
//            experiment.removeLastTrial();
//        }
//    }

}
