package com.example.sparktrials.main.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sparktrials.models.Experiment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchViewModel {

    // final private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<Experiment> experiments;

    /**
     * Constructor for SearchViewModel
     */
    public SearchViewModel() {
        refresh();

        // For testing
        for (int i = 1; i <= 10000; i++) {
            Experiment experiment = new Experiment(""+i);
            experiment.setTitle("Title " + Integer.toString(i) + " " +  (int) Math.pow(i, 2));
            experiment.setDesc("Description " + Integer.toString(i) + " " + (int) Math.pow(i, 3));
            experiments.add(experiment);
        }

    }

    /**
     * Gets the list of experiments in the database
     * @return
     *      Returns the list of experiments in the database
     */
    public ArrayList<Experiment> getExperiments() {

        //refresh();

        return experiments;
    }

    /**
     * Gets an updated list of experiments from the database.
     */
    private void refresh() {
        // IMPLEMENT REFRESH CODE ONCE THE EXPERIMENTS COLLECTIONS ARE CREATED
        ArrayList<Experiment> updatedExperiments = new ArrayList<>();
        //////////////////////////////////////////////////////////////////
        experiments = updatedExperiments;
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
        //refresh();

        if (keywords.length > 0) {
            HashSet<Experiment> resultSet = new HashSet<>();  // To avoid duplicates

            for (int experimentIndex = 0; experimentIndex < experiments.size(); experimentIndex++) {
                 for (int keywordIndex = 0; keywordIndex < keywords.length; keywordIndex++) {
                    String currentKeyword = keywords[keywordIndex];
                    Experiment experimentToBeSearched = experiments.get(experimentIndex);
                    if (experimentToBeSearched.getTitle().toLowerCase().contains(currentKeyword)
                            || experimentToBeSearched.getDesc().toLowerCase().contains(currentKeyword)) {
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

}
