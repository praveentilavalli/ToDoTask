package com.example.adityacomputers.todotask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adityacomputers.todotask.database.DBHelper;
import com.example.adityacomputers.todotask.modal.Task;

import java.util.List;

public class DisplayCompletedTaskActivity extends AppCompatActivity {

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_completed_task);
        //show completed tasks only
        showCompletedTasks();
        ListView clistview=(ListView)findViewById(R.id.clistview);
        // on long click delete the task
        clistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task=(Task)adapterView.getItemAtPosition(i);
                boolean flag=dbHelper.delete_Task(task.getKey_id());
                if(flag) {
                    Toast.makeText(getApplicationContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();
                    showCompletedTasks();
                }
                else
                    Toast.makeText(getApplicationContext(),"Unable to delete the task",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //methdo to show completed tasks
    public void showCompletedTasks()
    {
        dbHelper=DBHelper.getInstance(this);
        List<Task> completedtasklist=dbHelper.get_All_Completed_Tasks();
        ListView clistview=(ListView)findViewById(R.id.clistview);
        TaskAdapter taskAdapter=new TaskAdapter(this,completedtasklist,true);
        clistview.setAdapter(taskAdapter);
    }

}
