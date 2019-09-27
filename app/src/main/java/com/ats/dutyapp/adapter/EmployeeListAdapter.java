package com.ats.dutyapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.DashboardFragment;
import com.ats.dutyapp.model.ChatMemo;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    private ArrayList<Employee> empList;
    private Context context;
    public  static Login loginUser;
    public  static ChatTask chatTask;


    public EmployeeListAdapter(ArrayList<Employee> empList, Context context,ChatTask chatTask,Login loginUser) {
        this.empList = empList;
        this.context = context;
        this.chatTask = chatTask;
        this.loginUser = loginUser;
    }

    @NonNull
    @Override
    public EmployeeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_employee_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListAdapter.MyViewHolder myViewHolder, int i) {
        final Employee model=empList.get(i);

        myViewHolder.tvEmpName.setText(model.getEmpFname());


        if(model.getEmpCatId()==1) {
            myViewHolder.tvEmpDesig.setText("Superwiser");
        }else  if(model.getEmpCatId()==2) {
            myViewHolder.tvEmpDesig.setText("Admin");
        }else  if(model.getEmpCatId()==3) {
            myViewHolder.tvEmpDesig.setText("Employee");
        }else  if(model.getEmpCatId()==4) {
            myViewHolder.tvEmpDesig.setText("Security");
        }

        try {
            String imageUri = String.valueOf(model.getEmpPhoto());
            Log.e("Image Path","---------------------"+ Constants.IMAGE_URL+imageUri);
            Picasso.with(context).load(Constants.IMAGE_URL+imageUri).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(myViewHolder.ivPhoto);

        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolder.tvMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMemoDialog(context,chatTask,model).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivPhoto;
        private TextView tvEmpName,tvEmpDesig,tvEmpCount,tvMemo;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto=itemView.findViewById(R.id.ivEmployee);
            tvEmpName=itemView.findViewById(R.id.tvEmpName);
            tvEmpDesig=itemView.findViewById(R.id.tvEmpDesig);
            tvEmpCount=itemView.findViewById(R.id.tvEmpCount);
            tvMemo=itemView.findViewById(R.id.tvMemo);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    private class AddMemoDialog extends Dialog {
        public Button btnCancel, btnSubmit;
        public TextView tvTaskName,tvTaskDesc;
        public EditText edRemark;
        ChatTask chatTask;
        Employee employee;

        public AddMemoDialog(Context context,ChatTask chatTask,Employee employee) {
            super(context);
            this.chatTask = chatTask;
            this.employee = employee;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_memo);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            //  wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.x = 5;
            wlp.y = 5;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);

            tvTaskName=(TextView)findViewById(R.id.tvTaskName);
            tvTaskDesc=(TextView)findViewById(R.id.tvTaskDesc);

            try {
                tvTaskName.setText("" + chatTask.getHeaderName());
                tvTaskDesc.setText("" + chatTask.getTaskDesc());
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            edRemark=(EditText)findViewById(R.id.edRemark);

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strRmark;
                    boolean isValidRemark=false;

                    strRmark=edRemark.getText().toString();

                    if (strRmark.isEmpty()) {
                        edRemark.setError("required");
                    } else {
                        edRemark.setError(null);
                        isValidRemark = true;
                    }

                    if(isValidRemark)
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                       final ChatMemo chatMemo=new ChatMemo(0,employee.getEmpId(),chatTask.getHeaderId(),loginUser.getEmpId(),strRmark,sdf.format(System.currentTimeMillis()),0,1,0,0,0,"","","");
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to save memo ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                saveMemo(chatMemo);
                                dismiss();
                                Log.e("Save memo", "-------------------------------SAVE----------------------------------" + chatMemo);

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
            });

        }
    }

    private void saveMemo(ChatMemo chatMemo) {
        Log.e("PARAMETER","---------------------------------------MEMO LIST--------------------------"+chatMemo);

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChatMemo> listCall = Constants.myInterface.saveChatMemo(chatMemo);
            listCall.enqueue(new Callback<ChatMemo>() {
                @Override
                public void onResponse(Call<ChatMemo> call, Response<ChatMemo> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE MEMO LIST : ", " ------------------------------SAVE MEMO LIST------------------------- " + response.body());
                            HomeActivity activity = (HomeActivity) context;
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new DashboardFragment(), "MainFragment");
                            ft.commit();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("" + context.getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        builder.setTitle("" + context.getResources().getString(R.string.app_name));
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
                public void onFailure(Call<ChatMemo> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("" + context.getResources().getString(R.string.app_name));
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
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }
}
