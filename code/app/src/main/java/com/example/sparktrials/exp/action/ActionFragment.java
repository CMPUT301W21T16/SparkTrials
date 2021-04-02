package com.example.sparktrials.exp.action;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.sparktrials.IdManager;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Experiment;

import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.QrCode;
import com.google.zxing.WriterException;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.LOCATION_SERVICE;

public class ActionFragment extends Fragment implements LocationListener {
    View view;
    TextView trialsNumber;
    TextView trialsCount;
    int count;
    private ActionFragmentManager manager;
    private IdManager idManager;
    String id;

    Button leftButton;
    Button rightButton;
    Button uploadButton;
    Button recordNumButton;
    Button generateQR;
    Button deleteTrials;
    EditText valueEditText;

    LocationManager locationManager;
    boolean reqLocation;
    MutableLiveData<GeoLocation> currentLocation;

    public ActionFragment(Experiment experiment){
        this.manager= new ActionFragmentManager(experiment);
        reqLocation = experiment.getReqLocation();
        if (reqLocation) {
            currentLocation = new MutableLiveData<>();
        } else {
            currentLocation = new MutableLiveData<>(null);
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
            leftButton = view.findViewById(R.id.action_bar_pass);
            rightButton = view.findViewById(R.id.action_bar_fail);
            uploadButton = view.findViewById(R.id.action_bar_upload_trials);
            recordNumButton = view.findViewById(R.id.action_bar_recordnum);
            generateQR = view.findViewById(R.id.action_bar_generateQR);
            deleteTrials = view.findViewById(R.id.action_bar_delete_trials);
            valueEditText = view.findViewById(R.id.countvalue_editText);
            updateView();

            if (reqLocation) {
                getLocation();
                final Observer<GeoLocation> nameObserver = new Observer<GeoLocation>() {
                    @Override
                    public void onChanged(@Nullable final GeoLocation newLoc) {
                        //System.out.println(currentLocation.getValue().getLat());
                        if (newLoc != null) {
                            showViews();
                        }
                    }
                };
                currentLocation.observe(getViewLifecycleOwner(), nameObserver);
            } else {
                showViews();
            }
        }
        else {
            view = inflater.inflate(R.layout.fragment_closed_action, container, false);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        
        if (reqLocation) {
            final Observer<GeoLocation> nameObserver = new Observer<GeoLocation>() {
                @Override
                public void onChanged(@Nullable final GeoLocation newLoc) {
                    String locString = "(" + currentLocation.getValue().getLat() + ", "
                                            + currentLocation.getValue().getLon() + ")";
                    Log.d("Fetched Location", locString);
                }
            };
            currentLocation.observe(this, nameObserver);
        }
    }

    /**
     * saves a QrCode, passed in as a bitmap as a png file on the device
     * @param code
     *  The bitmap referring to the QrCode
     * @param id
     *  The Id of the QrCode, currently used as the name for the QrCode
     * @throws IOException
     */
    public void saveQrCode(Bitmap code, String id) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        code.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        //File dir = getContext().getExternalFilesDir("QrCodes" + File.separator + id + ".png");
        File f = new File(getContext().getExternalFilesDir("QrCodes"), id + ".png");
        boolean fc = f.createNewFile();
        if(fc){
            Log.d("File Creation", "Created file " + f.getAbsolutePath());
        } else {
            Log.d("File Creation", "File already exists");
        }
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
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
            trialsNumber.setText(""+trials+"/"+minimumNumberTrials);
        else
            trialsNumber.setText(""+trials);
    }

