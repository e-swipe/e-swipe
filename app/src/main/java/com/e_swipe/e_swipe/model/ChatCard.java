package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class ChatCard {

    String uuid;
    UserCard user;
    String last_message_content;

    public ChatCard(String uuid, UserCard user, String last_message_content) {
        this.uuid = uuid;
        this.user = user;
        this.last_message_content = last_message_content;
    }

    public String getUuid() {
        return uuid;
    }

    public UserCard getUser() {
        return user;
    }

    public String getLast_message_content() {
        return last_message_content;
    }
}
