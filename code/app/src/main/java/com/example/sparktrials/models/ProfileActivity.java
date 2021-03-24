package com.example.sparktrials.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sparktrials.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * This class represents an activity that displays a user's profile.
 */

public class ProfileActivity extends AppCompatActivity {

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private CollectionReference usersCollection = db.collection("users");

    private String userId;
    private MutableLiveData<Profile> userProfile;

    private TextView userNameTextView;
    private TextView userContactInfoTextView;

    private String TAG = "Fetching Profile...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        displayUpButton();

        userId = (String) getIntent().getExtras().get("USER_ID");

        userProfile = new MutableLiveData<>();

        userNameTextView = findViewById(R.id.experimenter_name);
        userContactInfoTextView = findViewById(R.id.experimenter_contact);

        // Gets the user's profile information from the database
        usersCollection.document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            Log.d(TAG, document.getId() + " => " + document.getData());
                            String userName = (String) document.get("name");
                            String userContactInfo = (String) document.get("contact");

                            Profile profile = new Profile(userId, userName, userContactInfo);

                            userProfile.setValue(profile);
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });


        // This will allow the list of experiments to be displayed when the search fragment
        // is launched
        final Observer<Profile> nameObserver = new Observer<Profile>() {
            @Override
            public void onChanged(@Nullable final Profile profile) {

                String userName = userProfile.getValue().getUsername();
                String userContactInfo = userProfile.getValue().getContact();

                userNameTextView.setText(userName);
                userContactInfoTextView.setText(userContactInfo);
            }
        };
        userProfile.observe(this, nameObserver);

    }

    /**
     * Displays the "Up" button in the toolbar of the activity.
     */
    private void displayUpButton() {
        Toolbar toolbar = findViewById(R.id.profile_activity_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Sets the "Up" button to act as if a the "Back" button was pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // If the "Back" button is pressed, finish the activity.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}