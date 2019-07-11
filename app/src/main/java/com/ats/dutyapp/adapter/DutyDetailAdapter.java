package com.ats.dutyapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.MainActivity;
import com.ats.dutyapp.activity.RemarkActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyDetail;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class DutyDetailAdapter extends RecyclerView.Adapter<DutyDetailAdapter.MyViewHolder> {
    private ArrayList<DutyDetail> dutyDetailList;
    private static Context context;


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
    public void onBindViewHolder(@NonNull DutyDetailAdapter.MyViewHolder myViewHolder, int i) {
        final DutyDetail model=dutyDetailList.get(i);
        Log.e("Model Duty Adapter","------------------"+model);

        myViewHolder.tvTaskName.setText(""+model.getTaskName());
        myViewHolder.tvWeight.setText(""+model.getTaskWeight());
        myViewHolder.tvRemark.setText("Remark : "+model.getRemark());
        myViewHolder.tvTaskDesc.setText("Task Description : "+model.getTaskDesc());

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

                    Gson gson = new Gson();
                    String json = gson.toJson(model);

                    Intent intent = new Intent(context, RemarkActivity.class);
                    Bundle args = new Bundle();
                    args.putString("model", json);
                    intent.putExtra("model", json);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                }else {
                    getTaskDone(model.getTaskDoneDetailId(),model.getTaskDoneHeaderId(),model.getPhotoReq(),model.getRemarkReq(),"","","","","","",1);
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
            linearLayoutPhoto=itemView.findViewById(R.id.linearLayoutPhoto);

        }
    }

    private static class AddApproveDialog extends Dialog implements View.OnClickListener {
        public Button btnCancel, btnSubmit;
        public TextView tvLabPhoto1,tvLabPhoto2;
        public TextInputLayout textInputLayout;
        public LinearLayout linearLayoutPhoto1,linearLayoutPhoto2;
        DutyDetail dutyDetail;
        public EditText edRemark;
        private Context context;


        private ImageView ivCamera1, ivCamera2, ivPhoto1, ivPhoto2;
        private TextView tvPhoto1, tvPhoto2;
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "gfpl_security");
        File f;
        Bitmap myBitmap1 = null, myBitmap2 = null, myBitmap3 = null;
        public static String path1, imagePath1 = null, imagePath2 = null, imagePath3 = null;

        public AddApproveDialog(Context context, DutyDetail dutyDetail) {
            super(context);
            this.dutyDetail = dutyDetail;
            this.context = context;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dailog_layout);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            //  wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.x = 5;
            wlp.y = 5;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            edRemark = (EditText) findViewById(R.id.edRemark);
            ivCamera1 = (ImageView) findViewById(R.id.ivCamera1);
            ivCamera2 = (ImageView) findViewById(R.id.ivCamera2);
            ivPhoto1 = (ImageView) findViewById(R.id.ivPhoto1);
            ivPhoto2 = (ImageView) findViewById(R.id.ivPhoto2);
            tvPhoto1 = (TextView) findViewById(R.id.tvPhoto1);
            tvPhoto2 = (TextView) findViewById(R.id.tvPhoto2);

            tvLabPhoto1 = (TextView) findViewById(R.id.tvLabPhoto1);
            tvLabPhoto2 = (TextView) findViewById(R.id.tvLabPhoto2);
            textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
            linearLayoutPhoto1 = (LinearLayout) findViewById(R.id.linearLayoutPhoto1);
            linearLayoutPhoto2 = (LinearLayout) findViewById(R.id.linearLayoutPhoto2);

            if(dutyDetail.getPhotoReq()==1 && dutyDetail.getRemarkReq()==0)
            {
                tvLabPhoto1.setVisibility(View.VISIBLE);
                tvLabPhoto2.setVisibility(View.VISIBLE);
                linearLayoutPhoto1.setVisibility(View.VISIBLE);
                linearLayoutPhoto2.setVisibility(View.VISIBLE);
                edRemark.setVisibility(View.GONE);
                textInputLayout.setVisibility(View.GONE);
            }else if(dutyDetail.getRemarkReq()==1 && dutyDetail.getPhotoReq()==0)
            {
                tvLabPhoto1.setVisibility(View.GONE);
                tvLabPhoto2.setVisibility(View.GONE);
                linearLayoutPhoto1.setVisibility(View.GONE);
                linearLayoutPhoto2.setVisibility(View.GONE);
                edRemark.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.VISIBLE);
            }else if(dutyDetail.getRemarkReq()==1 && dutyDetail.getPhotoReq()==1)
            {
                tvLabPhoto1.setVisibility(View.VISIBLE);
                tvLabPhoto2.setVisibility(View.VISIBLE);
                linearLayoutPhoto1.setVisibility(View.VISIBLE);
                linearLayoutPhoto2.setVisibility(View.VISIBLE);
                edRemark.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.VISIBLE);
            }


            ivCamera1.setOnClickListener(this);
            ivCamera2.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            btnSubmit.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnSubmit) {

                if(dutyDetail.getPhotoReq()==0 && dutyDetail.getRemarkReq()==1) {
                    String strRemark;
                    strRemark = edRemark.getText().toString();
                    boolean isValidRemark = false;

                    if (strRemark.isEmpty()) {
                        edRemark.setError("required");
                    } else {
                        edRemark.setError(null);
                        isValidRemark = true;
                    }

                    if (isValidRemark) {
                        getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), "", "", "", "", "", strRemark, 1);
                    }

                    dismiss();
                }else if(dutyDetail.getPhotoReq()==1 && dutyDetail.getRemarkReq()==0) {
                    if (imagePath1 != null && imagePath2 != null ) {
                        getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), "", "", "", "", "", "", 1);
                    }else{
                        Toast.makeText(context, "Please Attach photo", Toast.LENGTH_SHORT).show();
                    }
                }else if(dutyDetail.getPhotoReq()==1 && dutyDetail.getRemarkReq()==1)
                {
                    String strRemark;
                    strRemark = edRemark.getText().toString();
                    boolean isValidRemark = false;

                    if (strRemark.isEmpty()) {
                        edRemark.setError("required");
                    } else {
                        edRemark.setError(null);
                        isValidRemark = true;
                    }

                    if (isValidRemark) {
                        if (imagePath1 != null && imagePath2 != null ) {
                            getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), "", "", "", "", "", strRemark, 1);
                        }else{
                            Toast.makeText(context, "Please Attach photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } else if (v.getId() == R.id.btnCancel) {
                dismiss();

            } else if (v.getId() == R.id.ivCamera1) {
               // mCallback.showCameraDialog("Photo1");
            } else if (v.getId() == R.id.ivCamera2) {
                //mCallback.showCameraDialog("Photo2");
            }
        }



//        private void showCameraDialog(String type) {
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    if (type.equalsIgnoreCase("Photo1")) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p1.jpg");
//                        String authorities = BuildConfig.APPLICATION_ID + ".provider";
//                        Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        ((Activity) context).startActivityForResult(intent, 102);
//                    } else if (type.equalsIgnoreCase("Photo2")) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p2.jpg");
//                        String authorities = BuildConfig.APPLICATION_ID + ".provider";
//                        Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        ((Activity) context).startActivityForResult(intent, 202);
//                    }
//
//                } else {
//
//                    if (type.equalsIgnoreCase("Photo1")) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis() + "_p1.jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        ((Activity) context).startActivityForResult(intent, 102);
//                    } else if (type.equalsIgnoreCase("Photo2")) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p2.jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        ((Activity) context).startActivityForResult(intent, 202);
//                    }
//
//                }
//            } catch (Exception e) {
//                ////Log.e("select camera : ", " Exception : " + e.getMessage());
//            }
//        }



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

                                MainActivity activity = (MainActivity) context;

                                Toast.makeText(activity, "Task Done Successfully", Toast.LENGTH_SHORT).show();

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

}
