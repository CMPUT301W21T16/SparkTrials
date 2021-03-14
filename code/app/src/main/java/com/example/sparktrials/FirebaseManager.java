package com.example.sparktrials;


import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

interface Callback {
    void onCallback(DocumentSnapshot document);
}

public class FirebaseManager {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final String LOG_TAG = "FirebaseManager";

    public void retrieve(String collection, String document, Callback callback) {
        CollectionReference ref = firestore.collection(collection);
        ref.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(task.getResult());
                } else {
                    Log.d(LOG_TAG, "Retrieve failed." + task.getException());
                }

            }
        });
    }


//    private Future<DocumentSnapshot> retrieve() {


//    public DocumentSnapshot get() {
//        Future<> future = new FutureTask<>(new Callable<DocumentSnapshot>() {
//            @Override
//            public Task call() throws Exception {
//                return firestore.collection("users").document("726c77d8-54c7-41a1-a149-afe608892add").get();
//            }
//        });
//    }

//    public void retrieve(String collection, String id) {
//        readData(new Callback() {
//            @Override
//            public void onCallback(DocumentSnapshot document) {
//                Log.d("CALLBACK", document.getData().toString());
//            }
//        });
//    }

}
