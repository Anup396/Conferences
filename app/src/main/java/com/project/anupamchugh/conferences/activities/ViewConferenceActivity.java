package com.project.anupamchugh.conferences.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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
import com.project.anupamchugh.conferences.NonScrollListView;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.adapter.EditInviteListAdapter;
import com.project.anupamchugh.conferences.adapter.InviteListAdapter;
import com.project.anupamchugh.conferences.models.AllConferences;
import com.project.anupamchugh.conferences.models.Users;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by anupamchugh on 31/12/16.
 */

public class ViewConferenceActivity extends AppCompatActivity implements OnDateSelectedListener, View.OnClickListener {

    MaterialCalendarView widget;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView calendarDate;
    Button btnAccept, btnReject;
    TextView txtDescription, txtStatus;
    NonScrollListView nonScrollListView;
    DatabaseHelper db;
    String selected_date = "";
    long conference_id, user_id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_conference);
        initToolbar();
        initViews();

    }


    private void initViews() {
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnReject = (Button) findViewById(R.id.btnReject);
        nonScrollListView=(NonScrollListView)findViewById(R.id.nonScrollListView);
        btnReject.setOnClickListener(this);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(this);

        calendarDate = (TextView) findViewById(R.id.calendarDate);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        AllConferences allConferences = (AllConferences) getIntent().getSerializableExtra("conference");
        Users user = (Users) getIntent().getSerializableExtra("user");
        conference_id= allConferences.conf_id;
        user_id= user.user_id;

        if(user.accepted==1)
        {
            txtStatus.setText("ACCEPTED");
        }
        else if(user.rejected==1)
        {
            txtStatus.setText("REJECTED");
        }


        db= new DatabaseHelper(this);
        List<Users> invitedDoctors = db.getAllInvitedDoctors(conference_id);

        InviteListAdapter editInviteListAdapter = new InviteListAdapter(invitedDoctors,false);
        nonScrollListView.setAdapter(editInviteListAdapter);
        widget.setSelectedDate(convertStringtoDate(allConferences.date));
        txtDescription.setText(allConferences.description);
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
        actionBar.setTitle("View Conference");

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
            case R.id.btnAccept:

                db= new DatabaseHelper(this);
                db.statusAccepted(user_id,conference_id);
                txtStatus.setText("ACCEPTED");

                break;

            case R.id.btnReject:

                db= new DatabaseHelper(this);
                db.statusRejected(user_id,conference_id);
                txtStatus.setText("REJECTED");


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
