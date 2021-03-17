package com.example.sparktrials;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.UUID;

public class IdManager {
    private Context context;

    public IdManager(Context context) {
        this.context = context;
    }

    /**
     * Return the unique user id. Generate a random UUID if no id is found in the preference file.
     * Note: This user id is lost when the app is uninstalled.
     * @return
     * Return the user id as a string.
     */

    public String getUserId() {
        SharedPreferences sharedPref = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("userId", "-1");

        if (userId.equals("-1")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userId", generateRandomId());
            editor.apply();
            userId = sharedPref.getString("userId", "-1");
        }
        return userId;
    }


    public String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public void login() {
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.get("users", getUserId(), new Callback() {
            @Override
            public void onCallback(DocumentSnapshot document) {
                if (document.exists()) {
                    Toast.makeText(context.getApplicationContext(), "Welcome back, " + document.getData().get("name"), Toast.LENGTH_SHORT).show();
                    Log.d("USER INFO", "DocumentSnapshot data: " + document.getData());
                } else {
                    firebaseManager.createUserProfile(getUserId());
                    Toast.makeText(context.getApplicationContext(), "New user profile created", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
