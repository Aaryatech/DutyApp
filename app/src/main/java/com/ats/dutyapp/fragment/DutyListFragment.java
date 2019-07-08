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
import com.ats.dutyapp.adapter.DutyListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyHeader;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.utils.CommonDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyListFragment extends Fragment {
Employee model;
    private RecyclerView recyclerView;
    DutyListAdapter adapter;
    ArrayList<DutyHeader> dutyList =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_duty_list, container, false);
        getActivity().setTitle("Duty List");
        recyclerView = view.findViewById(R.id.recyclerView);

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, Employee.class);
            Log.e("MODEL EMPLOYEE INFO", "-----------------------------------" + model);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(model!=null) {
            getDutyList(model.getEmpId(), sdf.format(System.currentTimeMillis()));
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
