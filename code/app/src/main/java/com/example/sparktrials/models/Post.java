package com.example.sparktrials.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An abstract class that embodies questions and answers
 * Used in the forum
 */
public abstract class Post {
    private String id;
    private String body;
    private String expId;
    private Profile profile;
    private String author;
    private Date date;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * A constructor for post. A post always has a poster, some body, and for what experiment it was posted on
     * @param body
     * @param expId
     * @param profile
     */
    Post(String id, String body, String expId, Profile profile){
        this.id = id;
        this.body = body;
        this.expId = expId;
        this.profile = profile;
        this.date = new Date();

    }

    /**
     * A constructor for post. A post always has a poster, some body, and for what experiment it was posted on
     * @param body
     * @param expId
     * @param author
     */
    Post(String id, String body, String expId, String author){
        this.id = id;
        this.body = body;
        this.expId = expId;
        this.author = author;
        this.date = new Date();

    }

    /**
     * Retrieves the experiment id of this post
     * @return
     *    The experiment id of the post
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
     * Retrieves the body of the post
     * @return
     *    The body of the post
     */
    public String getBody() {
        return body;
    }

    /**
     * Retrieves the date of the post
     * @return
     */
    public Date getDate(){ return date; }


    public String getFormattedDate() {
        return formatter.format(date);
    }

  
    public String getDay(){
        String pattern = "EEE MMM DD HH:mm:ss z yyyy";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String strDate = df.format(date);
        strDate = strDate.substring(4,10);
        return strDate;
    }

    public String getAuthor() {
        return author;
    }
}
