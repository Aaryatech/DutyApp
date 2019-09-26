package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.ChecklistDetail;

import java.util.ArrayList;

public class ViewCheckListAdapter extends RecyclerView.Adapter<ViewCheckListAdapter.MyViewHolder>  {
    private ArrayList<ChecklistDetail> checkList;
    private Context context;

    public ViewCheckListAdapter(ArrayList<ChecklistDetail> checkList, Context context) {
        this.checkList = checkList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewCheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_view_checklist, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCheckListAdapter.MyViewHolder myViewHolder, int i) {
        ChecklistDetail model=checkList.get(i);
        myViewHolder.tv_checklist.setText(model.getChecklist_desc());
    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_checklist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_checklist=(itemView).findViewById(R.id.tv_checklist);
        }
    }
}
