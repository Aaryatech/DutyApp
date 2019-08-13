package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.TaskList;

import java.util.ArrayList;

public class TaskListReportAdapter extends RecyclerView.Adapter<TaskListReportAdapter.MyViewHolder> {
    private ArrayList<TaskList> taskList;
    private static Context context;

    public TaskListReportAdapter(ArrayList<TaskList> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskListReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_detail_pdf_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListReportAdapter.MyViewHolder myViewHolder, int i) {
        TaskList model=taskList.get(i);
        myViewHolder.tvName.setText(""+model.getTaskNameEng());
        myViewHolder.tvWeight.setText("Wgt  "+model.getTaskWeight());
        myViewHolder.tvTaskDesc.setText(""+model.getTaskDescEng());

        if(model.getPhotoReq()==0)
        {
            myViewHolder.tvPhotReq.setText("NO");
        }else if(model.getPhotoReq()==1)
        {
            myViewHolder.tvPhotReq.setText("YES"+" ("+model.getTaskTime()+")");
        }

        if(model.getRemarkReq()==0)
        {
            myViewHolder.tvRemReq.setText("NO");
        }else if(model.getRemarkReq()==1)
        {
            myViewHolder.tvRemReq.setText("YES");
        }

        if(model.getTimeReq()==0)
        {
            myViewHolder.tvTimeReq.setText("NO");
        }else if(model.getTimeReq()==1)
        {
            myViewHolder.tvTimeReq.setText("YES");
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPhotReq, tvRemReq,tvWeight,tvTimeReq,tvTaskDesc;
        public LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhotReq = itemView.findViewById(R.id.tvPhotoReq);
            tvRemReq = itemView.findViewById(R.id.tvRemReq);
            tvTimeReq = itemView.findViewById(R.id.tvTimeReq);
            tvTaskDesc = itemView.findViewById(R.id.tvTaskDesc);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
