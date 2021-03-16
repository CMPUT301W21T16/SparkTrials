package com.example.sparktrials;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

public class ExperimentActivity extends AppCompatActivity {

    private ExperimentViewModel expManager;
    private String userId;
    private String experimentId;
    private Button backToMain;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private TextView titleText;
    private TextView descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_main);

        experimentId = getIntent().getStringExtra("EXPERIMENT_ID");
        IdManager idManager = new IdManager(this);
        userId = idManager.getUserId();


        //NEW
        backToMain = findViewById(R.id.back_button);
        titleText = findViewById(R.id.text_title);
        descText = findViewById(R.id.text_desc);

        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        expManager = new ViewModelProvider(this).get(ExperimentViewModel.class);
        expManager.init(experimentId);

        // This will allow the experiment to be displayed when the activity is launched
        final Observer<Experiment> nameObserver = new Observer<Experiment>() {
            @Override
            public void onChanged(Experiment experiment) {
                titleText.setText(experiment.getTitle());
                descText.setText(experiment.getDesc());

                adapter.addFragment(new ActionFragment(experiment), "Action");
                adapter.addFragment(new StatsFragment(experiment), "Stats");
                adapter.addFragment(new ForumFragment(experiment), "Forum");
                adapter.addFragment(new LocationFragment(experiment), "Map");
                if (experiment.getType()==null) {
                    adapter.addFragment(new AdminFragment(experiment), "Admin");
                }
                viewPager.setAdapter(adapter);
                tablayout.setupWithViewPager(viewPager);
            }
        };
        expManager.getExperiment().observe(this, nameObserver);

        backToMain.setOnClickListener((v) -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        });
    }
}
