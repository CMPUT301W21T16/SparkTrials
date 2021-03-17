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
import java.util.HashMap;
import java.util.Map;

/**
 * A class to manage the data for ExperimentActivity - experiment and profile
 */
public class ExperimentViewModel extends ViewModel {
    FirebaseManager manager = new FirebaseManager();

    private MutableLiveData<Experiment> exp;
    private String expId;
    private MutableLiveData<Profile> pro;
    private String proId;

    final String TAG = "Fetching Experiment...";

    /**
     * Constructor for SearchViewModel
     */
    public ExperimentViewModel() {
        exp = new MutableLiveData<>();
        pro = new MutableLiveData<>();
    }

    /**
     * Initializes profile and experiment
     * @param id
     *    the id of the experiment to retrieve from firebase
     * @param uid
     *    the id of the profile to retrieve from firebase
     */
    public void init(String id, String uid){
        expId = id;
        proId = uid;
        downloadProfile();
        downloadExperiment();
    }

    /**
     * Gets the experiments in the database
     * @return
     *      Returns the list of experiments in the database
     */
    public MutableLiveData<Experiment> getExperiment() {
        return exp;
    }

    /**
     * Gets profile in the database
     * @return
     *    Returns the profile in the database
     */
    public MutableLiveData<Profile> getProfile() {return pro;}

    /**
     * Is called when subscribe button is pressed
     * Adds or remove the experiment from subscription list of the profile
     * @return
     *    returns the profile, so that it updates the button name
     */
    public MutableLiveData<Profile> subscribe(){
        Profile temp = new Profile(proId);

        if (pro.getValue() != null) {
            if (pro.getValue().getSubscriptions().contains(exp.getValue().getId())) {
                //unsubscribe
                pro.getValue().delSubscription(exp.getValue().getId());
            } else {
                //subscribe
                pro.getValue().addSubscription(exp.getValue().getId());
            }

            temp.setSubscriptions(pro.getValue().getSubscriptions());
            temp.setUsername(pro.getValue().getUsername());
            temp.setContact(pro.getValue().getContact());
        }
        pro.setValue(temp);
        return pro;
    }

    /**
     * Uploads the subscription list to firebase
     */
    public void updateSubscribe(){
        if (pro.getValue() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("subscriptions", pro.getValue().getSubscriptions());

            manager.update("users", proId, map);
        }
    }

    /**
     * FIlls the pro attribute with a profile from firebase
     */
    private void downloadProfile() {
        manager.get("users", proId, proData -> {
            Profile profile = new Profile(proId);

            Log.d(TAG, proData.getId() + " => " + proData.getData());
            profile.setUsername((String) proData.getData().get("name"));
            profile.setContact((String) proData.getData().get("contact"));
            profile.setSubscriptions((ArrayList<String>) proData.getData().get("subscriptions"));
            Log.d(TAG, proData.getId() + " => " + proData.getData());

            pro.setValue(profile);

        });
    }

    /**
     * Gets an experiment from the database.
     */
    private void downloadExperiment() {
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
