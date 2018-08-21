package com.example.adityacomputers.todotask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adityacomputers.todotask.modal.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by ADITYA COMPUTERS on 8/17/2018.
 */

public class TaskAdapter extends BaseAdapter{
    Context context;
    List<Task> taskList;
    boolean flag;
    LayoutInflater layoutInflater;
    public TaskAdapter(Context context,List<Task> taskList,boolean flag)
    {
        this.context=context;
        this.taskList=taskList;
        this.flag=flag;
    }


    @Override
    public int getCount() {
        return taskList.size();
    }


    @Override
    public Task getItem(int i) {
        return (Task)taskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row=view;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          if(row==null) {
              row = layoutInflater.inflate(R.layout.task_list_items, null);
          }
        TextView txttitle,txtdesc,txtdate;
            txttitle = (TextView) row.findViewById(R.id.txttitle);
            txtdesc = (TextView) row.findViewById(R.id.txtdesc);
            txtdate = (TextView) row.findViewById(R.id.txtdate);
            Task task = getItem(i);
            txttitle.setText(task.getKey_title());
            txtdesc.setText(task.getKey_desc());
            String date = task.getKey_date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = simpleDateFormat.parse(date);
                SimpleDateFormat sd2 = new SimpleDateFormat("dd/MM/yyyy");
                String fdate = sd2.format(date1);
                txtdate.setText(fdate);
            } catch (Exception e) {
            }
            ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
            if (flag)
                imageView.setImageResource(R.mipmap.completed_task);
            else
                imageView.setImageResource(R.mipmap.in_complete_task);

            return row;
    }
}
