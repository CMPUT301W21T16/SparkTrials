package com.example.sparktrials.exp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sparktrials.R;
import com.example.sparktrials.TrialsAdapter;
import com.example.sparktrials.exp.action.ActionFragmentManager;
import com.example.sparktrials.models.Experiment;


public class DraftsFragment extends Fragment {

    private TrialsAdapter trialsAdapter;
    private Button uploadBtn;
    private ListView trialsList;
    private DraftManager draftManager;
    private Context context;
    private Experiment experiment;
    private ActionFragmentManager manager;
    private Button cancelBtn;

    public DraftsFragment(Experiment experiment) {
        this.manager = new ActionFragmentManager(experiment);
        this.experiment = experiment;
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        draftManager = new DraftManager(context);
        draftManager.setExperiment(experiment);
        trialsList = getView().findViewById(R.id.trialsList_view);
        trialsAdapter = new TrialsAdapter(context,0,draftManager.getDraft_trials());
        trialsList.setAdapter(trialsAdapter);
        manager.setDraftManager(draftManager);
        uploadBtn = getView().findViewById(R.id.uploadDrafts);
        cancelBtn = getView().findViewById(R.id.clear);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.uploadDrafts(draftManager.getDraft_trials());
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               draftManager.deleteDrafts();
               trialsAdapter.notifyDataSetChanged();
                Toast.makeText(context,experiment.getType(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drafts, container, false);
    }
}