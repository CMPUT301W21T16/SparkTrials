package com.example.sparktrials;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ExperimentActivity extends AppCompatActivity {

    private String userID;
    private String experimentID;
    private TextView title;
    private TextView open;
    private TextView description;
    private TextView owner;
    private TextView date;
    private TextView region;
    private TextView minTrials;
    FloatingActionButton backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_main);
        //BottomNavigationView navView = findViewById(R.id.nav_view_exp);
        title = (TextView) findViewById(R.id.experiment_title);
        open = (TextView) findViewById(R.id.experiment_isOpen);
        description = (TextView) findViewById(R.id.experiment_description);
        owner = (TextView) findViewById(R.id.experiment_owner);
        date = (TextView) findViewById(R.id.experiment_date);
        region = (TextView) findViewById(R.id.experiment_region);
        minTrials = (TextView) findViewById(R.id.experiment_min_trials);
        backToMain = findViewById(R.id.back_button);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");



        userID = getIntent().getStringExtra("USER_ID");
        experimentID = getIntent().getStringExtra("EXPERIMENT_ID");
        ExperimentManager manager = new ExperimentManager(experimentID);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_search, R.id.navigation_me)
//                .build();
//        Activity mainActivity = MainActivity;
//        NavController navController = Navigation.findNavController(MainActivity, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        Experiment experiment = manager.getExperiment(experimentID);

        title.setText(experiment.getTitle());
        if(experiment.getOpen()){
            open.setText("Open");
        } else {
            open.setText("Closed");
        }
        description.setText(experiment.getDesc());
        owner.setText("Owner McOwnerface");
        date.setText(dateFormat.format(experiment.getDate()));
        String regionText = "Latitude: " + String.format("%.2f", experiment.getRegion().getLat()) +
                "  Longitude: " + String.format("%.2f", experiment.getRegion().getLon());
        region.setText(regionText);
        String trialsText = "Minimum trials to upload: " + experiment.getMinNTrials();
        minTrials.setText(trialsText);

        backToMain.setOnClickListener((v) -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        });


    }
}
