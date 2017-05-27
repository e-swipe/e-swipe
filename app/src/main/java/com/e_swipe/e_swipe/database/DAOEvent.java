package com.e_swipe.e_swipe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.e_swipe.e_swipe.model.Event;
import com.e_swipe.e_swipe.model.EventCard;

public class DAOEvent extends DAOBase {

    public static final String ID = "id";
    public static final String TEXT_EVENT = "event_card";
    public static final String LIST_EVENT_TABLE = "List_Events";
    public static final String EVENT_TABLE = "Events";

    public DAOEvent(Context pContext) {
        super(pContext);
    }

    /**
     * Add or update chat with uuid
     * @param uuid
     * @param jsonChat
     */
    public void save(String uuid, String jsonChat){
        ContentValues values = new ContentValues();
        if (get(uuid) != null) {
            values.put(TEXT_EVENT, jsonChat);
            mDb.update(LIST_EVENT_TABLE,values,ID + " = ?",new String[]{uuid});
        } else {
            values.put(ID, uuid);
            values.put(TEXT_EVENT, jsonChat);
            mDb.insert(LIST_EVENT_TABLE,null,values);
        }
    }

    /**
     * @param uuid
     * @return
     */
    public String get(String uuid){
        Cursor c = mDb.rawQuery("SELECT " + TEXT_EVENT + " FROM " + LIST_EVENT_TABLE + " WHERE " + ID + " =  ?" , new String[]{uuid});
        c.moveToFirst();
        return c.getString(0);

    }

    public void delete(String uuid){
        mDb.delete(LIST_EVENT_TABLE,ID + " = ?",new String[]{uuid});
    }
}
