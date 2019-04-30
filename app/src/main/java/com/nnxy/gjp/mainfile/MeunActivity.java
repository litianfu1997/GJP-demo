package com.nnxy.gjp.mainfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dbmanager.CommomUtils;
import com.nnxy.gjp.R;
import com.nnxy.gjp.application.MyApplication;
import com.nnxy.gjp.entity.Account;
import com.nnxy.gjp.fragment.AddAccountFragment;
import com.nnxy.gjp.fragment.AllAccountFragment;
import com.nnxy.gjp.fragment.SelectAccountFragment;

import org.json.JSONException;

import java.util.List;

public class MeunActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private  AddAccountFragment addAccountFragment;
    private SelectAccountFragment selectAccountFragment;
    private AllAccountFragment allAccountFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meun);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,new AllAccountFragment()).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meun, menu);
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

            CommomUtils accountUtils =new CommomUtils(getApplicationContext());

            List<Account> accountList = null;
            try {
                accountList = accountUtils.queryAllAccount(Integer.parseInt(MyApplication.getUser().getString("userId")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(accountList);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_account) {
            // Handle the camera action
            addAccountFragment =new AddAccountFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,addAccountFragment).commitAllowingStateLoss();

        } else if (id == R.id.nav_select_account) {
             selectAccountFragment =new SelectAccountFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,selectAccountFragment).commitAllowingStateLoss();
        } else if (id == R.id.nav_home) {
            allAccountFragment = new AllAccountFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,allAccountFragment).commitAllowingStateLoss();
        } else if (id == R.id.nav_account_tj) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
