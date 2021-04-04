package com.example.sparktrials.main.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sparktrials.CustomList;
import com.example.sparktrials.ExperimentActivity;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_subscribed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_subscribed extends Fragment {
    private ListView subExperiments;
    private CustomList subExperiment_adapter;
    private Context context;
    private HomeViewModel homeViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tab_subscribed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment tab_subscribed.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_subscribed newInstance() {
        return new tab_subscribed();
    }

    @Override
    public void onStart() {
        super.onStart();
        subExperiments = getView().findViewById(R.id.subscribed_list);
        context = getActivity();
        IdManager idManager = new IdManager(context);
        homeViewModel = new HomeViewModel(idManager.getUserId());

        final Observer<ArrayList<Experiment>> nameObserver = new Observer<ArrayList<Experiment>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Experiment> newList) {
                subExperiment_adapter = new CustomList(context, newList);
                subExperiments.setAdapter(subExperiment_adapter);
            }
        };
        homeViewModel.getSubExpList().observe(getViewLifecycleOwner(), nameObserver);
        subExperiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment experiment = subExperiment_adapter.getItem(position);
                startExperimentActivity(experiment.getId());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_subscribed, container, false);
    }
    private void startExperimentActivity(String experimentID){
        Intent intent = new Intent(this.getActivity(), ExperimentActivity.class);
        intent.putExtra("EXPERIMENT_ID", experimentID);
        startActivity(intent);

    }
}