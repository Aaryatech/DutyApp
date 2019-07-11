package com.ats.dutyapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.fragment.EmployeeDashboardFragment;
import com.ats.dutyapp.model.DeptCount;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder>  {
    private ArrayList<DeptCount> deptList;
    private Context context;

    public DepartmentAdapter(ArrayList<DeptCount> deptList, Context context) {
        this.deptList = deptList;
        this.context = context;
    }

    @NonNull
    @Override
    public DepartmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_dept_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentAdapter.MyViewHolder myViewHolder, int i) {
        final DeptCount model=deptList.get(i);
        myViewHolder.tvEmpName.setText(model.getDeptName());
        myViewHolder.tvEmpCount.setText(model.getCompleted()+"/"+model.getTotal());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                HomeActivity activity = (HomeActivity) context;
                EmployeeDashboardFragment adf = new EmployeeDashboardFragment();
                Bundle args = new Bundle();
                args.putString("model",json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return deptList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmpName,tvEmpCount;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=itemView.findViewById(R.id.tvEmpName);
            tvEmpCount=itemView.findViewById(R.id.tvEmpCount);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
