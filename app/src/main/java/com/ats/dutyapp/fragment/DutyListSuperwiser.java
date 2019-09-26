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
import com.ats.dutyapp.adapter.DutyHeaderDetailListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyListSuperwiser extends Fragment {
    Employee model;
    private RecyclerView recyclerView;
    DutyHeaderDetailListAdapter adapter;
    ArrayList<DutyHeaderDetail> dutyList =new ArrayList<>();
    Login loginUser;
    ArrayList<Sync> syncArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_duty_list_by_dept, container, false);
        getActivity().setTitle("Duty List");
        recyclerView = view.findViewById(R.id.recyclerView);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
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
              if(syncArray.get(j).getSettingKey().equals("Supervisor")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUser.getEmpCatId()))) {

                        ArrayList<Integer> deptList = new ArrayList<>();
                        deptList.add(loginUser.getEmpDeptId());
                        getDutyListByDept(deptList);

                    }
                }else if(syncArray.get(j).getSettingKey().equals("Admin")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUser.getEmpCatId()))) {
                        ArrayList<Integer> deptList = new ArrayList<>();
                        deptList.add(-1);
                        getDutyListByDept(deptList);

                    }
                }
            }
        }

        return view;
    }

    private void getDutyListByDept(ArrayList<Integer> empDeptId) {
        Log.e("PARAMETER","            EMP DEPT ID       "+empDeptId );

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DutyHeaderDetail>> listCall = Constants.myInterface.allDutyHeaderDetailByDept(empDeptId);
            listCall.enqueue(new Callback<ArrayList<DutyHeaderDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<DutyHeaderDetail>> call, Response<ArrayList<DutyHeaderDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DUTY LIST BY DEPT: ", " - " + response.body());
                            dutyList.clear();
                            dutyList = response.body();

                            adapter = new DutyHeaderDetailListAdapter(dutyList, getContext());
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
                public void onFailure(Call<ArrayList<DutyHeaderDetail>> call, Throwable t) {
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
