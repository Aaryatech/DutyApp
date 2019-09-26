package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.fragment.AddCheckListFragment;
import com.ats.dutyapp.model.ChecklistDetail;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {
    private ArrayList<ChecklistDetail> checkList;
    private Context context;

    public CheckListAdapter(ArrayList<ChecklistDetail> checkList, Context context) {
        this.checkList = checkList;
        this.context = context;
    }


    @NonNull
    @Override
    public CheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_check_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckListAdapter.MyViewHolder myViewHolder, final int i) {
        ChecklistDetail model=checkList.get(i);
        myViewHolder.tvCheckListName.setText(model.getChecklist_desc());

        myViewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkList.remove(i);
                notifyItemRemoved(i);
            }
        });

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "Edit Success.....", Toast.LENGTH_SHORT).show();
                new AddCheckListFragment().onClickData(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
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
