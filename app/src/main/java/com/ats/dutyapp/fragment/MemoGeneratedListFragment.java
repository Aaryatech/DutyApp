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
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.adapter.EmployeeListAdapter;
import com.ats.dutyapp.adapter.MemoGeneratedListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fcm.SharedPrefManager;
import com.ats.dutyapp.model.GroupEmp;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.MemoGenerated;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoGeneratedListFragment extends Fragment {

    private RecyclerView recyclerView;
    Login loginUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_generated_list, container, false);
        getActivity().setTitle("Memo Generated");


        recyclerView = view.findViewById(R.id.recyclerView);

        try {
            String userStr = CustomSharedPreference.getString(getContext(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser.getEmpId());

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String todaysDate = sdf.format(cal.getTimeInMillis());

            getAllMemo(loginUser.getEmpId());

        } catch (Exception e) {
            e.printStackTrace();
        }




        return view;
    }


   /* private void getAllMemo(String fromDate, String toDate, int empId) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<MemoGenerated>> listCall = Constants.myInterface.getMemoByDateAndEmpId(fromDate, toDate, empId);
            listCall.enqueue(new Callback<ArrayList<MemoGenerated>>() {
                @Override
                public void onResponse(Call<ArrayList<MemoGenerated>> call, Response<ArrayList<MemoGenerated>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("MEMO LIST : ", " --------------------- LIST---------------------------- " + response.body());

                            MemoGeneratedListAdapter adapter=new MemoGeneratedListAdapter(response.body(),getContext());
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
                public void onFailure(Call<ArrayList<MemoGenerated>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }*/


    private void getAllMemo( int empId) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<MemoGenerated>> listCall = Constants.myInterface.getMemoByEmpId(empId);
            listCall.enqueue(new Callback<ArrayList<MemoGenerated>>() {
                @Override
                public void onResponse(Call<ArrayList<MemoGenerated>> call, Response<ArrayList<MemoGenerated>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("MEMO LIST : ", " --------------------- LIST---------------------------- " + response.body());

                            MemoGeneratedListAdapter adapter=new MemoGeneratedListAdapter(response.body(),getContext());
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
                public void onFailure(Call<ArrayList<MemoGenerated>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


}
