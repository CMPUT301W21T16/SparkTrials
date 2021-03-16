package com.example.sparktrials;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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


//    public String generateRandomId(String collection_to_check) {
//        String id = generateRandomId();
//        FirebaseManager firebaseManager = new FirebaseManager();
//        firebaseManager.get(collection_to_check, id, new Callback() {
//            @Override
//            public void onCallback(DocumentSnapshot document) {
//                if (document.exists())
//                    generateRandomId(collection_to_check);
//            }
//        });
//
//    }

}
