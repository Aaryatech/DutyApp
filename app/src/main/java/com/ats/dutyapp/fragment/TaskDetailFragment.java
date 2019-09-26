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

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.TaskDetailAdapter;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.ats.dutyapp.model.TaskDetailDisplay;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    TaskDetailAdapter adapter;
    ArrayList<TaskDetailDisplay> dutyDetailList =new ArrayList<>();
    public static DutyHeaderDetail model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_task_detail, container, false);
        getActivity().setTitle("Task Detail");
        recyclerView = view.findViewById(R.id.recyclerView);

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, DutyHeaderDetail.class);
            Log.e("MODEL DUTY INFO", "-----------------------------------" + model);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        for (int i=0;i<model.getTaskDetailDisplay().size();i++) {
            dutyDetailList.add(model.getTaskDetailDisplay().get(i));
       }

        adapter = new TaskDetailAdapter(dutyDetailList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

}
