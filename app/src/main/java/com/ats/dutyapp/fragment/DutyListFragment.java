package com.ats.dutyapp.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.DutyListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyHeader;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyListFragment extends Fragment {
  public static Employee model;
    private RecyclerView recyclerView;
    DutyListAdapter adapter;
    ArrayList<DutyHeader> dutyList =new ArrayList<>();
    public static Login loginUserMain;
    ArrayList<Sync> syncArray = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_duty_list, container, false);
        getActivity().setTitle("Duty List");
        recyclerView = view.findViewById(R.id.recyclerView);

        try {
            String employeeStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(employeeStr, Employee.class);
            Log.e("MODEL EMPLOYEE INFO", "-----------------------------------" + model);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUserMain = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUserMain);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Gson gson = new Gson();
            String json = prefs.getString("Sync", null);
            Type type = new TypeToken<ArrayList<Sync>>() {}.getType();
            syncArray= gson.fromJson(json, type);

            Log.e("SYNC MAIN : ", "--------USER-------" + syncArray);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if(syncArray!=null) {
            for (int j = 0; j < syncArray.size(); j++) {
                if (syncArray.get(j).getSettingKey().equals("Employee")) {
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        getDutyList(loginUserMain.getEmpId(), sdf.format(System.currentTimeMillis()));

                    }
                } else if(syncArray.get(j).getSettingKey().equals("Supervisor")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        if(model!=null) {
                            getDutyList(model.getEmpId(), sdf.format(System.currentTimeMillis()));
                        }

                    }
                }else if(syncArray.get(j).getSettingKey().equals("Admin")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        if(model!=null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            getDutyList(model.getEmpId(), sdf.format(System.currentTimeMillis()));
                        }

                    }
                }
            }
        }

        return view;
    }

    private void getDutyList(Integer empId, String date) {
        Log.e("PARAMETER","            EMP ID       "+empId  +"          DATE           "+date);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DutyHeader>> listCall = Constants.myInterface.getTaskDoneHeaderByEmpAndDate(empId,date);
            listCall.enqueue(new Callback<ArrayList<DutyHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<DutyHeader>> call, Response<ArrayList<DutyHeader>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DUTY LIST : ", " - " + response.body());
                            dutyList.clear();
                            dutyList = response.body();

                            adapter = new DutyListAdapter(dutyList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DutyHeader>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

}
