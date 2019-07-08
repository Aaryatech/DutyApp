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
import com.ats.dutyapp.activity.MainActivity;
import com.ats.dutyapp.fragment.DutyDetailFragment;
import com.ats.dutyapp.model.DutyHeader;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DutyListAdapter extends RecyclerView.Adapter<DutyListAdapter.MyViewHolder> {
    private ArrayList<DutyHeader> dutyList;
    private Context context;

    public DutyListAdapter(ArrayList<DutyHeader> dutyList, Context context) {
        this.dutyList = dutyList;
        this.context = context;
    }

    @NonNull
    @Override
    public DutyListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_duty_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DutyListAdapter.MyViewHolder myViewHolder, int i) {
            final DutyHeader model=dutyList.get(i);
            myViewHolder.tvDutyName.setText(""+model.getDutyName());
            myViewHolder.tvDutyCount.setText("Count : "+model.getTaskCompleteWt()+"/"+model.getDutyWeight());
            myViewHolder.tvTimeOn.setText("From Time : "+model.getShiftFromTime());
            myViewHolder.tvTimeOff.setText("To Time : "+model.getShiftToTime());
            myViewHolder.tvDate.setText(""+model.getTaskDate());

            myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String json = gson.toJson(model);

                    MainActivity activity = (MainActivity) context;
                    DutyDetailFragment adf = new DutyDetailFragment();
                    Bundle args = new Bundle();
                    args.putString("model",json);
                    adf.setArguments(args);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DutyFragment").commit();

                }
            });
    }

    @Override
    public int getItemCount() {
        return dutyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDutyName,tvDutyCount,tvTimeOff,tvTimeOn,tvDate;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDutyName=itemView.findViewById(R.id.tvDutyName);
            tvDutyCount=itemView.findViewById(R.id.tvDutyCount);
            tvTimeOff=itemView.findViewById(R.id.tvTimeEnd);
            tvTimeOn=itemView.findViewById(R.id.tvTimeStart);
            tvDate=itemView.findViewById(R.id.tvDate);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
