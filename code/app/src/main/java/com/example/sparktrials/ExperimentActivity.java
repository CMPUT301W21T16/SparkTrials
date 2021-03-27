package com.example.sparktrials;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.sparktrials.exp.ViewPagerAdapter;
import com.example.sparktrials.exp.action.ActionFragment;
import com.example.sparktrials.exp.admin.AdminFragment;
import com.example.sparktrials.exp.forum.ForumFragment;
import com.example.sparktrials.exp.location.LocationFragment;
import com.example.sparktrials.exp.stats.StatsFragment;
import com.example.sparktrials.main.search.SearchViewModel;
import com.example.sparktrials.models.Experiment;
import com.example.sparktrials.models.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A class with tabs for each ability of an experiment
 * Displays the title and description of the experiment, also the subscribe button persistently
 */
public class ExperimentActivity extends AppCompatActivity {

    private ExperimentViewModel expManager;
    private String userId;
    private String experimentId;

    private TabLayout tablayout;
    private ViewPager viewPager;

    private Button subscribe;
    private Button backToMain;
    private TextView titleText;
    private TextView descText;

    private String textSubscribe = "Subscribe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_main);

        experimentId = getIntent().getStringExtra("EXPERIMENT_ID");
        IdManager idManager = new IdManager(this);
        userId = idManager.getUserId();
        expManager = new ViewModelProvider(this).get(ExperimentViewModel.class);
        expManager.init(experimentId, userId);

        subscribe = findViewById(R.id.button_subscribe);
        backToMain = findViewById(R.id.back_button);
        titleText = findViewById(R.id.text_title);
        descText = findViewById(R.id.text_desc);
        descText.setMovementMethod(new ScrollingMovementMethod());

        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // This will allow the subscription button to dynamically update
        final Observer<Profile> name1Observer = new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if (profile.getSubscriptions().contains(experimentId)){
                    subscribe.setText("Unsubscribe");
                } else {
                    subscribe.setText("Subscribe");
                }
            }
        };
        expManager.getProfile().observe(this, name1Observer);
        expManager.subscribe().observe(this, name1Observer);
        // This will allow the experiment to be displayed when the activity is launched
        final Observer<Experiment> name2Observer = new Observer<Experiment>() {

            @Override
            public void onChanged(Experiment experiment) {

                titleText.setText(experiment.getTitle());
                descText.setText(experiment.getDesc());

                adapter.addFragment(new ActionFragment(experiment), "Action");
                adapter.addFragment(new StatsFragment(experiment), "Stats");
                adapter.addFragment(new ForumFragment(experiment), "Forum");
                if (experiment.getReqLocation()) {
                    adapter.addFragment(new LocationFragment(experiment), "Map");
                }
                if (experiment.getOwner().getId().equals(userId)) {
                    adapter.addFragment(new AdminFragment(experiment), "Admin");
                }
                viewPager.setAdapter(adapter);
                tablayout.setupWithViewPager(viewPager);
            }
        };
        expManager.getExperiment().observe(this, name2Observer);

        subscribe.setOnClickListener((v) -> {
            this.subscribe();

        });

        backToMain.setOnClickListener((v) -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        });

    }

    public void subscribe() {
        if (expManager.getExperiment().getValue().getReqLocation() &&
                !expManager.getProfile().getValue().getSubscriptions().contains(experimentId)) {
            //Experiment requires locations and user is not currently subscribed
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This experiment requires your location to be submitted");
            builder.setMessage("Are you sure you want to subscribe?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //subscribe
                    expManager.subscribe();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            //Experiment doesn't require locations or user is already subscribed
            //unsubscribe
            expManager.subscribe();
        }
        expManager.updateSubscribe();
    }

    /**
     * When this activity is over, update the subscribe attribute
     */
//    @Override
//    protected void onStop() {
//        super.onStop();
//        expManager.updateSubscribe();
//    }
}
