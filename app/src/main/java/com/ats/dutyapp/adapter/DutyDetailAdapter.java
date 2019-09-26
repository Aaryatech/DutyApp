package com.ats.dutyapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.activity.ImageZoomActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.DutyDetailFragment;
import com.ats.dutyapp.model.DutyDetail;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class DutyDetailAdapter extends RecyclerView.Adapter<DutyDetailAdapter.MyViewHolder> {
    private ArrayList<DutyDetail> dutyDetailList;
    private static Context context;
    String language;
    int languageId;


    public DutyDetailAdapter(ArrayList<DutyDetail> dutyDetailList, Context context) {
        this.dutyDetailList = dutyDetailList;
        this.context = context;

    }

    @NonNull
    @Override
    public DutyDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_duty_detail_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DutyDetailAdapter.MyViewHolder myViewHolder, final int i) {
        final DutyDetail model=dutyDetailList.get(i);
        Log.e("Model Duty Adapter","------------------"+model);

        //language = CustomSharedPreference.LANGUAGE_SELECTED;
        language = CustomSharedPreference.getString(context,CustomSharedPreference.LANGUAGE_SELECTED);
        Log.e("LANGUAGE","----------------------------------------"+language);

        myViewHolder.tvWeight.setText("Wgt "+model.getTaskWeight());
        myViewHolder.tvRemark.setText("Remark : "+model.getRemark());

        myViewHolder.ivPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", Constants.IMAGE_URL + model.getPhoto1());
                context.startActivity(intent);
            }
        });

        myViewHolder.ivPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", Constants.IMAGE_URL + model.getPhoto2());
                context.startActivity(intent);
            }
        });

        if(language.equalsIgnoreCase("1"))
        {
            myViewHolder.tvTaskName.setText(""+model.getTaskNameEng());
            myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDescEng());
            Log.e("LANGUAGE","------------------------------ENG--------------------------------------");
        }else if(language.equalsIgnoreCase("2"))
        {
            myViewHolder.tvTaskName.setText(""+model.getTaskNameMar());
            myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDescMar());
            Log.e("LANGUAGE","------------------------------MAR--------------------------------------");
        }else if(language.equalsIgnoreCase("3"))
        {
            myViewHolder.tvTaskName.setText(""+model.getTaskNameHin());
            myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDescHin());
            Log.e("LANGUAGE","------------------------------HIN--------------------------------------");
        }


        String imageUri = String.valueOf(model.getPhoto1());
        try {
            Picasso.with(context).load(Constants.IMAGE_URL+imageUri).placeholder(context.getResources().getDrawable(R.drawable.ic_photo)).into(myViewHolder.ivPhoto1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String imageUri1 = String.valueOf(model.getPhoto2());
        try {
            Picasso.with(context).load(Constants.IMAGE_URL+imageUri1).placeholder(context.getResources().getDrawable(R.drawable.ic_photo)).into(myViewHolder.ivPhoto2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(model.getTaskStatus()==0)
        {
            myViewHolder.btnDone.setVisibility(View.VISIBLE);
        }else if(model.getTaskStatus()==1)
        {
            myViewHolder.btnDone.setVisibility(View.GONE);
            myViewHolder.tvTaskName.setPaintFlags(myViewHolder.tvTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            myViewHolder.tvTaskDesc.setPaintFlags(myViewHolder.tvTaskDesc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            myViewHolder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorLightPink));
        }

        if(model.getPhotoReq()==0 && model.getRemarkReq()==1) {
            myViewHolder.linearLayoutPhoto.setVisibility(View.GONE);
            myViewHolder.tvRemark.setVisibility(View.VISIBLE);
        }else if(model.getPhotoReq()==1 && model.getRemarkReq()==0) {
            myViewHolder.linearLayoutPhoto.setVisibility(View.VISIBLE);
            myViewHolder.tvRemark.setVisibility(View.GONE);
        }else if(model.getPhotoReq()==1 && model.getRemarkReq()==1)
        {
            myViewHolder.linearLayoutPhoto.setVisibility(View.VISIBLE);
            myViewHolder.tvRemark.setVisibility(View.VISIBLE);
        }

        myViewHolder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getRemarkReq()==1 || model.getPhotoReq()==1) {
                   // new AddApproveDialog(context, model).show();

//                    Gson gson = new Gson();
//                    String json = gson.toJson(model);
//
//                    Intent intent = new Intent(context, RemarkActivity.class);
//                    Bundle args = new Bundle();
//                    args.putString("model", json);
//                    intent.putExtra("model", json);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(intent);

                    new DutyDetailFragment().onClickData(i,context);


                }else if(model.getRemarkReq()==0 && model.getPhotoReq()==0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to done task ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getTaskDone(model.getTaskDoneDetailId(),model.getTaskDoneHeaderId(),model.getPhotoReq(),model.getRemarkReq(),"","","","","","",1);

                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dutyDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName,tvWeight,tvRemark,tvTaskDesc;
        private ImageView ivPhoto1,ivPhoto2;
        private LinearLayout linearLayoutPhoto;
        private Button btnDone;
        private CardView cardView;
        private CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTaskName=itemView.findViewById(R.id.tvTaskName);
            tvWeight=itemView.findViewById(R.id.tvWeight);
            tvRemark=itemView.findViewById(R.id.tvRemark);
            tvTaskDesc=itemView.findViewById(R.id.tvTaskDesc);
            ivPhoto1=itemView.findViewById(R.id.ivPhoto1);
            ivPhoto2=itemView.findViewById(R.id.ivPhoto2);
            btnDone=itemView.findViewById(R.id.btnDone);
            checkBox=itemView.findViewById(R.id.checkbox);
            cardView=itemView.findViewById(R.id.cardView);
            linearLayoutPhoto=itemView.findViewById(R.id.linearLayoutPhoto);

        }
    }



    private static void getTaskDone(Integer taskDoneDetailId, Integer taskDoneHeaderId, Integer photoReq, Integer remarkReq, String photo1, String photo2, String photo3, String photo4, String photo5, String strRemark, int status) {

        Log.e("PARAMETER","                 DETAIL ID     "+taskDoneDetailId +"              HEADER ID       "+taskDoneHeaderId +"       PHOTO REQ      "+photoReq  +"      REMARK ID         "+remarkReq+"      PHOTO 1         "+photo1+"      PHOTO 1         "+photo1+"      PHOTO 2         "+photo2+"      PHOTO 3         "+photo3+"      PHOTO 4         "+photo4+"      PHOTO 5         "+photo5+"      REMARK         "+strRemark+"      STATUS      "+status);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateTaskStatus(taskDoneDetailId,taskDoneHeaderId,photoReq,remarkReq,photo1,photo2,photo3,photo4,photo5,strRemark,status);
            listCall.enqueue(new retrofit2.Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DONE TASK: ", " - " + response.body());

                            if (!response.body().getError()) {

                                Toast.makeText(context, "Task Done Successfully", Toast.LENGTH_SHORT).show();

                                HomeActivity activity = (HomeActivity) context;

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new DutyDetailFragment(), "MainFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(context, "Unable to process1", Toast.LENGTH_SHORT).show();
                            }
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process2", Toast.LENGTH_SHORT).show();
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

}
