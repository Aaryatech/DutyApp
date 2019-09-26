package com.ats.dutyapp.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.CheckListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.interfaces.OnItemClickListener;
import com.ats.dutyapp.model.CheckListTemp;
import com.ats.dutyapp.model.ChecklistDetail;
import com.ats.dutyapp.model.ChecklistHeader;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCheckListFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
public EditText edName;
public static EditText edChekListName;
public static TextView tvCheckId;
public  RecyclerView recyclerView;
public Button btnAdd,btnSubmit;
public TextView tvDept,tvDeptId;
public RadioGroup rg,rgImg;
public RadioButton rbYes,rbNo,rbImgYes,rbImgNo;
CheckListAdapter checkListAdapter;
public static ArrayList<CheckListTemp> checkList = new ArrayList<>();
public static int pos;
Dialog dialog;
int deptId;
DepartmentListDialogAdapter deptAdapter;
int reportingType,photoReq;
String selectedText;
Login loginUser;
ChecklistHeader model;

private ImageView ivCamera1,ivPhoto1;
private TextView tvPhoto1,tvImageLable;
private LinearLayout linearLayoutImage;

ArrayList<String> deptNameList = new ArrayList<>();
ArrayList<Integer> deptIdList = new ArrayList<>();
ArrayList<Department> deptList = new ArrayList<>();
public static ArrayList<ChecklistDetail> checklistDetail = new ArrayList<>();

    //Image Upload

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "gfpl_security");
    File f;

    Bitmap myBitmap1 = null;
    public static String path1, imagePath1 = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_list, container, false);
        getActivity().setTitle("Add Checklist");
        edChekListName=(EditText)view.findViewById(R.id.edChekName);
        tvCheckId=(TextView) view.findViewById(R.id.tvCheckId);
        edName=(EditText)view.findViewById(R.id.edName);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        btnAdd=(Button) view.findViewById(R.id.btnAdd);
        btnSubmit=(Button) view.findViewById(R.id.btnSubmit);
        tvDept=(TextView) view.findViewById(R.id.tvDept);
        tvDeptId=(TextView) view.findViewById(R.id.tvDeptId);

        rg=(RadioGroup) view.findViewById(R.id.rg);
        rbYes=(RadioButton) view.findViewById(R.id.rbYes);
        rbNo=(RadioButton) view.findViewById(R.id.rbNo);

        rgImg=(RadioGroup) view.findViewById(R.id.rgImg);
        rbImgYes=(RadioButton) view.findViewById(R.id.rbImgYes);
        rbImgNo=(RadioButton) view.findViewById(R.id.rbImgNo);

        linearLayoutImage=(LinearLayout)view.findViewById(R.id.linearLayoutImage);
        tvImageLable=(TextView)view.findViewById(R.id.tvImageLable);

        ivCamera1 = view.findViewById(R.id.ivCamera1);
        ivPhoto1 = view.findViewById(R.id.ivPhoto1);
        tvPhoto1 = view.findViewById(R.id.tvPhoto1);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

       //   checkList.clear();
        checklistDetail.clear();

