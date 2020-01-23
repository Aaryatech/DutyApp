package com.ats.dutyapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.fragment.EditChecklistFragment;
import com.ats.dutyapp.fragment.GroupwiseTaskListFragment;
import com.ats.dutyapp.model.GroupList;

import java.util.ArrayList;

public class GroupListDashAdapter extends RecyclerView.Adapter<GroupListDashAdapter.MyViewHolder> {
    private ArrayList<GroupList> grpList;
    private Context context;

    public GroupListDashAdapter(ArrayList<GroupList> grpList, Context context) {
        this.grpList = grpList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_grp_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final GroupList model=grpList.get(i);
        myViewHolder.tvGrpName.setText(model.getGroupName());
        myViewHolder.tvEmpName.setText(model.getGroupDesc());

        myViewHolder.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity activity = (HomeActivity) context;
                Fragment adf = new GroupwiseTaskListFragment();
                Bundle args = new Bundle();
                args.putInt("groupId", model.getGroupId());
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "GroupListDashFragment").commit();


            }
        });
    }


    @Override
    public int getItemCount() {
        return grpList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGrpName,tvEmpName;
        private ImageView ivEdit;
        private LinearLayout llBack;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGrpName=(itemView).findViewById(R.id.tvGrpName);
            tvEmpName=(itemView).findViewById(R.id.tvEmpName);
            ivEdit=(itemView).findViewById(R.id.ivEdit);
            llBack=(itemView).findViewById(R.id.llBack);
        }
    }
}