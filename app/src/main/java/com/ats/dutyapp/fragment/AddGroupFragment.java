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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatGroup;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.GroupList;
import com.ats.dutyapp.model.Login;
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
public class AddGroupFragment extends Fragment implements View.OnClickListener{
public EditText edGrpName,edGrpDesc;
public TextView tvDept,tvEmp,tvDeptId,tvEmpId;
public Button btnSubmit;
Dialog dialog;
GroupList model;
String stringId,stringName;
Login loginUser;

int deptId;
DepartmentListDialogAdapter deptAdapter;
EmployeeListDialogAdapter empAdapter;
public static ArrayList<Employee> assignStaticEmpList = new ArrayList<>();

//DepartmentListDialogAdapter deptAdapter;
ArrayList<String> deptNameList = new ArrayList<>();
    ArrayList<Integer> deptIdList = new ArrayList<>();
    ArrayList<Department> deptList = new ArrayList<>();

    ArrayList<String> empNameList = new ArrayList<>();
    ArrayList<Integer> empIdList = new ArrayList<>();
    ArrayList<Employee> empList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_group, container, false);
        edGrpName=(EditText)view.findViewById(R.id.edGrpName);
        edGrpDesc=(EditText)view.findViewById(R.id.edGrpDesc);
        tvDept=(TextView) view.findViewById(R.id.tvDept);
        tvEmp=(TextView)view.findViewById(R.id.tvEmp);
        tvDeptId=(TextView)view.findViewById(R.id.tvDeptId);
        tvEmpId=(TextView)view.findViewById(R.id.tvEmpId);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        getAllDept();

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, GroupList.class);

            edGrpName.setText(""+model.getGroupName());
            edGrpDesc.setText(""+model.getGroupDesc());
            tvDept.setText(""+model.getExVar1());
            tvDeptId.setText(""+model.getExInt1());
            tvEmp.setText(""+model.getExVar2());
            tvEmpId.setText(""+model.getUserIds());

            deptId= Integer.parseInt(tvDeptId.getText().toString());
            final ArrayList<Integer> deptList = new ArrayList<>();
            deptList.add(deptId);
            getAllEmp(deptList);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        tvDept.setOnClickListener(this);
        tvEmp.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvDept)
        {
            showDialog();
        }else if(v.getId()==R.id.tvEmp)
        {
            showDialog1();
        }else if(v.getId()==R.id.btnSubmit)
        {
            String strGrpName,strGrpDesc,strDept,strEmp,strEmpId,strDeptId;
            boolean isValidGrpName = false,isValidDept =false,isValidEmp =false,isValidGrpDesc =false;
            int deptIds = 0;
            strGrpName=edGrpName.getText().toString();
            strGrpDesc=edGrpDesc.getText().toString();
            strDept=tvDept.getText().toString();
            strDeptId=tvDeptId.getText().toString();
            strEmp=tvEmp.getText().toString();
            strEmpId=tvEmpId.getText().toString();

            try{
                deptIds= Integer.parseInt(strDeptId);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            if (strGrpName.isEmpty()) {
                edGrpName.setError("required");
            } else {
                edGrpName.setError(null);
                isValidGrpName = true;
            }

            if (strGrpDesc.isEmpty()) {
                edGrpDesc.setError("required");
            } else {
                edGrpDesc.setError(null);
                isValidGrpDesc = true;
            }

            if (strDept.isEmpty()) {
                tvDept.setError("required");
            } else {
                tvDept.setError(null);
                isValidDept = true;
            }
            if (strEmp.isEmpty()) {
                tvEmp.setError("required");
            } else {
                tvEmp.setError(null);
                isValidEmp = true;
            }

            if(isValidGrpName && isValidGrpDesc && isValidDept && isValidEmp) {
                if (model == null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    final ChatGroup model = new ChatGroup(0, strGrpName, strGrpDesc, strEmpId, loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), 1, 1, deptIds, 0, strDept, strEmp);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to add group ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            saveGroup(model);
                            Log.e("Add Group", "-------------------------------SAVE----------------------------------" + model);

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

                    final ChatGroup chatGroup = new ChatGroup(model.getGroupId(), strGrpName, strGrpDesc, strEmpId,model.getGroupCreatedUserId(), model.getGroupCreatedDate(), model.getIsActive(), model.getDelStatus(), deptIds, 0, strDept, strEmp);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit group ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            saveGroup(chatGroup);
                            Log.e("Add Group", "-------------------------------SAVE----------------------------------" + model);

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

    private void saveGroup(ChatGroup model) {
        Log.e("PARAMETER","---------------------------------------ADD GROUP--------------------------"+model);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChatGroup> listCall = Constants.myInterface.saveChatGroup(model);
            listCall.enqueue(new Callback<ChatGroup>() {
                @Override
                public void onResponse(Call<ChatGroup> call, Response<ChatGroup> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ADD GROUP LIST : ", " ------------------------------SAVE ADD GROUP------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new GroupListFragment(), "MainFragment");
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
                public void onFailure(Call<ChatGroup> call, Throwable t) {
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                                assignedEmpNameArray.add(assignStaticEmpList.get(i).getEmpFname()+" " +assignStaticEmpList.get(i).getEmpMname()+" " +assignStaticEmpList.get(i).getEmpSname());

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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_department_dialog, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
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
                    final ArrayList<Integer> deptList = new ArrayList<>();
                    deptList.add(deptId);
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

    public class EmployeeListDialogAdapter extends RecyclerView.Adapter<EmployeeListDialogAdapter.MyViewHolder> {

        private ArrayList<Employee> empList;
        private Context context;

        public EmployeeListDialogAdapter(ArrayList<Employee> empList, Context context) {
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

//            if(model.isChecked())
//            {
//                myViewHolder.checkBox.setChecked(true);
//            }else{
//                myViewHolder.checkBox.setChecked(false);
//            }


//            myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//
//                        model.setChecked(true);
//
//                    } else {
//
//                        model.setChecked(false);
//
//                    }
//
//                }
//            });

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

        public void updateList(ArrayList<Employee> list) {
            empList = list;
            notifyDataSetChanged();
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

                            for (int i = 0; i < assignStaticEmpList.size(); i++) {
                                assignStaticEmpList.get(i).setChecked(false);
                            }

                            String strEmpId="";
                            if (model != null) {
                                strEmpId = model.getUserIds();
                            }

                            List<String> list = Arrays.asList(strEmpId.split("\\s*,\\s*"));

                            Log.e("LIST", "----------------------" + list);


                            Log.e("BIN", "---------------------------------Model-----------------" + empList);
                            for (int j = 0; j < assignStaticEmpList.size(); j++) {

                                for (int k = 0; k < list.size(); k++) {

                                    if (assignStaticEmpList.get(j).getEmpId() == Integer.parseInt(list.get(k))) {

                                        assignStaticEmpList.get(j).setChecked(true);
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

}
