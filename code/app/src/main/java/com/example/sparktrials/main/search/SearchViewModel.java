package com.example.sparktrials.main.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class acts as a manager for SearchFragment.
 */
public class SearchViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Experiment>> experiments;

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference experimentsCollection = db.collection("experiments");
    private CollectionReference usersCollection = db.collection("users");

    final String TAG = "Fetching documents...";

    /**
     * Constructor for SearchViewModel
     */
    public SearchViewModel() {
        experiments = new MutableLiveData<>();
        getExperimentsFromDB();
    }


    /**
     * Gets the list of experiments in the database
     * @return
     *      Returns the list of experiments in the database
     */
    public MutableLiveData<ArrayList<Experiment>> getExperiments() {
        return experiments;
    }

    /**
     * Gets an updated list of experiments from the database.
     */
    private void getExperimentsFromDB() {

        experimentsCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            experiments.setValue(new ArrayList<>());

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Boolean published = (Boolean) document.get("Published");
                                if (published == null || published == true) {
                                    String id = document.getId();
                                    String title = (String) document.get("Title");
                                    String desc = (String) document.get("Description");
                                    Boolean open = (Boolean) document.get("Open");
                                    Date date = document.getTimestamp("Date").toDate();

                                    Experiment experiment = new Experiment(id);
                                    experiment.setTitle(title);
                                    experiment.setDesc(desc);
                                    experiment.setOpen(open);
                                    experiment.setPublished(true || published);
                                    experiment.setDate(date);

                                    String ownerId = (String) document.get("profileID");

                                    usersCollection.document(ownerId).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        String username = (String) document.get("name");
                                                        String phoneNum = (String) document.get("contact");

                                                        Profile owner = new Profile(ownerId);
                                                        owner.setUsername(username);
                                                        owner.setContact(phoneNum);

                                                        experiment.setOwner(owner);

                                                        ArrayList<Experiment> x = experiments.getValue();
                                                        x.add(experiment);

                                                        experiments.setValue(x);
                                                    }
                                                }
                                            });
                                }
                            }
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

        ArrayList<Experiment> experimentsInDB = experiments.getValue();

        if (keywords.length == 0) {
            return experimentsInDB;
        } else {
            HashSet<Experiment> resultSet = new HashSet<>();  // To avoid duplicates

            for (int experimentIndex = 0; experimentIndex < experimentsInDB.size(); experimentIndex++) {
                for (int keywordIndex = 0; keywordIndex < keywords.length; keywordIndex++) {
                    String currentKeyword = keywords[keywordIndex];
                    Experiment experimentToBeSearched = experimentsInDB.get(experimentIndex);
                    if (experimentMatches(currentKeyword, experimentToBeSearched)) {
                        // If an experiment matches a keyword
                        resultSet.add(experimentToBeSearched);
                    }
                }
            }

            ArrayList<Experiment> results = new ArrayList<>();
            results.addAll(resultSet);

            return results;
        }
    }

    /**
     * Checks whether the experiment has fields that match a keyword.
     * @param keyword
     *      The keyword we want to check the experiment fields against.
     * @param experiment
     *      The experiment whose fields we want to check.
     * @return
     *      true if the experiment has matching fields, false otherwise.
     */
    private boolean experimentMatches(String keyword, Experiment experiment) {
        String experimentTitle = experiment.getTitle().toLowerCase();
        String experimentDescription = experiment.getDesc().toLowerCase();
        String experimentOwnerUsername = experiment.getOwner().getUsername().toLowerCase();

        return (experimentTitle.contains(keyword)
                    || experimentDescription.contains(keyword)
                    || experimentOwnerUsername.contains(keyword));
    }

}

