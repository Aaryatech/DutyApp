package com.ats.dutyapp.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.EmployeeCheckListAdapter;
import com.ats.dutyapp.adapter.ViewCheckListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.AssignChecklist;
import com.ats.dutyapp.model.ChecklistDetail;
import com.ats.dutyapp.model.ChecklistHeader;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.SaveAssigneChecklist;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAssignCheckListFragment extends Fragment implements View.OnClickListener {
public TextView tvDept,tvChecklist,tvDeptId,tvChecklistId,tvView;
public Button btnSubmit;
public RecyclerView recyclerView;
Dialog dialog;
int deptId;
DepartmentListDialogAdapter deptAdapter;
CheckListDialogAdapter checklistAdapter;
EmployeeCheckListAdapter empAdapter;

ArrayList<String> deptNameList = new ArrayList<>();
ArrayList<Integer> deptIdList = new ArrayList<>();
ArrayList<Department> deptList = new ArrayList<>();

ArrayList<Employee> empList = new ArrayList<>();
ArrayList<ChecklistHeader> checkList = new ArrayList<>();
AssignChecklist model;
Login loginUser;

int strDeptId = 0,strChecklistId = 0;

public static ArrayList<Employee> assignStaticEmpList = new ArrayList<>();
String stringId,stringName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_check_list, container, false);
        tvDept=view.findViewById(R.id.tvDept);
        tvChecklist=view.findViewById(R.id.tvChecklist);
        tvDeptId=view.findViewById(R.id.tvDeptId);
        tvView=view.findViewById(R.id.tvView);
        tvChecklistId=view.findViewById(R.id.tvChecklistId);
        btnSubmit=view.findViewById(R.id.btnSubmit);
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

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, AssignChecklist.class);
            Log.e("MODEL","-----------------------------------"+model);

            tvDept.setText(model.getDeptName());
            tvChecklist.setText(model.getChecklistName());
            tvChecklistId.setText(""+model.getChecklistHeaderId());
            tvDeptId.setText(""+model.getDeptId());

            ArrayList<Integer> deptIdList = new ArrayList<>();
            deptIdList.add(model.getDeptId());
            getAllEmp(deptIdList);
            getCheckList(model.getDeptId());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(model==null)
        {
            getActivity().setTitle("Add Assigne Checklist");
        }else{
            getActivity().setTitle("Edit Assigne Checklist");
        }

        getAllDept();

        tvDept.setOnClickListener(this);
        tvChecklist.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvDept)
        {
            showDialog();

        }else if(v.getId()==R.id.tvChecklist)
        {
            showDialog1();
        }
        else if(v.getId()==R.id.tvView)
        {
            new viewChecklistDialog(getContext(),checkList).show();

        }else if(v.getId()==R.id.btnSubmit)
        {
            String strDept,strChecklist;
           // int strDeptId = 0,strChecklistId = 0;
            boolean isValidChecklistName = false,isValidDept =false;

            strDept=tvDept.getText().toString();
            strChecklist=tvChecklist.getText().toString();

            try {
                strDeptId = Integer.parseInt(tvDeptId.getText().toString());
                strChecklistId = Integer.parseInt(tvChecklistId.getText().toString());
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            if (strChecklist.isEmpty()) {
                tvChecklist.setError("required");
            } else {
                tvChecklist.setError(null);
                isValidChecklistName = true;
            }

            if (strDept.isEmpty()) {
                tvDept.setError("required");
            } else {
                tvDept.setError(null);
                isValidDept = true;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            ArrayList<Employee> assignedArray = new ArrayList<>();
            final ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
            final ArrayList<String> assignedEmpNameArray = new ArrayList<>();
            if (assignStaticEmpList != null) {
                if (assignStaticEmpList.size() > 0) {
                    assignedArray.clear();
                    for (int i = 0; i < assignStaticEmpList.size(); i++) {
                        if (assignStaticEmpList.get(i).isChecked()) {
                            assignedArray.add(assignStaticEmpList.get(i));
                            assignedEmpIdArray.add(assignStaticEmpList.get(i).getEmpId());
                            assignedEmpNameArray.add(assignStaticEmpList.get(i).getEmpFname() + " " + assignStaticEmpList.get(i).getEmpMname() + " " + assignStaticEmpList.get(i).getEmpSname());

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

                String empName = assignedEmpNameArray.toString().trim();
                Log.e("ASSIGN EMP NAME", "---------------------------------" + empName);

                stringName = "" + empName.substring(1, empName.length() - 1).replace("][", ",") + "";

                // stringName = a.replaceAll("\\s","");

                Log.e("ASSIGN EMP NAME STRING", "---------------------------------" + stringName);
                //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);
            }

            if(isValidDept && isValidChecklistName) {
                if (model == null) {
                    final SaveAssigneChecklist saveAssigneChecklist = new SaveAssigneChecklist(0, strDeptId, strChecklistId, stringId, loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), 1, 0, 0, "", "");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to save assign checklist ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            saveAssignCheckList(saveAssigneChecklist);
                            Log.e("Assign Check List", "-----------------------------------Add-------------------------------------------" + saveAssigneChecklist);

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
                }else{
                    final SaveAssigneChecklist saveAssigneChecklist = new SaveAssigneChecklist(model.getAssignId(), strDeptId, strChecklistId, stringId, loginUser.getEmpId(), model.getAssignedDate(), model.getDelStatus(), model.getExInt1(), model.getExInt2(), model.getExVar1(), model.getExVar2());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit assign checklist ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            saveAssignCheckList(saveAssigneChecklist);
                            Log.e("Assign Check List", "---------------------------------------Edit------------------------------" + saveAssigneChecklist);

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

    private void saveAssignCheckList(SaveAssigneChecklist saveAssigneChecklist) {
        Log.e("PARAMETER","---------------------------------------CHECK LIST--------------------------"+saveAssigneChecklist);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<SaveAssigneChecklist> listCall = Constants.myInterface.saveChecklistAssign(saveAssigneChecklist);
            listCall.enqueue(new Callback<SaveAssigneChecklist>() {
                @Override
                public void onResponse(Call<SaveAssigneChecklist> call, Response<SaveAssigneChecklist> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE CHECK  : ", " ------------------------------SAVE ASSIGN CHECK LIST------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new AssignCheckListFragment(), "MainFragment");
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
                public void onFailure(Call<SaveAssigneChecklist> call, Throwable t) {
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

    private void showDialog1() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        checklistAdapter = new CheckListDialogAdapter(checkList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(checklistAdapter);

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
                    if (checklistAdapter != null) {
                        filterDept1(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterDept1(String text) {
        ArrayList<ChecklistHeader> temp = new ArrayList();
        for (ChecklistHeader d : checkList) {
            if (d.getChecklistName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        checklistAdapter.updateList1(temp);
    }

    public class CheckListDialogAdapter extends RecyclerView.Adapter<CheckListDialogAdapter.MyViewHolder> {

        private ArrayList<ChecklistHeader> checkList;
        private Context context;

        public CheckListDialogAdapter(ArrayList<ChecklistHeader> checkList, Context context) {
            this.checkList = checkList;
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
        public CheckListDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_department_dialog, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final CheckListDialogAdapter.MyViewHolder myViewHolder, int i) {
            final ChecklistHeader model = checkList.get(i);

            myViewHolder.tvName.setText(model.getChecklistName());
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
                    tvChecklist.setText(""+model.getChecklistName());
                    tvChecklistId.setText(""+model.getChecklistHeaderId());



                }
            });
        }



        @Override
        public int getItemCount() {
            return checkList.size();
        }

        public void updateList1(ArrayList<ChecklistHeader> list) {
            checkList = list;
            notifyDataSetChanged();
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


    private void showDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        deptAdapter = new DepartmentListDialogAdapter(deptList, getContext());
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
        ArrayList<Department> temp = new ArrayList();
        for (Department d : deptList) {
            if (d.getEmpDeptName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        deptAdapter.updateList(temp);
    }

    public class DepartmentListDialogAdapter extends RecyclerView.Adapter<DepartmentListDialogAdapter.MyViewHolder> {

        private ArrayList<Department> custList;
        private Context context;

        public DepartmentListDialogAdapter(ArrayList<Department> custList, Context context) {
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
            final Department model = custList.get(i);

            myViewHolder.tvName.setText(model.getEmpDeptName());
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
                    tvDept.setText(""+model.getEmpDeptName());
                    tvDeptId.setText(""+model.getEmpDeptId());
                    deptId= Integer.parseInt(tvDeptId.getText().toString());
                    ArrayList<Integer> deptList = new ArrayList<>();
                    deptList.add(deptId);

                    getCheckList(deptId);
                    getAllEmp(deptList);


                }
            });
        }



        @Override
        public int getItemCount() {
            return custList.size();
        }

        public void updateList(ArrayList<Department> list) {
            custList = list;
            notifyDataSetChanged();
        }

    }

    private void getCheckList(int deptId) {
        Log.e("PARAMETER","          DEPAT      "+ deptId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ChecklistHeader>> listCall = Constants.myInterface.getAllChecklistByDept(deptId);
            listCall.enqueue(new Callback<ArrayList<ChecklistHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<ChecklistHeader>> call, Response<ArrayList<ChecklistHeader>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGN CHECK LIST : ", " --------------------------------------------- " + response.body());
                            checkList.clear();
                            checkList = response.body();



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
                public void onFailure(Call<ArrayList<ChecklistHeader>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private class viewChecklistDialog extends Dialog {

        public Button btnCancel;
        public RecyclerView recyclerView;
        private ViewCheckListAdapter mAdapter;
       // ChecklistHeader checklistHeader;
        private ArrayList<ChecklistHeader> checklistHeader;

        public viewChecklistDialog(@NonNull Context context,ArrayList<ChecklistHeader> checklistHeader) {
            super(context);
            this.checklistHeader = checklistHeader;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_view_checklist);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnCancel = (Button) findViewById(R.id.btnCancel);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            if (checklistHeader != null) {
                ArrayList<ChecklistDetail> detailList = new ArrayList<>();
                int strCheckId = 0;
                try {

                    strCheckId = Integer.parseInt(tvChecklistId.getText().toString());
                       Log.e("Check Id", "---------------Add-----------------" + strCheckId);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < checklistHeader.size(); i++) {
                    if(checklistHeader.get(i).getChecklistHeaderId()== strCheckId) {
                        Log.e("Check Id", "--------------------------------" + strCheckId);
                        for (int j = 0; j < checklistHeader.get(i).getChecklistDetail().size(); j++) {
                            detailList.add(checklistHeader.get(i).getChecklistDetail().get(j));
                            Log.e("Detail1", "------------------------------------Checklist---------------------" + detailList);
                        }
                    }
                }

                Log.e("Detail","------------------------------------Checklist---------------------"+detailList);
                mAdapter = new ViewCheckListAdapter(detailList, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }

        }

    }

    private void getAllEmp(ArrayList<Integer> deptId) {
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

                            assignStaticEmpList.clear();
                            assignStaticEmpList = empList;

                            if(model==null) {

                                for (int i = 0; i < assignStaticEmpList.size(); i++) {
                                    assignStaticEmpList.get(i).setChecked(false);
                                }
                            }else if(model!=null){
                                for (int i = 0; i < assignStaticEmpList.size(); i++) {
                                    assignStaticEmpList.get(i).setChecked(false);
                                }

                                String strEmpId="";
                                if (model != null) {
                                    strEmpId = model.getAssignEmpIds();
                                }

                                List<String> list = Arrays.asList(strEmpId.split("\\s*,\\s*"));

                                Log.e("LIST", "----------------------" + list);

                                // assignStaticList.clear();
                                for (int j = 0; j < assignStaticEmpList.size(); j++) {

                                    for (int k = 0; k < list.size(); k++) {

                                        if (assignStaticEmpList.get(j).getEmpId() == Integer.parseInt(list.get(k))) {

                                            assignStaticEmpList.get(j).setChecked(true);
                                            // assignStaticList.add(empList.get(j));

                                        }
                                    }
                                }
                            }

                            empAdapter = new EmployeeCheckListAdapter(empList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(empAdapter);

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

    private void getAssigneEmp() {
        ArrayList<Employee> assignedArray = new ArrayList<>();
        final ArrayList<Integer> assignedEmpIdArray = new ArrayList<>();
        final ArrayList<String> assignedEmpNameArray = new ArrayList<>();
        if (assignStaticEmpList != null) {
            if (assignStaticEmpList.size() > 0) {
                assignedArray.clear();
                for (int i = 0; i < assignStaticEmpList.size(); i++) {
                    if (assignStaticEmpList.get(i).isChecked()) {
                        assignedArray.add(assignStaticEmpList.get(i));
                        assignedEmpIdArray.add(assignStaticEmpList.get(i).getEmpId());
                        assignedEmpNameArray.add(assignStaticEmpList.get(i).getEmpFname() + " " + assignStaticEmpList.get(i).getEmpMname() + " " + assignStaticEmpList.get(i).getEmpSname());

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

            String empName = assignedEmpNameArray.toString().trim();
            Log.e("ASSIGN EMP NAME", "---------------------------------" + empName);

            stringName = "" + empName.substring(1, empName.length() - 1).replace("][", ",") + "";

            // stringName = a.replaceAll("\\s","");

            Log.e("ASSIGN EMP NAME STRING", "---------------------------------" + stringName);
            //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);
        }
    }

}
