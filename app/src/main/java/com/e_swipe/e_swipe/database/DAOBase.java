package com.e_swipe.e_swipe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "database.db";

    protected SQLiteDatabase mDb = null;
    protected Database mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new Database(pContext, NOM, null, VERSION);
    }
    public SQLiteDatabase open() {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }
    public void close() {
        mDb.close();
    }
    public SQLiteDatabase getDb() {
        return mDb;
    }
}