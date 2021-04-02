package com.example.sparktrials.exp.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.ProfileActivity;
import com.example.sparktrials.models.Trial;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuditLog extends ArrayAdapter<Profile> {
    private ArrayList<Trial> trialList;
    private ArrayList<Profile> userList;
    private Experiment experiment;
    private final String TAG = "Audit Log: ";

    FirebaseManager dbManager = new FirebaseManager();

    private Context context;

    private TextView userTrials;
    private TextView results;
    private TextView userResults;
    private TextView ratio;
    private TextView userRatio;
    private TextView userName;
    private Button ignoreButton;

    public AuditLog(@NonNull Context context, Experiment experiment, ArrayList<Trial> trials, ArrayList<Profile> userList) {
        super(context, 0, userList);
        this.context = context;
        this.experiment = experiment;
        trialList = trials;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.audit_log_element, parent, false);
        }

        userTrials = view.findViewById(R.id.user_trials);
        results = view.findViewById(R.id.results_text);
        userResults = view.findViewById(R.id.user_results);
        ratio = view.findViewById(R.id.ratio_text);
        userRatio = view.findViewById(R.id.user_ratio);
        userName = view.findViewById(R.id.user_name);
        ignoreButton = view.findViewById(R.id.ignore_button);

        ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ArrayList<String> blacklist = experiment.getBlacklist();
                if(!blacklist.contains(userList.get(position).getId())){
                    blacklist.add(userList.get(position).getId());
                    Map<String, Object> map = new HashMap<>();
                    experiment.setBlacklist(blacklist);
                    map.put("Blacklist", blacklist);
                    dbManager.update("experiments", experiment.getId(), map);
                } else {
                    blacklist.remove(userList.get(position).getId());
                    Map<String, Object> map = new HashMap<>();
                    experiment.setBlacklist(blacklist);
                    map.put("Blacklist", blacklist);
                    dbManager.update("experiments", experiment.getId(), map);
                }

                String currentBtnText = ignoreButton.getText().toString();

                if(currentBtnText.equals("Ignore")){
                    ignoreButton.setText("IGNORED");
                } else {
                    ignoreButton.setText("IGNORE");
                }
            }
        });

        setFields(position);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = userList.get(position);
                startProfileActivity(profile.getId());
            }
        });

        return view;


    }

    /**
     * This method starts a ProfileActivity, which displays the information of the user referred
     * to by an audit log list element
     * @param userId
     *  The userId to make a profile page for
     */
    private void startProfileActivity(String userId){
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        ((Activity) getContext()).startActivityForResult(intent, 0);
    }

    /**
     * This method sets the fields of an experiment in the list to the data of the corresponding
     * users trials
     * @param index
     * the index in the userList
     */
    public void setFields(int index) {
        Profile user = userList.get(index);
        double trials = 0;
        double result = 0;
        double ratioOrMean;

        if (experiment.getType().equals("binomial trials")) {
            for (Trial trial : trialList) {
                if (trial.getProfile().equals(user)) {
                    trials++;
                    if (trial.getValue() > 0) {
                        result++;
                    }
                }
            }
            ratioOrMean = result / trials;

            userTrials.setText(String.format("%.0f", trials));
            userResults.setText(String.format("%.0f/%.0f", result, trials));
            userRatio.setText(String.format("%.0f", ratioOrMean * 100));
            userName.setText(user.getUsername());

        } else {
            for (Trial trial : trialList) {
                if (trial.getProfile().equals(user)) {
                    trials++;
                    result = result + trial.getValue();
                }
            }

            ratioOrMean = result / trials;
            userTrials.setText(String.format("%.0f", trials));
            results.setText("Mean");
            userResults.setText(String.format("%.2f", ratioOrMean));
            ratio.setText("");
            userRatio.setText("");
            userName.setText(user.getUsername());

        }

        if(experiment.getBlacklist().contains(user.getId())){
            ignoreButton.setText("Ignored");
        } else {
            ignoreButton.setText("Ignore");
        }

    }
}
