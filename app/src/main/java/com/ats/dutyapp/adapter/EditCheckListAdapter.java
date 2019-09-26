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
import com.ats.dutyapp.fragment.EditChecklistFragment;
import com.ats.dutyapp.model.ChecklistDetail;

import java.util.ArrayList;

public class EditCheckListAdapter extends RecyclerView.Adapter<EditCheckListAdapter.MyViewHolder> {
    private ArrayList<ChecklistDetail> checkList;
    private Context context;

    public EditCheckListAdapter(ArrayList<ChecklistDetail> checkList, Context context) {
        this.checkList = checkList;
        this.context = context;
    }
    @NonNull
    @Override
    public EditCheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_check_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EditCheckListAdapter.MyViewHolder myViewHolder, final int i) {
        final ChecklistDetail model=checkList.get(i);

        if(model.getDelStatus()==1) {
            Log.e("Delete Status","----------------------------");
            myViewHolder.tvCheckListName.setText(model.getChecklist_desc());
        }

        Log.e("List","------------------------------------------------------"+checkList.get(i));
        Log.e("List size","------------------------------------------------------"+checkList.size());

        myViewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Pos","------------------------------------------------------"+i);

                for(int i=0;i<checkList.size();i++) {
                    if (model.getChecklistDetailId() == checkList.get(i).getChecklistDetailId()) {
                        Log.e("Pos Comp","------------------------------------------------------"+model.getChecklistDetailId()+"     "+checkList.get(i).getChecklistDetailId());
                       // checkList.remove(i);
                        checkList.get(i).setDelStatus(0);
                        //notifyItemRemoved(i);
                        notifyDataSetChanged();
                        break;
                       // notifyItemRemoved(i);
                    }

                }
                Log.e("List Pos","------------------------------------------------------"+checkList.get(i));

            }
        });

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "Edit Success.....", Toast.LENGTH_SHORT).show();
                new EditChecklistFragment().onClickData(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count=0;
        for(int i=0;i<checkList.size();i++) {
        if(checkList.get(i).getDelStatus()==1)
        {
            count++;
        }
        }
        return count;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCheckListName,tvCheckListPhoto;
        public ImageView ivEdit,ivClose;
        public LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCheckListName=(itemView).findViewById(R.id.tvCheckListName);
            tvCheckListPhoto=(itemView).findViewById(R.id.tvCheckListPhoto);
            ivEdit=(itemView).findViewById(R.id.ivEdit);
            ivClose=(itemView).findViewById(R.id.ivClose);
            linearLayout=(itemView).findViewById(R.id.linearLayout);

        }
    }
}
