package com.ats.dutyapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.fragment.DutyDetailFragment;
import com.ats.dutyapp.model.DutyHeader;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DutyListAdapter extends RecyclerView.Adapter<DutyListAdapter.MyViewHolder> {
    private ArrayList<DutyHeader> dutyList;
    private Context context;
    ArrayList<Sync> syncArray = new ArrayList<>();
    private Login loginUserMain;

    public DutyListAdapter(ArrayList<DutyHeader> dutyList, Context context,ArrayList<Sync> syncArray,Login login) {
        this.dutyList = dutyList;
        this.context = context;
        this.syncArray = syncArray;
        this.loginUserMain = login;
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
            myViewHolder.tvTimeOn.setText("Time : "+model.getShiftFromTime()+"-"+model.getShiftToTime());
            //myViewHolder.tvTimeOff.setText("To Time : "+model.getShiftToTime());
            myViewHolder.tvDate.setText(""+model.getTaskDate());

        if(syncArray!=null) {
            for (int j = 0; j < syncArray.size(); j++) {
                if (syncArray.get(j).getSettingKey().equals("Employee")) {
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        myViewHolder.checkBox.setVisibility(View.GONE);


                    }
                } else if(syncArray.get(j).getSettingKey().equals("Supervisor")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        myViewHolder.checkBox.setVisibility(View.VISIBLE);


                    }
                }else if(syncArray.get(j).getSettingKey().equals("Admin")){
                    if (syncArray.get(j).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId()))) {
                        myViewHolder.checkBox.setVisibility(View.VISIBLE);


                    }
                }
            }
        }


        if(model.getChecked())
        {
            myViewHolder.checkBox.setChecked(true);
        }else{
            myViewHolder.checkBox.setChecked(false);
        }


        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    model.setChecked(true);

                } else {

                    model.setChecked(false);

                }

            }
        });

            myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String json = gson.toJson(model);

                    HomeActivity activity = (HomeActivity) context;
                    DutyDetailFragment adf = new DutyDetailFragment();
                    Bundle args = new Bundle();
                    args.putString("model",json);
                    adf.setArguments(args);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DutyFragment").commit();


//                    HomeActivity activity = (HomeActivity) context;
//                    DutyDetailBySuperwiserFragment adf = new DutyDetailBySuperwiserFragment();
//                    Bundle args = new Bundle();
//                    args.putString("model",json);
//                    adf.setArguments(args);
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DutyListSupFragment").commit();

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
        public CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDutyName=itemView.findViewById(R.id.tvDutyName);
            tvDutyCount=itemView.findViewById(R.id.tvDutyCount);
            tvTimeOff=itemView.findViewById(R.id.tvTimeEnd);
            tvTimeOn=itemView.findViewById(R.id.tvTimeStart);
            tvDate=itemView.findViewById(R.id.tvDate);
            cardView=itemView.findViewById(R.id.cardView);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
}
