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
import com.ats.dutyapp.adapter.TaskListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {
    public RecyclerView recyclerView;
    public static ArrayList<ChatTask> taskList =new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_task_list, container, false);
        getActivity().setTitle("Task List");
        recyclerView=view.findViewById(R.id.recyclerView);
       // prepareData();

        getAllTask(1);


        return view;
    }

    private void getAllTask(int isActive) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ChatTask>> listCall = Constants.myInterface.getAllChatHeaderDisplay(isActive);
            listCall.enqueue(new Callback<ArrayList<ChatTask>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatTask>> call, Response<ArrayList<ChatTask>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("GROUP LIST : ", "------------------------------------------------- " + response.body());
                            taskList.clear();
                            taskList = response.body();

                            TaskListAdapter adapter = new TaskListAdapter(taskList,getActivity());
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
                public void onFailure(Call<ArrayList<ChatTask>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

//    private void prepareData() {
//        TaskTemp myListData = new TaskTemp("Assignment Mapping","12-08-2019","23-08-2019","Avinash Kachru,Archana Tarate,Kavita Sonawne","Pending","fgffg huhies ijghroe hiejjjduj jfjfjffj");
//        taskList.add(myListData);
//
//        myListData = new TaskTemp("Assignment Mapping","12-08-2019","23-08-2019","Avinash Kachru,Archana Tarate,Kavita Sonawne","Pending","fgffg huhies ijghroe hiejjjduj jfjfjffj");
//        taskList.add(myListData);
//
//
//    }

}
