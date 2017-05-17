package com.e_swipe.e_swipe.objects;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Anthonny on 20/03/2017.
 */
public class ChatRoom implements Serializable {

    Person current;
    Person receiver;
    String lastMessage;
    String chatName;

    public ChatRoom(String chatName, Person current, Person receiver) {
        this.current = current;
        this.receiver = receiver;
        this.chatName = chatName;
        lastMessage = "";
    }

    public Person getCurrent() {
        return current;
    }

    public void setCurrent(Person current) {
        this.current = current;
    }

    public Person getReceiver() {
        return receiver;
    }

    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}

