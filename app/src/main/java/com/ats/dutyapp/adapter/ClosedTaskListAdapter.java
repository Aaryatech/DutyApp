package com.ats.dutyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.ats.dutyapp.activity.ChatActivity;
import com.ats.dutyapp.activity.ClosedChatActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClosedTaskListAdapter extends RecyclerView.Adapter<ClosedTaskListAdapter.MyViewHolder> {
    private ArrayList<ChatTask> taskList;
    private static Context context;
    private Activity activity;

    public ClosedTaskListAdapter(ArrayList<ChatTask> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;

    }

    public ClosedTaskListAdapter(ArrayList<ChatTask> taskList, Context context,Activity activity) {
        this.taskList = taskList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_task_header_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout llBack;
        private TextView tvName, tvCount, tvCloseReq;
        private CircleImageView ivPic;
        private View viewLine;
        private ImageView ivDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            llBack = (itemView).findViewById(R.id.llBack);
            tvName = (itemView).findViewById(R.id.tvName);
            tvCount = (itemView).findViewById(R.id.tvCount);
            ivPic = (itemView).findViewById(R.id.ivPic);
            tvCloseReq = (itemView).findViewById(R.id.tvCloseReq);
            viewLine = (itemView).findViewById(R.id.viewLine);
            ivDelete = (itemView).findViewById(R.id.ivDelete);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final ChatTask model = taskList.get(i);

        myViewHolder.ivDelete.setVisibility(View.GONE);

        myViewHolder.tvName.setText("" + model.getHeaderName());
        myViewHolder.tvCount.setVisibility(View.GONE);


        if (model.getStatus() == 2) {
            myViewHolder.viewLine.setVisibility(View.INVISIBLE);
            myViewHolder.tvCloseReq.setVisibility(View.VISIBLE);
            myViewHolder.tvCloseReq.setText("Closed by - " + model.getTaskCloseUserName());
        } else {
            myViewHolder.viewLine.setVisibility(View.INVISIBLE);
            myViewHolder.tvCloseReq.setVisibility(View.GONE);
        }

        try {
            String image = Constants.CHAT_IMAGE_URL + "/" + model.getImage();
            Log.e("TASK ADAPTER", "---------- IMAGE ---------- " + image);
            Picasso.with(context).load(image).placeholder(context.getResources().getDrawable(R.drawable.profile)).error(context.getResources().getDrawable(R.drawable.profile)).into(myViewHolder.ivPic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolder.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                Intent intent = new Intent(context, ClosedChatActivity.class);
                intent.putExtra("header", json);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
