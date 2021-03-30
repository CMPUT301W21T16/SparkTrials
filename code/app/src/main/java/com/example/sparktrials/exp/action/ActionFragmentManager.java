package com.example.sparktrials.exp.action;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.QrCode;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.example.sparktrials.models.TrialCount;
import com.example.sparktrials.models.TrialIntCount;
import com.example.sparktrials.models.TrialMeasurement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * When a user decides to upload a trial or generate a qr code or delete a trial action
 * fragment manager deals with that
 */
public class ActionFragmentManager {
    Experiment experiment;
    FirebaseManager firebaseManager = new FirebaseManager();
    int originalNTrials;
    String id;
    Profile profile;
    public ActionFragmentManager(Experiment experiment) {
        this.id=id;
        this.experiment=experiment;
        this.originalNTrials=Integer.parseInt(experiment.getNumTrials());
    }
    public void setProfile(String id){
        profile=firebaseManager.downloadProfile(id);
    }

    /**
     * Adds a binomial trial to the experiment
     * @param result
     */
    public void addBinomialTrial(Boolean result, GeoLocation location){
        TrialBinomial trial = new TrialBinomial(result);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        trial.setLocation(location);
        experiment.addTrial(trial);
    }

    /**
     * Adds a non negative Integer count trial to the experiment
     * @param result
     */
    public void addNonNegIntTrial(Integer result, GeoLocation location){
        TrialIntCount trial = new TrialIntCount(result);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        trial.setLocation(location);
        experiment.addTrial(trial);
    }

    /**
     * Adds a measurement trial to the experiment
     * @param result
     */
    public void addMeasurementTrial(Double result, GeoLocation location){
        TrialMeasurement trial = new TrialMeasurement(result);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        trial.setLocation(location);
        experiment.addTrial(trial);
    }

    /**
     * Adds a count trial to the experiment
     */
    public void addCountTrial(Integer count, GeoLocation location){
        TrialCount trial = new TrialCount();
        trial.setCount(count);
        trial.setId(UUID.randomUUID().toString());
        trial.setProfile(profile);
        trial.setLocation(location);
        experiment.addTrial(trial);
    }

    /**
     * Returns the number of trials in the experiment
     * @return
     */
    public Integer getNTrials(){
        return experiment.getUserTrials(profile.getId()).size();
    }

    /**
     * Returns the minimum number of trials of the experiment
     * @return
     */
    public int getMinNTrials(){
        return experiment.getMinNTrials();
    }

    /**
     * Returns if experiment is open
     * @return
     */
    public Boolean getOpen(){return experiment.getOpen();}
    /**
     * Returns the experiment type
     * @return
     */
    public String getType(){
        return experiment.getType();
    }

    /**
     * TO DO: Uploads the trials to firbase
     */
    public void uploadTrials(){
        firebaseManager.uploadTrials(experiment);
    }
    /**
     * Removes all trials inserted by the user from the experiment object
     */
    public void deleteTrials(){
        int elementsToRemove=(Integer.parseInt(experiment.getNumTrials()) - originalNTrials);
        for (int i=0;i<elementsToRemove;i++){
            experiment.delTrial(experiment.getAllTrials().size()-1);
        }
    }

    /**
     * Created a QrCode object to be uploaded to the database for later use
     * @param value
     *  the value attached to the trial, for non-negatice integer counts and measurements, it is
     *  a value given by the user, for binomials it is 1 for a pass, 0 for a fail, for counts,
     *  it is 1 for increment, -1 for commit
     * @return
     *  returns the QrCode object
     */
    public QrCode createQrCodeObject(double value){
        String id = UUID.randomUUID().toString();
        QrCode newQR = new QrCode(id, experiment.getId(), experiment.getType(), value);
        return newQR;
    }

    /**
     * Uploads a QrCode object to the Firestore database
     * @param qr
     *  The QrCode object to be uploaded
     */
    public void uploadQR(QrCode qr){
        Map<String, Object> map = new HashMap<>();
        map.put("Id", qr.getQrId());
        map.put("ExperimentId", qr.getExperimentId());
        map.put("TrialType", qr.getTrialType());
        map.put("Value", qr.getValue());
        firebaseManager.set("qrCodeData", qr.getQrId(), map);
    }

    /**
     * Generates a Bitmap QrCode corresponding to the given QrCode ID
     * adapted from the following stack overflow question answer from user user6017633 on April 7th 2017
     * https://stackoverflow.com/a/43284184
     * @param id
     *  The QrCode Id to be encoded
     * @return
     *  The bitmap of the QrCode
     * @throws WriterException
     */
    public Bitmap IdToQrCode(String id) throws WriterException{
        BitMatrix bitMatrix;
        int size = 400;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    id,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    size, size
            );
        } catch (IllegalArgumentException illegalArgumentException){
            Log.d("Encode", illegalArgumentException.getMessage());
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorBlack = Color.BLACK;
        int colorWhite = Color.WHITE;

        for(int i = 0; i < bitMatrixHeight; i++) {
            int offset = i * bitMatrixWidth;

            for (int j = 0; j < bitMatrixWidth; j++){
                pixels[offset + j] = bitMatrix.get(i, j) ? colorBlack:colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 400, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}
