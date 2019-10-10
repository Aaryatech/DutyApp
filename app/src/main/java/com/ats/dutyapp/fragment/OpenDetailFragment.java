package com.ats.dutyapp.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import com.ats.dutyapp.adapter.OpenDetailAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChecklistActionDetail;
import com.ats.dutyapp.model.ChecklistActionHeader;
import com.ats.dutyapp.model.Detail;
import com.ats.dutyapp.model.DetailList;
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
import static com.ats.dutyapp.activity.HomeActivity.imagePath3;
import static com.ats.dutyapp.activity.HomeActivity.ivPhoto3;
 /**
 * A simple {@link Fragment} subclass.
 */
public class OpenDetailFragment extends Fragment implements View.OnClickListener {
    public RecyclerView recyclerView;
    public Button btnSubmit;
    public static ArrayList<DetailList> detailList =new ArrayList<>();
    public static ArrayList<ChecklistActionDetail> detailList1 =new ArrayList<>();
    Login loginUser;
    Detail model;
    public static ArrayList<DetailList> assignStaticDetailList = new ArrayList<>();
    String stringId,stringName;
    Context mContext;
     public static OpenDetailAdapter adapter;
     public static String pos;
    //-----------------------------------Image-----------------------------------

    public static ImageView ivCamera3;
    private TextView tvPhoto3;
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
        View view= inflater.inflate(R.layout.fragment_open_detail, container, false);
        getActivity().setTitle("Open Detail");
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
            detailList.clear();
            detailList1.clear();
            String openMetting = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(openMetting, Detail.class);
            Log.e("Open Activity", "-----------------------------------------" + model);

            if (model.getDetailList() != null) {
                for (int i = 0; i < model.getDetailList().size(); i++) {
                    detailList.add(model.getDetailList().get(i));
                }
            }

