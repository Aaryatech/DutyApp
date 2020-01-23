package com.ats.dutyapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.ChatActivity;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.EditTaskFragment;
import com.ats.dutyapp.fragment.TaskListFragment;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatHeaderListAdapter extends RecyclerView.Adapter<ChatHeaderListAdapter.MyViewHolder>  {
    private ArrayList<ChatTask> taskList;
    private static Context context;

    public ChatHeaderListAdapter(ArrayList<ChatTask> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_task_list_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final ChatTask model =taskList.get(i);
        myViewHolder.tvCompletionDate.setText(""+model.getLastDate());
        myViewHolder.tvGenrateDate.setText(""+model.getCreatedDate());
        myViewHolder.tvEmpName.setText(""+model.getAssignUserNames());
        myViewHolder.tvRemark.setText(""+model.getTaskCompleteRemark());
        myViewHolder.tvTaskName.setText(""+model.getHeaderName());

        if(model.getStatus()==0)
        {
            myViewHolder.tvStatus.setText("On Going");
        }else if(model.getStatus()==1)
        {
            myViewHolder.tvStatus.setText("Request Close");
        }else if(model.getStatus()==2)
        {
            myViewHolder.tvStatus.setText("Close Task");
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("header",json);
                context.startActivity(intent);
            }
        });

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            Fragment adf = new EditTaskFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            args.putInt("type", 0);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "TaskListFragment").commit();

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGenrateDate,tvCompletionDate,tvRemark,tvEmpName,tvStatus,tvTaskName;
        public CardView cardView;
        public ImageView ivEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenrateDate=(itemView).findViewById(R.id.tvGenrateDate);
            tvCompletionDate=(itemView).findViewById(R.id.tvCompletionDate);
            tvRemark=(itemView).findViewById(R.id.tvRemark);
            tvEmpName=(itemView).findViewById(R.id.tvEmpName);
            tvStatus=(itemView).findViewById(R.id.tvStatus);
            tvTaskName=(itemView).findViewById(R.id.tvTaskName);
            cardView=(itemView).findViewById(R.id.cardView);
            ivEdit=(itemView).findViewById(R.id.ivEdit);
        }
    }
}
