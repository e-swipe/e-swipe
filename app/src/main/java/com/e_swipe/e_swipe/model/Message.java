package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class Message {

    String user_id;
    String content;
    String date;

    public Message(String user_id, String content, String date) {
        this.user_id = user_id;
        this.content = content;
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

}
