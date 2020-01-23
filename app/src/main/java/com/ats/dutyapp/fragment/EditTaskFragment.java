package com.ats.dutyapp.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.dutyapp.BuildConfig;
import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatHeader;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.DatewiseModel;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.GroupList;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.ats.dutyapp.utils.PermissionsUtil;
import com.ats.dutyapp.utils.RealPathUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTaskFragment extends Fragment implements View.OnClickListener {
    public RadioGroup rg, rgType, rgRepotType;
    public RadioButton rbDaily, rbWeekly, rbNo, rbYes,rbDatewise;
    public TextView tvGroupId,tvEmp, tvEmpId, tvEmpAdmin, tvEmpIdAdmin, tvDept, tvDeptId, tvDate, tvTime, tvEmpLable, tvDeptLable, tvEmpAdminLable;
    public EditText edDate, edRemTime, edDesc, edRemark, edDay, edTaskName,edDatewise;
    public View empView, deptView, viewAdmin;
    public TextInputLayout textInputLayoutDay;
    public Button btnSubmit;
    public LinearLayout linearLayout,llDatewise;
    public Button btn_mon, btn_sun, btn_tue, btn_wed, btn_thu, btn_fri, btn_sat;
    String selectedText = "Indivsual", rgTypeText, reminderType;
    int rgSelectType,remTypeValue;
    Login loginUser;
    ChatTask model;

    private ImageView ivCamera1, ivPhoto1;
    private TextView tvPhoto1;

    Dialog dialog;
    private BroadcastReceiver mBroadcastReceiver;
    DepartmentListDialogAdapter deptAdapter;
    EmployeeListDialogAdapter empAdapter;
    EmployeeListAdminDialogAdapter empAdminAdapter;
    int deptId;
    String stringId, stringName, stringIdAdmin, stringNameAdmin;

    Boolean isMonPressed = false, isSunPressed = false, isTuePressed = false, isWedPressed = false, isThuPressed = false, isFriPressed = false, isSatPressed = false;

    public static ArrayList<Employee> assignStaticTaskList = new ArrayList<>();
    public static ArrayList<Employee> assignStaticTaskAdminEmpList = new ArrayList<>();

    public static ArrayList<Employee> assignStaticTaskEmpList = new ArrayList<>();


    ArrayList<Employee> empListAdmin = new ArrayList<>();

    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;

    ArrayList<String> deptNameList = new ArrayList<>();
    ArrayList<Integer> deptIdList = new ArrayList<>();
    ArrayList<Department> deptList = new ArrayList<>();

    ArrayList<String> empNameList = new ArrayList<>();
    ArrayList<String> empNameList1 = new ArrayList<>();
    ArrayList<Integer> empIdList = new ArrayList<>();
    ArrayList<Employee> empList = new ArrayList<>();

    ArrayList<GroupList> grpList = new ArrayList<>();

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);
    File f;

    Bitmap myBitmap1 = null;
    public static String path1, imagePath1 = null;

    private List<String> notifyDaysArray = new ArrayList<>();

    static  ArrayList<DatewiseModel> staticDateList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        getActivity().setTitle("Edit Task");
        rg = view.findViewById(R.id.rg);
        rgType = view.findViewById(R.id.rgType);
        rgRepotType = view.findViewById(R.id.rgRepotType);
        rbDaily = view.findViewById(R.id.rbDaily);
        rbWeekly = view.findViewById(R.id.rbWeekly);
        rbNo = view.findViewById(R.id.rbNo);
        rbYes = view.findViewById(R.id.rbYes);
        tvEmp = view.findViewById(R.id.tvEmp);
        tvEmpId = view.findViewById(R.id.tvEmpId);
        tvGroupId = view.findViewById(R.id.tvGroupId);


        tvEmpAdmin = view.findViewById(R.id.tvEmpAdmin);
        tvEmpIdAdmin = view.findViewById(R.id.tvEmpIdAdmin);
        tvEmpAdminLable = view.findViewById(R.id.tvEmpAdminLable);
        viewAdmin = view.findViewById(R.id.viewAdmin);

        linearLayout = view.findViewById(R.id.linearLayout);
        btn_sun = view.findViewById(R.id.btn_sun);
        btn_mon = view.findViewById(R.id.btn_mon);
        btn_tue = view.findViewById(R.id.btn_tue);
        btn_wed = view.findViewById(R.id.btn_wed);
        btn_thu = view.findViewById(R.id.btn_thu);
        btn_fri = view.findViewById(R.id.btn_fri);
        btn_sat = view.findViewById(R.id.btn_sat);

        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);
        tvEmpLable = view.findViewById(R.id.tvEmpLable);
        edDate = view.findViewById(R.id.edDate);
        edRemTime = view.findViewById(R.id.edRemTime);
        edDesc = view.findViewById(R.id.edDesc);
        edRemark = view.findViewById(R.id.edRemark);
        edDay = view.findViewById(R.id.edDay);
        edTaskName = view.findViewById(R.id.edTaskName);
        textInputLayoutDay = view.findViewById(R.id.textInputLayoutDay);
        empView = view.findViewById(R.id.view);
        deptView = view.findViewById(R.id.viewDept);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        ivCamera1 = view.findViewById(R.id.ivCamera1);
        ivPhoto1 = view.findViewById(R.id.ivPhoto1);
        tvPhoto1 = view.findViewById(R.id.tvPhoto1);

        if (PermissionsUtil.checkAndRequestPermissions(getActivity())) {
        }

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, ChatTask.class);

            Log.e("Model", "-------------------------------------------------" + model);

            tvGroupId.setText(""+model.getExInt1());
            edTaskName.setText("" + model.getHeaderName());
            edDate.setText("" + model.getLastDate());
            edRemTime.setText("" + model.getExVar1());
            edDesc.setText("" + model.getTaskDesc());
            edRemark.setText("" + model.getTaskCompleteRemark());
            tvEmpIdAdmin.setText("" + model.getAdminUserIds());
            tvEmpAdmin.setText("" + model.getAdminUserNames());
            tvEmpId.setText("" + model.getAssignUserIds());
            tvEmp.setText("" + model.getAssignUserNames());
            reminderType = model.getReminderFrequency();

            try {
                final String image = Constants.CHAT_IMAGE_URL + model.getImage();

                Picasso.with(getContext())
                        .load(image)
                        .placeholder(getContext().getResources().getDrawable(R.drawable.progress_animation))
                        .error(getContext().getResources().getDrawable(R.drawable.progress_animation))
                        .resize(100, 100)
                        .into(ivPhoto1);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("Frquency", "---------------------------------" + reminderType);

            String strFrq = "";
            if (model != null) {
                strFrq = model.getReminderFrequency();

                if (!strFrq.isEmpty()) {
                    String str = strFrq.replaceAll("\\s", "");

                    List<String> temp = Arrays.asList(str.split("\\s*,\\s*"));
                    notifyDaysArray.addAll(temp);
                }
            }

            Log.e("List", "-------------------------------------" + notifyDaysArray);

            for (int i = 0; i < notifyDaysArray.size(); i++) {
                if (notifyDaysArray.get(i).equalsIgnoreCase("1")) {
                    btn_mon.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                } else if (notifyDaysArray.get(i).equalsIgnoreCase("2")) {
                    btn_tue.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                } else if (notifyDaysArray.get(i).equalsIgnoreCase("3")) {
                    btn_wed.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                } else if (notifyDaysArray.get(i).equalsIgnoreCase("4")) {
                    btn_thu.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                } else if (notifyDaysArray.get(i).equalsIgnoreCase("5")) {
                    btn_fri.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                } else if (notifyDaysArray.get(i).equalsIgnoreCase("6")) {
                    btn_sat.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                } else if (notifyDaysArray.get(i).equalsIgnoreCase("7")) {
                    btn_sun.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                }
            }

            if (model.getIsReminderRequired() == 0) {
                rbNo.setChecked(true);
                tvEmpAdmin.setVisibility(View.GONE);
                viewAdmin.setVisibility(View.GONE);
                tvEmpAdminLable.setVisibility(View.GONE);

            } else if (model.getIsReminderRequired() == 1) {
                rbYes.setChecked(true);
                tvEmpAdmin.setVisibility(View.VISIBLE);
                viewAdmin.setVisibility(View.VISIBLE);
                tvEmpAdminLable.setVisibility(View.VISIBLE);
            }

            if (model.getExInt2()==0){
                rbDaily.setChecked(true);
                linearLayout.setVisibility(View.GONE);
                llDatewise.setVisibility(View.GONE);
            }else if (model.getExInt2()==1){
                rbWeekly.setChecked(true);
                linearLayout.setVisibility(View.VISIBLE);
                llDatewise.setVisibility(View.GONE);
            }else if (model.getExInt2()==2){
                rbDatewise.setChecked(true);
                linearLayout.setVisibility(View.GONE);
                llDatewise.setVisibility(View.VISIBLE);
            }

           /* if (model.getReminderFrequency().equals("1,2,3,4,5,6,7")) {
                rbDaily.setChecked(true);
                linearLayout.setVisibility(View.GONE);
            } else {
                rbWeekly.setChecked(true);
                linearLayout.setVisibility(View.VISIBLE);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }


        rgRepotType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);
                RadioButton r = (RadioButton) group.getChildAt(idx);
                selectedText = r.getText().toString();
                Log.e(" Radio", "----------" + idx);
                Log.e(" Radio Text", "----------" + selectedText);

                if (selectedText.equalsIgnoreCase("No")) {
                    tvEmpAdmin.setVisibility(View.GONE);
                    tvEmpIdAdmin.setVisibility(View.GONE);
                    viewAdmin.setVisibility(View.GONE);
                    tvEmpAdminLable.setVisibility(View.GONE);

                } else if (selectedText.equalsIgnoreCase("Yes")) {
                    tvEmpAdmin.setVisibility(View.VISIBLE);
                    viewAdmin.setVisibility(View.VISIBLE);
                    tvEmpAdminLable.setVisibility(View.VISIBLE);

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

                if (rgTypeText.equalsIgnoreCase("Daily")) {
                    //edDay.setVisibility(View.GONE);
                    // textInputLayoutDay.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                } else if (rgTypeText.equalsIgnoreCase("Custom")) {
                    //edDay.setVisibility(View.VISIBLE);
                    //textInputLayoutDay.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        final ArrayList<Integer> deptList = new ArrayList<>();
        deptList.add(-1);

        Log.e(" Selected Text", "---------------------------------------------" + selectedText);

        getAllEmp(deptList, model.getAssignUserIds());
        getAllDept();
        getAllEmployee();
        //  getAllGroup(1);

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
        ivCamera1.setOnClickListener(this);

        btn_sun.setOnClickListener(this);
        btn_mon.setOnClickListener(this);
        btn_tue.setOnClickListener(this);
        btn_wed.setOnClickListener(this);
        btn_thu.setOnClickListener(this);
        btn_fri.setOnClickListener(this);
        btn_sat.setOnClickListener(this);

        createFolder();

        return view;
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private void getAllEmployee() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            final ArrayList<Integer> deptList = new ArrayList<>();
            deptList.add(-1);

            Call<ArrayList<Employee>> listCall = Constants.myInterface.allEmployeesByDept(deptList);
            listCall.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("EMPLOYEE LIST : ", " -----------------------------------EMPLOYEE LIST---------------------------- " + response.body());

                            empListAdmin.clear();
                            empListAdmin = response.body();

                            assignStaticTaskAdminEmpList.clear();
                            assignStaticTaskAdminEmpList = empListAdmin;

                            if (model == null) {

                                for (int i = 0; i < empListAdmin.size(); i++) {
                                    if (loginUser.getEmpId().equals(empListAdmin.get(i).getEmpId())) {
                                        tvEmpAdmin.setText(empListAdmin.get(i).getEmpFname() + " " + empListAdmin.get(i).getEmpMname() + " " + empListAdmin.get(i).getEmpSname());
                                        tvEmpIdAdmin.setText("" + empListAdmin.get(i).getEmpId());
                                    }
                                }
                            } else {

                                String strEmpId = "";
                                if (model != null) {
                                    strEmpId = model.getAdminUserIds();
                                }

                                List<String> list = Arrays.asList(strEmpId.split("\\s*,\\s*"));

                                Log.e("LIST", "----------------------" + list);


                                Log.e("BIN", "---------------------------------Model-----------------" + empListAdmin);
                                for (int j = 0; j < assignStaticTaskAdminEmpList.size(); j++) {

                                    for (int k = 0; k < list.size(); k++) {

                                        if (assignStaticTaskAdminEmpList.get(j).getEmpId() == Integer.parseInt(list.get(k))) {

                                            assignStaticTaskAdminEmpList.get(j).setChecked(true);

                                        }
                                    }
                                }

                            }
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
                            deptList = response.body();

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
        if (v.getId() == R.id.edDate) {
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
        } else if (v.getId() == R.id.edRemTime) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            final int am_pm = mcurrentTime.get(Calendar.AM_PM);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    Log.e("Selecte Hrs", "------------------" + selectedHour);
                    Log.e("Selecte Min", "------------------" + selectedMinute);

                    if (selectedHour > 12) {
                        edRemTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " pm"));
                        //edFrom.setText(selectedHour + ":" + selectedMinute);
                    } else if (selectedHour == 12) {
                        edRemTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " pm"));
                    } else if (selectedHour < 12) {
                        if (selectedHour != 0) {
                            edRemTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " am"));
                        } else {
                            edRemTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " am"));
                        }
                    }

                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } else if (v.getId() == R.id.tvEmp) {
            showDialog1();
        } else if (v.getId() == R.id.tvEmpAdmin) {
            showDialogAdmin();
        } else if (v.getId() == R.id.btn_mon) {
            if (!isMonPressed) {
                btn_mon.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isMonPressed = true;
                notifyDaysArray.remove("1");
                notifyDaysArray.add("1");
            } else {
                btn_mon.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isMonPressed = false;
                notifyDaysArray.remove("1");
            }
        } else if (v.getId() == R.id.btn_sun) {
            if (!isSunPressed) {
                btn_sun.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isSunPressed = true;
                Log.e("NOTIFY LIST", "------------------------- " + notifyDaysArray);
                notifyDaysArray.remove("7");
                notifyDaysArray.add("7");
            } else {
                btn_sun.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isSunPressed = false;
                notifyDaysArray.remove("7");
            }
        } else if (v.getId() == R.id.btn_tue) {
            if (!isTuePressed) {
                btn_tue.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isTuePressed = true;
                notifyDaysArray.remove("2");
                notifyDaysArray.add("2");
            } else {
                btn_tue.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isTuePressed = false;
                notifyDaysArray.remove("2");
            }
        } else if (v.getId() == R.id.btn_wed) {
            if (!isWedPressed) {
                btn_wed.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isWedPressed = true;
                notifyDaysArray.remove("3");
                notifyDaysArray.add("3");
            } else {
                btn_wed.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isWedPressed = false;
                notifyDaysArray.remove("3");
            }
        } else if (v.getId() == R.id.btn_thu) {
            if (!isThuPressed) {
                btn_thu.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isThuPressed = true;
                notifyDaysArray.remove("4");
                notifyDaysArray.add("4");
            } else {
                btn_thu.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isThuPressed = false;
                notifyDaysArray.remove("4");
            }
        } else if (v.getId() == R.id.btn_fri) {
            if (!isFriPressed) {
                btn_fri.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isFriPressed = true;
                notifyDaysArray.remove("5");
                notifyDaysArray.add("5");
            } else {
                btn_fri.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isFriPressed = false;
                notifyDaysArray.remove("5");
            }
        } else if (v.getId() == R.id.btn_sat) {
            if (!isSatPressed) {
                btn_sat.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap_primary));
                isSatPressed = true;
                notifyDaysArray.remove("6");
                notifyDaysArray.add("6");
            } else {
                btn_sat.setBackground(getActivity().getResources().getDrawable(R.drawable.circle_shap));
                isSatPressed = false;
                notifyDaysArray.remove("6");
            }
        }
//        else if(v.getId()==R.id.tvDept)
//        {
//            showDialog();
//        }
        else if (v.getId() == R.id.ivCamera1) {

            showCameraDialog("Photo1");

        } else if (v.getId() == R.id.btnSubmit) {
            String strRemidTime, strDesc, strRemark, strDate, strDay, strTaskName, strEmp, strEmpId, strEmpAdmin;
            boolean isValidTime = false, isValidDesc = false, isValidRemark = false, isValidDay = false, isValidTaskName = false, isValidEmp = false, isValidEmpAdmin = false;

            int groupId=0;

            groupId= Integer.parseInt(tvGroupId.getText().toString().trim());

            strRemidTime = edRemTime.getText().toString();
            strDesc = edDesc.getText().toString();
            strRemark = edRemark.getText().toString();
            strDate = edDate.getText().toString();
            strDay = edDay.getText().toString();
            strTaskName = edTaskName.getText().toString();
            strEmp = tvEmp.getText().toString();
            strEmpId = tvEmpId.getText().toString();
            strEmpAdmin = tvEmpAdmin.getText().toString();
            stringIdAdmin = tvEmpIdAdmin.getText().toString();

            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date ToDate = null;
            try {
                ToDate = formatter1.parse(strDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final String DateComp = formatter3.format(ToDate);
            Log.e("Date Completion", "--------------------" + DateComp);

            rgSelectType = 0;
            if (rbNo.isChecked()) {
                rgSelectType = 0;
            } else if (rbYes.isChecked()) {
                rgSelectType = 1;
            }


            remTypeValue = 0;
            if (rbDaily.isChecked()) {
                remTypeValue = 0;
                reminderType = "1,2,3,4,5,6,7";
            } else if (rbWeekly.isChecked()) {
                remTypeValue = 1;

                if (notifyDaysArray.isEmpty()){
                    reminderType="";
                }else {
                    Set<String> s = new LinkedHashSet<String>(notifyDaysArray);
                    notifyDaysArray.clear();
                    notifyDaysArray.addAll(s);
                    Collections.sort(notifyDaysArray);
                    reminderType = notifyDaysArray.toString().trim().substring(1, (notifyDaysArray.toString().length() - 1)).replaceAll("\\s+","");
                }
                Log.e("ARRAY ","------------------------------ "+reminderType.replaceAll("\\s+",""));

            } else if (rbDatewise.isChecked()) {
                remTypeValue = 2;

                reminderType=edDatewise.getText().toString().trim();

            }



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

           /* if (strDesc.isEmpty()) {
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
            }*/

            // if (isValidTime && isValidDesc && isValidRemark && isValidTaskName && isValidEmp && isValidEmpAdmin) {
            if (isValidTime && isValidTaskName && isValidEmp) {
                Log.e("Successfully", "------------------");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final ChatHeader chatHeader = new ChatHeader(model.getHeaderId(), model.getCreatedDate(), strTaskName, model.getCreatedUserId(), stringIdAdmin, strEmpId, strDesc, model.getImage(), model.getStatus(), model.getRequestUserId(), model.getTaskCloseUserId(), strRemark, rgSelectType, reminderType, DateComp, model.getIsActive(), model.getDelStatus(), groupId,remTypeValue, model.getExInt3(), strRemidTime, model.getExVar2(), model.getExVar3());

                if (imagePath1 == null) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit task?");
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

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit task?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ArrayList<String> pathArray = new ArrayList<>();
                            ArrayList<String> fileNameArray = new ArrayList<>();

                            String photo1 = "";

                            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            if (imagePath1 != null) {

                                pathArray.add(imagePath1);

                                File imgFile1 = new File(imagePath1);
                                int pos = imgFile1.getName().lastIndexOf(".");
                                String ext = imgFile1.getName().substring(pos + 1);
                                photo1 = Calendar.getInstance().getTimeInMillis() + "_ChatHead." + ext;
                                fileNameArray.add(photo1);
                            }

                            chatHeader.setImage(photo1);
                            sendImage(pathArray, fileNameArray, chatHeader);

                            //  saveTask(chatHeader);
                            Log.e("EDIT Task", "-------------------------------SAVE----------------------------------" + chatHeader);

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
    }

    private void saveTask(ChatHeader chatHeader) {
        Log.e("PARAMETER", "---------------------------------------ADD TASK--------------------------" + chatHeader);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChatHeader> listCall = Constants.myInterface.editChatHeader(chatHeader);
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
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ArrayList<Employee> assignedArray = new ArrayList<>();
                final ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
                final ArrayList<String> assignedEmpNameArray = new ArrayList<>();
                if (assignStaticTaskAdminEmpList != null) {
                    if (assignStaticTaskAdminEmpList.size() > 0) {
                        assignedArray.clear();
                        for (int i = 0; i < assignStaticTaskAdminEmpList.size(); i++) {
                            if (assignStaticTaskAdminEmpList.get(i).isChecked()) {
                                assignedArray.add(assignStaticTaskAdminEmpList.get(i));
                                assignedEmpIdArray.add(assignStaticTaskAdminEmpList.get(i).getEmpId());
                                assignedEmpNameArray.add(assignStaticTaskAdminEmpList.get(i).getEmpFname() + " " + assignStaticTaskAdminEmpList.get(i).getEmpMname() + " " + assignStaticTaskAdminEmpList.get(i).getEmpSname());

                            }
                        }
                    }
                    Log.e("ASSIGN EMP", "---------------------------------" + assignedArray);
                    Log.e("ASSIGN EMP SIZE", "---------------------------------" + assignedArray.size());
                    Log.e("ASSIGN EMP ID", "---------------------------------" + assignedEmpIdArray);
                    Log.e("ASSIGN EMP Name", "---------------------------------" + assignedEmpNameArray);

                    String empIds = assignedEmpIdArray.toString().trim();
                    Log.e("ASSIGN EMP ID", "---------------------------------" + empIds);

                    String a1 = "" + empIds.substring(1, empIds.length() - 1).replace("][", ",") + "";
                    stringIdAdmin = a1.replaceAll("\\s", "");

                    Log.e("ASSIGN EMP ID STRING", "---------------------------------" + stringIdAdmin);
                    Log.e("ASSIGN EMP ID STRING1", "---------------------------------" + a1);

                    String empName = assignedEmpNameArray.toString().trim();
                    Log.e("ASSIGN EMP NAME", "---------------------------------" + empName);

                    stringNameAdmin = "" + empName.substring(1, empName.length() - 1).replace("][", ",") + "";

                    // stringName = a.replaceAll("\\s","");

                    Log.e("ASSIGN EMP NAME STRING", "---------------------------------" + stringNameAdmin);
                    //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);

                    tvEmpAdmin.setText("" + stringNameAdmin);
                    tvEmpIdAdmin.setText("" + stringIdAdmin);
                }
            }
        });

        empAdminAdapter = new EmployeeListAdminDialogAdapter(empListAdmin, getContext());
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
        for (Employee d : empListAdmin) {
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
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

//        if(selectedText.equalsIgnoreCase("Indivsual"))
//        {
//            btnSubmit.setVisibility(View.GONE);
//        }else if(selectedText.equalsIgnoreCase("Group"))
//        {
//            btnSubmit.setVisibility(View.VISIBLE);
//        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getEmployeeName();
            }
        });

        empAdapter = new EmployeeListDialogAdapter(empList, getContext(), selectedText);
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

    private void getEmployeeName() {
        ArrayList<Employee> assignedArray = new ArrayList<>();
        final ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
        final ArrayList<String> assignedEmpNameArray = new ArrayList<>();

        Log.e("EDIT TASK FRAG","------------- getEmployeeName ------------- "+assignStaticTaskList);

        if (assignStaticTaskList != null) {
            if (assignStaticTaskList.size() > 0) {
                assignedArray.clear();
                for (int i = 0; i < assignStaticTaskList.size(); i++) {
                    if (assignStaticTaskList.get(i).isChecked()) {
                        assignedArray.add(assignStaticTaskList.get(i));
                        assignedEmpIdArray.add(assignStaticTaskList.get(i).getEmpId());
                        assignedEmpNameArray.add(assignStaticTaskList.get(i).getEmpFname() + " " + assignStaticTaskList.get(i).getEmpMname() + " " + assignStaticTaskList.get(i).getEmpSname());

                    }
                }
            }
            Log.e("ASSIGN EMP", "---------------------------------" + assignedArray);
            Log.e("ASSIGN EMP SIZE", "---------------------------------" + assignedArray.size());
            Log.e("ASSIGN EMP ID", "---------------------------------" + assignedEmpIdArray);
            Log.e("ASSIGN EMP Name", "---------------------------------" + assignedEmpNameArray);

            String empIds = assignedEmpIdArray.toString().trim();
            Log.e("ASSIGN EMP ID", "---------------------------------" + empIds);

            String a1 = "" + empIds.substring(1, empIds.length() - 1).replace("][", ",") + "";
            stringId = a1.replaceAll("\\s", "");

            Log.e("ASSIGN EMP ID STRING", "---------------------------------" + stringId);
            Log.e("ASSIGN EMP ID STRING1", "---------------------------------" + a1);

            String empName = assignedEmpNameArray.toString().trim();
            Log.e("ASSIGN EMP NAME", "---------------------------------" + empName);

            stringName = "" + empName.substring(1, empName.length() - 1).replace("][", ",") + "";

            // stringName = a.replaceAll("\\s","");

            Log.e("ASSIGN EMP NAME STRING", "---------------------------------" + stringName);
            //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);

            tvEmp.setText("" + stringName);
            tvEmpId.setText("" + stringId);

        }
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
        public DepartmentListDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_department_dialog, viewGroup, false);

            return new DepartmentListDialogAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final DepartmentListDialogAdapter.MyViewHolder myViewHolder, int i) {
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
                    tvDept.setText("" + model.getGroupName());
                    tvDeptId.setText("" + model.getGroupId());
                    tvEmp.setText("" + model.getExVar2());
                    tvEmpId.setText("" + model.getUserIds());
//                    deptId= Integer.parseInt(tvDeptId.getText().toString());
//                    Log.e("Department id","--------------------------------------------"+deptId);
                    final ArrayList<Integer> deptList = new ArrayList<>();
                    deptList.add(-1);
                    getAllEmp(deptList, model.getUserIds());

                   /* if (model.getExInt1()==0){
                        deptList.add(-1);
                        getAllEmp(deptList, model.getUserIds());

                    }else{
                        deptList.add(model.getExInt1());
                        getAllEmp(deptList, model.getUserIds());

                    }*/
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

        public EmployeeListDialogAdapter(ArrayList<Employee> empList, Context context, String selectedText) {
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
        public EmployeeListDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_employee_dialog_group_list, viewGroup, false);
            //adapter_department_dialog
            return new EmployeeListDialogAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final EmployeeListDialogAdapter.MyViewHolder myViewHolder, int i) {
            final Employee model = empList.get(i);
            final int pos = i;
            myViewHolder.tvName.setText(model.getEmpFname() + " " + model.getEmpMname() + " " + model.getEmpSname());
            //holder.tvAddress.setText(model.getCustAddress());

            Log.e("Adapter", "---------------------------selected text----------------------------" + selectedText);
//            if(selectedText.equalsIgnoreCase("Indivsual"))
//            {
//                myViewHolder.checkBox.setVisibility(View.GONE);
//            }else if(selectedText.equalsIgnoreCase("Group"))
//            {
//                myViewHolder.checkBox.setVisibility(View.VISIBLE);
//            }

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
                    tvEmp.setText("" + model.getEmpFname() + " " + model.getEmpMname() + " " + model.getEmpSname());
                    tvEmpId.setText("" + model.getEmpId());

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
        public EmployeeListAdminDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_employee_dialog_group_list, viewGroup, false);
            //adapter_department_dialog
            return new EmployeeListAdminDialogAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final EmployeeListAdminDialogAdapter.MyViewHolder myViewHolder, int i) {
            final Employee model = empList.get(i);
            final int pos = i;
            myViewHolder.tvName.setText(model.getEmpFname() + " " + model.getEmpMname() + " " + model.getEmpSname());
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
        Log.e("PARAMETER", "             DEPT ID       " + deptId + "                     USER ID     " + userIds);
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
                            empList = response.body();

                            assignStaticTaskList.clear();
                            assignStaticTaskList = empList;

                           // assignStaticTaskEmpList

//                            String strEmpId="";
//                            if (model != null) {
//                                strEmpId = model.getAdminUserIds();
//                            }

                            List<String> list = Arrays.asList(userIds.split("\\s*,\\s*"));

                            Log.e("LIST", "----------------------" + list);


                            Log.e("BIN", "---------------------------------Model-----------------" + empList);
                            for (int j = 0; j < assignStaticTaskList.size(); j++) {

                                for (int k = 0; k < list.size(); k++) {

                                    if (assignStaticTaskList.get(j).getEmpId() == Integer.parseInt(list.get(k))) {

                                        assignStaticTaskList.get(j).setChecked(true);
                                        // empNameList.add(response.body().get(j).getEmpFname() + " " + response.body().get(j).getEmpMname() + " " + response.body().get(j).getEmpSname());

                                        // assignStaticList.add(empList.get(j));

                                    }
                                }
                            }


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


    //------------------------------------IMAGE-----------------------------------------

    private void sendImage(ArrayList<String> filePath, ArrayList<String> fileName, final ChatHeader chatHeader) {
        Log.e("PARAMETER : ", "   FILE PATH : " + filePath + "            FILE NAME : " + fileName + "           CHAT HEADER      " + chatHeader);

        final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = null;

        MultipartBody.Part[] uploadImagesParts = new MultipartBody.Part[filePath.size()];

        for (int index = 0; index < filePath.size(); index++) {
            Log.e("ATTACH ACT", "requestUpload:  image " + index + "  " + filePath.get(index));
            imgFile = new File(filePath.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            uploadImagesParts[index] = MultipartBody.Part.createFormData("file", "" + fileName.get(index), surveyBody);
        }

        // RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), "photo1");
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), "chat");

        Call<JSONObject> call = Constants.myInterface.imageUpload(uploadImagesParts, fileName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

                imagePath1 = null;

                Log.e("Response : ", "--" + response.body());
                saveTask(chatHeader);
                commonDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(getContext(), "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showCameraDialog(String type) {
        try {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            builder.setTitle("Choose");
            builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 101);
                }
            });
            builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "Camera.jpg");

                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getActivity(), authorities, f);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);

                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "Camera.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);

                        }
                    } catch (Exception e) {
                        ////Log.e("select camera : ", " Exception : " + e.getMessage());
                    }
                }
            });
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String realPath;
        Bitmap bitmap = null;

        if (resultCode == RESULT_OK && requestCode == 102) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPhoto1.setImageBitmap(myBitmap1);

                    myBitmap1 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath1 = f.getAbsolutePath();
                tvPhoto1.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(getContext(), data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap1 = getBitmapFromCameraData(data, getContext());

                ivPhoto1.setImageBitmap(myBitmap1);
                imagePath1 = uriFromPath.getPath();
                tvPhoto1.setText("" + uriFromPath.getPath());

                try {
                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        }
    }


    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        path1 = picturePath;
        cursor.close();

        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);
        Log.e("Image Size : ---- ", " " + bitm.getByteCount());

        return bitm;
        // return BitmapFactory.decodeFile(picturePath, options);
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }


}
