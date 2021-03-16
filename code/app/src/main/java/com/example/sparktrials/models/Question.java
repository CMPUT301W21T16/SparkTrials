package com.example.sparktrials.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Question extends Post {
    ArrayList<Answer> answers;

    /**
     * A constructor for initializing a question
     * @param content
     *    The content (text body) of the post
     * @param expId
     *    The id of the experiment its for
     * @param profile
     *    The profile of the poster
     */
    Question(String content, String expId, Profile profile){
        super(content, expId, profile);
        this.answers = new ArrayList<>();
    }

    /**
     * A constructor for rebuilding a question from the database
     * @param content
     *    The content (text body) of the post
     * @param expId
     *    The id of the experiment its for
     * @param profile
     *    The profile of the poster
     * @param answers
     *    The list of answers that this post has
     */
    Question(String content, String expId, Profile profile, ArrayList<Answer> answers){
        super(content, expId, profile);
        this.answers = answers;
    }

    /**
     * Gets the array of answers that this post has
     * @return
     *    An array of answers
     */
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    /**
     * Replaces the list of answers with the one passed
     * Should not be used unless to setup a question
     * @param answers
     *    A list of new answers
     */
    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    /**
     * Adds a given answer to the list of answers
     * @param answer
     *    An answer, ideally an answer to this question
     */
    public void addAnswer(Answer answer){
        this.answers.add(answer);
    }
}
