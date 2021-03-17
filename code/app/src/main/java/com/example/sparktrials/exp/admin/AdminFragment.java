package com.example.sparktrials.exp.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;

import java.util.ArrayList;

public class AdminFragment extends Fragment {

    View view;
    private AuditLog userTrialListAdapter;
    private ListView userTrialListView;
    private AdminViewModel manager;

    private Button endButton;
    private Button unpublishButton;

    Experiment experiment;

    public AdminFragment(Experiment experiment){
        this.experiment = experiment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin, container, false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        userTrialListView = getView().findViewById(R.id.user_trials_list);

        manager = new ViewModelProvider(this).get(AdminViewModel.class);

        ArrayList<Trial> trialList = manager.getTrialList();
        ArrayList<Profile> userList = manager.getUserList();

        userTrialListAdapter = new AuditLog(getContext(), trialList, userList);
        userTrialListView.setAdapter(userTrialListAdapter);

        endButton = getView().findViewById(R.id.end_experiment_button);
        unpublishButton = getView().findViewById(R.id.unpublish_experiment_button);
    }
}