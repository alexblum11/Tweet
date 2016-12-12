package com.example.alexblum.tweet;

/**
 * Created by Alexander on 12/11/2016.
 */

public class Tweet {
    String message;
    String user;
    String timestamp;


    public Tweet() {

    }

    public Tweet(String message, String user, String timestamp) {
        this.message = message;
        this.user = user;
        this.timestamp = timestamp;

    }

}
