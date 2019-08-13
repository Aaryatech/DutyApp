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
import com.ats.dutyapp.adapter.TaskAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyDetail;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.ats.dutyapp.utils.CommonDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    private RecyclerView recyclerView;
    TaskAdapter adapter;
    ArrayList<DutyDetail> dutyDetailList =new ArrayList<>();
    public static DutyHeaderDetail model;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_task, container, false);
        getActivity().setTitle("Task List");
        recyclerView = view.findViewById(R.id.recyclerView);

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, DutyHeaderDetail.class);
            Log.e("MODEL TASK INFO", "-----------------------------------" + model);


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        
        getTask(model.getDutyId());
        
        return view;
    }

    private void getTask(Integer dutyId) {
        Log.e("PARAMETER","            HEADER ID       "+dutyId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DutyDetail>> listCall = Constants.myInterface.getTaskDoneDetailByHeaderId(dutyId);
            listCall.enqueue(new Callback<ArrayList<DutyDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<DutyDetail>> call, Response<ArrayList<DutyDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("TASK LIST : ", " - " + response.body());
                            dutyDetailList.clear();
                            dutyDetailList = response.body();

                            adapter = new TaskAdapter(dutyDetailList, getContext());
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
                public void onFailure(Call<ArrayList<DutyDetail>> call, Throwable t) {
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
