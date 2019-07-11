package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.EmpCount;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardEmployeeAdapter extends RecyclerView.Adapter<DashboardEmployeeAdapter.MyViewHolder> {
    private ArrayList<EmpCount> empList;
    private Context context;

    public DashboardEmployeeAdapter(ArrayList<EmpCount> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardEmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_emp_dashboard, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardEmployeeAdapter.MyViewHolder myViewHolder, int i) {
        final EmpCount model=empList.get(i);
        myViewHolder.tvEmpName.setText(model.getEmpName());
        myViewHolder.tvEmpDesig.setText(model.getDesgName());
        myViewHolder.tvEmpCount.setText(model.getCompleted()+"/"+model.getCompleted());

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivPhoto;
        private TextView tvEmpName,tvEmpDesig,tvEmpCount;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto=itemView.findViewById(R.id.ivEmployee);
            tvEmpName=itemView.findViewById(R.id.tvEmpName);
            tvEmpDesig=itemView.findViewById(R.id.tvEmpDesig);
            tvEmpCount=itemView.findViewById(R.id.tvEmpCount);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