//        try {
////            String quoteStr = getArguments().getString("model");
////            Gson gson = new Gson();
////            model = gson.fromJson(quoteStr, ChecklistHeader.class);
////            Log.e("MODEL", "-----------------CHECK LIST HEADER AND DETAIL------------------" + model);
////
////            edName.setText(""+model.getChecklistName());
////            tvDept.setText(""+model.getExVar1());
////
////            if(model.getExInt1()==0)
////            {
////                rbNo.setChecked(true);
////            }else if(model.getExInt1()==1)
////            {
////                rbYes.setChecked(true);
////            }
////
////            if (model.getChecklistDetail() != null) {
////                checklistDetail.clear();
//////                for (int i = 0; i < model.getChecklistDetail().size(); i++) {
//////                    CheckListTemp checkListTemp=new CheckListTemp(model.getChecklistDetail().get(i).getChecklist_desc());
//////                   checkList.add(checkListTemp);
//////                }
////
////                for(int j=0;j< model.getChecklistDetail().size();j++)
////                {
////                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////                    ChecklistDetail checklistActionDetail = new ChecklistDetail(model.getChecklistDetail().get(j).getChecklistDetailId(),model.getChecklistHeaderId(),model.getChecklistDetail().get(j).getChecklist_desc(),model.getChecklistDetail().get(j).getIsPhoto(),model.getChecklistDetail().get(j).getIsUsed(),model.getChecklistDetail().get(j).getDelStatus(),model.getChecklistDetail().get(j).getCreatedBy(),model.getChecklistDetail().get(j).getCreatedDate(),model.getChecklistDetail().get(j).getExInt1(),model.getChecklistDetail().get(j).getExInt2(),model.getChecklistDetail().get(j).getExVar1(),model.getChecklistDetail().get(j).getExVar2());
////                    checklistDetail.add(checklistActionDetail);
////                }
////
////                Log.e("CHECKLIST EDIT","---------------------------------------"+checklistDetail);
////                checkListAdapter = new CheckListAdapter(checklistDetail, getActivity());
////                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
////                recyclerView.setLayoutManager(mLayoutManager);
////                recyclerView.setItemAnimator(new DefaultItemAnimator());
////                recyclerView.setAdapter(checkListAdapter);
////            }
////
////        }catch (Exception e)
////        {
////            e.printStackTrace();
////        }



        getAllDept();
        rbNo.setChecked(true);
        rbImgNo.setChecked(true);

        edChekListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 ) {
                    btnAdd.setVisibility(View.VISIBLE);
                } else {
                    btnAdd.setVisibility(View.GONE);
                }
            }
        });

        btnAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvDept.setOnClickListener(this);
        ivCamera1.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btnAdd)
        {
            String strChekPos=tvCheckId.getText().toString();
            Log.e("strChekPos","------------------------------"+strChekPos);

            photoReq = 0;
            if (rbImgYes.isChecked()) {
                photoReq = 1;
            } else if (rbImgNo.isChecked()) {
                photoReq = 0;
            }

            if(strChekPos.equalsIgnoreCase("pos")) {

                tvCheckId.setText("pos");
                Log.e("Pos Add","------------------------------"+tvCheckId.getText().toString());

                String strChekListName=edChekListName.getText().toString();
//                CheckListTemp model=new CheckListTemp(strChekListName);
//                checkList.add(model);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ChecklistDetail checklistActionDetail = new ChecklistDetail(0,0,strChekListName,photoReq,1,1,loginUser.getEmpId(), sdf.format(System.currentTimeMillis()),0,0,"","");
                checklistDetail.add(checklistActionDetail);
                Log.e(" Add Checklist ","-----------------------------------------------"+checklistDetail);

                checkListAdapter = new CheckListAdapter(checklistDetail, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(checkListAdapter);

                edChekListName.setText("");

            }else{

//                tvCheckId.setText(""+pos);
                Log.e("Pos Edit","------------------------------"+pos);

                String strChekListName=edChekListName.getText().toString();
                //CheckListTemp model=new CheckListTemp(strChekListName);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                 ChecklistDetail model = new ChecklistDetail(0, 0, strChekListName, photoReq, 1, 1, loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), 0, 0, "", "");

                checklistDetail.set(pos,model);
                checkListAdapter.notifyDataSetChanged();

                edChekListName.setText("");
                tvCheckId.setText("pos");

            }



        }else if(v.getId()== R.id.tvDept) {
            showDialog();

        }else if(v.getId()== R.id.btnSubmit)
        {
            String strName,strDept,strNameDetail;
            int strDeptId = 0;
            boolean isValidName = false,isValidDept =false;

            strName=edName.getText().toString();
            strNameDetail=edChekListName.getText().toString();
            strDept=tvDept.getText().toString();

            try {
                strDeptId = Integer.parseInt(tvDeptId.getText().toString());
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            if (strName.isEmpty()) {
                edName.setError("required");
            } else {
                edName.setError(null);
                isValidName = true;
            }

            if (strDept.isEmpty()) {
                tvDept.setError("required");
            } else {
                tvDept.setError(null);
                isValidDept = true;
            }

            reportingType = 0;
            if (rbYes.isChecked()) {
                reportingType = 1;
            } else if (rbNo.isChecked()) {
                reportingType = 0;
            }



            if(isValidName && isValidDept)
            {
                //if(model==null) {

                    ArrayList<ChecklistDetail> detailList = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    for (int i = 0; i < checklistDetail.size(); i++) {
                        ChecklistDetail checklistDetail1 = new ChecklistDetail(0, 0, checklistDetail.get(i).getChecklist_desc(), checklistDetail.get(i).getIsPhoto(), 1, 1, loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), 0, 0, "", "");
                        detailList.add(checklistDetail1);
                    }

                    final ChecklistHeader checklistHeader = new ChecklistHeader(0, strDeptId, strName, 1, 1, loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), reportingType, 0, 0, strDept, "", "", detailList);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to save checklist ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            saveCheckList(checklistHeader);
                            Log.e("Check List", "-------------------------------SAVE----------------------------------" + checklistHeader);

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
//                else {
//                    ArrayList<ChecklistDetail> detailList = new ArrayList<>();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//                    Log.e("Check List", "---------------------------------EDIT LIST-------------------------------------" + checklistDetail);
//                    if(checklistDetail!=null) {
//                       // Log.e("Check List", "---------------------------------EDIT 888888888888888-------------------------------------" + checklistDetail);
//                        for (int i = 0; i < checklistDetail.size(); i++) {
//                            Log.e("Check List", "---------------------------------EDIT -------------------------------------" + checklistDetail);
//                            ChecklistDetail checklistDetail2 = new ChecklistDetail(checklistDetail.get(i).getChecklistDetailId(), checklistDetail.get(i).getChecklistHeaderId(), checklistDetail.get(i).getChecklist_desc(), photoReq, checklistDetail.get(i).getIsUsed(), checklistDetail.get(i).getDelStatus(), checklistDetail.get(i).getCreatedBy(), checklistDetail.get(i).getCreatedDate(), checklistDetail.get(i).getExInt1(), checklistDetail.get(i).getExInt2(), checklistDetail.get(i).getExVar1(), checklistDetail.get(i).getExVar2());
//                            detailList.add(checklistDetail2);
//                        }
//                        Log.e("Check List", "---------------------------------EDIT DETAIL-------------------------------------" + detailList);
//                    }
//
//                    final ChecklistHeader checklistHeader = new ChecklistHeader(model.getChecklistHeaderId(), strDeptId, strName, model.getIsUsed(), model.getDelStatus(), model.getCreatedBy(),model.getCreatedDate(), reportingType, model.getExInt2(), model.getExInt3(), strDept, model.getExVar2(), model.getExVar3(), detailList);
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
//                    builder.setTitle("Confirmation");
//                    builder.setMessage("Do you want to edit checklist ?");
//                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            saveCheckList(checklistHeader);
//                            Log.e("Check List", "---------------------------------EDIT-------------------------------------" + checklistHeader);
//
//                        }
//                    });
//                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }

          //  }
        }
    }

    private void saveCheckList(ChecklistHeader checklistHeader) {
        Log.e("PARAMETER","---------------------------------------CHECK LIST--------------------------"+checklistHeader);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChecklistHeader> listCall = Constants.myInterface.saveChecklistHeaderAndDetail(checklistHeader);
            listCall.enqueue(new Callback<ChecklistHeader>() {
                @Override
                public void onResponse(Call<ChecklistHeader> call, Response<ChecklistHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE CHECK LIST : ", " ------------------------------SAVE CHECK LIST------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new CheckListFragment(), "MainFragment");
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
                public void onFailure(Call<ChecklistHeader> call, Throwable t) {
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

    @Override
    public void onClick(View view, int position) {
        final CheckListTemp checkListTemp = checkList.get(position);
        Log.e("Adapter Click","-------------------------------------------------------"+checkListTemp);
       // edChekListName.setText(checkListTemp.getChecklistName());

    }

    public void onClickData(Integer position)
    {
       // CheckListAdapter adapterCheckList = null;
       // adapterCheckList = new CheckListAdapter(checkList, getActivity());
        pos=position;

        tvCheckId.setText(""+pos);
        Log.e("Pos Edit","------------------------------"+pos);


        Log.e("Position1","----------------------------"+position);
        Log.e("Position2","----------------------------"+pos);
        String data= checklistDetail.get(position).getChecklist_desc();
        Log.e("Data","----------------------------"+data);
        edChekListName.setText(data);

//        CheckListTemp model=new CheckListTemp(edChekListName.getText().toString());
//        checkList.add(model);
//
//        checkList.set(position,model);
//        checkListAdapter.notifyDataSetChanged();
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

            return new MyViewHolder(itemView);
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



}
