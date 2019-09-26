package com.ats.dutyapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.Employee;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    private ArrayList<Employee> empList;
    private Context context;


    public EmployeeListAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.context = context;
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
                new AddMemoDialog(context).show();
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

        public AddMemoDialog(Context context) {
            super(context);
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

            edRemark=(EditText)findViewById(R.id.edRemark);

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });



        }
    }
}
