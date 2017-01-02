package com.project.anupamchugh.conferences;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;

import com.project.anupamchugh.conferences.models.AllConferences;
import com.project.anupamchugh.conferences.models.Topics;
import com.project.anupamchugh.conferences.models.Users;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by javierg on 07/09/16.
 */

public class SQLiteTest extends InstrumentationTestCase {

    Context context;
    DatabaseHelper db;

    public void setUp() throws Exception {
        super.setUp();

        context = new MockContext();

        assertNotNull(context);

    }


    public void testDB()
    {
        db = new DatabaseHelper(context);
        assertNotNull(db);
    }

    public void testOpeningDb() throws Exception {
        SQLiteDatabase sqliteDb = db.getWritableDatabase();
        assertNotNull(sqliteDb);
    }

    /*public void tearDown() throws Exception{
        db.close();
        super.tearDown();
    }*/

    public void testUsers() {
        List<Users> doctors = db.getAllDoctors(0);
        assertThat(doctors.size(), is(0));
    }

    public void testTopics() {

        List<Topics> doctors = db.getAllTopics();
        assertThat(doctors.size(), is(0));
    }


    public void testConferences() {
        List<AllConferences> allConferencesList= db.getAllConferences();
        assertThat(allConferencesList.size(), is(0));
    }

    /*public void tearDown() throws Exception{
        db.close();
        super.tearDown();
    }*/

}
