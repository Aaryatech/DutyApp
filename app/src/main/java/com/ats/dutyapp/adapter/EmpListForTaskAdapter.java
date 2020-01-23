package com.ats.dutyapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.EditTaskFragment;
import com.ats.dutyapp.fragment.EmpTaskFragment;
import com.ats.dutyapp.model.Employee;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmpListForTaskAdapter extends RecyclerView.Adapter<EmpListForTaskAdapter.MyViewHolder>{

    private ArrayList<Employee> empList;
    private ArrayList<Employee> filteredEmpList;
    private Context context;

    public EmpListForTaskAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.filteredEmpList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_emp_list_for_task, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Employee emp=filteredEmpList.get(i);

        myViewHolder.tvName.setText(""+emp.getEmpFname()+" "+emp.getEmpMname()+" "+emp.getEmpSname());

        try {
            final String image = Constants.IMAGE_URL + emp.getEmpPhoto();

            Picasso.with(context)
                    .load(image)
                    .placeholder(context.getResources().getDrawable(R.drawable.profile))
                    .error(context.getResources().getDrawable(R.drawable.profile))
                    .into(myViewHolder.ivPic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolder.cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity activity=(HomeActivity) context;

                Fragment adf = new EmpTaskFragment();
                Bundle args = new Bundle();
                args.putString("empName", emp.getEmpFname()+" "+emp.getEmpMname()+" "+emp.getEmpSname());
                args.putInt("empId", emp.getEmpId());
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "EmpListForTaskFragment").commit();


            }
        });



    }

    @Override
    public int getItemCount() {
        return filteredEmpList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public CircleImageView ivPic;
        public CardView cardBack;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);
            ivPic=itemView.findViewById(R.id.ivPic);
            cardBack=itemView.findViewById(R.id.cardBack);

        }
    }



    public Filter getFilter() {
        Log.e("ADAPTER","---------------- Filter");

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredEmpList = empList;
                } else {
                    ArrayList<Employee> filteredList = new ArrayList<>();
                    for (Employee row : empList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getEmpFname().toLowerCase().contains(charString.toLowerCase()) || row.getEmpMname().toLowerCase().contains(charString.toLowerCase()) || row.getEmpSname().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    filteredEmpList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredEmpList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEmpList = (ArrayList<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
