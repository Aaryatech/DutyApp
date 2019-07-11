package com.ats.dutyapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ats.dutyapp.R;
import com.ats.dutyapp.fragment.DashboardFragment;
import com.ats.dutyapp.fragment.DutyDetailBySuperwiserFragment;
import com.ats.dutyapp.fragment.DutyDetailFragment;
import com.ats.dutyapp.fragment.DutyListSuperwiser;
import com.ats.dutyapp.fragment.DutyListFragment;
import com.ats.dutyapp.fragment.EmployeeListFragment;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
Login loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String userStr = CustomSharedPreference.getString(getApplication(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(loginUser.getEmpCatId()==1)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListSuperwiser(), "Exit");
            ft.commit();
        }else if(loginUser.getEmpCatId()==3) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeListFragment(), "Exit");
            ft.commit();
        }

    }

    @Override
    public void onBackPressed() {

        Fragment exit = getSupportFragmentManager().findFragmentByTag("Exit");
        Fragment mainFragment = getSupportFragmentManager().findFragmentByTag("MainFragment");
        Fragment dutyFragment = getSupportFragmentManager().findFragmentByTag("DutyFragment");
        Fragment dutyListSupFragment = getSupportFragmentManager().findFragmentByTag("DutyListSupFragment");
        if (exit instanceof EmployeeListFragment && exit.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
            builder.setMessage("Exit Application ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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

        }else if (mainFragment instanceof EmployeeListFragment && mainFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DashboardFragment(), "Exit");
            ft.commit();

        }
        else if (mainFragment instanceof DutyListFragment && mainFragment.isVisible() ||
                mainFragment instanceof DutyDetailFragment && mainFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeListFragment(), "Exit");
            ft.commit();

        }else if (dutyFragment instanceof DutyDetailFragment && dutyFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListFragment(), "Exit");
            ft.commit();

        }else if (dutyListSupFragment instanceof DutyDetailBySuperwiserFragment && dutyListSupFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DutyListSuperwiser(), "Exit");
            ft.commit();
        }
        else {

            super.onBackPressed();
        }
    }
}
