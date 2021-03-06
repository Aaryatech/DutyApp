package com.ats.dutyapp.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.DashboardEmployeeAdapter;
import com.ats.dutyapp.adapter.DepartmentAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DeptCount;
import com.ats.dutyapp.model.EmpCount;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    DepartmentAdapter adapter;
    Login loginUserMain;
    ArrayList<DeptCount> deptList = new ArrayList<>();
    ArrayList<Sync> syncArray = new ArrayList<>();
    DashboardEmployeeAdapter adapterEmp;
    ArrayList<EmpCount> empList = new ArrayList<>();

    private FloatingActionButton fab,fabGroup,fabTask,fabAddTask;
    private Animation fab_open, fab_close;
    private TextView tv_fab1,tv_fab2;
    Boolean isOpen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);
        getActivity().setTitle("Dashboard");
        recyclerView = view.findViewById(R.id.recyclerView);
        fab=view.findViewById(R.id.fab);
        fabGroup=view.findViewById(R.id.fabGroup);
        fabTask=view.findViewById(R.id.fabTask);
        fabAddTask=view.findViewById(R.id.fabAddTask);

        tv_fab1 = (TextView) view.findViewById(R.id.tv_fab1);
        tv_fab2 = (TextView) view.findViewById(R.id.tv_fab2);

        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);

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
                if (syncArray.get(j).getSettingKey().equals("Employee")) {
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        ArrayList<Integer> empList = new ArrayList<>();
                        empList.add(-1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        getEmployeeList(loginUserMain.getEmpDeptId(),empList,sdf.format(System.currentTimeMillis()));

                        Log.e("Employee","-------------------------");

                    }
                } else if(syncArray.get(j).getSettingKey().equals("Supervisor")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {

                        ArrayList<Integer> deptIdList = new ArrayList<>();
                        deptIdList.add(loginUserMain.getEmpDeptId());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        getDeptmentList(deptIdList,sdf.format(System.currentTimeMillis()));

                        Log.e("Supervisor","-------------------------");
                    }
                }else if(syncArray.get(j).getSettingKey().equals("Admin")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {

                        ArrayList<Integer> deptIdList = new ArrayList<>();
                        deptIdList.add(-1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        getDeptmentList(deptIdList,sdf.format(System.currentTimeMillis()));

                        Log.e("Admin","-------------------------");
                    }
                }
            }
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    fabGroup.startAnimation(fab_close);
                    fabTask.startAnimation(fab_close);
                    fabAddTask.startAnimation(fab_close);

                    fabGroup.setClickable(false);
                    fabTask.setClickable(false);
                    fabAddTask.setClickable(false);
                    isOpen = false;

                } else {
                    fabGroup.setClickable(true);
                    fabTask.setClickable(true);
                    fabAddTask.setClickable(true);

                    fabGroup.startAnimation(fab_open);
                    fabTask.startAnimation(fab_open);
                    fabAddTask.startAnimation(fab_open);
                    isOpen = true;
                }


//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, new TaskListFragment(), "MainFragment");
//                ft.commit();


            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new AddTaskFragment(), "MainFragment");
                ft.commit();
            }
        });


        fabGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new GroupListDashFragment(), "MainFragment");
                ft.commit();
            }
        });

        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new TaskListFragment(), "MainFragment");
                ft.commit();
            }
        });




        return view;
    }

    private void getEmployeeList(Integer deptId, ArrayList<Integer> empId, String date) {
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

                            adapterEmp = new DashboardEmployeeAdapter(empList, getContext());
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
           // Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDeptmentList(ArrayList<Integer> deptIdList, String date) {
        Log.e("PARAMETER","            DEPT ID       "+deptIdList+"         Date     "+date);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DeptCount>> listCall = Constants.myInterface.getDeptWiseCount(deptIdList,date);
            listCall.enqueue(new Callback<ArrayList<DeptCount>>() {
                @Override
                public void onResponse(Call<ArrayList<DeptCount>> call, Response<ArrayList<DeptCount>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DEPARTMENT LIST : ", " - " + response.body());
                            deptList.clear();
                            deptList = response.body();

                            adapter = new DepartmentAdapter(deptList, getContext());
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
                public void onFailure(Call<ArrayList<DeptCount>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
          //  Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


}
