package com.ats.dutyapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.DutyListFragment;
import com.ats.dutyapp.model.Employee;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>  {
    private ArrayList<Employee> empList;
    private Context context;

    public EmployeeAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_emp_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.MyViewHolder myViewHolder, int i) {
        final Employee model=empList.get(i);
        myViewHolder.tvEmpName.setText(model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());

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

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                HomeActivity activity = (HomeActivity) context;
                DutyListFragment adf = new DutyListFragment();
                Bundle args = new Bundle();
                args.putString("model",json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "EmployeeFragment").commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivPhoto;
        private TextView tvEmpName,tvEmpDesig;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhoto=itemView.findViewById(R.id.ivEmployee);
            tvEmpName=itemView.findViewById(R.id.tvEmpName);
            tvEmpDesig=itemView.findViewById(R.id.tvEmpDesig);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
