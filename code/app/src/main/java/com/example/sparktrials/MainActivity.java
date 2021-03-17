package com.example.sparktrials;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.sparktrials.main_ui.publish.PublishFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.top_app_bar);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.top_app_bar_draft:
                        Log.d("BUTTON", "draftClicked");
                        break;
                    case R.id.top_app_bar_scan_qr_code:
                        Log.d("BUTTON", "scanClicked");
                        break;
                    case R.id.top_app_bar_publish_experiment:
                        Log.d("BUTTON", "publishClicked");
                        new PublishFragment().show(getSupportFragmentManager(),"Add Experiment");
                        break;
                    default:
                        Log.d("BUTTON", "something wrong");
                }
                return true;
            }
        });
//        setSupportActionBar(myToolbar);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseManager firebaseManager = new FirebaseManager();
        IdManager idManager = new IdManager(this);
        Log.d("USER ID",idManager.getUserId());

        Map<String, Object> user_test = new HashMap<>();
        ArrayList<String> subscribers = new ArrayList<>();
        user_test.put("uid",idManager.getUserId());
        user_test.put("name", "Test");
        user_test.put("contact","123456890");
        user_test.put("subscribers",subscribers);

        // Add a test document to users collection.
        firebaseManager.set("users",idManager.getUserId(), user_test);

        // Retrieve and log user information from Firestore.
        firebaseManager.get("users", idManager.getUserId(), new Callback() {
            @Override
            public void onCallback(DocumentSnapshot document) {
                Log.d("USER INFO", "DocumentSnapshot data: " + document.getData());
            }
        });
    }
}