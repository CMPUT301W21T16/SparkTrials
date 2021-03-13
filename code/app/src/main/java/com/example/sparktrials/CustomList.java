package com.example.sparktrials;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sparktrials.models.Experiment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experimentsList;  // This is the list of experiments that the
                                                   // adapter will display on the ListView
    private Context context;

    // These are the TextViews in list_content.xml that will show the information about an
    // experiment
    private TextView experimentTitle;
    private TextView experimentDescription;
    private TextView experimentStatus;
    private TextView experimentOwner;
    private TextView experimentDate;

    /**
     * Constructor for a CustomList list adapter, which shows a customized list of experiments,
     * with some of their data
     * @param context
     *      The context which we are currently in
     * @param experiments
     *      The list of experiments which we want to display
     */
    public CustomList(@NonNull Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.context = context;
        experimentsList = experiments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // Set the layout of list items to be based on list_content.xml
            view = LayoutInflater.from(context).inflate(R.layout.list_content, parent, false);
        }

        experimentTitle = view.findViewById(R.id.list_experiment_title);
        experimentDescription = view.findViewById(R.id.list_experiment_description);
        experimentStatus = view.findViewById(R.id.list_experiment_status);
        experimentOwner = view.findViewById(R.id.list_experiment_owner);
        experimentDate = view.findViewById(R.id.list_experiment_date);

        setFields(position);


        return view;

    }

    /**
     * This method sets the fields of an Experiment in the list to the data of the corresponding
     * experiment.
     * @param index
     *      This is the index of the Experiment whose information is to be displayed
     */
    private void setFields(int index) {
        Experiment experiment = experimentsList.get(index); // Get the respective experiment

        experimentTitle.setText(experiment.getTitle());
        experimentDescription.setText(experiment.getDesc());
        experimentStatus.setText("Active");
        experimentOwner.setText(experiment.getOwner().getUsername());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        experimentDate.setText(dateFormat.format(experiment.getDate()));
    }

}
