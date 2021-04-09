package com.example.sparktrials.exp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.example.sparktrials.models.TrialCount;
import com.example.sparktrials.models.TrialIntCount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DraftManager {
    private ArrayList<Trial> draft_trials;
    private Context context;
    private Experiment experiment;

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public DraftManager(Context context) {
        this.context = context;
        this.draft_trials = new ArrayList<Trial>();
    }

    public ArrayList<Trial> getDraft_trials() {
        loadData(experiment.getType());
        return draft_trials;
    }


    public void setDraft_trials(ArrayList<Trial> draft_trials) {
        this.draft_trials = draft_trials;
    }
    public void addDraft(Trial trial){
        loadData(experiment.getType());
        draft_trials.add(trial);
        saveData();
    }

    private void saveData() {
        String experimentID = experiment.getId();
        SharedPreferences sharedPreferences = context.getSharedPreferences(experimentID,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(draft_trials);
        editor.putString(experimentID, json);
        editor.apply();
    }
    private void loadData(String typeTrial) {
        String experimentID = experiment.getId();
        SharedPreferences sharedPreferences = context.getSharedPreferences(experimentID,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(experimentID, null);
        switch (typeTrial.toLowerCase()){
            case "binomial trials":
                draft_trials = gson.fromJson(json, new TypeToken<ArrayList<TrialBinomial>>() {}.getType());

            case "counts":
                draft_trials = gson.fromJson(json, new TypeToken<ArrayList<TrialCount>>() {}.getType());
            case "non-negative integer counts":
                draft_trials = gson.fromJson(json, new TypeToken<ArrayList<TrialIntCount>>() {}.getType());
            case "measurement trials":
                draft_trials = gson.fromJson(json, new TypeToken<ArrayList<TrialIntCount>>() {}.getType());
        }

        if (draft_trials == null){
            draft_trials = new ArrayList<>();
        }
    }


    public void deleteDrafts() {
        draft_trials = new ArrayList<>();
        saveData();
    }
}
