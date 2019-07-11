package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.AssignDetail;

import java.util.ArrayList;

public class DutyDetailBySuperwiser extends RecyclerView.Adapter<DutyDetailBySuperwiser.MyViewHolder> {
    private ArrayList<AssignDetail> dutyDetailList;
    private  Context context;
    AssigneEmployeeAdapter adapterEmp;

    public DutyDetailBySuperwiser(ArrayList<AssignDetail> dutyDetailList, Context context) {
        this.dutyDetailList = dutyDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public DutyDetailBySuperwiser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_detail_list_superwiser, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DutyDetailBySuperwiser.MyViewHolder myViewHolder, int i) {
        AssignDetail model=dutyDetailList.get(i);

        Log.e("DUTY DETAIL BIN","------------------"+model);
        myViewHolder.tvTaskName.setText(""+model.getDutyName());
        myViewHolder.tvTaskStartTime.setText("From Time : "+model.getShiftFromTime());
        myViewHolder.tvTaskEndTime.setText("To Time : "+model.getShiftToTime());
        myViewHolder.tvDate.setText(""+model.getAssignDate());
        myViewHolder.tvAssignName.setText(""+model.getTaskAssignUserName());


//            if (model.getEmpList() != null) {
//            ArrayList<EmpList> detailList = new ArrayList<>();
//            for (int j = 0; j < model.getEmpList().size(); j++) {
//                detailList.add(model.getEmpList().get(j));
//            }
//
//                adapterEmp = new AssigneEmployeeAdapter(detailList, context);
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
//                myViewHolder.recyclerView.setLayoutManager(mLayoutManager);
//                myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
//                myViewHolder.recyclerView.setAdapter(adapterEmp);
//
//            }
    }

    @Override
    public int getItemCount() {
        return dutyDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTaskStartTime,tvTaskEndTime,tvDate,tvTaskName,tvAssignName;
        public RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskStartTime=itemView.findViewById(R.id.tvTaskStartTime);
            tvTaskEndTime=itemView.findViewById(R.id.tvTaskEndTime);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvTaskName=itemView.findViewById(R.id.tvTaskName);
            tvAssignName=itemView.findViewById(R.id.tvAssignName);
            recyclerView=itemView.findViewById(R.id.recyclerView);
        }
    }
}
