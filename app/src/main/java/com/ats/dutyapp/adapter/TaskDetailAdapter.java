package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.TaskDetailDisplay;
import com.ats.dutyapp.utils.CustomSharedPreference;

import java.util.ArrayList;

public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailAdapter.MyViewHolder>  {
    private ArrayList<TaskDetailDisplay> taskList;
    private static Context context;
    String language;

    public TaskDetailAdapter(ArrayList<TaskDetailDisplay> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;

    }

    @NonNull
    @Override
    public TaskDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_task_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailAdapter.MyViewHolder myViewHolder, int i) {
        final TaskDetailDisplay model=taskList.get(i);
        Log.e("Model Duty Adapter","------------------"+model);

        //language = CustomSharedPreference.LANGUAGE_SELECTED;
        language = CustomSharedPreference.getString(context,CustomSharedPreference.LANGUAGE_SELECTED);
        Log.e("LANGUAGE","----------------------------------------"+language);

        myViewHolder.tvWeight.setText("Wgt "+model.getTaskWeight());
      //  myViewHolder.tvRemark.setText("Remark : "+model.getRemark());


        if(language.equalsIgnoreCase("1"))
        {
            myViewHolder.tvTaskName.setText(""+model.getTaskNameEng());
            myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDescEng());
            Log.e("LANGUAGE","------------------------------ENG--------------------------------------");
        }else if(language.equalsIgnoreCase("2"))
        {
            myViewHolder.tvTaskName.setText(""+model.getTaskNameMar());
            myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDescMar());
            Log.e("LANGUAGE","------------------------------MAR--------------------------------------");
        }else if(language.equalsIgnoreCase("3"))
        {
            myViewHolder.tvTaskName.setText(""+model.getTaskNameHin());
            myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDescHin());
            Log.e("LANGUAGE","------------------------------HIN--------------------------------------");
        }


//        String imageUri = String.valueOf(model.getPhoto1());
//        try {
//            Picasso.with(context).load(Constants.IMAGE_URL+imageUri).placeholder(context.getResources().getDrawable(R.drawable.ic_photo)).into(myViewHolder.ivPhoto1);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String imageUri1 = String.valueOf(model.getPhoto2());
//        try {
//            Picasso.with(context).load(Constants.IMAGE_URL+imageUri1).placeholder(context.getResources().getDrawable(R.drawable.ic_photo)).into(myViewHolder.ivPhoto2);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        if(model.getPhotoReq()==0 && model.getRemarkReq()==1) {
//            myViewHolder.linearLayoutPhoto.setVisibility(View.GONE);
//            myViewHolder.tvRemark.setVisibility(View.VISIBLE);
//        }else if(model.getPhotoReq()==1 && model.getRemarkReq()==0) {
//            myViewHolder.linearLayoutPhoto.setVisibility(View.VISIBLE);
//            myViewHolder.tvRemark.setVisibility(View.GONE);
//        }else if(model.getPhotoReq()==1 && model.getRemarkReq()==1)
//        {
//            myViewHolder.linearLayoutPhoto.setVisibility(View.VISIBLE);
//            myViewHolder.tvRemark.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName,tvWeight,tvRemark,tvTaskDesc;
        private ImageView ivPhoto1,ivPhoto2;
        private LinearLayout linearLayoutPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName=itemView.findViewById(R.id.tvTaskName);
            tvWeight=itemView.findViewById(R.id.tvWeight);
            tvRemark=itemView.findViewById(R.id.tvRemark);
            tvTaskDesc=itemView.findViewById(R.id.tvTaskDesc);
            ivPhoto1=itemView.findViewById(R.id.ivPhoto1);
            ivPhoto2=itemView.findViewById(R.id.ivPhoto2);
            linearLayoutPhoto=itemView.findViewById(R.id.linearLayoutPhoto);
        }
    }
}
