package com.example.sparktrials.models;

/**
 * An abstract class that embodies questions and answers
 * Used in the forum
 */
public abstract class Post {
    String content;
    String expId;
    Profile profile;

    /**
     * A constructor for post. A post always has a poster, some content, and for what experiment it was posted on
     * @param content
     * @param expId
     * @param profile
     */
    Post(String content, String expId, Profile profile){
        this.content = content;
        this.expId = expId;
        this.profile = profile;

    }

    /**
     * Retrieves the experiment id of this post
     * @return
     *    The expeirment id of the post
     */
    public String getExpId() {
        return expId;
    }

    /**
     * Retrieves the profile of the user who made the post
     * @return
     *    The profile of the use rwho made the post
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Retrieves the content of the post
     * @return
     *    The content of the post
     */
    public String getContent() {
        return content;
    }

}
