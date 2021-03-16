package com.example.sparktrials;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExperimentManager {

    private String uId;
    private String expId;
    private Experiment experiment;
    private Profile profile;

    final String TAG = "Launching Experiment";


    public ExperimentManager(String uId, String expId){
        this.uId = uId;
        this.expId = expId;
        experiment = new Experiment(expId);
        profile = new Profile(uId);
        downloadExperiment(expId);
    }

    public void downloadExperiment(String id){
        FirebaseManager manager = new FirebaseManager();
        manager.get("experiments", id, new Callback() {
            @Override
            public void onCallback(DocumentSnapshot expData){
                if (expData.exists()) {
                    Log.d("Launching Experiment", expData.getId() + " => " + expData.getData());
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
                    experiment.setOwner(downloadProfile());
                    experiment.setTrials((ArrayList<Trial>) expData.getData().get("Trials"));
                    experiment.setBlacklist((ArrayList<String>) expData.getData().get("Blacklist"));
                } else {
                    Log.d("Launching Experiment", "Document does not exists");
                }
            }
        });
    }

    public void downloadExperimentV2(String id){
        DocumentReference exp = FirebaseFirestore.getInstance().collection("experiments").document(id);
        exp.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot expData = task.getResult();
                    // at the time of writing, experiments are not connected to users in the database yet
                    // so there is no setting of the experiments owner Profile yet. on the experiment
                    // activity a placeholder owner name "Owner McOwnerface" is used.
                    if (expData.exists()) {
                        Log.d("Launching Experiment", expData.getId() + " => " + expData.getData());
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
                        experiment.setOwner(downloadProfile());
                        experiment.setTrials((ArrayList<Trial>) expData.getData().get("Trials"));
                        experiment.setBlacklist((ArrayList<String>) expData.getData().get("Blacklist"));
                    } else {
                        Log.d("Launching Experiment", "Document does not exists");
                    }
                } else {
                    Log.d("Launching Experiment", "get failed with ", task.getException());
                }
            }
        });
    }

    public Profile downloadProfile(){
        return this.profile;

    }


    public Experiment getExperiment(){
        return this.experiment;
    }
}