package com.example.sparktrials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.QrCode;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.example.sparktrials.models.TrialCount;
import com.example.sparktrials.models.TrialIntCount;
import com.example.sparktrials.models.TrialMeasurement;
import com.google.firebase.Timestamp;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class QrScannerActivity extends AppCompatActivity {

    FirebaseManager db = new FirebaseManager();
    private String userId;
    private String trialId;
    private MutableLiveData<Experiment> exp = new MutableLiveData<>();
    private MutableLiveData<Trial> trial = new MutableLiveData<>();
    private MutableLiveData<Profile> profile = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IdManager idManager = new IdManager(this);
        userId = idManager.getUserId();
        downloadProfile();

        IntentIntegrator qrIntegrator = new IntentIntegrator(this);
        qrIntegrator.setPrompt("Scan a QRCode");
        qrIntegrator.setOrientationLocked(true);
        qrIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        trialId = intentResult.getContents();
        createTrial(trialId);
    }

    public void downloadProfile() {
        db.get("users", userId, proData -> {
            Profile prof = new Profile(userId);

            prof.setUsername((String) proData.getData().get("name"));
            prof.setContact((String) proData.getData().get("contact"));
            prof.setSubscriptions((ArrayList<String>) proData.getData().get("subscriptions"));

            profile.setValue(prof);
        });
    }

    public void createTrial(String qrId) {
        db.get("qrCodeData", qrId, qrData -> {
            String trialType = (String) qrData.getData().get("TrialType");
            Trial tri;
            Double value = (Double) qrData.getData().get("Value");
            if(trialType.equals("binomial trials")){
                tri = new TrialBinomial(value == 1.0 ? true : false);
            } else if(trialType.equals("counts")){
                tri = new TrialCount();
            } else if(trialType.equals("non-negative integer counts")) {
                tri = new TrialIntCount(value.intValue());
            } else {
                tri = new TrialMeasurement(value);
            }

            tri.setProfile(profile.getValue());
            tri.setId(UUID.randomUUID().toString());
            trial.setValue(tri);
            getExperiment((String) qrData.getData().get("ExperimentId"));
        });
    }

    public void getExperiment(String expId){
        db.get("experiments", expId, expData -> {
            Experiment experiment = new Experiment(expId);

            experiment.setTitle((String) expData.getData().get("Title"));
            experiment.setDesc((String) expData.getData().get("Description"));
            GeoLocation region = new GeoLocation();
            region.setLat((Double) expData.getData().get("Latitude"));
            region.setLon((Double) expData.getData().get("Longitude"));
            region.setRadius((Double) expData.getData().get("Radius"));
            experiment.setRegion(region);
            experiment.setReqLocation((Boolean) expData.getData().get("ReqLocation"));
            experiment.setMinNTrials(((Long) expData.getData().get("MinNTrials")).intValue());
            Timestamp date = (Timestamp) expData.getData().get("Date");
            experiment.setDate(date.toDate());
            experiment.setOpen((Boolean) expData.getData().get("Open"));
            experiment.setType((String) expData.getData().get("Type"));
            String uId = (String) expData.getData().get("profileID");
            String username = (String) expData.getData().get("ownerName");
            Profile owner = new Profile(uId);
            owner.setUsername(username);
            experiment.setOwner(owner);
//            experiment.setTrials((ArrayList<Trial>) expData.getData().get("Trials"));
            ArrayList<HashMap> trialsHash = (ArrayList<HashMap>) expData.getData().get("Trials");
            ArrayList<Trial> trials = new ArrayList<>();
            for(HashMap<String, Object> map: trialsHash){
                Trial trial;
                String type = experiment.getType();
                if(type.equals("binomial trials")){
                    if((Double) map.get("value") == 0.0){
                        trial = new TrialBinomial(false);
                    } else {
                        trial = new TrialBinomial(true);
                    }

                } else if(type.equals("non-negative integer counts")) {
                    trial = new TrialIntCount(((Double) map.get("value")).intValue());
                } else if(type.equals("measurement trials")){
                    trial = new TrialMeasurement((Double) map.get("value"));
                } else {
                    trial = new TrialCount();
                }

                if (experiment.hasLocationSet()) {
                    HashMap<String, Object> trialCoords = (HashMap<String, Object>) map.get("location");
                    GeoLocation trialLocation = new GeoLocation();
                    trialLocation.setLat((double) trialCoords.get("lat"));
                    trialLocation.setLon((double) trialCoords.get("lon"));
                    trial.setLocation(trialLocation);
                }

                Profile experimenter = new Profile();
                HashMap<String, Object> profile = (HashMap<String, Object>) map.get("profile");
                experimenter.setContact((String) profile.get("contact"));
                experimenter.setUsername((String) profile.get("username"));
                experimenter.setId((String) profile.get("id"));
                experimenter.setSubscriptions((ArrayList<String>) profile.get("subscriptions"));
                trial.setProfile(experimenter);
                trial.setId((String) map.get("id"));
                Timestamp trialDate = (Timestamp) map.get("date");
                trial.setDate(trialDate.toDate());
                trials.add(trial);
            }
            experiment.setTrials(trials);
            experiment.setBlacklist((ArrayList<String>) expData.getData().get("Blacklist"));

            exp.setValue(experiment);
            uploadTrial();
        });
    }

    public void uploadTrial(){
        Experiment experiment = exp.getValue();
        Trial tri = trial.getValue();

        experiment.addTrial(tri);
        db.uploadTrials(experiment);
        finish();
    }
}
