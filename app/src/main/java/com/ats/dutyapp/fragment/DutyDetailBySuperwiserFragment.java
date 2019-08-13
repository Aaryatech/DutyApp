package com.ats.dutyapp.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.AssigneEmployeeAdapter;
import com.ats.dutyapp.adapter.DutyDetailBySuperwiser;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.AssignDetail;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.ats.dutyapp.model.EmpList;
import com.ats.dutyapp.model.Info;
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
public class DutyDetailBySuperwiserFragment extends Fragment implements View.OnClickListener {

    public TextView tvTaskStartTime,tvTaskEndTime,tvDate,tvTaskName,tvAssignName,tvType;
    public RecyclerView recyclerView,recyclerViewEmp;
    public Button btnSubmit;
    public CardView cardView;
    public ImageView ivMenu;
    public static DutyHeaderDetail model;
    Login loginUser;
    AssigneEmployeeAdapter adapterEmp;
    AssignDetail assignDetail;
    DutyDetailBySuperwiser adapter;
    ArrayList<AssignDetail> detailList = new ArrayList<>();
    ArrayList<EmpList> empList = new ArrayList<>();

    public static ArrayList<EmpList> assignEmpStaticList = new ArrayList<>();
    String stringId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_duty_detail_by_superwiser, container, false);
        getActivity().setTitle("Duty Detail");
        tvTaskStartTime=(TextView)view.findViewById(R.id.tvTaskStartTime);
        tvTaskEndTime=(TextView)view.findViewById(R.id.tvTaskEndTime);
        tvDate=(TextView)view.findViewById(R.id.tvDate);
        tvType=(TextView)view.findViewById(R.id.tvType);
        tvTaskName=(TextView)view.findViewById(R.id.tvTaskName);
        tvAssignName=(TextView)view.findViewById(R.id.tvAssignName);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);
        ivMenu=(ImageView)view.findViewById(R.id.ivMenu);
        cardView=(CardView)view.findViewById(R.id.cardView);

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, DutyHeaderDetail.class);
            Log.e("MODEL DUTY INFO", "-----------------------------------" + model);


        }catch (Exception e)
        {
            e.printStackTrace();
        }

