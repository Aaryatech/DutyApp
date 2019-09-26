package com.ats.dutyapp.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.BuildConfig;
import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.adapter.CloseDetailAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ActionDetailList;
import com.ats.dutyapp.model.ActionHeaderClose;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.ats.dutyapp.utils.PermissionsUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.dutyapp.activity.HomeActivity.f;
import static com.ats.dutyapp.activity.HomeActivity.imagePath4;
import static com.ats.dutyapp.activity.HomeActivity.imagePath5;
import static com.ats.dutyapp.activity.HomeActivity.ivPhoto4;
import static com.ats.dutyapp.activity.HomeActivity.ivPhoto5;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseDetailFragment extends Fragment implements View.OnClickListener {
    public RecyclerView recyclerView;
    public Button btnSubmit;
    Login loginUser;
    ActionHeaderClose model;
    public static ArrayList<ActionDetailList> detailList =new ArrayList<>();
    public static ArrayList<ActionDetailList> assignStaticDetailList = new ArrayList<>();
    Context mContext;

    //-----------------------------------Image-----------------------------------

    public static ImageView ivCamera4,ivCamera5;
    private TextView tvPhoto4,tvPhoto5;
    public static File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "gfpl_security");
    // public static File f;
    Bitmap myBitmap1 = null;
    public static String path1;
    Dialog dialog;
    //----------------------------------Image End----------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_close_detail, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        btnSubmit=(Button)view.findViewById(R.id.btnSubmit);

        if (PermissionsUtil.checkAndRequestPermissions(getActivity())) {
        }

        createFolder();

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //String openMetting = getIntent().getStringExtra("model");
            String openMetting = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(openMetting, ActionHeaderClose.class);
            Log.e("Open Activity", "-----------------------------------------" + model);

            if (model.getActionDetailList() != null) {
                detailList.clear();
                for (int i = 0; i < model.getActionDetailList().size(); i++) {
                    detailList.add(model.getActionDetailList().get(i));
                }
            }

            assignStaticDetailList = detailList;

            for (int i = 0; i < assignStaticDetailList.size(); i++) {
                assignStaticDetailList.get(i).setChecked(false);
            }

            Log.e("Detail List", "-----------------------------------------" + detailList);

            CloseDetailAdapter adapter = new CloseDetailAdapter(detailList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        btnSubmit.setOnClickListener(this);

        return view;
    }

    public void onClickActivity(Integer position, Context context)
    {
        Log.e("Activity Position","--------------------------------------------"+position);
        Log.e("Detail Bin","--------------------------------------------"+detailList);
        Log.e("Detail Position","--------------------------------------------"+detailList.get(position));
        mContext=context;
        if(detailList.get(position).getCheckStatus()==1) {
            if (detailList.get(position).getIsPhoto() == 1) {
                Log.e("if", "--------------------START-------------------------");
                try {

                    new UploadCloseDialog(context, position).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
//        else if(detailList.get(position).getIsPhoto()==0)
//        {
//            detailList.get(position).setCheckStatus(2);
//            getUpdateStatus(detailList.get(position).getActionDetailId(),2,"");
//        }
//        Log.e("Detail list","-------------------------not req-------------------------------"+detailList);
    }

    public void onClickReject(int position,Context context)
    {
        Log.e("Reject Position","--------------------------------------------"+position);
        Log.e("Reject Detail Bin","--------------------------------------------"+detailList);
        Log.e("Reject Detail Position","--------------------------------------------"+detailList.get(position));
        mContext=context;
        if (detailList.get(position).getIsPhoto() == 1) {
            Log.e("if", "--------------------Reject START-------------------------");
            try {
                new UploadCloseRejectedDialog(context, position).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(detailList.get(position).getIsPhoto()==0)
        {
            detailList.get(position).setCheckStatus(3);
            getUpdateStatus(detailList.get(position).getActionDetailId(),3,"na");
        }
        Log.e("Detail list","-------------------------Reject not req-------------------------------"+detailList);
    }

    private void getUpdateStatus(int actionDetailId, int status, String photo) {

        Log.e("PARAMETER","                 ACTION DETAIL ID     "+actionDetailId +"             STSTUS       "+status +"       PHOTO      "+photo);

        if (Constants.isOnline(mContext)) {
            final CommonDialog commonDialog = new CommonDialog(mContext, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateClosedChecklistDetailStatus(actionDetailId,status,photo);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE STATUS : ", " - " + response.body());

                            if (!response.body().getError()) {

                                HomeActivity activity = (HomeActivity) mContext;

                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new TabFragment(), "TabFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(mContext, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmit) {
            ArrayList<ActionDetailList> assignedArray = new ArrayList<>();
            final ArrayList<Integer> assignedIdArray = new ArrayList<>();
            if (assignStaticDetailList != null) {
                if (assignStaticDetailList.size() > 0) {
                    assignedArray.clear();
                    for (int i = 0; i < assignStaticDetailList.size(); i++) {
                        if (assignStaticDetailList.get(i).isChecked()) {
                            assignedArray.add(assignStaticDetailList.get(i));
                            assignedIdArray.add(assignStaticDetailList.get(i).getActionDetailId());

                        }
                    }
                }
            }

            if(assignedIdArray.size()==0)
            {
                Toast.makeText(getActivity(), "Please Approve Close Checklist", Toast.LENGTH_SHORT).show();
            }else{

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to save detail ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getSave(assignedIdArray,2);

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();


            }

        }
    }

    private void getSave(ArrayList<Integer> assignedIdArray, int status) {
        Log.e("PARAMETER","           ASSIGN ID              "+assignedIdArray+"             STATUS              "+status);
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateClosedChecklistDetailStatusMultiple(assignedIdArray,status);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE CHECKLIST: ", " ----------------DETAIL-------------------------- " + response.body());

                            if (!response.body().getError()) {

                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                updateHeaderStatus(model.getActionHeaderId(),1,loginUser.getEmpId());

                            } else {
                                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateHeaderStatus(Integer actionHeaderId, int status, Integer empId) {
        Log.e("PARAMETER","           ASSIGN ID              "+actionHeaderId+"             STATUS              "+status+"             EMP ID               "+empId);
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateClosedChecklistHeaderStatus(actionHeaderId,status,empId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE CHECKLIST: ", " ----------------------------HEADER--------------------------- " + response.body());

                            if (!response.body().getError()) {

                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                HomeActivity activity = (HomeActivity) getActivity();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new TabFragment(), "TabFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private class UploadCloseDialog extends Dialog {
        public Button btnCancel, btnSubmit;
        public TextView tvLabPhoto1;
        public LinearLayout linearLayoutPhoto1;
        int position;
        Context context;

        public UploadCloseDialog(Context context, int position) {
            super(context);
            this.position=position;
            this.context=context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_upload_openchecklist);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.x = 5;
            wlp.y = 5;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);

            Log.e("Dialog", "---------------------------------------------");
            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            ivCamera4 = (ImageView) findViewById(R.id.ivCamera1);
            ivPhoto4 = (ImageView) findViewById(R.id.ivPhoto1);
            tvPhoto4 = (TextView) findViewById(R.id.tvPhoto1);

            tvLabPhoto1 = (TextView) findViewById(R.id.tvLabPhoto1);
            linearLayoutPhoto1 = (LinearLayout) findViewById(R.id.linearLayoutPhoto1);


            ivCamera4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Photo1 Click", "---------------------------------------");
                    //showCameraDialog1("Photo1");

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p1.jpg");
                    String authorities = BuildConfig.APPLICATION_ID + ".provider";
                    Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Activity activity=(Activity)context;
                    activity.startActivityForResult(intent, 402);


                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imagePath4 != null) {
                        //   && imagePath2 != null

                        final ArrayList<String> pathArray = new ArrayList<>();
                        final ArrayList<String> fileNameArray = new ArrayList<>();

                        String photo1 = "", photo2 = "", photo3 = "";
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (imagePath4 != null) {

                            pathArray.add(imagePath4);

                            File imgFile1 = new File(imagePath4);
                            int pos = imgFile1.getName().lastIndexOf(".");
                            String ext = imgFile1.getName().substring(pos + 1);
                            photo1 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p1." + ext;
                            fileNameArray.add(photo1);
                        }

                        detailList.get(position).setActionPhoto(photo1);
                       // detailList.get(position).setCheckStatus(2);

                        Log.e("Image","------------------------------set Image Detail---------------------"+detailList);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to save Photo ?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sendImage(pathArray, fileNameArray,detailList.get(position).getActionDetailId(),detailList.get(position).getActionPhoto());
                                dismiss();

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

                        // getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), "", "", "", "", "", "", 1);
                    } else {
                        Toast.makeText(getActivity(), "Please Attach photo", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private void sendImageReject(ArrayList<String> filePath, ArrayList<String> fileName, final Integer actionDetailId, final String photo) {
        Log.e("PARAMETER : ", "   FILE PATH : " + filePath + "            FILE NAME : " + fileName+"           DETAIL ID                "+actionDetailId);

        final CommonDialog commonDialog = new CommonDialog(mContext, "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = null;

        MultipartBody.Part[] uploadImagesParts = new MultipartBody.Part[filePath.size()];

        for (int index = 0; index < filePath.size(); index++) {
            Log.e("ATTACH ACT", "requestUpload:  image " + index + "  " + filePath.get(index));
            imgFile = new File(filePath.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            uploadImagesParts[index] = MultipartBody.Part.createFormData("file", "" + fileName.get(index), surveyBody);
        }

        // RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), "photo1");
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), "1");

        Call<JSONObject> call = Constants.myInterface.imageUpload(uploadImagesParts, fileName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

                imagePath4 = null;

                getUpdateStatus(actionDetailId,3,photo);


                Log.e("Response : ", "--" + response.body());


                commonDialog.dismiss();

            }
            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(mContext, "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class UploadCloseRejectedDialog extends Dialog {
        public Button btnCancel, btnSubmit;
        public TextView tvLabPhoto1;
        public LinearLayout linearLayoutPhoto1;
        int position;
        Context context;

        public UploadCloseRejectedDialog(Context context, int position) {
            super(context);
            this.position=position;
            this.context=context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_upload_openchecklist);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.x = 5;
            wlp.y = 5;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);

            Log.e("Dialog", "---------------------------------------------");
            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            ivCamera4 = (ImageView) findViewById(R.id.ivCamera1);
            ivPhoto5 = (ImageView) findViewById(R.id.ivPhoto1);
            tvPhoto4 = (TextView) findViewById(R.id.tvPhoto1);

            tvLabPhoto1 = (TextView) findViewById(R.id.tvLabPhoto1);
            linearLayoutPhoto1 = (LinearLayout) findViewById(R.id.linearLayoutPhoto1);


            ivCamera4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Photo1 Click", "---------------------------------------");
                    //showCameraDialog1("Photo1");

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p1.jpg");
                    String authorities = BuildConfig.APPLICATION_ID + ".provider";
                    Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Activity activity=(Activity)context;
                    activity.startActivityForResult(intent, 502);


                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imagePath5 != null) {
                        //   && imagePath2 != null

                        final ArrayList<String> pathArray = new ArrayList<>();
                        final ArrayList<String> fileNameArray = new ArrayList<>();

                        String photo1 = "", photo2 = "", photo3 = "";
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (imagePath5 != null) {

                            pathArray.add(imagePath5);

                            File imgFile1 = new File(imagePath5);
                            int pos = imgFile1.getName().lastIndexOf(".");
                            String ext = imgFile1.getName().substring(pos + 1);
                            photo1 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p1." + ext;
                            fileNameArray.add(photo1);
                        }

                        detailList.get(position).setClosedPhoto(photo1);
                        detailList.get(position).setCheckStatus(3);

                        Log.e("Image","------------------------------set Image Detail---------------------"+detailList);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to save Photo ?");
                        final String finalPhoto = photo1;
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sendImageReject(pathArray, fileNameArray,detailList.get(position).getActionDetailId(),detailList.get(position).getClosedPhoto());
                                dismiss();

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

                        // getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), "", "", "", "", "", "", 1);
                    } else {
                        Toast.makeText(getActivity(), "Please Attach photo", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    private void sendImage(final ArrayList<String> filePath, final ArrayList<String> fileName,final Integer actionDetailId, final String photo) {

        Log.e("PARAMETER : ", "   FILE PATH : " + filePath + "            FILE NAME : " + fileName);

        final CommonDialog commonDialog = new CommonDialog(mContext, "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = null;

        MultipartBody.Part[] uploadImagesParts = new MultipartBody.Part[filePath.size()];

        for (int index = 0; index < filePath.size(); index++) {
            Log.e("ATTACH ACT", "requestUpload:  image " + index + "  " + filePath.get(index));
            imgFile = new File(filePath.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            uploadImagesParts[index] = MultipartBody.Part.createFormData("file", "" + fileName.get(index), surveyBody);
        }

        // RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), "photo1");
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), "1");

        Call<JSONObject> call = Constants.myInterface.imageUpload(uploadImagesParts, fileName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

                imagePath4 = null;

                getUpdatePhoto(actionDetailId,photo);

                Log.e("Response : ", "--" + response.body());


                commonDialog.dismiss();

            }
            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(mContext, "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUpdatePhoto(Integer actionDetailId, String photo) {
        Log.e("PARAMETER","                 ACTION DETAIL ID     "+actionDetailId +"       PHOTO      "+photo);

        if (Constants.isOnline(mContext)) {
            final CommonDialog commonDialog = new CommonDialog(mContext, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateClosedChecklistDetailPhoto(actionDetailId,photo);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE STATUS : ", " ---------------------------PHOTO------------------------- " + response.body());

                            if (!response.body().getError()) {

                                HomeActivity activity = (HomeActivity) getActivity();

                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new CloseDetailFragment(), "MainFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(mContext, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

}
