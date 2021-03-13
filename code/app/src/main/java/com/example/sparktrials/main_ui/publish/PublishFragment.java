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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
    public PublishFragment(String id) {
        this.userID=id;
    }

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
        expLat.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        expLon.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        //experiment=this.experiment;
        String id = this.userID;
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
                        PublishFragmentManager manager = new PublishFragmentManager(id,desc,title,MinNTrialsString,latString,lonString);
                    }
                })
                .create();
    }
}
