package com.example.sparktrials.exp.admin;

import androidx.lifecycle.ViewModel;

import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminViewModel extends ViewModel {

    FirebaseManager dbManager = new FirebaseManager();

    /**
     * Public Constructor for the AdminViewModel, currently creates it's own trial data for testing
     */
    public AdminViewModel() {

    }

    /**
     * Returns the list of users who have uploaded trials to this experiment
     * @return
     *  The list of users
     */
    public ArrayList<Profile> getUserList(ArrayList<Trial> trialList){
        ArrayList<Profile> userList = new ArrayList<>();
        for(Trial trial: trialList){
            if(userList.contains(trial.getProfile())){
                continue;
            } else {
                userList.add(trial.getProfile());
            }
        }
        return userList;
    }

    /**
     * Toggles the open attribute of the experiment in question
     * @param exp
     *  The Experiment to be opened or closed
     */
    public void toggleExpOpen(Experiment exp){
        Map<String, Object> map = new HashMap<>();
        map.put("Open", !exp.getOpen());

        dbManager.update("experiments", exp.getId(), map);
    }

    /**
     * Unpublish i.e. delete an experiment
     * @param id
     *  The id of the experiment being deleted
     */
    public void deleteExperiment(String id){
        dbManager.delete("experiments", id);
    }

}
