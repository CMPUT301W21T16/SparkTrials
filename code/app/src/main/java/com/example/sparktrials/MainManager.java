package com.example.sparktrials;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class MainManager {
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
            editor.putString("userId", UUID.randomUUID().toString());
            editor.apply();
            userId = sharedPref.getString("userId", "-1");
        }
        return userId;
    }
}
