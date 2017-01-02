package com.project.anupamchugh.conferences.models;

import java.io.Serializable;

/**
 * Created by anupamchugh on 30/12/16.
 */

public class Users implements Serializable {



    public String username;
    public String password;
    public int isAdmin;
    public long user_id;
    public boolean selected;
    public int accepted, rejected;


    public Users()
    {
        selected=false;
    }

    public Users(String username, String password, int isAdmin)
    {
        this.isAdmin=isAdmin;
        this.username=username;
        this.password=password;
    }

}
