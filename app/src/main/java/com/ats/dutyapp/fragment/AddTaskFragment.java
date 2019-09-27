package com.ats.dutyapp.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatHeader;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.GroupList;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment implements View.OnClickListener {
public RadioGroup rg,rgType;
public RadioButton rbIndivsual,rbGroup,rbDaily,rbWeekly,rbNo,rbYes;
public TextView tvEmp,tvEmpId,tvEmpAdmin,tvEmpIdAdmin,tvDept,tvDeptId,tvDate,tvTime,tvEmpLable,tvDeptLable;
public EditText edDate,edRemTime,edDesc,edRemark,edDay,edTaskName;
public View empView,deptView;
public TextInputLayout textInputLayoutDay;
public Button btnSubmit;
public LinearLayout linearLayout;
public Button btn_mon,btn_sun,btn_tue,btn_wed,btn_thu,btn_fri,btn_sat;
String selectedText = "Indivsual",rgTypeText,reminderType;
int rgSelectType;
Login loginUser;

Dialog dialog;
private BroadcastReceiver mBroadcastReceiver;
DepartmentListDialogAdapter deptAdapter;
EmployeeListDialogAdapter empAdapter;
    EmployeeListAdminDialogAdapter empAdminAdapter;
int deptId;
String stringId,stringName,stringIdAdmin,stringNameAdmin;

Boolean isMonPressed = false,isSunPressed = false,isTuePressed = false,isWedPressed = false,isThuPressed = false,isFriPressed = false,isSatPressed = false;

public static ArrayList<Employee> assignStaticTaskEmpList = new ArrayList<>();

long fromDateMillis, toDateMillis;
int yyyy, mm, dd;

    ArrayList<String> deptNameList = new ArrayList<>();
    ArrayList<Integer> deptIdList = new ArrayList<>();
    ArrayList<Department> deptList = new ArrayList<>();

    ArrayList<String> empNameList = new ArrayList<>();
    ArrayList<Integer> empIdList = new ArrayList<>();
    ArrayList<Employee> empList = new ArrayList<>();

    ArrayList<GroupList> grpList =new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_task, container, false);
        getActivity().setTitle("Add Task");
        rg=view.findViewById(R.id.rg);
        rgType=view.findViewById(R.id.rgType);
        rbIndivsual=view.findViewById(R.id.rbIndivsual);
        rbGroup=view.findViewById(R.id.rbGroup);
        rbDaily=view.findViewById(R.id.rbDaily);
        rbWeekly=view.findViewById(R.id.rbWeekly);
        rbNo=view.findViewById(R.id.rbNo);
        rbYes=view.findViewById(R.id.rbYes);
        tvEmp=view.findViewById(R.id.tvEmp);
        tvEmpId=view.findViewById(R.id.tvEmpId);

        tvEmpAdmin=view.findViewById(R.id.tvEmpAdmin);
        tvEmpIdAdmin=view.findViewById(R.id.tvEmpIdAdmin);

        linearLayout=view.findViewById(R.id.linearLayout);
        btn_sun=view.findViewById(R.id.btn_sun);
        btn_mon=view.findViewById(R.id.btn_mon);
        btn_tue=view.findViewById(R.id.btn_tue);
        btn_wed=view.findViewById(R.id.btn_wed);
        btn_thu=view.findViewById(R.id.btn_thu);
        btn_fri=view.findViewById(R.id.btn_fri);
        btn_sat=view.findViewById(R.id.btn_sat);

        tvDept=view.findViewById(R.id.tvDept);
        tvDeptId=view.findViewById(R.id.tvDeptId);
        tvDate=view.findViewById(R.id.tvDate);
        tvTime=view.findViewById(R.id.tvTime);
        tvEmpLable=view.findViewById(R.id.tvEmpLable);
        tvDeptLable=view.findViewById(R.id.tvDeptLable);
        edDate=view.findViewById(R.id.edDate);
        edRemTime=view.findViewById(R.id.edRemTime);
        edDesc=view.findViewById(R.id.edDesc);
        edRemark=view.findViewById(R.id.edRemark);
        edDay=view.findViewById(R.id.edDay);
        edTaskName=view.findViewById(R.id.edTaskName);
        textInputLayoutDay=view.findViewById(R.id.textInputLayoutDay);
        empView=view.findViewById(R.id.view);
        deptView=view.findViewById(R.id.viewDept);
        btnSubmit=view.findViewById(R.id.btnSubmit);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        final ArrayList<Integer> deptList = new ArrayList<>();
        deptList.add(-1);

        Log.e(" Selected Text", "---------------------------------------------" + selectedText);

        //getAllEmp(deptList);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);
                RadioButton r = (RadioButton) group.getChildAt(idx);
                selectedText = r.getText().toString();
                Log.e(" Radio", "----------" + idx);
                Log.e(" Radio Text", "----------" + selectedText);

                if(selectedText.equalsIgnoreCase("Indivsual"))
                {
                    tvEmp.setVisibility(View.VISIBLE);
                    tvEmpLable.setVisibility(View.VISIBLE);
                    empView.setVisibility(View.VISIBLE);

                    tvDept.setVisibility(View.GONE);
                    tvDeptLable.setVisibility(View.GONE);
                    deptView.setVisibility(View.GONE);

                    getAllEmp(deptList,"");

                }else if(selectedText.equalsIgnoreCase("Group"))
                {
                    tvEmp.setVisibility(View.VISIBLE);
                    tvEmpLable.setVisibility(View.VISIBLE);
                    empView.setVisibility(View.VISIBLE);

                    tvDept.setVisibility(View.VISIBLE);
                    tvDeptLable.setVisibility(View.VISIBLE);
                    deptView.setVisibility(View.VISIBLE);

                }
            }
        });

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);
                RadioButton r = (RadioButton) group.getChildAt(idx);
                rgTypeText = r.getText().toString();
                Log.e(" Radio Type", "----------" + idx);
                Log.e(" Radio Type", "----------" + rgTypeText);

                if(rgTypeText.equalsIgnoreCase("Daily"))
                {
                    //edDay.setVisibility(View.GONE);
                   // textInputLayoutDay.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                }else if(rgTypeText.equalsIgnoreCase("Custome"))
                {
                    //edDay.setVisibility(View.VISIBLE);
                    //textInputLayoutDay.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        getAllDept();
        getAllGroup(1);

        rbDaily.setChecked(true);
        rbIndivsual.setChecked(true);
        rbNo.setChecked(true);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(todayDate);

        fromDateMillis = todayDate.getTime();
        toDateMillis = todayDate.getTime();

        edDate.setText(currentDate);

        edDate.setOnClickListener(this);
        edRemTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvEmp.setOnClickListener(this);
        tvEmpAdmin.setOnClickListener(this);
        tvDept.setOnClickListener(this);

        btn_sun.setOnClickListener(this);
        btn_mon.setOnClickListener(this);
        btn_tue.setOnClickListener(this);
        btn_wed.setOnClickListener(this);
        btn_thu.setOnClickListener(this);
        btn_fri.setOnClickListener(this);
        btn_sat.setOnClickListener(this);

        return view;
    }

    private void getAllGroup(int isActive) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<GroupList>> listCall = Constants.myInterface.getAllChatGroupDisplay(isActive);
            listCall.enqueue(new Callback<ArrayList<GroupList>>() {
                @Override
                public void onResponse(Call<ArrayList<GroupList>> call, Response<ArrayList<GroupList>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("GROUP LIST : ", "------------------------------------------------- " + response.body());
                            grpList.clear();
                            grpList = response.body();

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

    private void getAllDept() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Department>> listCall = Constants.myInterface.allEmployeeDepartment();
            listCall.enqueue(new Callback<ArrayList<Department>>() {
                @Override
                public void onResponse(Call<ArrayList<Department>> call, Response<ArrayList<Department>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DEPT LIST : ", " - " + response.body());

                            deptNameList.clear();
                            deptIdList.clear();
                            deptList=response.body();

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
                public void onFailure(Call<ArrayList<Department>> call, Throwable t) {
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
        if(v.getId()==R.id.edDate)
        {
            int yr, mn, dy;

            Calendar purchaseCal;

            long minDate = 0;

            purchaseCal = Calendar.getInstance();
            minDate = purchaseCal.getTime().getTime();
            purchaseCal.setTimeInMillis(fromDateMillis);

            yr = purchaseCal.get(Calendar.YEAR);
            mn = purchaseCal.get(Calendar.MONTH);
            dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), fromDateListener, yr, mn, dy);
            dialog.getDatePicker().setMinDate(minDate);
            dialog.show();
        }else if(v.getId()==R.id.edRemTime)
        {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            final int am_pm = mcurrentTime.get(Calendar.AM_PM);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    Log.e("Selecte Hrs","------------------"+selectedHour);
                    Log.e("Selecte Min","------------------"+selectedMinute);

                    if(selectedHour>12) {
                        edRemTime.setText(String.valueOf(selectedHour-12)+ ":"+(String.valueOf(selectedMinute)+" pm"));
                        //edFrom.setText(selectedHour + ":" + selectedMinute);
                    } else if(selectedHour==12) {
                        edRemTime.setText("12"+ ":"+(String.valueOf(selectedMinute)+" pm"));
                    } else if(selectedHour<12) {
                        if(selectedHour!=0) {
                            edRemTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " am"));
                        } else {
                            edRemTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " am"));
                        }
                    }

                }
            }, hour, minute,false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }else if(v.getId()==R.id.tvEmp)
        {
            showDialog1();
        }else if(v.getId()==R.id.tvEmpAdmin)
        {
            showDialogAdmin();
        }else if(v.getId()==R.id.btn_mon)
        {
            if (!isMonPressed) {
                btn_mon.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isMonPressed = true;
                reminderType="1";
            } else {
                btn_mon.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isMonPressed = false;
            }
        }else if(v.getId()==R.id.btn_sun)
        {
            if (!isSunPressed) {
                btn_sun.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isSunPressed = true;
                reminderType="7";
            } else {
                btn_sun.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isSunPressed = false;
            }
        }else if(v.getId()==R.id.btn_tue)
        {
            if (!isTuePressed) {
                btn_tue.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isTuePressed = true;
                reminderType="2";
            } else {
                btn_tue.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isTuePressed = false;
            }
        }else if(v.getId()==R.id.btn_wed)
        {
            if (!isWedPressed) {
                btn_wed.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isWedPressed = true;
                reminderType="3";
            } else {
                btn_wed.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isWedPressed = false;
            }
        }else if(v.getId()==R.id.btn_thu)
        {
            if (!isThuPressed) {
                btn_thu.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isThuPressed = true;
                reminderType="4";
            } else {
                btn_thu.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isThuPressed = false;
            }
        }else if(v.getId()==R.id.btn_fri)
        {
            if (!isFriPressed) {
                btn_fri.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isFriPressed = true;
                reminderType="5";
            } else {
                btn_fri.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isFriPressed = false;
            }
        }else if(v.getId()==R.id.btn_sat)
        {
            if (!isSatPressed) {
                btn_sat.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isSatPressed = true;
                reminderType="6";
            } else {
                btn_sat.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isSatPressed = false;
            }
        }
        else if(v.getId()==R.id.tvDept)
        {
            showDialog();
        }
        else if(v.getId()==R.id.btnSubmit)
        {
            String strRemidTime,strDesc,strRemark,strDate,strDay,strTaskName,strEmp,strEmpId,strEmpAdmin;
            boolean isValidTime = false,isValidDesc =false,isValidRemark =false,isValidDay =false,isValidTaskName =false,isValidEmp =false,isValidEmpAdmin =false;

            strRemidTime=edRemTime.getText().toString();
            strDesc=edDesc.getText().toString();
            strRemark=edRemark.getText().toString();
            strDate=edDate.getText().toString();
            strDay=edDay.getText().toString();
            strTaskName=edTaskName.getText().toString();
            strEmp=tvEmp.getText().toString();
            strEmpId=tvEmpId.getText().toString();
            strEmpAdmin=tvEmpAdmin.getText().toString();

            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date ToDate = null;
            try {
                ToDate = formatter1.parse(strDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final String DateComp = formatter3.format(ToDate);
            Log.e("Date Completion","--------------------"+DateComp);

            rgSelectType = 0;
            if (rbNo.isChecked()) {
                rgSelectType = 0;
            } else if (rbYes.isChecked()) {
                rgSelectType = 1;
            }

            //reminderType = "1,2,3,4,5,6,7";
            if (rbDaily.isChecked()) {
                reminderType = "1,2,3,4,5,6,7";
            }
           // else if (rbWeekly.isChecked()) {
             //   reminderType = 1;

//                if (strDay.isEmpty()) {
//                    edDay.setError("required");
//                } else {
//                    edDay.setError(null);
//                    isValidDay = true;
//                }
          //  }

            if (strRemidTime.isEmpty()) {
                edRemTime.setError("required");
            } else {
                edRemTime.setError(null);
                isValidTime = true;
            }

            if (strTaskName.isEmpty()) {
                edTaskName.setError("required");
            } else {
                edTaskName.setError(null);
                isValidTaskName = true;
            }

            if (strEmp.isEmpty()) {
                tvEmp.setError("required");
            } else {
                tvEmp.setError(null);
                isValidEmp = true;
            }

            if (strEmpAdmin.isEmpty()) {
                tvEmpAdmin.setError("required");
            } else {
                tvEmpAdmin.setError(null);
                isValidEmpAdmin = true;
            }

            if (strDesc.isEmpty()) {
                edDesc.setError("required");
            } else {
                edDesc.setError(null);
                isValidDesc = true;
            }

            if (strRemark.isEmpty()) {
                edRemark.setError("required");
            } else {
                edRemark.setError(null);
                isValidRemark = true;
            }

            if(isValidTime && isValidDesc && isValidRemark && isValidTaskName && isValidEmp && isValidEmpAdmin)
            {
                Log.e("Successfully","------------------");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final ChatHeader chatHeader=new ChatHeader(0, sdf.format(System.currentTimeMillis()),strTaskName,loginUser.getEmpId(),stringIdAdmin,strEmpId,strDesc,"",0,0,0,strRemark,rgSelectType,reminderType,DateComp,1,1,0,0,0,strRemidTime,"","");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to add task ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        saveTask(chatHeader);
                        Log.e("Add Task", "-------------------------------SAVE----------------------------------" + chatHeader);

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
    }

    private void saveTask(ChatHeader chatHeader) {
        Log.e("PARAMETER","---------------------------------------ADD TASK--------------------------"+chatHeader);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChatHeader> listCall = Constants.myInterface.saveChatHeader(chatHeader);
            listCall.enqueue(new Callback<ChatHeader>() {
                @Override
                public void onResponse(Call<ChatHeader> call, Response<ChatHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ADD TASK LIST : ", " ------------------------------SAVE ADD TASK------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new TaskListFragment(), "MainFragment");
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

    private void showDialogAdmin() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search_button, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);
        Button btnSubmit=dialog.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ArrayList<Employee> assignedArray = new ArrayList<>();
                final ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
                final ArrayList<String> assignedEmpNameArray = new ArrayList<>();
                if (assignStaticTaskEmpList != null) {
                    if (assignStaticTaskEmpList.size() > 0) {
                        assignedArray.clear();
                        for (int i = 0; i < assignStaticTaskEmpList.size(); i++) {
                            if (assignStaticTaskEmpList.get(i).isChecked()) {
                                assignedArray.add(assignStaticTaskEmpList.get(i));
                                assignedEmpIdArray.add(assignStaticTaskEmpList.get(i).getEmpId());
                                assignedEmpNameArray.add(assignStaticTaskEmpList.get(i).getEmpFname()+" " +assignStaticTaskEmpList.get(i).getEmpMname()+" " +assignStaticTaskEmpList.get(i).getEmpSname());

                            }
                        }
                    }
                    Log.e("ASSIGN EMP", "---------------------------------" + assignedArray);
                    Log.e("ASSIGN EMP SIZE", "---------------------------------" + assignedArray.size());
                    Log.e("ASSIGN EMP ID", "---------------------------------" + assignedEmpIdArray);
                    Log.e("ASSIGN EMP Name", "---------------------------------" + assignedEmpNameArray);

                    String empIds=assignedEmpIdArray.toString().trim();
                    Log.e("ASSIGN EMP ID","---------------------------------"+empIds);

                    String a1 = ""+empIds.substring(1, empIds.length()-1).replace("][", ",")+"";
                    stringIdAdmin = a1.replaceAll("\\s","");

                    Log.e("ASSIGN EMP ID STRING","---------------------------------"+stringIdAdmin);
                    Log.e("ASSIGN EMP ID STRING1","---------------------------------"+a1);

                    String empName=assignedEmpNameArray.toString().trim();
                    Log.e("ASSIGN EMP NAME","---------------------------------"+empName);

                    stringNameAdmin = ""+empName.substring(1, empName.length()-1).replace("][", ",")+"";

                    // stringName = a.replaceAll("\\s","");

                    Log.e("ASSIGN EMP NAME STRING","---------------------------------"+stringNameAdmin);
                    //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);

                    tvEmpAdmin.setText(""+stringNameAdmin);
                    tvEmpIdAdmin.setText(""+stringIdAdmin);
                }
            }
        });

        empAdminAdapter = new EmployeeListAdminDialogAdapter(empList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(empAdminAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (empAdminAdapter != null) {
                        filterEmpAdmin(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterEmpAdmin(String text) {
        ArrayList<Employee> temp = new ArrayList();
        for (Employee d : empList) {
            if (d.getEmpFname().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        empAdminAdapter.updateListAdmin(temp);
    }

    private void showDialog1() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search_button, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);
        Button btnSubmit=dialog.findViewById(R.id.btnSubmit);

        if(selectedText.equalsIgnoreCase("Indivsual"))
        {
            btnSubmit.setVisibility(View.GONE);
        }else if(selectedText.equalsIgnoreCase("Group"))
        {
            btnSubmit.setVisibility(View.VISIBLE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ArrayList<Employee> assignedArray = new ArrayList<>();
                final ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
                final ArrayList<String> assignedEmpNameArray = new ArrayList<>();
                if (assignStaticTaskEmpList != null) {
                    if (assignStaticTaskEmpList.size() > 0) {
                        assignedArray.clear();
                        for (int i = 0; i < assignStaticTaskEmpList.size(); i++) {
                            if (assignStaticTaskEmpList.get(i).isChecked()) {
                                assignedArray.add(assignStaticTaskEmpList.get(i));
                                assignedEmpIdArray.add(assignStaticTaskEmpList.get(i).getEmpId());
                                assignedEmpNameArray.add(assignStaticTaskEmpList.get(i).getEmpFname()+" " +assignStaticTaskEmpList.get(i).getEmpMname()+" " +assignStaticTaskEmpList.get(i).getEmpSname());

                            }
                        }
                    }
                    Log.e("ASSIGN EMP", "---------------------------------" + assignedArray);
                    Log.e("ASSIGN EMP SIZE", "---------------------------------" + assignedArray.size());
                    Log.e("ASSIGN EMP ID", "---------------------------------" + assignedEmpIdArray);
                    Log.e("ASSIGN EMP Name", "---------------------------------" + assignedEmpNameArray);

                    String empIds=assignedEmpIdArray.toString().trim();
                    Log.e("ASSIGN EMP ID","---------------------------------"+empIds);

                    String a1 = ""+empIds.substring(1, empIds.length()-1).replace("][", ",")+"";
                    stringId = a1.replaceAll("\\s","");

                    Log.e("ASSIGN EMP ID STRING","---------------------------------"+stringId);
                    Log.e("ASSIGN EMP ID STRING1","---------------------------------"+a1);

                    String empName=assignedEmpNameArray.toString().trim();
                    Log.e("ASSIGN EMP NAME","---------------------------------"+empName);

                    stringName = ""+empName.substring(1, empName.length()-1).replace("][", ",")+"";

                    // stringName = a.replaceAll("\\s","");

                    Log.e("ASSIGN EMP NAME STRING","---------------------------------"+stringName);
                    //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);

                    tvEmp.setText(""+stringName);
                    tvEmpId.setText(""+stringId);
                }
            }
        });

        empAdapter = new EmployeeListDialogAdapter(empList, getContext(),selectedText);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(empAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (empAdapter != null) {
                        filterEmp(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterEmp(String text) {
        ArrayList<Employee> temp = new ArrayList();
        for (Employee d : empList) {
            if (d.getEmpFname().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        empAdapter.updateList(temp);
    }


    private void showDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        deptAdapter = new DepartmentListDialogAdapter(grpList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(deptAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (deptAdapter != null) {
                        filterDept(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterDept(String text) {
        ArrayList<GroupList> temp = new ArrayList();
        for (GroupList d : grpList) {
            if (d.getGroupName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        deptAdapter.updateList(temp);
    }


    public class DepartmentListDialogAdapter extends RecyclerView.Adapter<DepartmentListDialogAdapter.MyViewHolder> {

        private ArrayList<GroupList> custList;
        //Department
        private Context context;

        public DepartmentListDialogAdapter(ArrayList<GroupList> custList, Context context) {
            this.custList = custList;
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvAddress;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvName);
                tvAddress = view.findViewById(R.id.tvAddress);
                linearLayout = view.findViewById(R.id.linearLayout);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_department_dialog, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final GroupList model = custList.get(i);

            myViewHolder.tvName.setText(model.getGroupName());
            //holder.tvAddress.setText(model.getCustAddress());

            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        Intent customerDataIntent = new Intent();
//                        customerDataIntent.setAction("CUSTOMER_DATA");
//                        customerDataIntent.putExtra("name", model.getEmpDeptName());
//                        customerDataIntent.putExtra("id", model.getEmpDeptId());
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);
                    dialog.dismiss();
                    tvDept.setText(""+model.getGroupName());
                    tvDeptId.setText(""+model.getGroupId());
//                    deptId= Integer.parseInt(tvDeptId.getText().toString());
//                    Log.e("Department id","--------------------------------------------"+deptId);
                    final ArrayList<Integer> deptList = new ArrayList<>();
                    deptList.add(model.getExInt1());
                    getAllEmp(deptList,model.getUserIds());
                }
            });
        }



        @Override
        public int getItemCount() {
            return custList.size();
        }

        public void updateList(ArrayList<GroupList> list) {
            custList = list;
            notifyDataSetChanged();
        }

    }

    public class EmployeeListDialogAdapter extends RecyclerView.Adapter<EmployeeListDialogAdapter.MyViewHolder> {

        private ArrayList<Employee> empList;
        private Context context;
        private String selectedText;

        public EmployeeListDialogAdapter(ArrayList<Employee> empList, Context context,String selectedText) {
            this.empList = empList;
            this.context = context;
            this.selectedText = selectedText;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvAddress;
            public CheckBox checkBox;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvName);
                tvAddress = view.findViewById(R.id.tvAddress);
                checkBox = view.findViewById(R.id.checkBox);
                linearLayout = view.findViewById(R.id.linearLayout);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_employee_dialog_group_list, viewGroup, false);
            //adapter_department_dialog
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final Employee model = empList.get(i);
            final int pos = i;
            myViewHolder.tvName.setText(model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
            //holder.tvAddress.setText(model.getCustAddress());

            Log.e("Adapter","---------------------------selected text----------------------------"+selectedText);
            if(selectedText.equalsIgnoreCase("Indivsual"))
            {
                myViewHolder.checkBox.setVisibility(View.GONE);
            }else if(selectedText.equalsIgnoreCase("Group"))
            {
                myViewHolder.checkBox.setVisibility(View.VISIBLE);
            }

            myViewHolder.checkBox.setChecked(empList.get(i).isChecked());

            myViewHolder.checkBox.setTag(empList.get(i));

            myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Employee employee = (Employee) cb.getTag();

                    employee.setChecked(cb.isChecked());
                    empList.get(pos).setChecked(cb.isChecked());

                }
            });

            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        Intent customerDataIntent = new Intent();
//                        customerDataIntent.setAction("CUSTOMER_DATA");
//                        customerDataIntent.putExtra("name", model.getEmpDeptName());
//                        customerDataIntent.putExtra("id", model.getEmpDeptId());
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);
                    dialog.dismiss();
                    tvEmp.setText(""+model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
                    tvEmpId.setText(""+model.getEmpId());

                }
            });
        }



        @Override
        public int getItemCount() {
            return empList.size();
        }

        public void updateList(ArrayList<Employee> list) {
            empList = list;
            notifyDataSetChanged();
        }

    }

    public class EmployeeListAdminDialogAdapter extends RecyclerView.Adapter<EmployeeListAdminDialogAdapter.MyViewHolder> {

        private ArrayList<Employee> empList;
        private Context context;

        public EmployeeListAdminDialogAdapter(ArrayList<Employee> empList, Context context) {
            this.empList = empList;
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvAddress;
            public CheckBox checkBox;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvName);
                tvAddress = view.findViewById(R.id.tvAddress);
                checkBox = view.findViewById(R.id.checkBox);
                linearLayout = view.findViewById(R.id.linearLayout);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_employee_dialog_group_list, viewGroup, false);
            //adapter_department_dialog
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final Employee model = empList.get(i);
            final int pos = i;
            myViewHolder.tvName.setText(model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
            //holder.tvAddress.setText(model.getCustAddress());


            myViewHolder.checkBox.setChecked(empList.get(i).isChecked());

            myViewHolder.checkBox.setTag(empList.get(i));

            myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Employee employee = (Employee) cb.getTag();

                    employee.setChecked(cb.isChecked());
                    empList.get(pos).setChecked(cb.isChecked());

                }
            });

//            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                        Intent customerDataIntent = new Intent();
////                        customerDataIntent.setAction("CUSTOMER_DATA");
////                        customerDataIntent.putExtra("name", model.getEmpDeptName());
////                        customerDataIntent.putExtra("id", model.getEmpDeptId());
////                        LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);
//                    dialog.dismiss();
//                    tvEmp.setText(""+model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
//                    tvEmpId.setText(""+model.getEmpId());
//
//                }
//            });
        }



        @Override
        public int getItemCount() {
            return empList.size();
        }

        public void updateListAdmin(ArrayList<Employee> list) {
            empList = list;
            notifyDataSetChanged();
        }

    }

    private void getAllEmp(ArrayList<Integer> deptId, final String userIds) {
        Log.e("PARAMETER","             DEPT ID       "+deptId+"                     USER ID     "+userIds);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Employee>> listCall = Constants.myInterface.allEmployeesByDept(deptId);
            listCall.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("EMPLOYEE LIST : ", " -----------------------------------EMPLOYEE LIST---------------------------- " + response.body());
//                            empNameList.clear();
//                            empIdList.clear();
                            empList.clear();
                            empList=response.body();

                            assignStaticTaskEmpList.clear();
                            assignStaticTaskEmpList=empList;

//                            String strEmpId="";
//                            if (model != null) {
//                                strEmpId = model.getUserIds();
//                            }

                            List<String> list = Arrays.asList(userIds.split("\\s*,\\s*"));

                            Log.e("LIST", "----------------------" + list);


                            Log.e("BIN", "---------------------------------Model-----------------" + empList);
                            for (int j = 0; j < assignStaticTaskEmpList.size(); j++) {

                                for (int k = 0; k < list.size(); k++) {

                                    if (assignStaticTaskEmpList.get(j).getEmpId() == Integer.parseInt(list.get(k))) {

                                        assignStaticTaskEmpList.get(j).setChecked(true);
                                        // assignStaticList.add(empList.get(j));

                                    }
                                }
                            }

//                            empNameList.add("");
//                            empIdList.add(0);
//
//                            if (response.body().size() > 0) {
//                                for (int i = 0; i < response.body().size(); i++) {
//
//                                    if (response.body().get(i).getEmpDeptId() == deptId) {
//
//                                        // empIdList.add(response.body().get(i).getEmpDeptId());
//                                        // empNameList.add(response.body().get(i).getEmpFname() + " " + response.body().get(i).getEmpMname() + " " + response.body().get(i).getEmpSname());
//
//                                        Emp employee = new Emp(response.body().get(i).getEmpId(),response.body().get(i).getEmpDsc(),response.body().get(i).getEmpCode(),response.body().get(i).getCompanyId(),response.body().get(i).getEmpCatId(),response.body().get(i).getEmpTypeId(),response.body().get(i).getEmpDeptId(),response.body().get(i).getLocId(),response.body().get(i).getEmpFname(),response.body().get(i).getEmpMname(),response.body().get(i).getEmpSname(),response.body().get(i).getEmpPhoto(),response.body().get(i).getEmpMobile1(),response.body().get(i).getEmpMobile2(),response.body().get(i).getEmpEmail(),response.body().get(i).getEmpAddressTemp(),response.body().get(i).getEmpAddressPerm(),response.body().get(i).getEmpBloodgrp(),response.body().get(i).getEmpEmergencyPerson1(),response.body().get(i).getEmpEmergencyNo1(),response.body().get(i).getEmpEmergencyPerson2(),response.body().get(i).getEmpEmergencyNo2(),response.body().get(i).getEmpRatePerhr(),response.body().get(i).getEmpJoiningDate(),response.body().get(i).getEmpPrevExpYrs(),response.body().get(i).getEmpPrevExpMonths(),response.body().get(i).getEmpLeavingDate(),response.body().get(i).getEmpLeavingReason(),response.body().get(i).getLockPeriod(),response.body().get(i).getTermConditions(),response.body().get(i).getSalaryId(),response.body().get(i).getDelStatus(),response.body().get(i).getIsActive(),response.body().get(i).getMakerUserId(),response.body().get(i).getMakerEnterDatetime(),response.body().get(i).getExInt1(),response.body().get(i).getExInt2(),response.body().get(i).getExInt3(),response.body().get(i).getExVar1(),response.body().get(i).getExVar2(),response.body().get(i).getExVar3());
//                                        empList.add(employee);
//                                    }
//                                }
//                            }
//
//                                    Log.e("EMP NAME","-------------------------------------------"+empNameList);
//                                    Log.e("EMP ID","-------------------------------------------"+empIdList);
//
//                                    ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, empNameList);
//                                    spEmp.setAdapter(projectAdapter);
//
//                                }

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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDate.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -7);
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();


        }
    };
}
