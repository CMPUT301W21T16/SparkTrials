package com.example.sparktrials.models;

public class TrialCount {
    Integer count;

    public TrialCount(Integer count){
        this.count = count;
    }

    public void addCount() {
        this.count++;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
