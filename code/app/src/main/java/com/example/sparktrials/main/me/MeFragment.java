package com.example.sparktrials.main.me;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sparktrials.IdManager;
import com.example.sparktrials.R;
import com.example.sparktrials.models.Profile;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MeFragment extends Fragment {


    EditText etName, etContact;
    Profile userProfile;
    Button updateButton;
    String userID = "726c77d8-54c7-41a1-a149-afe608892add";
    String name, cellphone;
    TextView tvUserID;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        return root;
    }

    public MeFragment() {

        firestore.collection("users").document(userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        name= (String) document.get("name");
                        etName = getView().findViewById(R.id.et_name);
                        etName.setText(name);
                        cellphone = (String) document.get("contact");
                        etContact = getView().findViewById(R.id.et_contact);
                        etContact.setText(cellphone);



                        Log.d("Data", document.getId() + " => " + document.getData());

                    }
                });
    }




    public void onViewCreated(View view , Bundle savedInstanceState){
        // Set user id text view
        tvUserID= getView().findViewById(R.id.user_id);
        tvUserID.setText(userID);

        // Set username
        etName = getView().findViewById(R.id.et_name);
        etName.setText(name);


        // Set contact
        etContact = getView().findViewById(R.id.et_contact);
        etContact.setText(cellphone);

        // initialize update button
        updateButton = getView().findViewById(R.id.btn_up);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });



    }









    /** Update Profile Method
     * Checks to make sure data is input
     *
     *
     */
    public void updateProfile() {
        Map<String,Object> data = new HashMap<>();
        DocumentReference dRef = FirebaseFirestore.getInstance().collection("users").document("726c77d8-54c7-41a1-a149-afe608892add");


        String nameInput = etName.getText().toString();
        if (!nameInput.isEmpty() ){
            data.put("name",nameInput);
            dRef.set(data);

        }else{
            Toast.makeText(this.getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
        }


        String contactInput = etContact.getText().toString();

        if (!contactInput.isEmpty() ){
            data.put("contact",contactInput);
            dRef.set(data);

        }else{
            Toast.makeText(this.getContext(), "Please enter a valid contact", Toast.LENGTH_LONG).show();
        }




        Log.d("Button", "update profile clicked");
    }


}