            for(int j=0;j<detailList.size();j++)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ChecklistActionDetail checklistActionDetail = new ChecklistActionDetail(0, detailList.get(j).getChecklistHeaderId(), 0, 0, detailList.get(j).getChecklistDesc(), detailList.get(j).getIsPhoto(), 0," ", "", sdf.format(System.currentTimeMillis()), 1, 0, 0, "", "");
                detailList1.add(checklistActionDetail);
            }

            assignStaticDetailList = detailList;

            for (int i = 0; i < assignStaticDetailList.size(); i++) {
                assignStaticDetailList.get(i).setChecked(false);
            }

            Log.e("Detail List", "-----------------------------------------" + detailList1);

             adapter = new OpenDetailAdapter(detailList1, getActivity());
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            Log.e("Position","------------------------------"+pos);
            if(pos!=null)
            {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Log.e("Image","------------------------------set Image Detail Submit---------------------"+detailList1);

            final ChecklistActionHeader checklistActionHeader = new ChecklistActionHeader(0, model.getAssignId(), model.getDeptId(), model.getChecklistHeaderId(), model.getChecklistName(), 0, loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), sdf1.format(System.currentTimeMillis()), loginUser.getEmpId(), sdf.format(System.currentTimeMillis()), sdf1.format(System.currentTimeMillis()), 1, 0, 0, "", "", detailList1);
            Log.e("Header & Detail", "--------------------------------------------------------------------------" + checklistActionHeader);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to save Detail ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    getSaveOpen(checklistActionHeader);

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
        }else{
                    Toast.makeText(getActivity(), "Please select checklist.......", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void onClickActivity(Integer position,Context context)
    {
        Log.e("Activity Position","--------------------------------------------"+position);
        Log.e("Detail Bin","--------------------------------------------"+detailList1);
        Log.e("Detail Position","--------------------------------------------"+detailList1.get(position));
        mContext=context;
        pos= String.valueOf(position);
        if (detailList1.get(position).getIsPhoto() == 1) {
            Log.e("if", "--------------------START-------------------------");
            try {

                new UploadOpenDialog(context, position).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(detailList1.get(position).getIsPhoto()==0)
        {
            detailList1.get(position).setCheckStatus(1);
        }
        Log.e("Detail list","-------------------------not req-------------------------------"+detailList1);
    }

    private class UploadOpenDialog extends Dialog {
        public Button btnCancel, btnSubmit;
        public TextView tvLabPhoto1;
        public LinearLayout linearLayoutPhoto1;
        int position;
        Context context;

        public UploadOpenDialog(Context context, int position) {
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
            ivCamera3 = (ImageView) findViewById(R.id.ivCamera1);
            ivPhoto3 = (ImageView) findViewById(R.id.ivPhoto1);
            tvPhoto3 = (TextView) findViewById(R.id.tvPhoto1);

            tvLabPhoto1 = (TextView) findViewById(R.id.tvLabPhoto1);
            linearLayoutPhoto1 = (LinearLayout) findViewById(R.id.linearLayoutPhoto1);


            ivCamera3.setOnClickListener(new View.OnClickListener() {
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
                    activity.startActivityForResult(intent, 302);


                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    detailList1.get(position).setChecked(false);
                    Log.e("Adapter","-------------------------------------------------"+adapter);
                    adapter.notifyDataSetChanged();
                    pos=null;

                    tvPhoto3.setText(" ");
                    imagePath3=null;
                    Log.e("Image Path3","-----------------------------------------------"+imagePath3);

                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imagePath3 != null) {
                        //   && imagePath2 != null

                        final ArrayList<String> pathArray = new ArrayList<>();
                        final ArrayList<String> fileNameArray = new ArrayList<>();

                        String photo1 = "", photo2 = "", photo3 = "";
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (imagePath3 != null) {

                            pathArray.add(imagePath3);

                            File imgFile1 = new File(imagePath3);
                            int pos = imgFile1.getName().lastIndexOf(".");
                            String ext = imgFile1.getName().substring(pos + 1);
                            photo1 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p1." + ext;
                            fileNameArray.add(photo1);
                        }

//                        for (int i = 0; i < detailList.size(); i++) {
//                            ChecklistActionDetail checklistActionDetail = new ChecklistActionDetail(0, detailList.get(i).getChecklistHeaderId(), 0, 0, detailList.get(i).getChecklistDesc(), detailList.get(i).getIsPhoto(), 0,"", "", sdf.format(System.currentTimeMillis()), 1, 0, 0, "", "");
//                            detailList1.add(checklistActionDetail);
//                        }
//                        detailList1.get(position).setActionPhoto(photo1);
//                        detailList1.get(position).setCheckStatus(1);

//
//                        ChecklistActionDetail checklistActionDetail = new ChecklistActionDetail(0, detailList.get(position).getChecklistHeaderId(), 0, 0, detailList.get(position).getChecklistDesc(), detailList.get(position).getIsPhoto(), 0,photo1, "", sdf.format(System.currentTimeMillis()), 1, 0, 0, "", "");
//                        detailList1.add(checklistActionDetail);

                        detailList1.get(position).setActionPhoto(photo1);
                        detailList1.get(position).setCheckStatus(1);

                        Log.e("Image","------------------------------set Image Detail---------------------"+detailList1);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to save Photo ?");
                        final String finalPhoto = photo1;
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                sendImage(pathArray, fileNameArray);
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
                        Toast.makeText(context, "Please Attach photo", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private void showCameraDialog1(String type) {
        Log.e("Photo","-----------------"+type);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.e("N","---------------------------------------------------");
                if (type.equalsIgnoreCase("Photo1")) {
                    Log.e("Compaire","-----------------------START----------------------------");

//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p1.jpg");
//                    String authorities = BuildConfig.APPLICATION_ID + ".provider";
//                    Uri imageUri = FileProvider.getUriForFile(OpenActivity.this, authorities, f);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                    startActivityForResult(intent, 102);

                    Log.e("Compaire","-------------------------END--------------------------");

                }

            } else {
                if (type.equalsIgnoreCase("Photo1")) {
                    Log.e("OTHER","---------------------------------------------------");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis() + "_p1.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 102);
                }

            }
        } catch (Exception e) {
            ////Log.e("select camera : ", " Exception : " + e.getMessage());
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        String realPath;
//        Bitmap bitmap = null;
//
//        Log.e("onActivityResult   ", "---------*******************************------");
//
//        if (resultCode == RESULT_OK && requestCode == 102) {
//            try {
//
//                Log.e("Image 102  ", "---------*******************************------");
//
//                String path = f.getAbsolutePath();
//                File imgFile = new File(path);
//                if (imgFile.exists()) {
//                    myBitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    ivPhoto1.setImageBitmap(myBitmap1);
//
//                    myBitmap1 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);
//
//                    try {
//                        FileOutputStream out = new FileOutputStream(path);
//                        myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
//                        out.flush();
//                        out.close();
//                        Log.e("Image Saved  ", "---------------");
//
//                    } catch (Exception e) {
//                        Log.e("Exception : ", "--------" + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//                imagePath1 = f.getAbsolutePath();
//                tvPhoto1.setText("" + f.getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else if (resultCode == RESULT_OK && requestCode == 101) {
//            try {
//                realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());
//                Uri uriFromPath = Uri.fromFile(new File(realPath));
//                myBitmap1 = getBitmapFromCameraData(data, getActivity());
//
//                ivPhoto1.setImageBitmap(myBitmap1);
//                imagePath1 = uriFromPath.getPath();
//                tvPhoto1.setText("" + uriFromPath.getPath());
//
//                try {
//
//                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
//                    myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
//                    out.flush();
//                    out.close();
//                    //Log.e("Image Saved  ", "---------------");
//
//                } catch (Exception e) {
//                    // Log.e("Exception : ", "--------" + e.getMessage());
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
//            }
//        }
//    }

//    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
//        Uri selectedImage = data.getData();
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//
//        String picturePath = cursor.getString(columnIndex);
//        path1 = picturePath;
//        cursor.close();
//
//        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);
//        Log.e("Image Size : ---- ", " " + bitm.getByteCount());
//
//        return bitm;
//        // return BitmapFactory.decodeFile(picturePath, options);
//    }
//
//    public static Bitmap shrinkBitmap(String file, int width, int height) {
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
//
//        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
//        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
//
//        if (heightRatio > 1 || widthRatio > 1) {
//            if (heightRatio > widthRatio) {
//                bmpFactoryOptions.inSampleSize = heightRatio;
//            } else {
//                bmpFactoryOptions.inSampleSize = widthRatio;
//            }
//        }
//
//        bmpFactoryOptions.inJustDecodeBounds = false;
//        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
//        return bitmap;
//    }
//


    private void sendImage(final ArrayList<String> filePath, final ArrayList<String> fileName) {

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

                imagePath3 = null;

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


     private void getSaveOpen(ChecklistActionHeader checklistActionHeader) {
         Log.e("PARAMETER","---------------------------------------CHECK ACTION OPEN--------------------------"+checklistActionHeader);

         if (Constants.isOnline(getActivity())) {
             final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
             commonDialog.show();

             Call<ChecklistActionHeader> listCall = Constants.myInterface.saveChecklistActionHeaderAndDetail(checklistActionHeader);
             listCall.enqueue(new Callback<ChecklistActionHeader>() {
                 @Override
                 public void onResponse(Call<ChecklistActionHeader> call, Response<ChecklistActionHeader> response) {
                     try {
                         if (response.body() != null) {

                             Log.e("CHECK ACTION OPEN  : ", " ------------------------------SAVE CHECK ACTION OPEN------------------------- " + response.body());
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new TabFragment(), "MainFragment");
                            ft.commit();

                             commonDialog.dismiss();

                         } else {
                             commonDialog.dismiss();
                             Log.e("Data Null : ", "-----------");

                             android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                             builder.setTitle("" + getResources().getString(R.string.app_name));
                             builder.setMessage("Unable to process! please try again.");

                             builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                 }
                             });
                             android.support.v7.app.AlertDialog dialog = builder.create();
                             dialog.show();

                         }
                     } catch (Exception e) {
                         commonDialog.dismiss();
                         Log.e("Exception : ", "-----------" + e.getMessage());
                         e.printStackTrace();

                         android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                         builder.setTitle("" + getResources().getString(R.string.app_name));
                         builder.setMessage("Unable to process! please try again.");

                         builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                             }
                         });
                         android.support.v7.app.AlertDialog dialog = builder.create();
                         dialog.show();

                     }
                 }

                 @Override
                 public void onFailure(Call<ChecklistActionHeader> call, Throwable t) {
                     commonDialog.dismiss();
                     Log.e("onFailure : ", "-----------" + t.getMessage());
                     t.printStackTrace();

                     android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                     builder.setTitle("" + getResources().getString(R.string.app_name));
                     builder.setMessage("Unable to process! please try again.");

                     builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     });
                     android.support.v7.app.AlertDialog dialog = builder.create();
                     dialog.show();

                 }
             });
         } else {
             Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
         }
     }

}
