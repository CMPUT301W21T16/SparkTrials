package com.example.sparktrials.main.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.CustomList;
import com.example.sparktrials.ExperimentActivity;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel searchManager;
    private CustomList searchListAdapter;
    private ListView searchListView;

    private EditText searchBar;
    private Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchListView = getView().findViewById(R.id.search_list);

        searchManager = new SearchViewModel();

        searchListAdapter = new CustomList(getActivity(), searchManager.getExperiments());
        searchListView.setAdapter(searchListAdapter);

        searchBar = getView().findViewById(R.id.search_bar);
        searchButton = getView().findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the keyboard from the current screen
                hideKeyboardFrom();

                updateAdapter();
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment experiment = searchListAdapter.getItem(position);

                startExperimentActivity(experiment.getId(), experiment.getOwner().getId());
            }
        });

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If a key-down event was invoked on the "Enter" key on the keyboard
                // and the key pressed was the "Enter" key, it acts as if the "SEARCH" button was
                // clicked
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchButton.callOnClick();
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * Gets a list of keywords that the user entered, which are all the words entered in the search
     * bar.
     * @return
     *      Returns a String array of keywords in lowercase.
     */
    private String[] getKeywords() {
        // Trim to remove meaningless trailing and leading whitespace.
        // Lowercase because we want to get matching experiments, regardless of case.
        String searchString = searchBar.getText().toString().trim().toLowerCase();
        String[] keywords = searchString.split(" ");

        return keywords;
    }

    /**
     * Updates the list adapter with the list of matching experiments. Meant to be invoked only
     * when the user tries to search.
     */
    private void updateAdapter() {
        String[] keywords = getKeywords();

        ArrayList<Experiment> searchResults = searchManager.search(keywords);

        searchListAdapter = new CustomList(getContext(), searchResults);

        searchListView.setAdapter(searchListAdapter);
    }

    /**
     * Hides the keyboard from the screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /**
     * Starts an ExperimentActivity when an item on the ListView is clicked to display its
     * information
     * @param experimentId
     *      This is the ID of the experiment whose information we want displayed, namely the
     *      experiment that the user chose after clicking on its corresponding ListView element
     * @param ownerId
     *      This is the ID of the owner of the experiment
     */
    public void startExperimentActivity(String experimentId, String ownerId) {
        Intent intent = new Intent(this.getActivity(), ExperimentActivity.class);
        intent.putExtra("EXPERIMENT_ID", experimentId);
        intent.putExtra("USER_ID", ownerId);
        startActivity(intent);
    }

}
