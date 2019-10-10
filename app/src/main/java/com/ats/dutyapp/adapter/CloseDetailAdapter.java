package com.ats.dutyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.activity.ImageZoomActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.CloseDetailFragment;
import com.ats.dutyapp.fragment.TabFragment;
import com.ats.dutyapp.model.ActionDetailList;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseDetailAdapter extends RecyclerView.Adapter<CloseDetailAdapter.MyViewHolder> {

    private ArrayList<ActionDetailList> detailList;
    private static Context context;

    public CloseDetailAdapter(ArrayList<ActionDetailList> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;

    }

    @NonNull
    @Override
    public CloseDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_close_detail_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CloseDetailAdapter.MyViewHolder myViewHolder, final int i) {
        final ActionDetailList model=detailList.get(i);
        final int pos = i;
        myViewHolder.tvChecklist.setText(""+model.getChecklistDesc());
        myViewHolder.tvDate.setText(""+model.getCheckDate());

        String imageUri = String.valueOf(model.getActionPhoto());
        try {
            Picasso.with(context).load(Constants.IMAGE_URL+imageUri).placeholder(context.getResources().getDrawable(R.drawable.ic_photo)).into(myViewHolder.ivPhoto1);

        } catch (Exception e) {

        }

        myViewHolder.ivPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", Constants.IMAGE_URL + model.getActionPhoto());
                context.startActivity(intent);
            }
        });

        String imageUri1 = String.valueOf(model.getClosedPhoto());
        try {
            Picasso.with(context).load(Constants.IMAGE_URL+imageUri1).placeholder(context.getResources().getDrawable(R.drawable.ic_photo)).into(myViewHolder.ivPhoto2);

        } catch (Exception e) {

        }

        myViewHolder.ivPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", Constants.IMAGE_URL + model.getClosedPhoto());
                context.startActivity(intent);
            }
        });


        if(model.getIsPhoto()==0)
        {
            myViewHolder.tvPhotoReq.setText("Photo Req : No");
        }else if(model.getIsPhoto()==1) {
            myViewHolder.tvPhotoReq.setText("Photo Req : Yes");
        }

        myViewHolder.checkBox.setChecked(detailList.get(i).isChecked());

        myViewHolder.checkBox.setTag(detailList.get(i));

        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ActionDetailList detail = (ActionDetailList) cb.getTag();

                detail.setChecked(cb.isChecked());
                detailList.get(pos).setChecked(cb.isChecked());

                new CloseDetailFragment().onClickActivity(i,context);

            }
        });

//        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new CloseDetailFragment().onClickActivity(i,context);
//            }
//        });

        myViewHolder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CloseDetailFragment().onClickReject(i,context);
            }
        });

        myViewHolder.tvPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPending(model.getActionDetailId(),0,"");
            }
        });

        if(model.getCheckStatus()==1)
        {
            myViewHolder.checkBox.setVisibility(View.VISIBLE);
            myViewHolder.tvReject.setVisibility(View.VISIBLE);
            myViewHolder.tvPending.setVisibility(View.GONE);
        }else if(model.getCheckStatus()==0)
        {
            myViewHolder.checkBox.setVisibility(View.VISIBLE);
            myViewHolder.tvReject.setVisibility(View.VISIBLE);
            myViewHolder.tvPending.setVisibility(View.GONE);
        }else if(model.getCheckStatus()==3)
        {
            myViewHolder.checkBox.setVisibility(View.GONE);
            myViewHolder.tvReject.setVisibility(View.GONE);
            myViewHolder.tvPending.setVisibility(View.VISIBLE);
        }


    }

    private void getPending(Integer actionDetailId, int status, String photo) {
        Log.e("PARAMETER","                 ACTION DETAIL ID     "+actionDetailId +"             STSTUS       "+status +"       PHOTO      "+photo);

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateClosedChecklistDetailStatus(actionDetailId,status,photo);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PENDING CHECKLIST: ", " - " + response.body());

                            if (!response.body().getError()) {

                                HomeActivity activity = (HomeActivity) context;

                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new TabFragment(), "MainFragment");
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
        return detailList.size();
    }

//    public void onClickActivity(int position, FragmentActivity activity) {
//        detailList.get(position).setChecked();
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvChecklist,tvPending,tvReject,tvPhotoReq,tvDate;
        public CheckBox checkBox;
        public LinearLayout linearLayout;
        public ImageView ivPhoto2,ivPhoto1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChecklist=(itemView).findViewById(R.id.tvChecklist);
            tvPending=(itemView).findViewById(R.id.tvPending);
            tvReject=(itemView).findViewById(R.id.tvReject);
            tvPhotoReq=(itemView).findViewById(R.id.tvPhotoReq);
            tvDate=(itemView).findViewById(R.id.tvDate);
            checkBox=(itemView).findViewById(R.id.checkbox);
            linearLayout=(itemView).findViewById(R.id.linearLayout);
            ivPhoto2=(itemView).findViewById(R.id.ivPhoto2);
            ivPhoto1=(itemView).findViewById(R.id.ivPhoto1);
        }
    }
}
