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
import com.ats.dutyapp.model.ChecklistDetail;

import java.util.ArrayList;

public class ChecklistDetailAdapter extends RecyclerView.Adapter<ChecklistDetailAdapter.MyViewHolder> {
    private ArrayList<ChecklistDetail> detailList;
    private Context context;

    public ChecklistDetailAdapter(ArrayList<ChecklistDetail> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChecklistDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_checklist_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistDetailAdapter.MyViewHolder myViewHolder, int i) {
        ChecklistDetail model=detailList.get(i);
        myViewHolder.tvChecklistName.setText(""+model.getChecklist_desc());

        if(model.getIsPhoto()==0)
        {
            myViewHolder.tvPhotoReq.setText("No");
        }else if(model.getIsPhoto()==1)
        {
            myViewHolder.tvPhotoReq.setText("Yes");
        }
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvChecklistName, tvPhotoReq;
        public LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvChecklistName = itemView.findViewById(R.id.tvChecklistName);
            tvPhotoReq = itemView.findViewById(R.id.tvPhotoReq);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
