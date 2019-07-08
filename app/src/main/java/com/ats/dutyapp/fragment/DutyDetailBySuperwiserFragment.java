package com.ats.dutyapp.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.AssigneEmployeeAdapter;
import com.ats.dutyapp.adapter.DutyDetailBySuperwiser;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.AssignDuty;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.TaskDetailDisplay;
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
public class DutyDetailBySuperwiserFragment extends Fragment implements View.OnClickListener {

    public TextView tvTaskStartTime,tvTaskEndTime,tvDate,tvTaskName;
    public RecyclerView recyclerView,recyclerViewEmp;
    public Button btnSubmit;
    DutyHeaderDetail model;
    Login loginUser;
    AssigneEmployeeAdapter adapterEmp;
    ArrayList<Employee> empList = new ArrayList<>();
    DutyDetailBySuperwiser adapter;

    public static ArrayList<Employee> assignEmpStaticList = new ArrayList<>();
    String stringId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_duty_detail_by_superwiser, container, false);

        tvTaskStartTime=(TextView)view.findViewById(R.id.tvTaskStartTime);
        tvTaskEndTime=(TextView)view.findViewById(R.id.tvTaskEndTime);
        tvDate=(TextView)view.findViewById(R.id.tvDate);
        tvTaskName=(TextView)view.findViewById(R.id.tvTaskName);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerViewEmp=(RecyclerView)view.findViewById(R.id.recyclerViewEmp);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, DutyHeaderDetail.class);
            Log.e("MODEL DUTY INFO", "-----------------------------------" + model);

            tvTaskStartTime.setText("From Time : "+model.getShiftFromTime());
            tvTaskEndTime.setText("To Time : "+model.getShiftToTime());
            tvDate.setText(""+model.getCreatedDate());
            tvTaskName.setText(""+model.getDutyName());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if (model.getTaskDetailDisplay() != null) {
            ArrayList<TaskDetailDisplay> detailList = new ArrayList<>();
            for (int i = 0; i < model.getTaskDetailDisplay().size(); i++) {
                detailList.add(model.getTaskDetailDisplay().get(i));
            }

            adapter = new DutyDetailBySuperwiser(detailList, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        ArrayList<Integer> deptIdList = new ArrayList<>();
        deptIdList.add(loginUser.getEmpDeptId());

        getEmployee(deptIdList);

        btnSubmit.setOnClickListener(this);

        return view;
    }

    private void getEmployee(ArrayList<Integer> deptIdList) {
        Log.e("PARAMETER","            DEPT ID       "+deptIdList);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Employee>> listCall = Constants.myInterface.allEmployeesByDept(deptIdList);
            listCall.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGN EMPLOYEE LIST : ", " - " + response.body());
                            empList.clear();
                            empList = response.body();

                            assignEmpStaticList.clear();
                            assignEmpStaticList = empList;

                            for (int i = 0; i < assignEmpStaticList.size(); i++) {
                                assignEmpStaticList.get(i).setChecked(false);
                            }

                           // AssignUser();

                            adapterEmp = new AssigneEmployeeAdapter(empList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerViewEmp.setLayoutManager(mLayoutManager);
                            recyclerViewEmp.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewEmp.setAdapter(adapterEmp);

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
                public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
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
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmit)
        {
            AssignUser();
        }
    }

    private void AssignUser() {

        ArrayList<Employee> assignedEmpArray = new ArrayList<>();
        ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
        ArrayList<String> assignedEmpNameArray = new ArrayList<>();

        if (assignEmpStaticList != null) {
            if (assignEmpStaticList.size() > 0) {
                assignedEmpArray.clear();
                for (int i = 0; i < assignEmpStaticList.size(); i++) {
                    if (assignEmpStaticList.get(i).isChecked()) {
                        assignedEmpArray.add(assignEmpStaticList.get(i));
                        assignedEmpIdArray.add(assignEmpStaticList.get(i).getEmpId());
                        assignedEmpNameArray.add(assignEmpStaticList.get(i).getEmpFname() + " " + assignEmpStaticList.get(i).getEmpMname() + " " + assignEmpStaticList.get(i).getEmpSname());
                    }
                }
            }
            Log.e("ASSIGN EMP", "---------------------------------" + assignedEmpArray);
            Log.e("ASSIGN EMP SIZE", "---------------------------------" + assignedEmpArray.size());

            String empIds = assignedEmpIdArray.toString().trim();
            Log.e("ASSIGN EMP ID", "---------------------------------" + empIds);

            String a1 = "" + empIds.substring(1, empIds.length() - 1).replace("][", ",") + "";
            stringId = a1.replaceAll("\\s", "");

            Log.e("ASSIGN EMP ID STRING", "---------------------------------" + stringId);
            Log.e("ASSIGN EMP ID STRING1", "---------------------------------" + a1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            AssignDuty assignDuty= new AssignDuty(0,sdf.format(System.currentTimeMillis()),model.getDutyId(),stringId,"",loginUser.getEmpId(),"",0,1,1,0,0,"","","");
            saveAssigneDuty(assignDuty);

        }
    }

    private void saveAssigneDuty(AssignDuty assignDuty) {
        Log.e("PARAMETER","---------------------------------------ASSIGHN VISITOR--------------------------"+assignDuty);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<AssignDuty> listCall = Constants.myInterface.saveAssignDuty(assignDuty);
            listCall.enqueue(new Callback<AssignDuty>() {
                @Override
                public void onResponse(Call<AssignDuty> call, Response<AssignDuty> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGHN VISITOR : ", " ------------------------------ASSIGHN VISITOR------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new DutyListSuperwiser(), "MainFragment");
                            ft.commit();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<AssignDuty> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }
}
