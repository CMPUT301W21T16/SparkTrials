package com.example.sparktrials.main.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sparktrials.CustomList;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab_my_experiments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab_my_experiments extends Fragment {
    private ListView myExperiments;
    private CustomList myExperiment_adapter;
    private Context context;
    private HomeViewModel homeViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tab_my_experiments() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        myExperiments = getView().findViewById(R.id.myExperiment_list);
        context = requireActivity().getApplicationContext();
        IdManager idManager = new IdManager(context);
        homeViewModel = new HomeViewModel(idManager.getUserId());

        final Observer<ArrayList<Experiment>> nameObserver = new Observer<ArrayList<Experiment>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Experiment> newList) {
                myExperiment_adapter = new CustomList(context, newList);
                myExperiments.setAdapter(myExperiment_adapter);
            }
        };
        homeViewModel.getMyExpList().observe(getViewLifecycleOwner(), nameObserver);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment tab_my_experiments.
     */
    // TODO: Rename and change types and number of parameters
    public static tab_my_experiments newInstance() {
        return new tab_my_experiments();
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
        return inflater.inflate(R.layout.fragment_tab_my_experiments, container, false);
    }
}