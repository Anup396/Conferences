package com.project.anupamchugh.conferences.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.anupamchugh.conferences.DatabaseHelper;
import com.project.anupamchugh.conferences.GlobalConstants;
import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.models.Users;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSignUp, btnSignIn, btnCreateAccount;
    LinearLayout llSwitch, llSignIn;
    EditText inUsername, inPassword, inConfirmPassword;
    SwitchCompat isAdminSwitch;
    boolean isEnabled=false;
    boolean signUpEnabled = true;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
        int nLoginType = pref.getInt(GlobalConstants.PREF_KEY_LOGIN_TYPE, GlobalConstants.LOGIN_NONE);
        if (nLoginType == GlobalConstants.LOGIN_ALREADY) {
            goToMain();
            return;
        }

        setContentView(R.layout.activity_login);
        initViews();

    }

    private void initViews() {
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        llSignIn = (LinearLayout) findViewById(R.id.linearLayoutSignIn);
        llSwitch = (LinearLayout) findViewById(R.id.linearLayoutSwitch);
        inUsername = (EditText) findViewById(R.id.inUsername);
        inPassword = (EditText) findViewById(R.id.inPassword);
        inConfirmPassword = (EditText) findViewById(R.id.inConfirmPassword);
        isAdminSwitch = (SwitchCompat) findViewById(R.id.isAdminSwitch);


        isAdminSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked)
                {
                    isEnabled=true;
                }
                else
                    isEnabled=false;

            }
        });

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSignUp:

                if (signUpEnabled) {
                    String username = inUsername.getText().toString().trim();
                    String password = inPassword.getText().toString().trim();
                    String confirm_password = inConfirmPassword.getText().toString().trim();

                    if (username.length() > 1 && password.length() > 1) {
                        if (password.equals(confirm_password)) {

                            db = new DatabaseHelper(LoginActivity.this);
                            if (!db.checkIfUserNameExists(username)) {

                                int isAdmin = isEnabled ? 1 : 0;

                                Users newUser = new Users(username, password, isAdmin);

                                long user_id = db.creatNewUser(newUser);





                                    SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = s.edit();
                                    editor.putString(GlobalConstants.PREF_KEY_USERNAME, username);
                                    editor.putString(GlobalConstants.PREF_KEY_PASSWORD, password);
                                    editor.putString(GlobalConstants.PREF_KEY_USER_ID, String.valueOf(user_id));

                                    String account_type;

                                    if (isAdmin==1)
                                        account_type = "ADMIN";
                                    else
                                        account_type = "DOCTOR";

                                    editor.putString(GlobalConstants.PREF_KEY_ACCOUNT_TYPE, account_type);


                                    editor.commit();

                                    goToMain();

                            } else {
                                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Looks like password and confirm password are not the same. Please enter the same in both the fields.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Username and password require a minimum of two characters each.", Toast.LENGTH_LONG).show();
                    }

                } else {

                    String username = inUsername.getText().toString().trim();
                    String password = inPassword.getText().toString().trim();

                    if (username.length() > 1 && password.length() > 1) {
                        db = new DatabaseHelper(LoginActivity.this);
                        ArrayList<String> returnData = db.getKeyPassword(username);

                        if (returnData.size() == 0) {
                            Toast.makeText(getApplicationContext(), "Username does not exist", Toast.LENGTH_LONG).show();
                        } else {
                            if (returnData.get(0).equals(password)) {
                                SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = s.edit();
                                editor.putString(GlobalConstants.PREF_KEY_USERNAME, username);
                                editor.putString(GlobalConstants.PREF_KEY_PASSWORD, password);
                                editor.putString(GlobalConstants.PREF_KEY_USER_ID, returnData.get(2));
                                editor.putInt(GlobalConstants.PREF_KEY_LOGIN_TYPE, GlobalConstants.LOGIN_ALREADY);

                                String account_type;

                                if (returnData.get(1).equals("1"))
                                    account_type = "ADMIN";
                                else
                                    account_type = "DOCTOR";

                                editor.putString(GlobalConstants.PREF_KEY_ACCOUNT_TYPE, account_type);
                                editor.commit();
                                goToMain();

                            } else {
                                Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Username and password require a minimum of two characters each.", Toast.LENGTH_LONG).show();
                    }


                }


                break;
            case R.id.btnSignIn:

                if (signUpEnabled) {
                    btnSignUp.setText("SIGN IN");
                    signUpEnabled = false;
                    llSwitch.setVisibility(View.GONE);
                    llSignIn.setVisibility(View.GONE);
                    btnCreateAccount.setVisibility(View.VISIBLE);
                    inConfirmPassword.setVisibility(View.GONE);
                }

                break;
            case R.id.btnCreateAccount:

                btnSignUp.setText("SIGN UP");
                signUpEnabled = true;
                llSwitch.setVisibility(View.VISIBLE);
                llSignIn.setVisibility(View.VISIBLE);
                btnCreateAccount.setVisibility(View.GONE);
                inConfirmPassword.setVisibility(View.VISIBLE);

                break;

            default:


        }

    }

    private void goToMain() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
