package com.ats.dutyapp.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.GroupListAdapter;
import com.ats.dutyapp.adapter.GroupListDashAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.GroupList;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupListDashFragment extends Fragment  {

    private RecyclerView recyclerView;

    ArrayList<GroupList> grpList =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list_dash, container, false);
        getActivity().setTitle("Group List");

        recyclerView=view.findViewById(R.id.recyclerView);

        try{

            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            Login loginUserMain = gson.fromJson(userStr, Login.class);

            groupList(loginUserMain.getEmpId());


        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }


    private void groupList(int userId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<GroupList>> listCall = Constants.myInterface.getAllChatGroupDisplayByUser(userId);
            listCall.enqueue(new Callback<ArrayList<GroupList>>() {
                @Override
                public void onResponse(Call<ArrayList<GroupList>> call, Response<ArrayList<GroupList>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("GROUP LIST : ", "------------------------------------------------- " + response.body());
                            grpList.clear();
                            grpList = response.body();

                            GroupListDashAdapter adapter = new GroupListDashAdapter(grpList,getActivity());
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
                public void onFailure(Call<ArrayList<GroupList>> call, Throwable t) {
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
