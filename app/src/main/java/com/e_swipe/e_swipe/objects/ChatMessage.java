package com.e_swipe.e_swipe.objects;

/**
 * Created by Anthonny on 16/05/2017.
 */
public class ChatMessage {
    private String senderName;
    private String message;
    private boolean isMine;

    public ChatMessage(String senderName, String message, boolean isMine){
        this.senderName = senderName;
        this.message = message;
        this.isMine = isMine;
    }

    public String getMessage(){
        return message;
    }
    public boolean isMine(){
        return isMine;
    }
}
