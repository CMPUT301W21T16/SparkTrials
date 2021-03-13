package com.example.sparktrials;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExperimentManager {

    /**
     * Returns an Experiment object when given an experiment ID
     * @param expID
     *  The ID of the experiment to get from FireStore
     * @return
     *  The experiment object created from the data in FireStore
     */
    public Experiment getExperiment(String expID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference exp = db.collection("experiments").document(expID);
        Experiment experiment = new Experiment(expID);

        exp.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot expData = task.getResult();
                    // at the time of writing, experiments are not connected to users in the database yet
                    // so there is no setting of the experiments owner Profile yet. on the experiment
                    // activity a placeholder owner name "Owner McOwnerface" is used.
                    if(expData.exists()){
                        experiment.setTitle((String) expData.getData().get("Title"));
                        experiment.setDesc((String) expData.getData().get("Description"));
                        GeoLocation region = new GeoLocation();
                        region.setLat((Double) expData.getData().get("Latitude"));
                        region.setLon((Double) expData.getData().get("Longitude"));
                        experiment.setRegion(region);
                        experiment.setMinNTrials((Integer) expData.getData().get("MinNTrials"));
                        Timestamp date = (Timestamp) expData.getData().get("Date");
                        experiment.setDate(date.toDate());
                        experiment.setOpen((Boolean) expData.getData().get("Open"));
                    } else {
                        Log.d("Retrieval", "Document does not exists");
                    }
                } else {
                    Log.d("Retrieval", "get failed with ", task.getException());
                }
            }
        });

        return experiment;
    }
}