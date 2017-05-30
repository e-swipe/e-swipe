package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 29/05/2017.
 */

public class MessageCard {
    String content;
    String date;

    public MessageCard(String content, String date) {
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
