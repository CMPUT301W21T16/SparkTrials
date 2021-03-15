package com.example.sparktrials.models;

public class Answer extends Post{
    Question question;

    /**
     * An answer to a question for an experiment
     * @param content
     *   The body of the answer
     * @param expId
     *   The id of the experiment that this answer is part of
     * @param profile
     *   The profile of the user who wrote this answer
     * @param question
     *    The question that this answer is answering
     */
    Answer(String content, String expId, Profile profile, Question question){
        super(content, expId, profile);
        this.question = question;
    }

    /**
     * Retrieves the question which this answer is answering
     * @return
     *   Returns a Question
     */
    public Question getQuestion() {
        return question;
    }
}
