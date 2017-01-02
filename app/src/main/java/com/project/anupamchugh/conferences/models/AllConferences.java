package com.project.anupamchugh.conferences.models;

import java.io.Serializable;

/**
 * Created by anupamchugh on 30/12/16.
 */

public class AllConferences implements Serializable {


    public String date, description, createdBy, createdAt, modifiedAt;
    public long conf_id;

    public AllConferences()
    {

    }
}
