package com.example.sparktrials.main_ui.publish;

import android.util.Log;

import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialCount;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The class which manages the experiment the user wishes to publish and handles all logic associated with the task
 */
public class PublishFragmentManager {
    private int minNTrials;
    private GeoLocation geoLocation;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Profile profile;
    private Experiment experiment;

    /**
     *Constructor which receives the input from the UI and converts the minimum number of trials into int and lat/lon pair into double
     * Then accesses firebase and retrieves the name and cellphone associated with the UUID
     * @param userID
     * The UUID of the user publishing the experiment
     * @param desc
     * The Experiment Description
     * @param title
     * The experiment title
     * @param MinNTrialsString
     * The string containing an integer describing the minimum number of trials
     * @param latString
     * String containing the latitude
     * @param lonString
     * String containing the longitude
     */
    public PublishFragmentManager(String userID,String desc, String title, String MinNTrialsString,String latString, String lonString,String experimentType){
        try {
            minNTrials = Integer.parseInt(MinNTrialsString);
        }catch(NumberFormatException e){
            minNTrials = 0;
        }
        try{
            double lat = Double.parseDouble(latString);
            double lon = Double.parseDouble(lonString);
            geoLocation= new GeoLocation(lat,lon);
        }catch (NumberFormatException e){
            geoLocation = new GeoLocation();
        }
        firestore.collection("users").document(userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        String name= (String) document.get("name");
                        String cellphone = (String) document.get("cellphone");
                        String experimentID= UUID.randomUUID().toString();
                        profile = new Profile(userID,name,cellphone);
                        experiment= new Experiment(experimentID,experimentType,profile,title,desc,geoLocation,minNTrials);
                        Log.d("Data", document.getId() + " => " + document.getData());
                        FirebaseManager manager = new FirebaseManager();
                        manager.uploadExperiment(experiment,geoLocation,profile);
                    }
                });
    }
}
