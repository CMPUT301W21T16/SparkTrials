package com.example.sparktrials.main_ui.publish;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sparktrials.IdManager;
import com.example.sparktrials.models.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * The class containing the UI associated with publishing experiments, handles input and UI
 */
public class PublishFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener expDateListener;
    private EditText expDesc;
    private EditText expTitle;
    private EditText expMinNTrials;
    private EditText expRegion;
    private EditText expLat;
    private EditText expLon;
    private Experiment experiment;
    private String userID;
    private TextView reqLocation;

    /**
     * onCreate Dialog which prompts the user to enter a title, description, minimum number of trials and a lat long pair.
     * Then calls PublishFragmentManager which uploads the experiment to firestore
     * @param savedInstanceState
     * @return
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_publish, null);
        expDesc = view.findViewById(R.id.expDesc_editText);
        expTitle = view.findViewById(R.id.expTitle_editText);
        expMinNTrials = view.findViewById(R.id.expMinNTrials_editText);
        expLat = view.findViewById(R.id.expLat_editText);
        expLon = view.findViewById(R.id.expLon_editText);
        reqLocation=view.findViewById(R.id.request_experiment_location);
        expLat.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        expLon.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        Spinner spinner = view.findViewById(R.id.experiment_type_spinner);
        Spinner spinner2 = view.findViewById(R.id.experiment_location_spinner);
        String[] items = new String[]{"Binomial Trials", "Counts", "Non-Negative Integer Counts","Measurement Trials"};
        String[] locationOptions = new String[]{"True","False"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,locationOptions);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        //experiment=this.experiment;
        IdManager idManager = new IdManager(getActivity());
        String id = idManager.getUserId();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Experiment")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String desc = expDesc.getText().toString();
                        String title = expTitle.getText().toString();
                        String MinNTrialsString = expMinNTrials.getText().toString();
                        String latString = expLat.getText().toString();
                        String lonString = expLon.getText().toString();
                        String experimentType = spinner.getSelectedItem().toString();
                        Boolean reqLocation = Boolean.parseBoolean(spinner2.getSelectedItem().toString());
                        Log.d("Type",experimentType);
                        PublishFragmentManager manager = new PublishFragmentManager(id,desc,title,MinNTrialsString,latString,lonString,experimentType,reqLocation);
                    }
                })
                .create();
    }
}
