package com.project.anupamchugh.conferences.models;

/**
 * Created by anupamchugh on 30/12/16.
 */

public class NewConferences {


    public String date, description, createdBy;

    public NewConferences(String createdBy, String date, String description)
    {
        this.createdBy=createdBy;
        this.date=date;
        this.description=description;
    }
}
