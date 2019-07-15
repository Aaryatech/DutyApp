package com.ats.dutyapp.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.DashboardFragment;
import com.ats.dutyapp.fragment.DutyDetailBySuperwiserFragment;
import com.ats.dutyapp.fragment.DutyDetailFragment;
import com.ats.dutyapp.fragment.DutyListFragment;
import com.ats.dutyapp.fragment.DutyListSuperwiser;
import com.ats.dutyapp.fragment.EmployeeDashboardFragment;
import com.ats.dutyapp.fragment.EmployeeListFragment;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
ArrayList<Sync> syncArray = new ArrayList<>();
Login loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            String userStr = CustomSharedPreference.getString(getApplication(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = prefs.getString("Sync", null);
            Type type = new TypeToken<ArrayList<Sync>>() {}.getType();
            syncArray= gson.fromJson(json, type);

            Log.e("SYNC MAIN : ", "--------USER-------" + syncArray);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        View header = navigationView.getHeaderView(0);

        TextView tvNavHeadName = header.findViewById(R.id.tvNavHeadName);
        TextView tvNavHeadDesg = header.findViewById(R.id.tvNavHeadDesg);
        CircleImageView ivNavHeadPhoto = header.findViewById(R.id.ivNavHeadPhoto);

        if (loginUser != null) {
            tvNavHeadName.setText("" + loginUser.getEmpFname() + " " + loginUser.getEmpMname() + " " + loginUser.getEmpSname());
            if(loginUser.getEmpCatId()==1) {
                tvNavHeadDesg.setText("Superwiser");
            }else  if(loginUser.getEmpCatId()==2) {
                tvNavHeadDesg.setText("Admin");
            }else  if(loginUser.getEmpCatId()==3) {
                tvNavHeadDesg.setText("Employee");
            }else  if(loginUser.getEmpCatId()==4) {
                tvNavHeadDesg.setText("Security");
            }

            try {
                Picasso.with(HomeActivity.this).load(Constants.IMAGE_URL+loginUser.getEmpPhoto()).placeholder(getResources().getDrawable(R.drawable.profile)).into(ivNavHeadPhoto);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<syncArray.size();i++)
        {
            Log.e("MY TAG","-----syncArray-------");
            if(syncArray.get(i).getSettingKey().equals("Employee"))
            {
                Log.e("MY TAG1","-----Employee-------");
                if(syncArray.get(i).getSettingValue().equals(String.valueOf(loginUser.getEmpCatId())))
                {
                    navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_emp_list).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_assigne_emp).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_duty_list_emp).setVisible(true);

                    Log.e("MY TAG","-----Employee-------");
                }
            }

            if(syncArray.get(i).getSettingKey().equals("Admin"))
            {
                Log.e("MY TAG1","-----Admin-------");
                if(syncArray.get(i).getSettingValue().equals(String.valueOf(loginUser.getEmpCatId())))
                {
                    navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_emp_list).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_assigne_emp).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_duty_list_emp).setVisible(false);

                    Log.e("MY TAG","-----Admin-------");
                }
            }
            if(syncArray.get(i).getSettingKey().equals("Supervisor")) {
                Log.e("MY TAG1","------Supervisor------");
                if (syncArray.get(i).getSettingValue().equals(String.valueOf(loginUser.getEmpCatId()))) {
                    navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_emp_list).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_assigne_emp).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_duty_list_emp).setVisible(false);
                    Log.e("MY TAG","------Supervisor------");
                }
            }

        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new DashboardFragment(), "Exit");
        ft.commit();

    }

    @Override
    public void onBackPressed() {

        Fragment exit = getSupportFragmentManager().findFragmentByTag("Exit");
        Fragment mainFragment = getSupportFragmentManager().findFragmentByTag("MainFragment");
        Fragment employeeFragment = getSupportFragmentManager().findFragmentByTag("EmployeeFragment");
        Fragment dutyFragment = getSupportFragmentManager().findFragmentByTag("DutyFragment");
        Fragment dutyListSupFragment = getSupportFragmentManager().findFragmentByTag("DutyListSupFragment");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (exit instanceof DashboardFragment && exit.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setMessage("Exit Application ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // updateToken(loginUser.getEmpId(), "");
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (mainFragment instanceof EmployeeListFragment && mainFragment.isVisible() ||
                mainFragment instanceof DutyListSuperwiser && mainFragment.isVisible() ||
                mainFragment instanceof EmployeeDashboardFragment && mainFragment.isVisible() ) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DashboardFragment(), "Exit");
            ft.commit();

        }else if (employeeFragment instanceof DutyListFragment && employeeFragment.isVisible() ) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeListFragment(), "MainFragment");
            ft.commit();

        }
        else if (dutyFragment instanceof DutyDetailFragment && dutyFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListFragment(), "EmployeeFragment");
            ft.commit();

        }else if (dutyListSupFragment instanceof DutyDetailBySuperwiserFragment && dutyListSupFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListSuperwiser(), "MainFragment");
            ft.commit();

        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DashboardFragment(), "MainFragment");
            ft.commit();
        } else if (id == R.id.nav_assigne_emp) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListSuperwiser(), "MainFragment");
            ft.commit();
        }else if (id == R.id.nav_emp_list) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeListFragment(), "MainFragment");
            ft.commit();
        }else if (id == R.id.nav_duty_list_emp) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListFragment(), "MainFragment");
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
