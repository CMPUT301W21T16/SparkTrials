package com.example.sparktrials.main_ui.publish;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sparktrials.IdManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

/**
 * The class containing the UI associated with publishing experiments, handles input and UI
 */
public class PublishFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener expDateListener;
    private EditText expDesc;
    private EditText expTitle;
    private EditText expMinNTrials;
    private EditText expRegion;
    private Experiment experiment;
    private String userID;
    private Spinner spinner;
    private Spinner locationSet;
    private Spinner trialLocations;

    private double lat;
    private double lon;
    private double radius;

    final private int didNotPickLocation = 0;
    final private int didPickLocation = 1;

    /**
     * onCreate Dialog which prompts the user to enter a title, description, minimum number of trials and a lat long pair.
     * Then calls PublishFragmentManager which uploads the experiment to firestore
     * @param savedInstanceState
     * @return
     */
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_publish, null);
        expDesc = view.findViewById(R.id.expDesc_editText);
        expTitle = view.findViewById(R.id.expTitle_editText);
        expMinNTrials = view.findViewById(R.id.expMinNTrials_editText);
        spinner = view.findViewById(R.id.experiment_type_spinner);
        locationSet = view.findViewById(R.id.experiment_location_spinner);
        trialLocations = view.findViewById(R.id.trial_location_spinner);
        String[] items = new String[]{"Binomial Trials", "Counts", "Non-Negative Integer Counts","Measurement Trials"};
        String[] locationOptions = new String[]{"False", "True"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,locationOptions);
        spinner.setAdapter(adapter);
        locationSet.setAdapter(adapter2);
        trialLocations.setAdapter(adapter2);



        locationSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Boolean chooseLocation = Boolean.parseBoolean(locationSet.getSelectedItem().toString());
                if (chooseLocation) {
                    startMapsActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        IdManager idManager = new IdManager(getActivity());
        String id = idManager.getUserId();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder
                .setView(view)
                .setNeutralButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String desc = expDesc.getText().toString();
                        String title = expTitle.getText().toString();
                        String MinNTrialsString = expMinNTrials.getText().toString();
                        //String latString = expLat.getText().toString();
                        //String lonString = expLon.getText().toString();
                        String experimentType = spinner.getSelectedItem().toString();
                        Boolean reqLocation = Boolean.parseBoolean(trialLocations.getSelectedItem().toString());
                        Log.d("Type",experimentType);
                        PublishFragmentManager manager = new PublishFragmentManager(id,desc,title,MinNTrialsString,lat,lon,radius,experimentType,reqLocation);
                    }
                })
                .create();

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.spark_text));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.neutral));
        return dialog;
    }

    private void startMapsActivity() {
        Intent launchMapsActivity = new Intent(getContext(), MapsActivity.class);
        launchMapsActivity.putExtra("NO_LOCATION_PICKED", didNotPickLocation);
        launchMapsActivity.putExtra("LOCATION_PICKED", didPickLocation);
        startActivityForResult(launchMapsActivity, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == didNotPickLocation) {
            // Set Location field to False
            locationSet.setSelection(0);
        } else if (resultCode == didPickLocation) {
            lat = (double) data.getExtras().get("Latitude");
            lon = (double) data.getExtras().get("Longitude");
            radius = (double) data.getExtras().get("Radius");
        }

    }
}
