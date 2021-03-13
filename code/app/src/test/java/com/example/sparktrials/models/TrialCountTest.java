package com.example.sparktrials.models;

import junit.framework.TestCase;

/**
 * A class to test only the TrialCount subclass methods
 */
public class TrialCountTest extends TestCase {

    TrialCount trial;

    /**
     * Tests that the get and set functions both work by verifying that
     * we retrieve the correct count and after a change to count, we also retrieve
     * that correct count
     */
    public void testGetSetCount() {
        GeoLocation location = new GeoLocation(30.0, 40.0);
        Profile profile = new Profile("foo1", "foo2", "foo3");
        this.trial = new TrialCount(1, location, profile);
        assertEquals("get/setCount does not work", 0, (int)trial.getCount());
        this.trial.setCount(100);
        assertEquals("get/setCount does not work", 100, (int)trial.getCount());
    }

    /**
     * Tests that addCount increments count by 1
     */
    public void testAddCount() {
        GeoLocation location = new GeoLocation(30.0, 40.0);
        Profile profile = new Profile("foo1", "foo2", "foo3");
        this.trial = new TrialCount(1, location, profile);
        trial.addCount();
        assertEquals("addCount does not work", 1, (int)trial.getCount());
        trial.addCount();
        trial.addCount();
        trial.addCount();
        assertEquals("addCount does not work", 4, (int)trial.getCount());

    }
}