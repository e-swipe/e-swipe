package com.e_swipe.e_swipe.objects;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Anthony on 20/03/2017.
 */
public class Person {

    private String displayName;
    private String token;
    private String imageUrl;
    private ArrayList<ChatRoom> chatRooms;

    public Person(String displayName, @NonNull String token, String imageUrl){
        this.displayName = displayName;
        this.token = token;
        this.imageUrl = imageUrl;
        chatRooms = new ArrayList<ChatRoom>();
    }

    public void addChat(ChatRoom chatRoom){
        chatRooms.add(chatRoom);
    }
}
