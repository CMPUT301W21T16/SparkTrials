package com.example.sparktrials.main.me;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sparktrials.Callback;
import com.example.sparktrials.FirebaseManager;
import com.example.sparktrials.IdManager;
import com.example.sparktrials.R;
import com.google.firebase.firestore.DocumentSnapshot;

public class MeFragment extends Fragment {


    EditText et_name, etContact;
    Button updateButton;

    TextView tvUserID, tvName, tvContact;

    String userID;

    /**
     * on Create view creates the view when fragment is clicked
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        return root;
    }

    /**
     * When the view is created this method initializes all the necessary values and handles button clicking
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view , Bundle savedInstanceState){

        FirebaseManager firebaseManager = new FirebaseManager();

        IdManager idManager = new IdManager(this.getContext());
        userID=  idManager.getUserId();


        firebaseManager.get("users", userID, new Callback() {
            @Override
            public void onCallback(DocumentSnapshot document) {

                String name, contact;
                name = (String) document.get("name");
                contact = (String) document.get("contact");


                // Set user id text view
                tvUserID= getView().findViewById(R.id.user_id);
                tvUserID.setText(userID);

                // Set username
                tvName = getView().findViewById(R.id.tvName);
                tvName.setText(name);
                // Set contact
                tvContact = getView().findViewById(R.id.experimenter_contact);
                tvContact.setText(contact);

                // initialize update button

            }
        });

        updateButton = getView().findViewById(R.id.btn_ep);


        /**
         * Method to check when a user clicks update profile
         */
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View updateMe = getLayoutInflater().inflate(R.layout.update_me_fragment, null);
                builder.setView(updateMe);
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_name = updateMe.findViewById(R.id.et_name);
                            etContact= updateMe.findViewById((R.id.etContact));
                            updateProfile();
                            Toast.makeText(getContext() , "updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext() , "Nevermind", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /** Update Profile Method
     * Checks to make sure data is input and then updates data to the database
     */
    public void updateProfile() {
        IdManager idManager = new IdManager(this.getContext());
        userID=  idManager.getUserId();
        FirebaseManager firebaseManager = new FirebaseManager();
        String nameInput = et_name.getText().toString();
        String contactInput = etContact.getText().toString();
        if (!nameInput.isEmpty() ){

            firebaseManager.update("users", userID, "name", nameInput );
            tvName.setText(nameInput);

        }
        if (!contactInput.isEmpty() ){
            firebaseManager.update("users", userID, "contact", contactInput );
            tvContact.setText(contactInput);
        }
    }
}

