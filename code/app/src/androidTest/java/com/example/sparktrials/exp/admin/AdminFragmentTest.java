package com.example.sparktrials.exp.admin;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.sparktrials.MainActivity;
import com.example.sparktrials.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdminFragmentTest {
    private Solo solo;
    private FirebaseFirestore db;
    String expTitle, expOwnerId;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        db = FirebaseFirestore.getInstance();
        expTitle = "AdminFragmentTest";
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        // Get owner's username
        solo.clickOnView(solo.getView(R.id.navigation_me));
        expOwnerId =  ((TextView) solo.getView(R.id.user_id)).getText().toString();
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.top_app_bar_publish_experiment));
        Spinner spinner = (Spinner) solo.getView(R.id.experiment_type_spinner);
        spinner.setSelection(0, true);
        solo.enterText((EditText) solo.getView(R.id.expTitle_editText), expTitle);
        solo.clickOnButton("POST");
        solo.clickOnView(solo.getView(R.id.navigation_search));
        solo.enterText((EditText) solo.getView(R.id.search_bar),expTitle);
        solo.clickInList(0,0);
        solo.clickOnScreen(900,600);
        solo.sleep(1000);
    }
    @Test
    public void testEndExperiment(){
        solo.clickOnView(solo.getView(R.id.end_experiment_button));
        solo.sleep(1000);
        solo.clickOnScreen(150,600);
        solo.sleep(1000);
        assertTrue(solo.searchText("Experiment is Closed"));
    }

    @Test
    public void testUnPublish(){
        solo.clickOnView(solo.getView(R.id.unpublish_experiment_button));
        solo.sleep(1000);
        solo.clickOnButton("YES");
        assertTrue(solo.searchText("Publish Experiment"));
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.clickOnView(solo.getView(R.id.navigation_search));
        solo.enterText((EditText) solo.getView(R.id.search_bar),expTitle);
        solo.clickOnView(solo.getView(R.id.search_button));
        solo.enterText((EditText) solo.getView(R.id.search_bar),"");
        assertFalse(solo.searchText(expTitle));
    }

    @After
    public void deleteExperiment() throws InterruptedException {
        CollectionReference expCollection = db.collection("experiments");
        expCollection.whereEqualTo("Title", expTitle).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()) {
                            expCollection.document(doc.getId()).delete();
                        }
                    }
                });

        synchronized (solo) {
            solo.wait(2000);
        }
    }
}
