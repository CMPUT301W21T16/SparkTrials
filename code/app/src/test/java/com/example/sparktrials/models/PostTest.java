package com.example.sparktrials.models;

import junit.framework.TestCase;

public class PostTest extends TestCase {
    /**
     * Test content getter, expId getter, profile getter, question getter, and answer getter
     * by verifying return results with inputted results
     */
    public void testGetters() {
        Profile profile1 = new Profile("proId1");
        Profile profile2 = new Profile("proId2");
        Question question = new Question("content", "expId", profile1);
        Answer answer = new Answer("content", "expId", profile2, question);
        question.addAnswer(answer);
        assertEquals("get content does not work", "content", question.getContent());
        assertEquals("get id does not work", "expId", question.getExpId());
        assertEquals("get profile does not work", "proId1", question.getProfile().getId());
        assertEquals("get answers does not work", 1, question.getAnswers().size());
        assertEquals("get question does not work", "proId1", answer.getQuestion().getProfile().getId());
    }
}