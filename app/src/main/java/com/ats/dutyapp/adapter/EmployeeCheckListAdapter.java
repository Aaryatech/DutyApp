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
import com.ats.dutyapp.model.Employee;

import java.util.ArrayList;

public class EmployeeCheckListAdapter extends RecyclerView.Adapter<EmployeeCheckListAdapter.MyViewHolder> {
    private ArrayList<Employee> empList;
    private Context context;

    public EmployeeCheckListAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeCheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_employee_checklist, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeCheckListAdapter.MyViewHolder myViewHolder, int i) {
        final Employee model=empList.get(i);
        final int pos = i;
        myViewHolder.tvEmpName.setText(model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());

        myViewHolder.checkBox.setChecked(empList.get(i).isChecked());

        myViewHolder.checkBox.setTag(empList.get(i));

        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Employee employee = (Employee) cb.getTag();

                employee.setChecked(cb.isChecked());
                empList.get(pos).setChecked(cb.isChecked());

            }
        });


//        if(model.isChecked())
//        {
//            myViewHolder.checkBox.setChecked(true);
//        }else{
//            myViewHolder.checkBox.setChecked(false);
//        }
//
//        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    model.setChecked(true);
//
//                } else {
//                    model.setChecked(false);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmpName;
        public CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(itemView).findViewById(R.id.tvEmpName);
            checkBox=(itemView).findViewById(R.id.checkbox);
        }
    }
}
