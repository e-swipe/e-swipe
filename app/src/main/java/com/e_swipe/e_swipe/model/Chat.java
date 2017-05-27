package com.e_swipe.e_swipe.model;

import java.util.List;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class Chat {

    String uuid;
    UserCard user;
    String last_message_content;
    List<Message> messages;

    public Chat(String uuid, UserCard user, String last_message_content, List<Message> messages) {
        this.uuid = uuid;
        this.user = user;
        this.last_message_content = last_message_content;
        this.messages = messages;
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

    public List<Message> getMessages() {
        return messages;
    }
}
