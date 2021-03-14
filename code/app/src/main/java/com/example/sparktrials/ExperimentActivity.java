package com.example.sparktrials;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sparktrials.exp.ViewPagerAdapter;
import com.example.sparktrials.exp.action.ActionFragment;
import com.example.sparktrials.exp.admin.AdminFragment;
import com.example.sparktrials.exp.forum.ForumFragment;
import com.example.sparktrials.exp.location.LocationFragment;
import com.example.sparktrials.exp.stats.StatsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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

    private TabLayout tablayout;
    private ViewPager viewPager;
    private TextView title_text;
    private TextView desc_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_main);

        //NEW
        //SETUP fields
        backToMain = findViewById(R.id.back_button);
        title_text = findViewById(R.id.text_title);
        desc_text = findViewById(R.id.text_desc);
        //Setup tabs
        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Add fragments
        //TO MODIFY THE FRAGMENTS THEMSELVES GO TO i.e. com.example.sparktrials.action.ActionFragment
        adapter.addFragment(new ActionFragment(), "Action");
        adapter.addFragment(new StatsFragment(), "Stats");
        adapter.addFragment(new ForumFragment(), "Forum");
        adapter.addFragment(new LocationFragment(), "Map");
        adapter.addFragment(new AdminFragment(), "Admin");
        //Adapter Setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        //END NEW

//        //BottomNavigationView navView = findViewById(R.id.nav_view_exp);
//        title = (TextView) findViewById(R.id.experiment_title);
//        open = (TextView) findViewById(R.id.experiment_isOpen);
//        description = (TextView) findViewById(R.id.experiment_description);
//        owner = (TextView) findViewById(R.id.experiment_owner);
//        date = (TextView) findViewById(R.id.experiment_date);
//        region = (TextView) findViewById(R.id.experiment_region);
//        minTrials = (TextView) findViewById(R.id.experiment_min_trials);
//        backToMain = findViewById(R.id.back_button);
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//
//        userID = getIntent().getStringExtra("USER_ID");
//        experimentID = getIntent().getStringExtra("EXPERIMENT_ID");
//        ExperimentManager manager = new ExperimentManager(experimentID);
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.navigation_home, R.id.navigation_search, R.id.navigation_me)
////                .build();
////        Activity mainActivity = MainActivity;
////        NavController navController = Navigation.findNavController(MainActivity, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
////        NavigationUI.setupWithNavController(navView, navController);
//        Experiment experiment = manager.getExperiment(experimentID);
//
//        title.setText(experiment.getTitle());
//        if(experiment.getOpen()){
//            open.setText("Open");
//        } else {
//            open.setText("Closed");
//        }
//        description.setText(experiment.getDesc());
//        owner.setText("Owner McOwnerface");
//        date.setText(dateFormat.format(experiment.getDate()));
//        GeoLocation regionObj = new GeoLocation();
//        String regionText = "Latitude: " + String.format("%.2f", regionObj.getLat()) +
//                "  Longitude: " + String.format("%.2f", regionObj.getLon());
//        region.setText(regionText);
//        String trialsText = "Minimum trials to upload: " + experiment.getMinNTrials();
//        minTrials.setText(trialsText);
//
        backToMain.setOnClickListener((v) -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        });
    }
}
