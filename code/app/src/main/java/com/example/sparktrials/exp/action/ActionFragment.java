package com.example.sparktrials.exp.action;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.fragment.app.FragmentManager;

import com.example.sparktrials.CustomList;
import com.example.sparktrials.ExperimentActivity;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.MainActivity;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Trial;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class ActionFragment extends Fragment implements LocationListener {
    View view;
    TextView trialsNumber;
    TextView trialsCount;
    int count;
    private ActionFragmentManager manager;
    private IdManager idManager;
    String id;

    LocationManager locationManager;
    boolean reqLocation;
    MutableLiveData<GeoLocation> currentLocation;

    public ActionFragment(Experiment experiment){
        this.manager= new ActionFragmentManager(experiment);
        reqLocation = experiment.getReqLocation();
        if (reqLocation) {
            currentLocation = new MutableLiveData<>();
        } else {
            currentLocation = null;
        }
    }

    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        idManager= new IdManager(context);
        id= idManager.getUserId();
        manager.setProfile(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (manager.getOpen()) {
            view = inflater.inflate(R.layout.fragment_action, container, false);
            Button leftButton = view.findViewById(R.id.action_bar_pass);
            Button rightButton = view.findViewById(R.id.action_bar_fail);
            Button uploadButton = view.findViewById(R.id.action_bar_upload_trials);
            Button recordNumButton = view.findViewById(R.id.action_bar_recordnum);
            Button generateQR = view.findViewById(R.id.action_bar_generateQR);
            Button deleteTrials = view.findViewById(R.id.action_bar_delete_trials);
            EditText valueEditText = view.findViewById(R.id.countvalue_editText);
            updateView();

            if (reqLocation) {
                getLocation();
            }

            if (manager.getType().equals("binomial trials".toLowerCase())) {
                if (reqLocation) {
                    getLocation();
                }
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.addBinomialTrial(true, currentLocation.getValue());
                        updateView();
                    }
                });
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.addBinomialTrial(false, currentLocation.getValue());
                        updateView();
                    }
                });
            } else if (manager.getType().equals("Non-Negative Integer Counts".toLowerCase())) {
                if (reqLocation) {
                    getLocation();
                }
                recordNumButton.setVisibility(View.VISIBLE);
                valueEditText.setVisibility(View.VISIBLE);
                recordNumButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String valueString = valueEditText.getText().toString();
                        Integer result;
                        try {
                            result = Integer.parseInt(valueString);
                            manager.addNonNegIntTrial(result, currentLocation.getValue());
                            updateView();
                        } catch (NumberFormatException e) {
                            AlertDialog builder = new AlertDialog.Builder(getContext())
                                    .setTitle("ERROR")
                                    .setMessage("You must enter a number")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                });
            } else if (manager.getType().equals("Measurement Trials".toLowerCase())) {
                if (reqLocation) {
                    getLocation();
                }
                recordNumButton.setVisibility(View.VISIBLE);
                valueEditText.setVisibility(View.VISIBLE);
                valueEditText.setInputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL |
                        InputType.TYPE_NUMBER_FLAG_SIGNED);
                recordNumButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double result;
                        String valueString = valueEditText.getText().toString();
                        try {
                            result = Double.parseDouble(valueString);
                            manager.addMeasurementTrial(result, currentLocation.getValue());
                            updateView();
                        } catch (NumberFormatException e) {
                            AlertDialog builder = new AlertDialog.Builder(getContext())
                                    .setTitle("ERROR")
                                    .setMessage("You must enter a number")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                });
            } else if (manager.getType().equals("Counts".toLowerCase())) {
                if (reqLocation) {
                    getLocation();
                }
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                leftButton.setText("Add Count");
                rightButton.setText("Commit Trial");
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count += 1;
                        updateView();
                    }
                });
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.addCountTrial(count, currentLocation.getValue());
                        count = 0;
                        updateView();
                    }
                });
            }
            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (manager.getNTrials() >= manager.getMinNTrials())
                        manager.uploadTrials();
                    else {
                        AlertDialog builder = new AlertDialog.Builder(getContext())
                                .setTitle("ERROR")
                                .setMessage("You have not reached the minimum number of trials")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            });
            generateQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            deleteTrials.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.deleteTrials();
                    updateView();
                }
            });
        }
        else {
            view = inflater.inflate(R.layout.fragment_closed_action, container, false);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentLocation != null) {
            final Observer<GeoLocation> nameObserver = new Observer<GeoLocation>() {
                @Override
                public void onChanged(@Nullable final GeoLocation newLoc) {
                    System.out.println(currentLocation.getValue().getLat());
                }
            };
            currentLocation.observe(this, nameObserver);
        }
    }

    public void updateView(){
        int trials=manager.getNTrials();
        Log.d("NUM Is", String.valueOf(trials));
        int minimumNumberTrials = manager.getMinNTrials();
        trialsNumber=view.findViewById(R.id.trials_completed);
        trialsCount=view.findViewById(R.id.trials_count);
        if(manager.getType().equals("Counts".toLowerCase())){
            trialsCount.setText("Trial count: "+count);
        }
        if (minimumNumberTrials>0)
            trialsNumber.setText("Trials completed: "+trials+"/"+minimumNumberTrials);
        else
            trialsNumber.setText("Trials completed: "+trials);
    }

    /**
     * Gets the updated location of the device, and causes onLocationChanged() to be called as
     * a callback function.
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5, this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update the currentLocation of the user
        GeoLocation cLoc = new GeoLocation(location.getLatitude(), location.getLongitude());

        currentLocation.setValue(cLoc);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {}
}