    /**
     * Makes the required Views visible.
     */
    private void showViews() {
        uploadButton.setVisibility(View.VISIBLE);
        generateQR.setVisibility(View.VISIBLE);
        deleteTrials.setVisibility(View.VISIBLE);
        if (manager.getType().equals("binomial trials".toLowerCase())) {
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
            generateQR.setOnClickListener((v) -> {
                AlertDialog.Builder biDialog = new AlertDialog.Builder(getContext());
                biDialog.setTitle("Select QR Code Value");
                final Spinner selection = new Spinner(getContext());
                String[] items = new String[]{"Pass", "Fail"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                selection.setAdapter(adapter);
                biDialog.setView(selection);
                biDialog.setPositiveButton("GENERATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = selection.getSelectedItem().toString();
                        QrCode generated;
                        if(value.equals("Pass")){
                            generated = manager.createQrCodeObject(1.0);
                        } else {
                            generated = manager.createQrCodeObject(0.0);
                        }
                        Bitmap qrMap = null;
                        try {
                            qrMap = manager.IdToQrCode(generated.getQrId());
                        } catch(WriterException writerException){
                            Log.d("QrGen", writerException.getMessage());
                        }
                        manager.uploadQR(generated);
                        Log.d("Generated", generated.getQrId());
                        try {
                            saveQrCode(qrMap, generated.getQrId());
                        } catch (IOException e) {
                            Log.d("QrSave", e.getMessage());
                        }
                        dialog.dismiss();
                    }
                });
                biDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = biDialog.create();
                alert.show();
            });
        } else if (manager.getType().equals("Non-Negative Integer Counts".toLowerCase())) {
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
            generateQR.setOnClickListener((v) -> {
                AlertDialog.Builder nncoDialog = new AlertDialog.Builder(getContext());
                nncoDialog.setTitle("Enter QR Code Value");
                final EditText value = new EditText(getContext());
                value.setHint("Count");
                value.setInputType(InputType.TYPE_CLASS_NUMBER);
                nncoDialog.setView(value);
                nncoDialog.setPositiveButton("GENERATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double input = Double.parseDouble(value.getText().toString());
                        QrCode generated = manager.createQrCodeObject(input);
                        Bitmap qrMap = null;
                        try {
                            qrMap = manager.IdToQrCode(generated.getQrId());
                        } catch(WriterException writerException){
                            Log.d("QrGen", writerException.getMessage());
                        }
                        manager.uploadQR(generated);
                        Log.d("Generated", generated.getQrId());
                        try {
                            saveQrCode(qrMap, generated.getQrId());
                        } catch (IOException e) {
                            Log.d("QrSave", e.getMessage());
                        }
                        Log.d("Generated", generated.getQrId());
                        dialog.dismiss();
                    }
                });
                nncoDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = nncoDialog.create();
                alert.show();
            });
        } else if (manager.getType().equals("Measurement Trials".toLowerCase())) {
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
            generateQR.setOnClickListener((v) -> {
                AlertDialog.Builder measDialog = new AlertDialog.Builder(getContext());
                measDialog.setTitle("Enter QR Code Value");
                final EditText value = new EditText(getContext());
                value.setHint("Measurement");
                value.setInputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL |
                        InputType.TYPE_NUMBER_FLAG_SIGNED);
                measDialog.setView(value);
                measDialog.setPositiveButton("GENERATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double input = Double.parseDouble(value.getText().toString());
                        QrCode generated = manager.createQrCodeObject(input);
                        Bitmap qrMap = null;
                        try {
                            qrMap = manager.IdToQrCode(generated.getQrId());
                        } catch(WriterException writerException){
                            Log.d("QrGen", writerException.getMessage());
                        }
                        manager.uploadQR(generated);
                        Log.d("Generated", generated.getQrId());
                        try {
                            saveQrCode(qrMap, generated.getQrId());
                        } catch (IOException e) {
                            Log.d("QrSave", e.getMessage());
                        }
                        Log.d("Generated", generated.getQrId());
                        dialog.dismiss();
                    }
                });
                measDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = measDialog.create();
                alert.show();
            });
        } else if (manager.getType().equals("Counts".toLowerCase())) {
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
            generateQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            generateQR.setOnClickListener((v) -> {
                AlertDialog.Builder coDialog = new AlertDialog.Builder(getContext());
                coDialog.setTitle("Enter QR Code Value");
                final Spinner selection = new Spinner(getContext());
                String[] items = new String[]{"Increment trial", "Commit trial"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                selection.setAdapter(adapter);
                coDialog.setView(selection);
                coDialog.setPositiveButton("GENERATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = selection.getSelectedItem().toString();
                        QrCode generated;
                        if(value.equals("Increment trial")){
                            generated = manager.createQrCodeObject(1.0);
                        } else {
                            generated = manager.createQrCodeObject(-1.0);
                        }
                        Bitmap qrMap = null;
                        try {
                            qrMap = manager.IdToQrCode(generated.getQrId());
                        } catch(WriterException writerException){
                            Log.d("QrGen", writerException.getMessage());
                        }
                        manager.uploadQR(generated);
                        Log.d("Generated", generated.getQrId());
                        try {
                            saveQrCode(qrMap, generated.getQrId());
                        } catch (IOException e) {
                            Log.d("QrSave", e.getMessage());
                        }
                        Log.d("Generated", generated.getQrId());
                        dialog.dismiss();
                    }
                });
                coDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = coDialog.create();
                alert.show();
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
        deleteTrials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.deleteTrials();
                updateView();
            }
        });
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