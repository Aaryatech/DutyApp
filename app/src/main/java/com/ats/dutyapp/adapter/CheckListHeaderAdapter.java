package com.ats.dutyapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.CheckListFragment;
import com.ats.dutyapp.fragment.EditChecklistFragment;
import com.ats.dutyapp.model.ChecklistDetail;
import com.ats.dutyapp.model.ChecklistHeader;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckListHeaderAdapter extends RecyclerView.Adapter<CheckListHeaderAdapter.MyViewHolder>  {
    private ArrayList<ChecklistHeader> checkHeaderList;
    private Context context;


    public CheckListHeaderAdapter(ArrayList<ChecklistHeader> checkHeaderList, Context context) {
        this.checkHeaderList = checkHeaderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckListHeaderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_checklist_header_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckListHeaderAdapter.MyViewHolder myViewHolder, int i) {
            final ChecklistHeader model=checkHeaderList.get(i);
            myViewHolder.tvChecklistName.setText(model.getChecklistName());
            myViewHolder.tvDept.setText("Department Name : "+model.getExVar1());


        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItems.setVisibility(View.VISIBLE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
        } else {
            myViewHolder.llItems.setVisibility(View.GONE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
        }

        myViewHolder.tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItems.setVisibility(View.VISIBLE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItems.setVisibility(View.GONE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
                }
            }
        });


        if (model.getChecklistDetail() != null) {
            ArrayList<ChecklistDetail> detailList = new ArrayList<>();
            for (int j = 0; j < model.getChecklistDetail().size(); j++) {
                detailList.add(model.getChecklistDetail().get(j));
            }
            ChecklistDetailAdapter adapter = new ChecklistDetailAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(mLayoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            myViewHolder.recyclerView.setAdapter(adapter);
        }

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
                            Fragment adf = new EditChecklistFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "CheckListFragment").commit();

                        }else if(menuItem.getItemId()==R.id.action_delete)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete Checklist?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                     deleteChecklist(model.getChecklistHeaderId());
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

    private void deleteChecklist(int checklistHeaderId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteChecklistHeader(checklistHeaderId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DELETE CHECKLIST: ", " - " + response.body());

                            if (!response.body().getError()) {

                                HomeActivity activity = (HomeActivity) context;

                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new CheckListFragment(), "MainFragment");
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
        return checkHeaderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDept,tvPhotoReq,tvChecklistName,tvItems;
        public ImageView ivEdit,imageView;
        public RecyclerView recyclerView;
        public CardView cardView;
        public LinearLayout llItems;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDept=(itemView).findViewById(R.id.tvDept);
            tvPhotoReq=(itemView).findViewById(R.id.tvPhotoReq);
            tvChecklistName=(itemView).findViewById(R.id.tvChecklistName);
            ivEdit=(itemView).findViewById(R.id.ivEdit);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            tvItems = itemView.findViewById(R.id.tvItems);
            llItems = itemView.findViewById(R.id.llItems);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
