package com.ats.dutyapp.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.DutyListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyDone;
import com.ats.dutyapp.model.DutyHeader;
import com.ats.dutyapp.model.EmpCount;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyListFragment extends Fragment implements View.OnClickListener{
  public static EmpCount model;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    DutyListAdapter adapter;
    CheckBox checkBox;
    ArrayList<DutyHeader> dutyList =new ArrayList<>();
    public static Login loginUserMain;
    ArrayList<Sync> syncArray = new ArrayList<>();

    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;

    public static ArrayList<DutyHeader> assignStaticDutyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_duty_list, container, false);
        getActivity().setTitle("Duty List");
        recyclerView = view.findViewById(R.id.recyclerView);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        checkBox = view.findViewById(R.id.cbAll);

        try {
            String employeeStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(employeeStr, EmpCount.class);
            Log.e("MODEL EMPLOYEE INFO", "-----------------------------------" + model);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

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

        btnSubmit.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Log.e("LIST","------------------------"+dutyList);
                    for(int k=0;k<dutyList.size();k++)
                    {
                        Log.e("LIST SET","------------------------"+dutyList.get(k));
                        dutyList.get(k).setChecked(true);
                    }
                }else{
                    for(int k=0;k<dutyList.size();k++)
                    {
                        Log.e("LIST SET","------------------------"+dutyList.get(k));
                        dutyList.get(k).setChecked(false);

                    }
                }
                adapter = new DutyListAdapter(dutyList, getContext(),syncArray,loginUserMain);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        });

        if(syncArray!=null) {
            for (int j = 0; j < syncArray.size(); j++) {
                if (syncArray.get(j).getSettingKey().equals("Employee")) {
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        checkBox.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.GONE);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        getDutyList(loginUserMain.getEmpId(), sdf.format(System.currentTimeMillis()),sdf.format(System.currentTimeMillis()));

                    }
                } else if(syncArray.get(j).getSettingKey().equals("Supervisor")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        checkBox.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        if(model!=null) {
                            getDutyList(model.getEmpId(), sdf.format(System.currentTimeMillis()),sdf.format(System.currentTimeMillis()));
                        }

                    }
                }else if(syncArray.get(j).getSettingKey().equals("Admin")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        checkBox.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        if(model!=null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            getDutyList(model.getEmpId(), sdf.format(System.currentTimeMillis()),sdf.format(System.currentTimeMillis()));
                        }
                    }
                }else if (syncArray.get(j).getSettingKey().equals("Superwiser")) {
                        if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                            checkBox.setVisibility(View.GONE);
                            btnSubmit.setVisibility(View.GONE);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            getDutyList(loginUserMain.getEmpId(), sdf.format(System.currentTimeMillis()),sdf.format(System.currentTimeMillis()));

                        }
                    }
            }
        }

        return view;
    }

    private void getDutyList(Integer empId, String fromDate,String toDate) {
        Log.e("PARAMETER","            EMP ID       "+empId  +"          FROM DATE           "+fromDate +"                 TO DATE   "+toDate);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DutyHeader>> listCall = Constants.myInterface.getTaskDoneHeaderByEmpAndDate(empId,fromDate,toDate);
            listCall.enqueue(new Callback<ArrayList<DutyHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<DutyHeader>> call, Response<ArrayList<DutyHeader>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DUTY LIST : ", " - " + response.body());
                            dutyList.clear();
                            dutyList = response.body();

                            assignStaticDutyList.clear();
                            assignStaticDutyList = dutyList;

                            for (int i = 0; i < assignStaticDutyList.size(); i++) {
                                assignStaticDutyList.get(i).setChecked(false);
                            }

                            adapter = new DutyListAdapter(dutyList, getContext(),syncArray,loginUserMain);
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
                public void onFailure(Call<ArrayList<DutyHeader>> call, Throwable t) {
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
        if (v.getId() == R.id.btnSubmit) {
            ArrayList<DutyHeader> assignedArray = new ArrayList<>();
            final ArrayList<Integer> assignedDutyIdArray = new ArrayList<>();
            if (assignStaticDutyList != null) {
                if (assignStaticDutyList.size() > 0) {
                    assignedArray.clear();
                    for (int i = 0; i < assignStaticDutyList.size(); i++) {
                        if (assignStaticDutyList.get(i).getChecked()) {
                            assignedArray.add(assignStaticDutyList.get(i));
                            assignedDutyIdArray.add(assignStaticDutyList.get(i).getTaskDoneHeaderId());

                        }
                    }
                }
                Log.e("ASSIGN EMP", "---------------------------------" + assignedArray);
                Log.e("ASSIGN EMP SIZE", "---------------------------------" + assignedArray.size());
                Log.e("ASSIGN EMP ID", "---------------------------------" + assignedDutyIdArray);

                //                String empIds=assignedMaterialIdArray.toString().trim();
//                Log.e("ASSIGN EMP ID","---------------------------------"+empIds);
//
//                stringId = ""+empIds.substring(1, empIds.length()-1).replace("][", ",")+"";
//
//                Log.e("ASSIGN EMP ID STRING","---------------------------------"+stringId);
                if(assignedDutyIdArray.size()==0) {
                    Toast.makeText(getActivity(), "Please select duty you want to edit.....", Toast.LENGTH_SHORT).show();
                }else{
                    new ApproveDialog(getContext(), assignedDutyIdArray, loginUserMain,model).show();
                }
            }

        }
        }

    private class ApproveDialog extends Dialog implements View.OnClickListener{
        public Button btnCancel,btnSubmit;
        private EditText edFromDate;
        public TextView tvEmp,tvEmpId;
        Login loginUser;
        Dialog dialog;
        int empId = 0;
        EmpCount empCount;

        long fromDateMillis, toDateMillis;
        int yyyy, mm, dd;

        ArrayList<String> empNameList = new ArrayList<>();
        ArrayList<Integer> empIdList = new ArrayList<>();
        ArrayList<Employee> empList = new ArrayList<>();

        EmployeeListDialogAdapter empAdapter;

        ArrayList<Integer> assignedDutyIdArray = new ArrayList<>();

        public ApproveDialog(Context context, ArrayList<Integer> assignedDutyIdArray, Login loginUser ,EmpCount empCount) {
            super(context);
            this.assignedDutyIdArray=assignedDutyIdArray;
            this.loginUser=loginUser;
            this.empCount=empCount;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_duty_edit);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.x = 5;
            wlp.y = 5;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            edFromDate = (EditText)findViewById(R.id.edFromDate);
            tvEmp=(TextView)findViewById(R.id.spEmp);
            tvEmpId=(TextView)findViewById(R.id.tvEmpId);

            edFromDate.setOnClickListener(this);
            tvEmp.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            btnSubmit.setOnClickListener(this);

            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = formatter.format(todayDate);

            fromDateMillis = todayDate.getTime();
            toDateMillis = todayDate.getTime();

            //  Log.e("Mytag", "todayString" + currentDate);
            edFromDate.setText(currentDate);
           // spEmp.setText(loginUser.getEmpId());

            ArrayList<Integer> deptIdList = new ArrayList<>();
            deptIdList.add(-1);
            //loginUser.getEmpDeptId()
            getEmployeeList(deptIdList);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.edFromDate)
            {
                int yr, mn, dy;

                Calendar purchaseCal;

                long minDate = 0;

                purchaseCal = Calendar.getInstance();
                //purchaseCal.add(Calendar.DAY_OF_MONTH, -7);
                minDate = purchaseCal.getTime().getTime();
                purchaseCal.setTimeInMillis(fromDateMillis);

                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), fromDateListener, yr, mn, dy);
                dialog.getDatePicker().setMinDate(minDate);
                dialog.show();
            }else if(v.getId()==R.id.spEmp)
            {
                showDialog();
            }else if(v.getId()==R.id.btnCancel)
            {
                dismiss();
            }else if(v.getId()==R.id.btnSubmit)
            {
                boolean isValidEmpName = false;
                String empName=tvEmp.getText().toString();
                String strdutyDate=edFromDate.getText().toString();

                try {
                    empId = Integer.parseInt(tvEmpId.getText().toString());

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                Date ToDate = null;
                try {
                    ToDate = formatter1.parse(strdutyDate);//catch exception
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (empName.isEmpty()) {
                    tvEmp.setError("required");
                } else {
                    tvEmp.setError(null);
                    isValidEmpName = true;
                }

                final String dutyDate = formatter3.format(ToDate);
                Log.e("Date Formate Duty","--------------------"+dutyDate);
                Log.e("Emp Id","--------------------"+empId);
                Log.e("Assigne Duty","--------------------"+assignedDutyIdArray);

                if(isValidEmpName) {

                    for (int i = 0; i < assignedDutyIdArray.size(); i++) {
                        for (int j = 0; j < dutyList.size(); j++) {
                            if (assignedDutyIdArray.get(i) == dutyList.get(j).getTaskDoneHeaderId()) {
                                DutyDone dutyDone = new DutyDone(assignedDutyIdArray.get(i), dutyDate, dutyList.get(j).getDutyId(), empId, dutyList.get(j).getEmpIdOld(), dutyList.get(j).getTaskShiftRemark(), dutyList.get(j).getTaskShiftUserId(), dutyList.get(j).getTaskAssignUserId(), dutyList.get(j).getDelStatus(), dutyList.get(j).getStatus(), dutyList.get(j).getDutyWeight(), dutyList.get(j).getTaskCompleteWt(), dutyList.get(j).getCompletionPercent(), dutyList.get(j).getExInt1(), dutyList.get(j).getExInt2(), dutyList.get(j).getExInt3(), dutyList.get(j).getExVar1(), dutyList.get(j).getExVar2(), dutyList.get(j).getExVar3());
                                saveDuty(dutyDone);
                                dismiss();
                            }
                        }
                    }
                }

            }
        }

        private void saveDuty(DutyDone dutyDone) {
            Log.e("PARAMETER","---------------------------------------DUTY EDIT--------------------------"+dutyDone);

            if (Constants.isOnline(getContext())) {
                final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
                commonDialog.show();
                Call<DutyDone> listCall = Constants.myInterface.saveTaskDoneHeader(dutyDone);
                listCall.enqueue(new Callback<DutyDone>() {
                    @Override
                    public void onResponse(Call<DutyDone> call, Response<DutyDone> response) {
                        try {
                            if (response.body() != null) {

                                Log.e("EDIT DUTY : ", " ------------------------------EDIT DUTY------------------------- " + response.body());
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new DutyListFragment(), "MainFragment");
                                ft.commit();

                                commonDialog.dismiss();

                            } else {
                                commonDialog.dismiss();
                                Log.e("Data Null : ", "-----------");

//                                                              AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
//                                builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
//                                builder.setMessage("Unable to process! please try again.");
//
//                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                                AlertDialog dialog = builder.create();
//                                dialog.show();

                            }
                        } catch (Exception e) {
                            commonDialog.dismiss();
                            Log.e("Exception : ", "-----------" + e.getMessage());
                            e.printStackTrace();

//                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
//                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
//                            builder.setMessage("Unable to process! please try again.");
//
//                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                            AlertDialog dialog = builder.create();
//                            dialog.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<DutyDone> call, Throwable t) {
                        commonDialog.dismiss();
                        Log.e("onFailure : ", "-----------" + t.getMessage());
                        t.printStackTrace();

//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
//                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
//                        builder.setMessage("Unable to process! please try again.");
//
//                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        AlertDialog dialog = builder.create();
//                        dialog.show();

                    }
                });
            } else {
                Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
            }
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

            empAdapter = new EmployeeListDialogAdapter(empList, getContext());
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

        private void getEmployeeList(ArrayList<Integer> deptId) {
            Log.e("PARAMETER","            DEPT ID       "+deptId);

           if (Constants.isOnline(getContext())) {
                final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
                commonDialog.show();

                Call<ArrayList<Employee>> listCall = Constants.myInterface.allEmployeesByDept(deptId);
                listCall.enqueue(new Callback<ArrayList<Employee>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                        try {
                            if (response.body() != null) {

                                Log.e("EMPLOYEE  : ", " - " + response.body());
                                empNameList.add("All");
                                empIdList.add(0);
                                empList=response.body();
                                Log.e("EMPLOYEE LIST : ", " ****************************************************************** " + empList);

                                if (response.body().size() > 0) {
                                    for (int i = 0; i < response.body().size(); i++) {
                                        empIdList.add(response.body().get(i).getEmpId());
                                        empNameList.add(response.body().get(i).getEmpFname()+" "+response.body().get(i).getEmpMname()+" "+response.body().get(i).getEmpSname());
                                    }

                                }

                                if (loginUser != null) {
                                    Log.e("LOGIN USER NOT NULL","----------------------------------------------");
                                    int position = 0;
                                    if (empIdList.size() > 0) {
                                        Log.e("EMP SIZE","----------------------------------------------");
                                        for (int i = 0; i < empIdList.size(); i++) {
                                            Log.e("EMP ID FOR","----------------------------------------------");
                                            if (model.getEmpId().equals(empIdList.get(i))) {
                                                Log.e("CHEK EQUAL","----------------------------------------------");
                                                empId=empIdList.get(i);
                                                position = i;
                                               // tvEmp.setText(empNameList.get(position));
                                                break;
                                            }
                                        }
                                       tvEmp.setText(empNameList.get(position));

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
                Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }


        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edFromDate.setText(dd + "-" + mm + "-" + yyyy);

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


        public class EmployeeListDialogAdapter extends RecyclerView.Adapter<EmployeeListDialogAdapter.MyViewHolder> {

            private ArrayList<Employee> empList;
            private Context context;

            public EmployeeListDialogAdapter(ArrayList<Employee> empList, Context context) {
                this.empList = empList;
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
                        .inflate(R.layout.adapter_employee_dialog, viewGroup, false);

                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
                final Employee model = empList.get(i);

                myViewHolder.tvName.setText(model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
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


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        item.setVisible(true);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                new FilterDialog(getContext()).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class FilterDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;
        ImageView ivClose;
        CardView cardView;

        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_filter_all_duty);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edFromDate = findViewById(R.id.edFromDate);
            edToDate = findViewById(R.id.edTodate);
            tvFromDate = findViewById(R.id.tvFromDate);
            tvToDate = findViewById(R.id.tvToDate);
            Button btnFilter = findViewById(R.id.btnFilter);
            ivClose = findViewById(R.id.ivClose);


            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = formatter.format(todayDate);
            Log.e("Mytag","todayString"+currentDate);

            edToDate.setText(currentDate);
            edFromDate.setText(currentDate);

            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date FromDate = null;
            try {
                FromDate = formatter1.parse(currentDate);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String DateFrom = formatter2.format(FromDate);

            tvFromDate.setText(DateFrom);

            Date ToDate = null;
            try {
                ToDate = formatter1.parse(currentDate);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String DateTo = formatter2.format(ToDate);

            tvToDate.setText(DateTo);

            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (fromDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(fromDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                   }
                    DatePickerDialog dialog = new DatePickerDialog(getContext(), fromDateListener, yr, mn, dy);
                   dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();


                }
            });

            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (toDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(toDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, yr, mn, dy);
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();

//                    int yr, mn, dy;
//
//                    long minValue = 0;
//
//                    Calendar purchaseCal;
//
//                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
//                    String fromDate = edFromDate.getText().toString().trim();
//                    Date fromdate = null;
//
//                    try {
//                        fromdate = formatter1.parse(fromDate);//catch exception
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    purchaseCal = Calendar.getInstance();
//                    purchaseCal.add(Calendar.DAY_OF_MONTH, -7);
//                    minValue = purchaseCal.getTime().getTime();
//                    purchaseCal.setTimeInMillis(toDateMillis);
//                    yr = purchaseCal.get(Calendar.YEAR);
//                    mn = purchaseCal.get(Calendar.MONTH);
//                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
//
//                    DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, yr, mn, dy);
//                    dialog.getDatePicker().setMinDate(fromdate.getTime());
//                    //dialog.getDatePicker().setMinDate(purchaseCal.getTimeInMillis());
//                    dialog.show();

                }
            });


            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String fromDate = tvFromDate.getText().toString();
                    String toDate = tvToDate.getText().toString();
                    SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                    Date FromDate = null;
                    try {
                        FromDate = formatter1.parse(fromDate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String DateFrom = formatter2.format(FromDate);

                    Date ToDate = null;
                    try {
                        ToDate = formatter1.parse(toDate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String DateTo = formatter2.format(ToDate);


                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    }
//                    else if (dfDate.parse(fromDate).after(dfDate.parse(toDate))){
//                                edFromDate.setError("From Date must be less than To Date ");
//                                edFromDate.requestFocus();
//                     }
                    else if((FromDate.after(ToDate)))
                    {
                        edFromDate.setError("From Date must be less than To Date ");
                        edFromDate.requestFocus();
                    }else if(FromDate.equals(ToDate))
                    {
                        Log.e("Mytag Equal","fromDate"+fromDate);
                        Log.e("Mytag Equal","toDate"+toDate);

                        getDutyList(model.getEmpId(), fromDate, toDate);

                        dismiss();
                    }
                    else {
                                //dismiss();
                                boolean isFromDate=false;

                                Log.e("Mytag","fromDate"+fromDate);
                                Log.e("Mytag","toDate"+toDate);

                                getDutyList(model.getEmpId(), fromDate, toDate);

                                dismiss();

                            }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                fromDateMillis = calendar.getTimeInMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                edFromDate.setText(""+sdf1.format(calendar.getTime()));
                tvFromDate.setText(""+sdf.format(calendar.getTime()));
            }
        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;


                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                toDateMillis = calendar.getTimeInMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                edToDate.setText(""+sdf1.format(calendar.getTime()));
                tvToDate.setText(""+sdf.format(calendar.getTime()));
            }
        };
    }


}
