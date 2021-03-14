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


    /**
     * Return the unique user id. Generate a random UUID if no id is found in the preference file.
     * Note: This user id is lost when the app is uninstalled.
     * @return
     * Return the user id as a string.
     */

    public static String getUserId(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String userId = sharedPref.getString("userId", "-1");

        if (userId.equals("-1")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userId", generateRandomId());
            editor.apply();
            userId = sharedPref.getString("userId", "-1");
        }
        return userId;
    }


    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }


    public static boolean generateRandomId(CollectionReference collection_to_check) {
        String id = generateRandomId();
        collection_to_check.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                }
            }
        });
        return true;
    }

}
