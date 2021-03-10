package com.example.sparktrials.models;

import java.util.ArrayList;

public class Experiment {
    private Profile owner;
    private String experimentID;
    private ArrayList<Trial> trials;
    private String title;
    private String desc;
    private GeoLocation region;
    private Integer minNTrials;

    public Experiment(){
        this.owner = null;
        this.trials = new ArrayList<>();
        this.title = "N/A";
        this.desc = "N/A";
        this.region = null;
        this.minNTrials = 0;
    }

    public Experiment(Profile owner, String title, String desc, GeoLocation region, Integer minNTrials){
        this.owner = owner;
        this.title = title;
        this.desc = desc;
        this.region = region;
        this.minNTrials = minNTrials;
    }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    public Trial getTrial(Integer id) {
        for (int i=0; i < this.trials.size(); i++){
            if (this.trials.get(i).getId()==id){
                return this.trials.get(i);
            }
        }

        return null;
    }

    public void addTrial(Trial trial){
        this.trials.add(trial);
    }

    public ArrayList<Trial> getTrials(ArrayList<Integer> ids){
        ArrayList<Trial> chosenTrials = new ArrayList<>();
        for (int i=0; i < this.trials.size(); i++){
            for (int j=0; j < ids.size(); j++){
                if (this.trials.get(i).getId()==ids.get(j)){
                    chosenTrials.add(this.trials.get(i));
                    break;
                }
            }
        }
        return chosenTrials;
    }

    public void addTrials(ArrayList<Trial> trials) {
        for (int i=0; i < trials.size(); i++){
            this.trials.add(trials.get(i));
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public GeoLocation getRegion() {
        return region;
    }

    public void setRegion(GeoLocation region) {
        this.region = region;
    }

    public Integer getMinNTrials() {
        return minNTrials;
    }

    public void setMinNTrials(Integer minNTrials) {
        this.minNTrials = minNTrials;
    }
}
