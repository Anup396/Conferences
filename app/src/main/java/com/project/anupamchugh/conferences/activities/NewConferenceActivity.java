package com.project.anupamchugh.conferences.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.anupamchugh.conferences.DatabaseHelper;
import com.project.anupamchugh.conferences.GlobalConstants;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.models.NewConferences;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by anupamchugh on 31/12/16.
 */

public class NewConferenceActivity extends AppCompatActivity implements OnDateSelectedListener, View.OnClickListener {

    MaterialCalendarView widget;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView calendarDate;
    Button createConference;
    EditText inDescription;

    DatabaseHelper db;

    String selected_date = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conference);
        initToolbar();
        initViews();

    }


    private void initViews() {
        inDescription = (EditText) findViewById(R.id.inDescription);
        createConference = (Button) findViewById(R.id.createConf);
        createConference.setOnClickListener(this);
        calendarDate = (TextView) findViewById(R.id.calendarDate);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        createConference = (Button) findViewById(R.id.createConf);
        inDescription = (EditText) findViewById(R.id.inDescription);

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("New Conference");

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
            case R.id.createConf:

                SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
                String username = s.getString(GlobalConstants.PREF_KEY_USERNAME, "");


                if (selected_date.length() > 0) {

                    if (inDescription.getText().toString().length() > 0) {

                        db = new DatabaseHelper(NewConferenceActivity.this);

                        long conf_id = db.creatNewConf(new NewConferences(username, selected_date, inDescription.getText().toString()));

                        startActivity(new Intent(NewConferenceActivity.this,InviteActivity.class).putExtra("conf_id",conf_id));
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), "Please enter a description", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}
