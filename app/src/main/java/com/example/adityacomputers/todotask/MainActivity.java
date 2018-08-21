package com.example.adityacomputers.todotask;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adityacomputers.todotask.database.DBHelper;
import com.example.adityacomputers.todotask.modal.Task;
import com.example.adityacomputers.todotask.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create the isntance of dbhelper class
        dbHelper=DBHelper.getInstance(MainActivity.this);
        //show all tasks created
        show_All_Tasks();

        ListView listView=(ListView)findViewById(R.id.tasklistview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //take existing data from ui
                TextView txttitle=(TextView)view.findViewById(R.id.txttitle);
                TextView txtdesc =(TextView)view.findViewById(R.id.txtdesc);
                TextView txtdate=(TextView)view.findViewById(R.id.txtdate);
                String title=txttitle.getText().toString();
                String desc=txtdesc.getText().toString();
                String tdate=txtdate.getText().toString();
                //get the keyid
                Task task=(Task)adapterView.getItemAtPosition(i);
                //call update the task method
                updatedTask(task.getKey_id(),title,desc,tdate);
            }
        });
        //onlong click of item update the task status
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task=(Task)adapterView.getItemAtPosition(i);
                boolean flag=dbHelper.update_Task_Status(task.getKey_id());
                if(flag==true) {
                    Toast.makeText(getApplicationContext(), "Task status updated successfully", Toast.LENGTH_SHORT).show();
                    show_All_Tasks();
                }
                else
                    Toast.makeText(getApplicationContext(),"Unable to update Task status",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        show_All_Tasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.task_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //on click of plus menu item call add new task method
        int menuId=item.getItemId();
        if(menuId==R.id.addtask)
         add_new_Task();
        //on click of second menu item start second activity and show all completed tasks
        else if(menuId==R.id.Completed_Task)
         completedTask();
        return true;
    }
    //method to add new task
    public void add_new_Task()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add new task");
        builder.setCancelable(true);
        final View view=getLayoutInflater().inflate(R.layout.task_form,null);
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText ettitle=(EditText)view.findViewById(R.id.ettitle);
                EditText etdesc=(EditText)view.findViewById(R.id.etdesc);
                DatePicker datePicker=(DatePicker)view.findViewById(R.id.datePicker);
                String title=ettitle.getText().toString();
                String desc=etdesc.getText().toString();
                int day=datePicker.getDayOfMonth();
                int month=datePicker.getMonth()+1;
                int year=datePicker.getYear();
                StringBuilder sb=new StringBuilder();
                sb.append(year);
                sb.append("-");
                sb.append(month);
                sb.append("-");
                sb.append(day);
                String date=sb.toString();
                if(title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter the title of task", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(desc.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter the description about the task", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues cv=new ContentValues();
                cv.put(Constants.TITLE,title);
                cv.put(Constants.DESC,desc);
                cv.put(Constants.DATE,date);
                cv.put(Constants.STATUS,0);
                long id=dbHelper.add_new_Task_Data(Constants.TABLE_NAME,cv);
                if(id>-1) {
                    Toast.makeText(getApplicationContext(), "New Task added Successfully", Toast.LENGTH_SHORT).show();
                   show_All_Tasks();
                }
                else
                    Toast.makeText(getApplicationContext(),"Unable to create New Task sorry ",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void completedTask()
    {
        Intent intent=new Intent(MainActivity.this,DisplayCompletedTaskActivity.class);
        startActivity(intent);

    }
    public  void show_All_Tasks()
    {
        List<Task> taskList=new LinkedList<>();

        Cursor cursor=null;
        try {
            cursor = dbHelper.get_All_Tasks();
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
         catch (Exception e){e.printStackTrace();}
        TaskAdapter taskadapter=new TaskAdapter(MainActivity.this,taskList,false);
        ListView listView=(ListView)findViewById(R.id.tasklistview);
        listView.setAdapter(taskadapter);

    }
    public void updatedTask(final int keyid, final String pretitle, final String predesc, final String predate)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Update task");
        builder.setCancelable(true);
        final View view=getLayoutInflater().inflate(R.layout.task_form,null);
        final EditText ettitle=(EditText)view.findViewById(R.id.ettitle);
        final EditText etdesc=(EditText)view.findViewById(R.id.etdesc);
        final DatePicker datePicker=(DatePicker)view.findViewById(R.id.datePicker);
        ettitle.setText(pretitle);
        etdesc.setText(predesc);
        Date pdate;
        try {
            pdate = new SimpleDateFormat("dd/MM/yyyy").parse(predate);
            Calendar calendar=new GregorianCalendar();
            calendar.setTime(pdate);
            datePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
        }
        catch(ParseException pe){}
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title=ettitle.getText().toString();
                String desc=etdesc.getText().toString();
                int day=datePicker.getDayOfMonth();
                int month=datePicker.getMonth()+1;
                int year=datePicker.getYear();
                StringBuilder sb=new StringBuilder();
                sb.append(year);
                sb.append("-");
                sb.append(month);
                sb.append("-");
                sb.append(day);
                String date=sb.toString();
                if(title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter the title of task", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(desc.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter the description about the task", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues cv=new ContentValues();
                cv.put(Constants.TITLE,title);
                cv.put(Constants.DESC,desc);
                cv.put(Constants.DATE,date);
//                cv.put(Constants.STATUS,0);
                long id=dbHelper.update_Task(cv,keyid);
                if(id>-1) {

                    Toast.makeText(getApplicationContext(),"Task updated Successfully", Toast.LENGTH_SHORT).show();
                    show_All_Tasks();
                }
                else
                    Toast.makeText(getApplicationContext(),"Unable to create New Task sorry ",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
