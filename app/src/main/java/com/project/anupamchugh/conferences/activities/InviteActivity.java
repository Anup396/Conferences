package com.project.anupamchugh.conferences.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.project.anupamchugh.conferences.DatabaseHelper;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.adapter.InviteListAdapter;
import com.project.anupamchugh.conferences.models.Users;

import java.util.List;

/**
 * Created by anupamchugh on 31/12/16.
 */

public class InviteActivity extends AppCompatActivity implements View.OnClickListener {


    DatabaseHelper db;
    ListView listView;
    Button btnFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_doctors);
        initToolbar();
        initViews();

    }

    private void saveInvite(Users user)
    {

        db= new DatabaseHelper(this);
        db.inviteDoctor(user, getIntent().getLongExtra("conf_id", -1));


    }

    private void deleteInvite(Users user)
    {

        db= new DatabaseHelper(this);
        db.deleteInviteToDoctor(user.user_id,getIntent().getLongExtra("conf_id",-1));

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Invite Doctors");

    }

    private void initViews() {
        btnFinish=(Button)findViewById(R.id.btnFinish);
        listView = (ListView) findViewById(R.id.listView);
        db = new DatabaseHelper(InviteActivity.this);
        List<Users> doctors = db.getAllDoctors(0);
        InviteListAdapter inviteListAdapter = new InviteListAdapter(doctors, true);
        listView.setAdapter(inviteListAdapter);
        btnFinish.setOnClickListener(this);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RelativeLayout rl = (RelativeLayout) view;
                CheckBox checkBox = (CheckBox) rl.getChildAt(1);

                Users user = ((Users) listView.getAdapter().getItem(i));

                if(checkBox.isChecked())
                {
                    listView.setItemChecked(i, false);
                    checkBox.setChecked(false);
                    user.selected=false;
                    deleteInvite(user);
                }
                else{
                    listView.setItemChecked(i, true);
                    checkBox.setChecked(true);
                    user.selected=true;
                    saveInvite(user);
                }


            }
        };

        listView.setOnItemClickListener(itemClickListener);
        listView.setEmptyView(findViewById(R.id.displayText));

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnFinish:
                startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
                break;

        }

    }
}
