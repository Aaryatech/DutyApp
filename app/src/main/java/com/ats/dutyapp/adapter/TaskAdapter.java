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
import com.ats.dutyapp.model.DutyDetail;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private ArrayList<DutyDetail> taskList;
    private static Context context;


    public TaskAdapter(ArrayList<DutyDetail> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;

    }

    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_task_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder myViewHolder, int i) {
        DutyDetail model=taskList.get(i);
        myViewHolder.tvTaskName.setText(model.getTaskName());
        myViewHolder.tvWeight.setText(model.getTaskWeight());

        if(model.getRemarkReq()==0)
        {
            myViewHolder.tvRemarkReq.setText("NO");
        }else if(model.getRemarkReq()==1)
        {
            myViewHolder.tvRemarkReq.setText("YES");
        }

        if(model.getPhotoReq()==0)
        {
            myViewHolder.tvPhotoReq.setText("NO");
        }else if(model.getPhotoReq()==1)
        {
            myViewHolder.tvPhotoReq.setText("YES");
        }


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName,tvWeight,tvRemarkReq,tvPhotoReq;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName=itemView.findViewById(R.id.tvTaskName);
            tvWeight=itemView.findViewById(R.id.tvWeight);
            tvRemarkReq=itemView.findViewById(R.id.tvRemarkReq);
            tvPhotoReq=itemView.findViewById(R.id.tvPhotoReq);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
