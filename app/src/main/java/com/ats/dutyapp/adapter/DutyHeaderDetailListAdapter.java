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
import com.ats.dutyapp.fragment.DutyDetailBySuperwiserFragment;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DutyHeaderDetailListAdapter extends RecyclerView.Adapter<DutyHeaderDetailListAdapter.MyViewHolder> {
    private ArrayList<DutyHeaderDetail> dutyList;
    private Context context;

    public DutyHeaderDetailListAdapter(ArrayList<DutyHeaderDetail> dutyList, Context context) {
        this.dutyList = dutyList;
        this.context = context;
    }

    @NonNull
    @Override
    public DutyHeaderDetailListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_duty_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DutyHeaderDetailListAdapter.MyViewHolder myViewHolder, int i) {
        final DutyHeaderDetail model=dutyList.get(i);
        myViewHolder.tvDutyName.setText(""+model.getDutyName());
        myViewHolder.tvDutyCount.setText("Wgt : "+model.getTotalTaskWt());
        myViewHolder.tvTimeOn.setText("Time : "+model.getShiftFromTime()+"-"+model.getShiftToTime());
       // myViewHolder.tvTimeOff.setText("To Time : "+model.getShiftToTime());
        myViewHolder.tvDate.setText(""+model.getCreatedDate());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                HomeActivity activity = (HomeActivity) context;
                DutyDetailBySuperwiserFragment adf = new DutyDetailBySuperwiserFragment();
                Bundle args = new Bundle();
                args.putString("model",json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DutyListSupFragment").commit();

//                    MainActivity activity = (MainActivity) context;
//                    DutyDetailFragment adf = new DutyDetailFragment();
//                    Bundle args = new Bundle();
//                    args.putString("model",json);
//                    adf.setArguments(args);
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DutyFragment").commit();


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
