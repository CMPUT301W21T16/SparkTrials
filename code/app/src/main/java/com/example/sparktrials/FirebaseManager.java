package com.example.sparktrials;


import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FirebaseManager {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final String LOG_TAG = "Firebase ";

    /**
     * Retrieve data from Firestore.
     * @param collection
     *      Collection name.
     * @param document
     *      Document name.
     * @param callback
     *      A callback method to handle retrieved result.
     *      This method is only called if valid documents are retrieved.
     */
    public void get(String collection, String document, Callback callback) {
        CollectionReference ref = firestore.collection(collection);
        ref.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String task_path = collection + "/" + document;
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result.exists()) {
                        callback.onCallback(result);
                        Log.d(LOG_TAG + "[Retrieve]", "Succeed: " + task_path);
                    } else
                        Log.d(LOG_TAG + "[Retrieve]", "Failed (result empty): " + task_path);
                } else {
                    Log.d(LOG_TAG + "[Retrieve]", "Failed: " + task_path);
                }

            }
        });
    }


    /**
     * Write a document to Firestore.
     * The new data will overwrite any old data if the document already exists on Firestore.
     * @param collection
     *      Collection name.
     * @param document
     *      Document name.
     * @param map
     *      A Map object that holds all the fields and values of an document.
     */
    public void set(String collection, String document, Map<String, Object> map) {
        CollectionReference ref = firestore.collection(collection);
        ref.document(document).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String task_path = collection + "/" + document;
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG + "[Set]", "Succeed: " + task_path);
                } else {
                    Log.d(LOG_TAG + "[Set]", "Failed: " + task_path);
                }
            }
        });
    }


    /**
     * Update data in a document on Firestore.
     * This method only supports one change to the Firestore document.
     * @param collection
     *      Collection name.
     * @param document
     *      Document name.
     * @param field
     *      Field to change.
     * @param value
     *      Value of the field to change.
     *
     */
    public void update(String collection, String document, String field, String value) {
        CollectionReference ref = firestore.collection(collection);
        ref.document(document).update(field, value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String task_path = collection + "/" + document;
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG + "[Update]", "Succeed: " + task_path);
                } else {
                    Log.d(LOG_TAG + "[Update]", "Failed: " + task_path);
                }
            }
        });
    }


    /**
     * Update data in a document on Firestore.
     * This method supports multiple changes to the Firestore document via a map object.
     * @param collection
     *      Collection name.
     * @param document
     *      Document name.
     * @param map
     *      A Map object that holds all the fields and values of an document.
     */
    public void update(String collection, String document, Map<String, Object> map) {
        CollectionReference ref = firestore.collection(collection);
        ref.document(document).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String task_path = collection + "/" + document;
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG + "[Update]", "Succeed: " + task_path);
                } else {
                    Log.d(LOG_TAG + "[Update]", "Failed: " + task_path);
                }
            }
        });
    }






}
