package com.example.sparktrials;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sparktrials.models.Trial;

import java.util.ArrayList;

public class TrialsAdapter extends ArrayAdapter<Trial> {

    private Context context;
    private ArrayList<Trial> trialsList;
    private TextView result;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // Set the layout of list items to be based on list_content.xml
            view = LayoutInflater.from(context).inflate(R.layout.trials_list, parent, false);
        }
        result = view.findViewById(R.id.resultDraft);
        Trial trial = trialsList.get(position);
        result.setText("Result: "+String.valueOf(trial.getValue()));

        return view;
    }

    public TrialsAdapter(@NonNull Context context, int resource, ArrayList<Trial> trials) {
        super(context, 0, trials);
        this.context = context;
        trialsList = trials;

    }
}
