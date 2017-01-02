package com.project.anupamchugh.conferences.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.RecyclerViewCacheUtil;
import com.project.anupamchugh.conferences.DatabaseHelper;
import com.project.anupamchugh.conferences.GlobalConstants;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.adapter.AllAdminConferences;
import com.project.anupamchugh.conferences.adapter.TopicListAdapter;
import com.project.anupamchugh.conferences.models.AllConferences;
import com.project.anupamchugh.conferences.models.Topics;
import com.project.anupamchugh.conferences.models.Users;

import java.util.List;

public class SuggestionsActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViews();
    }

    private void initViews()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.displayText));

        SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
        String account_type= s.getString(GlobalConstants.PREF_KEY_ACCOUNT_TYPE,"");


        if(account_type.equals("DOCTOR"))
        {
            fab.setVisibility(View.VISIBLE);
        }

        else{
            fab.setVisibility(View.GONE);
        }

        db = new DatabaseHelper(this);
        List<Topics> topicsList =db.getAllTopics();
        final TopicListAdapter topicListAdapter= new TopicListAdapter(topicsList,getApplicationContext());
        listView.setAdapter(topicListAdapter);


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Suggestions");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.fab:
                Intent add_mem = new Intent(this, AddTopicDialogActivity.class);
                startActivity(add_mem);
                break;

        }

    }
}
