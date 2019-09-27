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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.EmployeeListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatHeader;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.GroupEmp;
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
public class AboutTaskFragment extends Fragment implements View.OnClickListener {
public RecyclerView recyclerView;
public CardView cardViewTaskReq,cardViewTaskComp,cardViewTaskStart;
public TextView tvTaskName,tvCompletionDate,tvRemark,tvDesc;
ArrayList<GroupEmp> empList = new ArrayList<>();
 ArrayList<Employee> employeeList = new ArrayList<>();
EmployeeListAdapter adapter;
public static Login loginUser;
public static ChatTask model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about_task, container, false);
        getActivity().setTitle("About Task");

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        cardViewTaskReq=(CardView) view.findViewById(R.id.cardViewTaskReq);
        cardViewTaskComp=(CardView)view.findViewById(R.id.cardViewTaskComp);
        cardViewTaskStart=(CardView)view.findViewById(R.id.cardViewTaskStart);

        tvTaskName=(TextView) view.findViewById(R.id.tvTaskName);
        tvCompletionDate=(TextView)view.findViewById(R.id.tvCompletionDate);
        tvRemark=(TextView)view.findViewById(R.id.tvRemark);
        tvDesc=(TextView)view.findViewById(R.id.tvDesc);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, ChatTask.class);
            tvTaskName.setText(""+model.getHeaderName());
            tvCompletionDate.setText(""+model.getLastDate());
            tvRemark.setText(""+model.getTaskCompleteRemark());
            tvDesc.setText(""+model.getTaskDesc());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(loginUser.getEmpId().equals(model.getCreatedUserId()))
        {
            cardViewTaskComp.setVisibility(View.VISIBLE);
            cardViewTaskReq.setVisibility(View.GONE);
        }else if(!loginUser.getEmpId().equals(model.getCreatedUserId()))
        {
            cardViewTaskComp.setVisibility(View.GONE);
            cardViewTaskReq.setVisibility(View.VISIBLE);
        }

        final ArrayList<Integer> deptList = new ArrayList<>();
        deptList.add(-1);
        getAllEmp(model.getHeaderId());

        cardViewTaskComp.setOnClickListener(this);
        cardViewTaskReq.setOnClickListener(this);

        return view;
    }

    private void getAllEmp(int headerId) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<GroupEmp>> listCall = Constants.myInterface.getChatEmpListByHeader(headerId);
            listCall.enqueue(new Callback<ArrayList<GroupEmp>>() {
                @Override
                public void onResponse(Call<ArrayList<GroupEmp>> call, Response<ArrayList<GroupEmp>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("EMPLOYEE LIST : ", " -----------------------------------EMPLOYEE LIST---------------------------- " + response.body());
                            empList.clear();
                            empList=response.body();

                            Log.e("BIN", "---------------------------------Model-----------------" + employeeList);

                            adapter = new EmployeeListAdapter(empList,getContext(),model,loginUser);
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
                public void onFailure(Call<ArrayList<GroupEmp>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cardViewTaskReq)
        {
            final ChatHeader chatHeader=new ChatHeader(model.getHeaderId(),model.getCreatedDate(),model.getHeaderName(),model.getCreatedUserId(),model.getAdminUserIds(),model.getAssignUserIds(),model.getTaskDesc(),model.getImage(),1,model.getRequestUserId(),model.getTaskCloseUserId(),model.getTaskCompleteRemark(),model.getIsReminderRequired(),model.getReminderFrequency(),model.getLastDate(),model.getIsActive(),model.getDelStatus(),model.getExInt1(),model.getExInt2(),model.getExInt3(),model.getExVar1(),model.getExVar2(),model.getExVar3());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to request for task ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    saveHeader(chatHeader);
                    Log.e("Add Task", "-------------------------------UPDATE TASK REQ----------------------------------" + chatHeader);

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

        }else if(v.getId()==R.id.cardViewTaskComp)
        {
            final ChatHeader chatHeader=new ChatHeader(model.getHeaderId(),model.getCreatedDate(),model.getHeaderName(),model.getCreatedUserId(),model.getAdminUserIds(),model.getAssignUserIds(),model.getTaskDesc(),model.getImage(),2,model.getRequestUserId(),model.getTaskCloseUserId(),model.getTaskCompleteRemark(),model.getIsReminderRequired(),model.getReminderFrequency(),model.getLastDate(),model.getIsActive(),model.getDelStatus(),model.getExInt1(),model.getExInt2(),model.getExInt3(),model.getExVar1(),model.getExVar2(),model.getExVar3());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to complete task ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    saveHeader(chatHeader);
                    Log.e("Add Task", "-------------------------------UPDATE TASK----------------------------------" + chatHeader);

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

    private void saveHeader(ChatHeader chatHeader) {
        Log.e("PARAMETER","---------------------------------------UPDATE STATUS--------------------------"+chatHeader);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChatHeader> listCall = Constants.myInterface.saveChatHeader(chatHeader);
            listCall.enqueue(new Callback<ChatHeader>() {
                @Override
                public void onResponse(Call<ChatHeader> call, Response<ChatHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE STATUS : ", " ------------------------------UPDATE STATUS------------------------ " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new DashboardFragment(), "MainFragment");
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
                public void onFailure(Call<ChatHeader> call, Throwable t) {
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
