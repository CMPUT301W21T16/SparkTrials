package com.example.sparktrials;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
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

import java.util.List;

public class ExperimentManager {

    private Experiment experiment;
    private String expID;

    final String TAG = "Launching Experiment";


    public ExperimentManager(String expID){
        this.expID = expID;
        experiment = new Experiment(expID);
        getExperimentFromDatabase(expID);
    }


    public Experiment getExperiment(String expID){
        return experiment;
    }


    public void getExperimentFromDatabase(String expID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //DocumentReference exp = db.collection("experiments").document(expID);
        DocumentReference exp = db.collection("experiments").document(expID);
        Log.d(TAG, expID);
        exp.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d(TAG, "just inside the onComplete");
                if (task.isSuccessful()) {
                    DocumentSnapshot expData = task.getResult();
                    // at the time of writing, experiments are not connected to users in the database yet
                    // so there is no setting of the experiments owner Profile yet. on the experiment
                    // activity a placeholder owner name "Owner McOwnerface" is used.
                    if (expData.exists()) {
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
                    } else {
                        Log.d(TAG, "Document does not exists");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}