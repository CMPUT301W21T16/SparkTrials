package com.example.sparktrials;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExperimentActivity extends AppCompatActivity {

    private String userID;
    private String experimentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_main);
        BottomNavigationView navView = findViewById(R.id.nav_view_exp);

        userID = getIntent().getStringExtra("USER_ID");
        experimentID = getIntent().getStringExtra("EXPERIMENT_ID");

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference userCollectionReference = db.collection("users");
        final CollectionReference expCollectionReference = db.collection("experiments");



    }
}
