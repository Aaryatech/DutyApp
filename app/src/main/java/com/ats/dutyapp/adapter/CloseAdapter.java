package com.ats.dutyapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.fragment.CloseDetailFragment;
import com.ats.dutyapp.model.ActionHeaderClose;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CloseAdapter  extends RecyclerView.Adapter<CloseAdapter.MyViewHolder> {
    private ArrayList<ActionHeaderClose> detailList;
    private static Context context;

    public CloseAdapter(ArrayList<ActionHeaderClose> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;

    }

    @NonNull
    @Override
    public CloseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_open_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CloseAdapter.MyViewHolder myViewHolder, int i) {
        final ActionHeaderClose model=detailList.get(i);
        myViewHolder.tvChecklistName.setText(""+model.getChecklistName());
        myViewHolder.tvCheckDate.setText(""+model.getActionDate());
        myViewHolder.tvDept.setText("Department : "+model.getDeptName());
        myViewHolder.tvEmpName.setText("Action Close Name : "+model.getClosedByName());
        myViewHolder.tvAssignByName.setText("Action By Name : "+model.getActionByName());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);
                HomeActivity activity = (HomeActivity) context;
                CloseDetailFragment adf = new CloseDetailFragment();
                Bundle args = new Bundle();
                args.putString("model",json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "TabFragment").commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCheckDate,tvChecklistName,tvDept,tvEmpName,tvAssignByName;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChecklistName=(itemView).findViewById(R.id.tvChecklistName);
            tvDept=(itemView).findViewById(R.id.tvDept);
            tvEmpName=(itemView).findViewById(R.id.tvEmpName);
            tvCheckDate=(itemView).findViewById(R.id.tvCheckDate);
            tvAssignByName=(itemView).findViewById(R.id.tvAssignByName);
            cardView=(itemView).findViewById(R.id.cardView);
        }
    }
}
