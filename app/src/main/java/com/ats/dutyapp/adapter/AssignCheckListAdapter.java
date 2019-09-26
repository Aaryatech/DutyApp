package com.ats.dutyapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.AddAssignCheckListFragment;
import com.ats.dutyapp.fragment.AssignCheckListFragment;
import com.ats.dutyapp.model.AssignChecklist;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignCheckListAdapter extends RecyclerView.Adapter<AssignCheckListAdapter.MyViewHolder> {
    private ArrayList<AssignChecklist> assignList;
    private Context context;

    public AssignCheckListAdapter(ArrayList<AssignChecklist> assignList, Context context) {
        this.assignList = assignList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssignCheckListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_assign_checklist, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignCheckListAdapter.MyViewHolder myViewHolder, int i) {
            final AssignChecklist model=assignList.get(i);
            myViewHolder.tvDept.setText(model.getDeptName());
            myViewHolder.tvChecklist.setText(model.getChecklistName());
            myViewHolder.tvEmpName.setText(model.getAssignEmpName());

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {
                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new AddAssignCheckListFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "AssignCheckListFragment").commit();

                        }else if(menuItem.getItemId()==R.id.action_delete)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete assign checklist?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   deleteAssigneChecklist(model.getAssignId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void deleteAssigneChecklist(Integer assignId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteChecklistAssign(assignId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGNE CHECKLIST :", " ----------------------DELETE---------------------------- " + response.body());

                            if (!response.body().getError()) {

                                HomeActivity activity = (HomeActivity) context;

                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new AssignCheckListFragment(), "DashFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return assignList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDept,tvEmpName,tvChecklist;
        public ImageView ivEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDept=(itemView).findViewById(R.id.tvDept);
            tvEmpName=(itemView).findViewById(R.id.tvEmpName);
            tvChecklist=(itemView).findViewById(R.id.tvChecklist);
            ivEdit=(itemView).findViewById(R.id.ivEdit);
        }
    }
}
