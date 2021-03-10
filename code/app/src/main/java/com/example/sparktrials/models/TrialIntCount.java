package com.example.sparktrials.models;

public class TrialIntCount {
    Integer count;

    public TrialIntCount(Integer count){
        if (count>0) {
            this.count = count;
        } else {
            this.count = 0;
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        if (count>0){
            this.count = count;
        }
    }
}
