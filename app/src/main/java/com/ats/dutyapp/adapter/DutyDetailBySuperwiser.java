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
import com.ats.dutyapp.model.TaskDetailDisplay;

import java.util.ArrayList;

public class DutyDetailBySuperwiser extends RecyclerView.Adapter<DutyDetailBySuperwiser.MyViewHolder> {
    private ArrayList<TaskDetailDisplay> dutyDetailList;
    private  Context context;

    public DutyDetailBySuperwiser(ArrayList<TaskDetailDisplay> dutyDetailList, Context context) {
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
        TaskDetailDisplay model=dutyDetailList.get(i);

        Log.e("DUTY DETAIL BIN","------------------"+model);
        myViewHolder.tvTaskName.setText(""+model.getDutyName());
        myViewHolder.tvWeight.setText(""+model.getTaskWeight());
        myViewHolder.tvDate.setText(""+model.getCreatedDate());

    }

    @Override
    public int getItemCount() {
        return dutyDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName,tvWeight,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName=itemView.findViewById(R.id.tvTaskName);
            tvWeight=itemView.findViewById(R.id.tvWeight);
            tvDate=itemView.findViewById(R.id.tvDate);
        }
    }
}
