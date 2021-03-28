package com.example.sparktrials;

import android.content.Context;
import android.util.Log;

import com.example.sparktrials.models.Profile;
import com.example.sparktrials.FirebaseManager;
import com.google.firebase.FirebaseApp;

import junit.framework.TestCase;

import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManagerTest extends TestCase {
    private FirebaseManager manager = new FirebaseManager();

    public void testAll() {

        String test_col = "test_col";
        String test_doc = "test_doc";
        String test_entry1 = "test_entry1";
        String test_entry2 = "test_entry2";

        Integer value1 = 5;
        Integer new_value1 = 10;
        String value2 = "sup";
        String new_value2 = "wassup";

        Map<String, Object> map = new HashMap<>();
        map.put(test_entry1, value1);
        map.put(test_entry2, value2);

        manager.set(test_col, test_doc, map); //tests the set method

        manager.get(test_col, test_doc, data -> { //tests the get method
            assertEquals("firebase setting/getting not working", value1, (Integer)data.getData().get("test_entry1"));
            assertEquals("firebase setting/getting not working", value2, (String)data.getData().get("test_entry2"));
        });

        map = new HashMap<>();
        map.put(test_entry1, new_value1);
        map.put(test_entry2, new_value2);

        manager.update(test_col, test_doc, map); //tests the update method

        manager.get(test_col, test_doc, data -> { //tests the get method
            assertEquals("firebase setting/getting not working", new_value1, (Integer)data.getData().get("test_entry1"));
            assertEquals("firebase setting/getting not working", new_value2, (String)data.getData().get("test_entry2"));
        });

        }
}