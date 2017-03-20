package com.e_swipe.e_swipe.objects;

import android.support.annotation.NonNull;

import com.e_swipe.e_swipe.objects.Chat;

import java.util.ArrayList;

/**
 * Created by Anthony on 20/03/2017.
 */
public class Person {

    private String displayName;
    private String token;
    private String imageUrl;
    private ArrayList<Chat> chats;

    public Person(String displayName, @NonNull String token, String imageUrl){
        this.displayName = displayName;
        this.token = token;
        this.imageUrl = imageUrl;
        chats = new ArrayList<Chat>();
    }

    public void addChat(Chat chat){
        chats.add(chat);
    }
}
