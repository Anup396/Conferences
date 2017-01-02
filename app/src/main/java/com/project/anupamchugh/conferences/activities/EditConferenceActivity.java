package com.project.anupamchugh.conferences.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.anupamchugh.conferences.DatabaseHelper;
import com.project.anupamchugh.conferences.GlobalConstants;
import com.project.anupamchugh.conferences.NonScrollListView;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.adapter.EditInviteListAdapter;
import com.project.anupamchugh.conferences.adapter.InviteListAdapter;
import com.project.anupamchugh.conferences.models.AllConferences;
import com.project.anupamchugh.conferences.models.NewConferences;
import com.project.anupamchugh.conferences.models.Users;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by anupamchugh on 31/12/16.
 */

public class EditConferenceActivity extends AppCompatActivity implements OnDateSelectedListener, View.OnClickListener {

    MaterialCalendarView widget;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView calendarDate;
    Button btnUpdateChanges, btnDelete;
    EditText inDescription;
    NonScrollListView nonScrollListView;
    DatabaseHelper db;
    String selected_date = "";
    long conference_id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_conference);
        initToolbar();
        initViews();

    }


    private void initViews() {
        inDescription = (EditText) findViewById(R.id.inDescription);
        btnUpdateChanges = (Button) findViewById(R.id.updateChanges);
        nonScrollListView=(NonScrollListView)findViewById(R.id.nonScrollListView);
        btnUpdateChanges.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        calendarDate = (TextView) findViewById(R.id.calendarDate);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        AllConferences allConferences = (AllConferences) getIntent().getSerializableExtra("conference");
        conference_id= allConferences.conf_id;


        db= new DatabaseHelper(this);

        List<Users> doctors=db.getAllDoctors(0);
        List<Users> invitedDoctors = db.getAllInvitedDoctors(conference_id);


        EditInviteListAdapter editInviteListAdapter = new EditInviteListAdapter(doctors, invitedDoctors);
        nonScrollListView.setAdapter(editInviteListAdapter);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RelativeLayout rl = (RelativeLayout) view;
                CheckBox checkBox = (CheckBox) rl.getChildAt(1);

                Users user = ((Users) nonScrollListView.getAdapter().getItem(i));

                if(checkBox.isChecked())
                {
                    nonScrollListView.setItemChecked(i, false);
                    checkBox.setChecked(false);
                    user.selected=false;
                    deleteInvite(user);
                }
                else{
                    nonScrollListView.setItemChecked(i, true);
                    checkBox.setChecked(true);
                    user.selected=true;
                    saveInvite(user);
                }


            }
        };

        nonScrollListView.setOnItemClickListener(itemClickListener);



        widget.setSelectedDate(convertStringtoDate(allConferences.date));
        inDescription.setText(allConferences.description);


        widget.setOnDateChangedListener(this);
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.DECEMBER, 1);
        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR) + 2, Calendar.OCTOBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        calendarDate.setText(getSelectedDatesString());

    }

    private void saveInvite(Users user)
    {
        db= new DatabaseHelper(this);
        db.inviteDoctor(user,conference_id);
    }

    private void deleteInvite(Users user)
    {
        db= new DatabaseHelper(this);
        db.deleteInviteToDoctor(user.user_id,conference_id);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Edit Conference");

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
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        calendarDate.setText(getSelectedDatesString());
    }


    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }

        selected_date = FORMATTER.format(date.getDate());

        return selected_date;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.updateChanges:

                if (selected_date.length() > 0) {

                    if (inDescription.getText().toString().length() > 0) {

                        db = new DatabaseHelper(EditConferenceActivity.this);
                        db.updateConference(inDescription.getText().toString(),selected_date,conference_id);
                        Intent intent= getIntent();
                        setResult(RESULT_OK,intent);
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), "Please enter a description", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnDelete:

                new AlertDialog.Builder(this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db = new DatabaseHelper(EditConferenceActivity.this);
                                db.deleteConference(conference_id);
                                db.deleteConferenceLinks(conference_id);
                                Intent intent= getIntent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();




                break;
        }

    }

    public Date convertStringtoDate(String dateString) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date d = dateFormat.parse(dateString);


            return d;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
