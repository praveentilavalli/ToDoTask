package com.example.adityacomputers.todotask.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import com.example.adityacomputers.todotask.utils.Constants;

/**
 * Created by ADITYA COMPUTERS on 8/14/2018.
 */

public class TableClass extends SQLiteOpenHelper {
    Context context;
    String query="CREATE TABLE IF NOT EXISTS "+ Constants.TABLE_NAME+"("+
            Constants.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Constants.TITLE+" TEXT, "+
            Constants.DESC+" TEXT, "+
            Constants.DATE+" TEXT, "+
            Constants.STATUS+" INTEGER);";

    public TableClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        context.deleteDatabase(Constants.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
