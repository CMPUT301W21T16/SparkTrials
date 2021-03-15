package com.example.sparktrials.exp.admin;

import androidx.lifecycle.ViewModel;

import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;

import java.util.ArrayList;

public class AdminViewModel extends ViewModel {
    private ArrayList<Trial> trialList = new ArrayList<>();
    private ArrayList<Profile> userList = new ArrayList<>();

    public AdminViewModel() {
        Trial testTrial1 = new TrialBinomial(true);
        testTrial1.setId(1);
        Profile user1 = new Profile("1cd3a4d4-4bec-47b1-9fc6-bbd96be600f4");
        user1.setUsername("TestUser1");
        testTrial1.setProfile(user1);
        Trial testTrial2 = new TrialBinomial(false);
        testTrial2.setId(2);
        testTrial2.setProfile(user1);
        Trial testTrial3 = new TrialBinomial(true);
        testTrial3.setId(3);
        Profile user2 = new Profile("d4d6f42b-dbcf-4d5a-9c4f-66f0a51d7fbd");
        user2.setUsername("TestUser2");
        testTrial3.setProfile(user2);
        trialList.add(testTrial1);
        trialList.add(testTrial2);
        trialList.add(testTrial3);
        for(Trial trial: trialList){
            if(userList.contains(trial.getProfile())){
                continue;
            } else {
                userList.add(trial.getProfile());
            }
        }
    }

    public ArrayList<Trial> getTrialList(){
        return trialList;
    }

    public ArrayList<Profile> getUserList(){
        return userList;
    }

}
