package com.example.sparktrials;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class ExperimentViewModel extends ViewModel {

    private MutableLiveData<Experiment> exp;
    private String expId;

    final String TAG = "Fetching Experiment...";

    /**
     * Constructor for SearchViewModel
     */
    public ExperimentViewModel() {
        exp = new MutableLiveData<>();
    }

    public void init(String id){
        expId = id;
        downloadExperiment();
    }

    /**
     * Gets the list of experiments in the database
     * @return
     *      Returns the list of experiments in the database
     */
    public MutableLiveData<Experiment> getExperiment() {
        return exp;
    }

    /**
     * Gets an updated list of experiments from the database.
     */
    private void downloadExperiment() {

        FirebaseManager manager = new FirebaseManager();
        manager.get("experiments", expId, expData -> {
            Experiment experiment = new Experiment(expId);

            Log.d(TAG, expData.getId() + " => " + expData.getData());
            experiment.setTitle((String) expData.getData().get("Title"));
            experiment.setDesc((String) expData.getData().get("Description"));
            GeoLocation region = new GeoLocation();
            region.setLat((Double) expData.getData().get("Latitude"));
            region.setLon((Double) expData.getData().get("Longitude"));
            experiment.setRegion(region);
            experiment.setMinNTrials(((Long) expData.getData().get("MinNTrials")).intValue());
            Timestamp date = (Timestamp) expData.getData().get("Date");
            experiment.setDate(date.toDate());
            experiment.setOpen((Boolean) expData.getData().get("Open"));
            experiment.setType((String) expData.getData().get("Type"));
            String uId = (String) expData.getData().get("profileID");
            experiment.setOwner(new Profile(uId));
            experiment.setTrials((ArrayList<Trial>) expData.getData().get("Trials"));
            experiment.setBlacklist((ArrayList<String>) expData.getData().get("Blacklist"));
            Log.d(TAG, expData.getId() + " => " + expData.getData());

            exp.setValue(experiment);
        });
    }

}
