package com.example.sparktrials.exp.action;


import android.util.Log;

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

    /**
     * Adds a binomial trial to the experiment
     * @param result
     */
    public void addBinomialTrial(Boolean result){
        TrialBinomial trial = new TrialBinomial(result);
        experiment.addTrial(trial);
    }

    /**
     * Adds a non negative Integer count trial to the experiment
     * @param result
     */
    public void addNonNegIntTrial(Integer result){
        TrialIntCount trial = new TrialIntCount(result);
        experiment.addTrial(trial);
    }

    /**
     * Adds a measurement trial to the experiment
     * @param result
     */
    public void addMeasurmentTrial(Double result){
        TrialMeasurement trial = new TrialMeasurement(result);
        experiment.addTrial(trial);
    }

    /**
     * Adds a count trial to the experiment
     */
    public void addCountTrial(){
        TrialCount trial = new TrialCount();
        experiment.addTrial(trial);
    }

    /**
     * Returns the number of trials in the experiment
     * @return
     */
    public String getNTrials(){
        return experiment.getNumTrials();
    }

    /**
     * Returns the minimum number of trials of the experiment
     * @return
     */
    public int getMinNTrials(){
        return experiment.getMinNTrials();
    }

    /**
     * Returns the experiment type
     * @return
     */
    public String getType(){
        return experiment.getType();
    }

    /**
     * TO DO: Uploads the trials to firbase
     */
    public void uploadTrials(){};



    /**
     * Removes all trials inserted by the user from the experiment object
     */
    public void deleteTrials(){
        int elementsToRemove=(Integer.parseInt(experiment.getNumTrials()) - originalNTrials);
        for (int i=0;i<elementsToRemove;i++){
            experiment.delTrial(experiment.getAllTrials().size()-1);
        }
    }

}
