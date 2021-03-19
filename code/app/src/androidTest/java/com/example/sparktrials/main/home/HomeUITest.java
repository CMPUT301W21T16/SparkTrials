package com.example.sparktrials.main.home;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.sparktrials.ExperimentActivity;
import com.example.sparktrials.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.example.sparktrials.R;
import static org.junit.Assert.assertTrue;


public class HomeUITest {

    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    @Test
    public void testAddExperiment() {
            solo.clickOnView(solo.getView(R.id.top_app_bar_publish_experiment));
            solo.enterText((EditText)solo.getView(R.id.expTitle_editText),"CoinFlip");
            solo.clickOnButton("OK");
            assertTrue(solo.waitForText("CoinFlip", 1, 2000));


    }
    @Test
    public void testMyExperimentList(){

        solo.clickOnView(solo.getView(R.id.top_app_bar_publish_experiment));
        solo.enterText((EditText)solo.getView(R.id.expTitle_editText),"CoinFlip");
        solo.clickOnButton("OK");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentActivity.class);
        assertTrue(solo.waitForText("CoinFlip", 1, 2000));
    }
    @Test
    public void testSubscribe(){
        solo.clickOnView(solo.getView(R.id.top_app_bar_publish_experiment));
        solo.enterText((EditText)solo.getView(R.id.expTitle_editText),"CoinFlip");
        solo.clickOnButton("OK");
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ExperimentActivity.class);
        solo.clickOnButton("Subscribe");
        solo.goBack();
        solo.assertCurrentActivity("Wrong Actiity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.tab_sub));
        assertTrue(solo.waitForText("CoinFlip", 1, 2000));
    }
}
