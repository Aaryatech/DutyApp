package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.EmpList;

import java.util.ArrayList;

public class AssigneEmployeeAdapter extends RecyclerView.Adapter<AssigneEmployeeAdapter.MyViewHolder> {
    private ArrayList<EmpList> empList;
    private Context context;

    public AssigneEmployeeAdapter(ArrayList<EmpList> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssigneEmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_assigne_emp, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssigneEmployeeAdapter.MyViewHolder myViewHolder, int i) {
        final EmpList model=empList.get(i);
        final int pos = i;
        myViewHolder.tvEmpName.setText(model.getEmpName());

//        if(model.getAssigned())
//        {
//            myViewHolder.checkBox.setChecked(true);
//        }else{
//            myViewHolder.checkBox.setChecked(false);
//        }

        myViewHolder.checkBox.setChecked(empList.get(i).getAssigned());

        myViewHolder.checkBox.setTag(empList.get(i));


//        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    model.setAssigned(true);
//                } else {
//                    model.setAssigned(false);
//
//                }
//
//            }
//        });

        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                EmpList employee = (EmpList) cb.getTag();

                employee.setAssigned(cb.isChecked());
                empList.get(pos).setAssigned(cb.isChecked());

            }
        });

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public void updateList(ArrayList<EmpList> temp) {
        empList = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmpName,tvEmpDesig;
        private CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=itemView.findViewById(R.id.tvEmpName);
            tvEmpDesig=itemView.findViewById(R.id.tvEmpDesig);
            tvEmpDesig=itemView.findViewById(R.id.tvEmpDesig);
            checkBox=itemView.findViewById(R.id.checkbox_emp);
        }
    }
}
