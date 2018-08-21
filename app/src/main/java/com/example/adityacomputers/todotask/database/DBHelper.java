package com.example.adityacomputers.todotask.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.CollationKey;
import android.widget.Toast;

import com.example.adityacomputers.todotask.modal.Task;
import com.example.adityacomputers.todotask.utils.Constants;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ADITYA COMPUTERS on 8/14/2018.
 */

public class DBHelper {
    private SQLiteDatabase db;
    private final Context context;
    private final TableClass tableClass;
    private static DBHelper dbHelper;
    private DBHelper(Context context)
    {
        this.context=context;
        tableClass=new TableClass(context, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
    }
    public static DBHelper getInstance(Context context)
    {
        if(dbHelper==null)
        {
            dbHelper=new DBHelper(context);
        }
        return dbHelper;
    }
    public void close()
    {
        if(db.isOpen())
            db.close();
    }
    public long add_new_Task_Data(String tablename, ContentValues cv) {
        long id = 0;
            db=tableClass.getWritableDatabase();
            id = db.insert(Constants.TABLE_NAME, null, cv);
        return id;
    }
    public long update_Task(ContentValues cv,int keyid)
    {
        long l=-1;
        try {
            db = tableClass.getWritableDatabase();
            l=db.update(Constants.TABLE_NAME,cv,Constants.ID+"="+keyid,null);
        }
        catch(Exception e){e.printStackTrace();}
        return l;
    }
    public boolean update_Task_Status(int keyid)
    {
        boolean flag=false;
        db=tableClass.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.ID+"="+keyid,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            int status=c.getInt(c.getColumnIndex(Constants.STATUS));

            if(status==0)
                status=1;
            else
                status=0;
            db=tableClass.getWritableDatabase();
            db.execSQL("UPDATE "+Constants.TABLE_NAME+" SET "+Constants.STATUS+"="+status+" WHERE "+Constants.ID+"="+keyid);
            flag=true;

            c.close();
        }
        return flag;
    }
    public Cursor get_All_Tasks()
    {
        List<Task> taskList=new LinkedList<>();

        Cursor cursor=null;
            db=tableClass.getWritableDatabase();
            cursor=db.rawQuery("SELECT * FROM "+Constants.TABLE_NAME+" ORDER BY DATE("+Constants.DATE+")",null);
        return cursor;
    }
    public List<Task> get_All_Completed_Tasks()
    {
        List<Task> taskList=new LinkedList<>();
        Cursor cursor=null;
        try {
            db=tableClass.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.STATUS+"=1",null);
            Task task = null;
            if (cursor.moveToFirst()) {
                do {
                    task = new Task();
                    task.setKey_id(cursor.getInt(cursor.getColumnIndex(Constants.ID)));
                    task.setKey_title(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
                    task.setKey_desc(cursor.getString(cursor.getColumnIndex(Constants.DESC)));
                    task.setKey_date(cursor.getString(cursor.getColumnIndex(Constants.DATE)));
                    task.setKey_status(cursor.getInt(cursor.getColumnIndex(Constants.STATUS)));
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (Exception e){}
        return taskList;
    }
    public boolean delete_Task(int keyid)
    {
        boolean flag=false;
        db=tableClass.getWritableDatabase();
        int r=db.delete(Constants.TABLE_NAME,Constants.ID+"="+keyid,null);
        if(r>0)
            flag=true;
        return flag;
    }

}
