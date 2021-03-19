package com.example.sparktrials.exp.action;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.example.sparktrials.models.TrialCount;
import com.example.sparktrials.models.TrialIntCount;
import com.example.sparktrials.models.TrialMeasurement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * When a user decides to upload a trial or generate a qr code or delete a trial action
 * fragment manager deals with that
 */
public class ActionFragmentManager {
    Experiment experiment;
    FirebaseManager firebaseManager = new FirebaseManager();
    int originalNTrials;
    String id;
    Profile profile;
    public ActionFragmentManager(Experiment experiment) {
        this.id=id;
        this.experiment=experiment;
        this.originalNTrials=Integer.parseInt(experiment.getNumTrials());
    }
    public void setProfile(String id){
        profile=firebaseManager.downloadProfile(id);
    }

    /**
     * Adds a binomial trial to the experiment
     * @param result
     */
    public void addBinomialTrial(Boolean result){
        TrialBinomial trial = new TrialBinomial(result);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        experiment.addTrial(trial);
    }

    /**
     * Adds a non negative Integer count trial to the experiment
     * @param result
     */
    public void addNonNegIntTrial(Integer result){
        TrialIntCount trial = new TrialIntCount(result);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        experiment.addTrial(trial);
    }

    /**
     * Adds a measurement trial to the experiment
     * @param result
     */
    public void addMeasurementTrial(Double result){
        TrialMeasurement trial = new TrialMeasurement(result);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        experiment.addTrial(trial);
    }

    /**
     * Adds a count trial to the experiment
     */
    public void addCountTrial(Integer count){
        TrialCount trial = new TrialCount();
        trial.setCount(count);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        experiment.addTrial(trial);
    }

    /**
     * Returns the number of trials in the experiment
     * @return
     */
    public Integer getNTrials(){
        return experiment.getUserTrials(profile.getId()).size();
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
    public void uploadTrials(){
        firebaseManager.uploadTrials(experiment);
    }
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
