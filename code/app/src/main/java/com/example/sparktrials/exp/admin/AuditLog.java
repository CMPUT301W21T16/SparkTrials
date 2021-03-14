package com.example.sparktrials.exp.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;

import java.util.ArrayList;

public class AuditLog extends ArrayAdapter<Profile> {
    private ArrayList<Trial> trialList;
    private ArrayList<Profile> userList;

    private Context context;

    private TextView userTrials;
    private TextView userResults;
    private TextView userRatio;
    private TextView userName;
    private Button ignoreButton;

    public AuditLog(@NonNull Context context, ArrayList<Trial> trials, ArrayList<Profile> userList) {
        super(context, 0, userList);
        this.context = context;
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
        userResults = view.findViewById(R.id.user_results);
        userRatio = view.findViewById(R.id.user_ratio);
        userName = view.findViewById(R.id.user_name);
        ignoreButton = view.findViewById(R.id.ignore_button);

//        // let people know i'm overriding the equals() method for profile so that .contains() works
//        for(Trial trial: trialList){
//            if(userList.contains(trial.getProfile())){
//                continue;
//            } else {
//                userList.add(trial.getProfile());
//            }
//        }

        return view;

    }

    /**
     * This method sets the fields of an experiment in the list to the data of the corresponding
     * users trials
     * @param index
     * the index in the userList
     */
    public void setFields(int index){
        Profile user = userList.get(index);
        double trials = 0;
        double result = 0;
        Double ratio;

        for(Trial trial: trialList){
            if(trial.getProfile().equals(user)){
                trials++;
            }
        }
        ratio = result/trials;

        userTrials.setText(String.format("%.0f", trials));
        userResults.setText(String.format("%.0f", result));
        userRatio.setText(String.format("%.0f", ratio*100));
        userName.setText(user.getUsername());
    }


}