//        if (model.getTaskDetailDisplay() != null) {
//            ArrayList<TaskDetailDisplay> detailList = new ArrayList<>();
//            for (int i = 0; i < model.getTaskDetailDisplay().size(); i++) {
//                detailList.add(model.getTaskDetailDisplay().get(i));
//            }
//
//            adapter = new DutyDetailBySuperwiser(detailList, getContext());
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(adapter);
//        }

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getAssignEmp(model.getDutyId());

        btnSubmit.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        //cardView.setOnClickListener(this);

        return view;
    }

    private void getAssignEmp(Integer dutyId) {

        Log.e("PARAMETER","            DUTY ID       "+dutyId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<AssignDetail> listCall = Constants.myInterface.getAssignDutyByDutyId(dutyId);
            listCall.enqueue(new Callback<AssignDetail>() {
                @Override
                public void onResponse(Call<AssignDetail> call, Response<AssignDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DUTY DETAIL LIST : ", " - " + response.body());
                             assignDetail=response.body();
                             // detailList.add(assignDetail);

                            if (assignDetail.getEmpList() != null) {

                            for (int j = 0; j < assignDetail.getEmpList().size(); j++) {
                                empList.add(assignDetail.getEmpList().get(j));
                            }

                         }

                            assignEmpStaticList.clear();
                            assignEmpStaticList = empList;

//                            for (int i = 0; i < assignEmpStaticList.size(); i++) {
//                                assignEmpStaticList.get(i).setChecked(false);
//                            }

                            tvTaskStartTime.setText("From Time : "+assignDetail.getShiftFromTime());
                            tvTaskEndTime.setText("To Time : "+assignDetail.getShiftToTime());
                            tvDate.setText(""+assignDetail.getAssignDate());
                            tvTaskName.setText(""+assignDetail.getDutyName());
                            tvAssignName.setText(""+assignDetail.getTaskAssignUserName());

                            if(assignDetail.getExInt1()==0)
                            {
                                tvType.setText("OFF");
                            }else if(assignDetail.getExInt1()==1)
                            {
                                tvType.setText("ON");
                            }

                            adapterEmp = new AssigneEmployeeAdapter(empList, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapterEmp);

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
                public void onFailure(Call<AssignDetail> call, Throwable t) {
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
        }else if(v.getId()==R.id.ivMenu)
        {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_on_off, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.action_on) {

                        updateAssignDutySchedule(assignDetail.getAssignId(),1);

                    }else if(menuItem.getItemId()==R.id.action_off)
                    {
                        updateAssignDutySchedule(assignDetail.getAssignId(),0);
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
//        else if(v.getId()==R.id.cardView)
//        {
////            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
////            ft.replace(R.id.content_frame, new TaskFragment(), "MainFragment");
////            ft.commit();
//
//            Gson gson = new Gson();
//            String json = gson.toJson(model);
//
//            TaskFragment adf = new TaskFragment();
//            Bundle args = new Bundle();
//            args.putString("model",json);
//            adf.setArguments(args);
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DutyDetialFragment").commit();
//
//        }
    }

    private void updateAssignDutySchedule(Integer assignId, int status) {
        Log.e("PARAMETER","---------------------------------------ASSIGN ID--------------------------"+assignId+"---------------------------STATUS-----------------"+status);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateAssignDutySchedule(assignId,status);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            if(!response.body().getError()) {

                                Log.e("UPDATE STATUS : ", " ------------------------------UPDATE STATUS------------------------- " + response.body());
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new DutyDetailBySuperwiserFragment(), "MainFragment");
                                ft.commit();

                                commonDialog.dismiss();

                            }else{
                                Toast.makeText(getActivity(), "Unable to assign", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            }

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
                public void onFailure(Call<Info> call, Throwable t) {
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

    private void AssignUser() {

        ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
        ArrayList<String> assignedEmpNameArray = new ArrayList<>();

        if (assignEmpStaticList != null) {
            if (assignEmpStaticList.size() > 0) {
                for (int i = 0; i < assignEmpStaticList.size(); i++) {
                    if (assignEmpStaticList.get(i).getAssigned()) {
                        assignedEmpIdArray.add(assignEmpStaticList.get(i).getEmpId());
                        assignedEmpNameArray.add(assignEmpStaticList.get(i).getEmpName());
                    }
                }
            }

            String empIds = assignedEmpIdArray.toString().trim();
            Log.e("ASSIGN EMP ID", "---------------------------------" + empIds);

            String a1 = "" + empIds.substring(1, empIds.length() - 1).replace("][", ",") + "";
            stringId = a1.replaceAll("\\s", "");

            Log.e("ASSIGN EMP ID STRING", "---------------------------------" + stringId);
            Log.e("ASSIGN EMP ID STRING1", "---------------------------------" + a1);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to submit ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    saveAssigneDuty(assignDetail.getAssignId(),model.getShiftFromTime(),stringId);

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    private void saveAssigneDuty(Integer dutyId, String notifyTime, String empId) {
        Log.e("PARAMETER","---------------------------------------DUTY ID--------------------------"+dutyId+"---------------------------NOTIFY TIME------------------"+notifyTime+"--------------------------EMP ID-----------------------------"+empId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateAssignDutyTimeAndEmpIds(dutyId,notifyTime,empId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            if(!response.body().getError()) {
                                Log.e("SAVE ASSIGN EMP : ", " ------------------------------SAVE ASSIGN EMP------------------------- " + response.body());
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new DutyListSuperwiser(), "MainFragment");
                                ft.commit();

                                commonDialog.dismiss();
                            }else{
                                Toast.makeText(getActivity(), "Unable to assign", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            }

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
                public void onFailure(Call<Info> call, Throwable t) {
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
