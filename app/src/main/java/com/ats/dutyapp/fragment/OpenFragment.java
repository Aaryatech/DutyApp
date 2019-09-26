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
import com.ats.dutyapp.adapter.OpenAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.interfaces.OpenInterface;
import com.ats.dutyapp.model.Detail;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenFragment extends Fragment implements OpenInterface {
    public RecyclerView recyclerView;
    ArrayList<Detail> detailList =new ArrayList<>();
    Login loginUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_open, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getOpenlist(loginUser.getEmpId());

        return view;
    }


    private void getOpenlist(Integer empId) {
        Log.e("PARAMETER","                         EMP ID                "+empId);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Detail>> listCall = Constants.myInterface.getOpenChecklistByEmp(empId);
            listCall.enqueue(new Callback<ArrayList<Detail>>() {
                @Override
                public void onResponse(Call<ArrayList<Detail>> call, Response<ArrayList<Detail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("OPEN CHECK LIST : ", "------------------------------------------------- " + response.body());
                            detailList.clear();
                            detailList = response.body();

                            OpenAdapter adapter = new OpenAdapter(detailList,getActivity());
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
                public void onFailure(Call<ArrayList<Detail>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
