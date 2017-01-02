package com.project.anupamchugh.conferences.activities;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.project.anupamchugh.conferences.DatabaseHelper;
import com.project.anupamchugh.conferences.GlobalConstants;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.models.Topics;

public class AddTopicDialogActivity extends Activity implements OnClickListener {

    private Button btnAddTopic;
    private EditText inTopic;

    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Topic");

        setContentView(R.layout.activity_add_record);

        inTopic = (EditText) findViewById(R.id.inTopic);

        btnAddTopic = (Button) findViewById(R.id.btnAddTopic);

        btnAddTopic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddTopic:

                SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
                String username= s.getString(GlobalConstants.PREF_KEY_USERNAME,"");

                Topics topics= new Topics();
                topics.description=inTopic.getText().toString();
                topics.createdBy= username;
                db= new DatabaseHelper(this);
                db.createNewTopic(topics);

                Intent main = new Intent(AddTopicDialogActivity.this, SuggestionsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}