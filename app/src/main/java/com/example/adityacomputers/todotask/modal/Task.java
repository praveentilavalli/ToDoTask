package com.example.adityacomputers.todotask.modal;

/**
 * Created by ADITYA COMPUTERS on 8/14/2018.
 */

public class Task {
    private int key_id;
    private String key_title;

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getKey_title() {
        return key_title;
    }

    public void setKey_title(String key_title) {
        this.key_title = key_title;
    }

    public String getKey_desc() {
        return key_desc;
    }

    public void setKey_desc(String key_desc) {
        this.key_desc = key_desc;
    }

    public String getKey_date() {
        return key_date;
    }

    public void setKey_date(String key_date) {
        this.key_date = key_date;
    }

    public int getKey_status() {
        return key_status;
    }

    public void setKey_status(int key_status) {
        this.key_status = key_status;
    }

    private String key_desc;
    private String key_date;
    private int key_status;

    public Task(int key_id, String key_title, String key_desc, String key_date, int key_status) {
        this.key_id = key_id;
        this.key_title = key_title;
        this.key_desc = key_desc;
        this.key_date = key_date;
        this.key_status = key_status;
    }
    public Task()
    {

    }
}
