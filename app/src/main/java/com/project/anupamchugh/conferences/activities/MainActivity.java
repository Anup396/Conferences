package com.project.anupamchugh.conferences.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import com.project.anupamchugh.conferences.models.AllConferences;
import com.project.anupamchugh.conferences.models.Users;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Drawer mDrawer = null;
    ListView listView;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initViews();
        initDrawerLayout(savedInstanceState);


    }

    private void initViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.displayText));
        SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
        String account_type = s.getString(GlobalConstants.PREF_KEY_ACCOUNT_TYPE, "");


        if (account_type.equals("ADMIN")) {

            fab.setVisibility(View.VISIBLE);
            db = new DatabaseHelper(this);
            List<AllConferences> allConferencesList = db.getAllConferences();
            final AllAdminConferences allAdminConferences = new AllAdminConferences(allConferencesList, getApplicationContext());
            listView.setAdapter(allAdminConferences);

            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    AllConferences allConferences = ((AllConferences) listView.getAdapter().getItem(i));
                    startActivityForResult(new Intent(MainActivity.this, EditConferenceActivity.class).putExtra("conference", allConferences), 200);

                }
            };

            listView.setOnItemClickListener(itemClickListener);


        } else {

            fab.setVisibility(View.GONE);
            final String user_id = s.getString(GlobalConstants.PREF_KEY_USER_ID, "");

            Log.d("API123", "user_id " + user_id);

            db = new DatabaseHelper(this);
            List<AllConferences> allConferencesList = db.getAllConfForUserId(Long.parseLong(user_id));
            final AllAdminConferences allAdminConferences = new AllAdminConferences(allConferencesList, getApplicationContext());
            listView.setAdapter(allAdminConferences);

            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    AllConferences allConferences = ((AllConferences) listView.getAdapter().getItem(i));
                    db = new DatabaseHelper(MainActivity.this);
                    Users user = db.getStatusForUser(Long.parseLong(user_id), allConferences.conf_id);
                    startActivity(new Intent(MainActivity.this, ViewConferenceActivity.class).putExtra("conference", allConferences).putExtra("user", user));

                }
            };

            listView.setOnItemClickListener(itemClickListener);

        }


    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            if (mDrawer.isDrawerOpen())
                mDrawer.closeDrawer();
            else
                mDrawer.openDrawer();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDrawerLayout(final Bundle savedInstanceState) {


        SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
        String username = s.getString(GlobalConstants.PREF_KEY_USERNAME, "");
        String account_type = s.getString(GlobalConstants.PREF_KEY_ACCOUNT_TYPE, "");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.material_drawer_dark_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(username).withEmail(account_type).withIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withCloseOnClick(true)
                .withHasStableIds(true)
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("CONFERENCES").withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("SUGGESTED TOPICS").withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("SIGN OUT").withIdentifier(3).withSelectable(false)
                ) // added the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {


                            } else if (drawerItem.getIdentifier() == 2) {

                                startActivity(new Intent(MainActivity.this, SuggestionsActivity.class));


                            } else if (drawerItem.getIdentifier() == 3) {

                                SharedPreferences s = getSharedPreferences(GlobalConstants.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = s.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(mDrawer);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, NewConferenceActivity.class));
                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("API123", "onResume");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("API123", "onResume");

        List<AllConferences> allConferencesList = db.getAllConferences();
        AllAdminConferences allAdminConferences = new AllAdminConferences(allConferencesList, getApplicationContext());
        listView.setAdapter(allAdminConferences);
        allAdminConferences.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            List<AllConferences> allConferencesList = db.getAllConferences();
            AllAdminConferences allAdminConferences = new AllAdminConferences(allConferencesList, getApplicationContext());
            listView.setAdapter(allAdminConferences);
            allAdminConferences.notifyDataSetChanged();
        }
    }
}
