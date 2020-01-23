package com.ats.dutyapp.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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
import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.GroupList;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupwiseTaskListFragment extends Fragment implements View.OnClickListener {

    public RecyclerView recyclerView;
    public ArrayList<ChatTask> taskList = new ArrayList<>();

    FloatingActionButton fab;

    Login loginUser;
    TaskListAdapter adapter;

    DatabaseHandler db;

    private BroadcastReceiver broadcastReceiver, bRChatDetail;

   static int groupId=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupwise_task_list, container, false);
        getActivity().setTitle("Task");

        recyclerView = view.findViewById(R.id.recyclerView);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        db = new DatabaseHandler(getContext());

        try {
            String userStr = CustomSharedPreference.getString(getContext(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("USER  : ", "--------PREF-------" + loginUser);

            groupId = getArguments().getInt("groupId");

            getAllTask(loginUser.getEmpId(),groupId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_NOTIFICATION")) {
                    handlePushNotification(intent);
                }
            }
        };

        bRChatDetail = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("CHAT_DETAIL")) {
                    handlePushNotification(intent);
                }
            }
        };

        return view;
    }


    private void getAllTask(int userId,int groupId) {
        if (Constants.isOnline(getContext())) {

            Call<ArrayList<ChatTask>> listCall = Constants.myInterface.getAllChatHeaderDisplayByUserAndGroup(userId,groupId);
            listCall.enqueue(new Callback<ArrayList<ChatTask>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatTask>> call, Response<ArrayList<ChatTask>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER LIST : ", "------------------------------------------------- " + response.body());
                            taskList.clear();
                            taskList = response.body();

                            if (taskList.size() > 0) {

                                adapter = new TaskListAdapter(taskList, getActivity(),"group");
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                            }

                        } else {
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatTask>> call, Throwable t) {
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("TASK_LIST_FRG", "----------- ON PAUSE");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(bRChatDetail);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TASK_LIST_FRG", "----------- ON RESUME");

        if (loginUser != null) {
            getAllTask(loginUser.getEmpId(),groupId);
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter("REFRESH_NOTIFICATION"));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(bRChatDetail,
                new IntentFilter("CHAT_DETAIL"));
    }


    private void handlePushNotification(Intent intent) {

        if (loginUser != null) {
            getAllTask(loginUser.getEmpId(),groupId);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddTaskFragment(), "GroupwiseTaskListDashFragment");
            ft.commit();
        }
    }
}
