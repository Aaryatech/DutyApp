package com.ats.dutyapp.fragment;


import android.os.Bundle;
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
import com.ats.dutyapp.adapter.EmployeeAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DeptCount;
import com.ats.dutyapp.model.EmpCount;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeDashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    EmployeeAdapter adapter;
    ArrayList<EmpCount> empList = new ArrayList<>();
    DeptCount model;
    Login loginUserMain;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_dashboard, container, false);
        getActivity().setTitle("Employee List");
        recyclerView = view.findViewById(R.id.recyclerView);

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
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, DeptCount.class);
            Log.e("MODEL EMPLOYEE INFO", "-----------------------------------" + model);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        ArrayList<Integer> empList = new ArrayList<>();
        empList.add(-1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        getEmployeeList(model.getDeptId(),empList,sdf.format(System.currentTimeMillis()));

        return view;
    }

    private void getEmployeeList(int deptId, ArrayList<Integer>  empId, String date) {

        Log.e("PARAMETER","            DEPT ID       "+deptId+"     EMP ID   "+empId+"         Date     "+date);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<EmpCount>> listCall = Constants.myInterface.getEmpWiseCount(deptId,empId,date);
            listCall.enqueue(new Callback<ArrayList<EmpCount>>() {
                @Override
                public void onResponse(Call<ArrayList<EmpCount>> call, Response<ArrayList<EmpCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DEPARTMENT LIST : ", " - " + response.body());
                            empList.clear();
                            empList = response.body();

                            adapter = new EmployeeAdapter(empList, getContext());
//                            DashboardEmployeeAdapter
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
                public void onFailure(Call<ArrayList<EmpCount>> call, Throwable t) {
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
