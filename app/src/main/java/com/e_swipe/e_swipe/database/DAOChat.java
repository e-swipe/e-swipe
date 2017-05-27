package com.e_swipe.e_swipe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.e_swipe.e_swipe.model.Chat;
import com.e_swipe.e_swipe.model.ChatCard;
import com.e_swipe.e_swipe.model.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DAOChat extends DAOBase {

    public static final String ID = "id";
    public static final String LIST_CHAT_TABLE = "List_Chats";
    public static final String TEXT_CHAT = "chat_card";
    public static final String CHAT_TABLE = "Chats";
    public static final String ID_CHAT = "idChat";
    public static final String ID_MESSAGE = "idMessage";
    public static final String TEXT_MESSAGE = "Message";

    public DAOChat(Context pContext) {
        super(pContext);
    }

    //CHAT
    /**
     * Add or update chat with uuid
     * @param uuid
     * @param jsonChat
     */
    public void save(String uuid, String jsonChat){
        ContentValues values = new ContentValues();
        if (get(uuid) != null) {
            values.put(TEXT_CHAT, jsonChat);
            mDb.update(LIST_CHAT_TABLE,values,ID + " = ?",new String[]{uuid});
        } else {
            values.put(ID, uuid);
            values.put(TEXT_CHAT, jsonChat);
            mDb.insert(LIST_CHAT_TABLE,null,values);
        }

    }

    /**
     * @param uuid
     * @return
     */
    public String get(String uuid){
        Cursor c = mDb.rawQuery("SELECT " + TEXT_CHAT + " FROM " + LIST_CHAT_TABLE + " WHERE " + ID + " =  ?" , new String[]{uuid});
        c.moveToFirst();
        return c.getString(0);
     // select chat_card from list_chats where uuid = uuid

    }

    public void delete(String uuid){
        // delete from listchats where uuid = uuid
        mDb.delete(LIST_CHAT_TABLE,ID + " = ?",new String[]{uuid});
    }

    //MESSAGE

    public ArrayList<String> getMessage(String uuid){
        ArrayList<String> listMessage = new ArrayList<>();
        int index = 0;
        Cursor c = mDb.rawQuery("SELECT " + TEXT_MESSAGE + " FROM " + CHAT_TABLE + " WHERE " + ID_CHAT + " = ?", new String[]{uuid});
        if(c.moveToFirst()){
            do {
                listMessage.add(c.getString(index));
                index++;
            } while (c.moveToNext());
        }
        return listMessage;
    }

}
