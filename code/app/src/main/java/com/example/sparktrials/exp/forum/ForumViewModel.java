package com.example.sparktrials.exp.forum;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sparktrials.Callback;
import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.models.Profile;
import com.example.sparktrials.models.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ForumViewModel extends ViewModel {
    private String experimentId;
    private MutableLiveData<ArrayList<Question>> questions;
    private FirebaseFirestore db;
    private final String LOG_TAG = "ForumViewModel";

    public ForumViewModel(String experimentId) {
        this.experimentId = experimentId;
        questions = new MutableLiveData<>();
        questions.setValue(new ArrayList<>());
        db = FirebaseFirestore.getInstance();
        getForumQuestions();
    }

    private void getForumQuestions() {
        CollectionReference ref = db.collection("experiments").document(experimentId).collection("posts");
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(LOG_TAG, "Listen failed.", error);
                    return;
                }

                for (QueryDocumentSnapshot document: value) {
                    String id = document.getId();
                    String title = (String) document.get("title");
                    String body = (String) document.get("body");
                    String author = (String) document.get("author");
//                        String date = (String) document.get("date");

                    Question question = new Question(id, title, body, experimentId, author);
                    ArrayList<Question> existingQuestions = questions.getValue();
                    existingQuestions.add(question);
                    questions.setValue(existingQuestions);


                }

            }
        });
//        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document: task.getResult()) {
//                        String id = document.getId();
//                        String title = (String) document.get("title");
//                        String body = (String) document.get("body");
//                        String author = (String) document.get("author");
////                        String date = (String) document.get("date");
//
//                        Question question = new Question(id, title, body, experimentId, author);
//                        ArrayList<Question> existingQuestions = questions.getValue();
//                        existingQuestions.add(question);
//                        questions.setValue(existingQuestions);
//
//
//                    }
//                } else {
//                    Log.d(LOG_TAG, "Failed to get documents.", task.getException());
//                }
//            }
//        });
    }

    public MutableLiveData<ArrayList<Question>> getQuestions() {
        return questions;
    }
}
