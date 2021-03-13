package com.example.sparktrials.main.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SearchViewModel {

    private ArrayList<Experiment> experiments;

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference experimentsCollection = db.collection("experiments");

    final String TAG = "Fetching documents...";

    /**
     * Constructor for SearchViewModel
     */
    public SearchViewModel() {
        experiments = new ArrayList<>();
        refresh();
    }

    /**
     * Gets the list of experiments in the database
     * @return
     *      Returns the list of experiments in the database
     */
    public ArrayList<Experiment> getExperiments() {
        refresh();
        return experiments;
    }

    /**
     * Gets an updated list of experiments from the database.
     */
    private void refresh() {

        experimentsCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            experiments.clear();

                            ArrayList<QueryDocumentSnapshot> experimentsDocuments = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                experimentsDocuments.add(document);
                            }

                            addExperimentsFromQuery(experimentsDocuments);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Searches through the list of experiments (from the database) and finds any experiments that
     * match at least one of the keywords.
     * @param keywords
     *      This is the array of keywords that we will check the experiments against
     * @return
     *      Returns the list of experiments that match at least one of the keywords
     */
    public ArrayList<Experiment> search(String[] keywords) {
        refresh();

        if (keywords.length > 0) {
            HashSet<Experiment> resultSet = new HashSet<>();  // To avoid duplicates

            for (int experimentIndex = 0; experimentIndex < experiments.size(); experimentIndex++) {
                 for (int keywordIndex = 0; keywordIndex < keywords.length; keywordIndex++) {
                    String currentKeyword = keywords[keywordIndex];
                    Experiment experimentToBeSearched = experiments.get(experimentIndex);
                    if (experimentMatches(currentKeyword, experimentToBeSearched)) {
                        // If an experiment matches a keyword
                        resultSet.add(experimentToBeSearched);
                    }
                }
            }

            ArrayList<Experiment> results = new ArrayList<>();
            results.addAll(resultSet);

            return results;
        } else {
            return experiments;
        }
    }

    private void addExperimentsFromQuery(ArrayList<QueryDocumentSnapshot> experimentsDocuments) {

        for (int i = 0; i < experimentsDocuments.size(); i++) {
            QueryDocumentSnapshot experimentDocument = experimentsDocuments.get(i);

            Experiment experiment = new Experiment(experimentDocument.getId());
            Profile owner = new Profile((String) experimentDocument.get("profileID"), "User X", "123-456-7890");

            experiment.setTitle((String) experimentDocument.get("Title"));
            experiment.setDesc((String) experimentDocument.get("Description"));
            experiment.setOwner(owner);

            experiments.add(experiment);
        }

    }

    private boolean experimentMatches(String keyword, Experiment experiment) {
        String experimentTitle = experiment.getTitle().toLowerCase();
        String experimentDescription = experiment.getDesc().toLowerCase();
        String experimentOwnerUsername = experiment.getOwner().getUsername().toLowerCase();

        return (experimentTitle.contains(keyword)
                    || experimentDescription.contains(keyword)
                    || experimentOwnerUsername.contains(keyword));
    }

}
