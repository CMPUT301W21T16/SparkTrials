package com.example.sparktrials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.QrCode;
import com.example.sparktrials.models.Trial;
import com.example.sparktrials.models.TrialBinomial;
import com.example.sparktrials.models.TrialCount;
import com.example.sparktrials.models.TrialIntCount;
import com.example.sparktrials.models.TrialMeasurement;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class QrScannerActivity extends AppCompatActivity {

    FirebaseManager db = new FirebaseManager();
    private String userId;
    private String trialId;
    private MutableLiveData<Trial> trial = new MutableLiveData<>();
    private MutableLiveData<Profile> profile = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IdManager idManager = new IdManager(this);
        userId = idManager.getUserId();
        downloadProfile();

        IntentIntegrator qrIntegrator = new IntentIntegrator(this);
        qrIntegrator.setPrompt("Scan a QRCode");
        qrIntegrator.setOrientationLocked(true);
        qrIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        createTrial(intentResult.getContents());
        Intent trialResult = new Intent();
        trialResult.putExtra("trial", (Serializable) trial.getValue());
        setResult(1, trialResult);
        finish();
    }

    public void downloadProfile() {
        db.get("users", userId, proData -> {
            Profile prof = new Profile(userId);

            prof.setUsername((String) proData.getData().get("name"));
            prof.setContact((String) proData.getData().get("contact"));
            prof.setSubscriptions((ArrayList<String>) proData.getData().get("subscriptions"));

            profile.setValue(prof);
        });
    }

    public void createTrial(String qrId) {
        db.get("qrCodeData", qrId, qrData -> {
            String trialType = (String) qrData.getData().get("TrialType");
            Trial tri;
            Double value = (Double) qrData.getData().get("Value");
            if(trialType.equals("binomial trials")){
                tri = new TrialBinomial(value == 1.0 ? true : false);
            } else if(trialType.equals("counts")){
                tri = new TrialCount();
            } else if(trialType.equals("non-negative integer counts")) {
                tri = new TrialIntCount(value.intValue());
            } else {
                tri = new TrialMeasurement(value);
            }

            tri.setProfile(profile.getValue());
            tri.setId(UUID.randomUUID().toString());
            trial.setValue(tri);
        });
    }
}
