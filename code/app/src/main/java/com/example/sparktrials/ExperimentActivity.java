package com.example.sparktrials;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ExperimentActivity extends AppCompatActivity {

    private String userID;
    private String experimentID;
    private TextView title;
    private TextView description;
    private TextView region;
    private TextView minTrials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_main);
        BottomNavigationView navView = findViewById(R.id.nav_view_exp);
        title = (TextView) findViewById(R.id.experiment_title);
        description = (TextView) findViewById(R.id.experiment_description);
        region = (TextView) findViewById(R.id.experiment_region);
        minTrials = (TextView) findViewById(R.id.experiment_min_trials);

        ExperimentManager manager = new ExperimentManager();

        userID = getIntent().getStringExtra("USER_ID");
        experimentID = getIntent().getStringExtra("EXPERIMENT_ID");

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Experiment experiment = manager.getExperiment(experimentID);

        title.setText(experiment.getTitle());
        description.setText(experiment.getDesc());
        region.setText("Latitude: " + String.format("%.2f", experiment.getRegion().getLat().toString()) +
        "  Longitude: " + String.format("%.2f", experiment.getRegion().getLon().toString()));
        minTrials.setText("Minimum trials to upload: " + experiment.getMinNTrials());


    }
}
