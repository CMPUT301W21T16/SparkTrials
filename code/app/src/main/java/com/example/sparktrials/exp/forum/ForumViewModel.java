package com.example.sparktrials.exp.forum;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sparktrials.Callback;
import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ForumViewModel extends ViewModel {
    private String experimentId;
    private MutableLiveData<Question> questions;
    private FirebaseManager firebaseManager;

    public ForumViewModel(String experimentId) {
        questions = new MutableLiveData<>();
        firebaseManager = new FirebaseManager();
        getForumQuestions();
    }

    private void getForumQuestions() {
        String subcollection = experimentId + '/' + experimentId;
        firebaseManager.get(subcollection, "posts", new Callback() {
            @Override
            public void onCallback(DocumentSnapshot document) {

            }
        });
    }
}
