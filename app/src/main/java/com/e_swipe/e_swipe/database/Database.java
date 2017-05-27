package com.e_swipe.e_swipe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper{

    public static final String ID = "id";
    public static final String TEXT_EVENT = "event_card";
    public static final String LIST_EVENT_TABLE = "List_Events";
    public static final String EVENT_TABLE = "Events";
    public static final String LIST_CHAT_TABLE = "List_Chats";
    public static final String TEXT_CHAT = "chat_card";
    public static final String CHAT_TABLE = "Chats";
    public static final String ID_CHAT = "idChat";
    public static final String ID_MESSAGE = "idMessage";
    public static final String TEXT_MESSAGE = "Message";

    public static final String EVENT_TABLE_CREATE =
            "CREATE TABLE " + EVENT_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                TEXT_EVENT + "TEXT);";
    public static final String LIST_EVENT_TABLE_CREATE =
            "CREATE TABLE " + LIST_EVENT_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    TEXT_EVENT + "TEXT," +
                    "FOREIGN KEY("+ ID +") REFERENCES "+ EVENT_TABLE + " (" + ID  + "));";
    public static final String CHAT_TABLE_CREATE =
            "CREATE TABLE " + CHAT_TABLE + " (" +
                    ID_CHAT + " INTEGER PRIMARY KEY, " +
                    ID_MESSAGE + " INTEGER PRIMARY KEY, " +
                    TEXT_MESSAGE + "TEXT);";
    public static final String LIST_CHAT_TABLE_CREATE =
            "CREATE TABLE " + LIST_CHAT_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    TEXT_CHAT + "TEXT," +
                    "FOREIGN KEY("+ ID +") REFERENCES "+ CHAT_TABLE + " (" + ID_CHAT  + "));";
    public static final String TABLE_CREATE =
            EVENT_TABLE_CREATE + "\n" +
                    LIST_EVENT_TABLE_CREATE + "\n" +
                    CHAT_TABLE_CREATE + "\n" +
                    LIST_CHAT_TABLE_CREATE;

    public static final String DROP_TABLE = "" +
            "DROP TABLE IF EXISTS " + LIST_EVENT_TABLE + ";" +
            "DROP TABLE IF EXISTS " + LIST_CHAT_TABLE + ";" +
            "DROP TABLE IF EXISTS " + EVENT_TABLE + ";" +
            "DROP TABLE IF EXISTS " + CHAT_TABLE + ";";


    /*public static final String METIER_KEY = "id";
    public static final String METIER_INTITULE = "intitule";
    public static final String METIER_SALAIRE = "salaire";
    public static final String METIER_TABLE_NAME = "Metier";
    public static final String METIER_TABLE_CREATE =
            "CREATE TABLE " + METIER_TABLE_NAME + " (" +
                    METIER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    METIER_INTITULE + " TEXT, " +
                    METIER_SALAIRE + " REAL);";*/

    public Database(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
