package com.ats.dutyapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.BuildConfig;
import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.DutyDetail;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.PermissionsUtil;
import com.ats.dutyapp.utils.RealPathUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemarkActivity extends AppCompatActivity implements View.OnClickListener {
    public Button btnSubmit;
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
    Bitmap myBitmap1 = null, myBitmap2 = null;
    public static String path1, imagePath1 = null, imagePath2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        setTitle("Duty Remark");

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

        try {
            String closeMetting = getIntent().getStringExtra("model");
            Gson gson = new Gson();
            dutyDetail = gson.fromJson(closeMetting, DutyDetail.class);
            Log.e("Remark Model", "-----------------------" + dutyDetail);

        }catch(Exception e)
        {
            e.printStackTrace();
        }

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

        if (PermissionsUtil.checkAndRequestPermissions(RemarkActivity.this)) {
        }

        createFolder();

        ivCamera1.setOnClickListener(this);
        ivCamera2.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivCamera1) {

            showCameraDialog("Photo1");
            Log.e("Photo1","-----------------");

        } else if (v.getId() == R.id.ivCamera2) {

            Log.e("Photo2","-----------------");
            showCameraDialog("Photo2");

        }else if (v.getId() == R.id.btnSubmit) {

            if(dutyDetail.getPhotoReq()==0 && dutyDetail.getRemarkReq()==1) {
                final String strRemark;
                strRemark = edRemark.getText().toString();
                boolean isValidRemark = false;

                if (strRemark.isEmpty()) {
                    edRemark.setError("required");
                } else {
                    edRemark.setError(null);
                    isValidRemark = true;
                }

                if (isValidRemark) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this, R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to save remark ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), "", "", "", "", "", strRemark, 1);
                            finish();
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

            }else if(dutyDetail.getPhotoReq()==1 && dutyDetail.getRemarkReq()==0) {
                if (imagePath1 != null) {
                 //   && imagePath2 != null

                    final ArrayList<String> pathArray = new ArrayList<>();
                    final ArrayList<String> fileNameArray = new ArrayList<>();

                    String photo1 = "", photo2 = "", photo3 = "";
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    if (imagePath1 != null) {

                        pathArray.add(imagePath1);

                        File imgFile1 = new File(imagePath1);
                        int pos = imgFile1.getName().lastIndexOf(".");
                        String ext = imgFile1.getName().substring(pos + 1);
                        photo1 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p1." + ext;
                        fileNameArray.add(photo1);
                    }

                    if (imagePath2 != null) {

                        pathArray.add(imagePath2);

                        File imgFile2 = new File(imagePath2);
                        int pos2 = imgFile2.getName().lastIndexOf(".");
                        String ext2 = imgFile2.getName().substring(pos2 + 1);
                        photo2 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p2." + ext2;
                        fileNameArray.add(photo2);

                    }

                    dutyDetail.setPhoto1(photo1);
                    dutyDetail.setPhoto2(photo2);

                    //getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), photo1, photo2, "", "", "", strRemark, 1);

                    AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this, R.style.AlertDialogTheme);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to save remark ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // saveVisitor(visitor);
                            // Log.e("VISITOR", "-----------------------" + visitor);
                            sendImage(pathArray, fileNameArray, dutyDetail);

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
                }else{
                    Toast.makeText(RemarkActivity.this, "Please Attach photo", Toast.LENGTH_SHORT).show();
                }
            }else if(dutyDetail.getPhotoReq()==1 && dutyDetail.getRemarkReq()==1)
            {
                final String strRemark;
                strRemark = edRemark.getText().toString();
                boolean isValidRemark = false;

                if (strRemark.isEmpty()) {
                    edRemark.setError("required");
                } else {
                    edRemark.setError(null);
                    isValidRemark = true;
                }

                if (isValidRemark) {
                    if (imagePath1 != null  ) {

                            //&& imagePath2 != null

                        final ArrayList<String> pathArray = new ArrayList<>();
                        final ArrayList<String> fileNameArray = new ArrayList<>();

                        String photo1 = "", photo2 = "", photo3 = "";

                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (imagePath1 != null) {

                            pathArray.add(imagePath1);

                            File imgFile1 = new File(imagePath1);
                            int pos = imgFile1.getName().lastIndexOf(".");
                            String ext = imgFile1.getName().substring(pos + 1);
                            photo1 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p1." + ext;
                            fileNameArray.add(photo1);
                        }

                        if (imagePath2 != null) {

                            pathArray.add(imagePath2);

                            File imgFile2 = new File(imagePath2);
                            int pos2 = imgFile2.getName().lastIndexOf(".");
                            String ext2 = imgFile2.getName().substring(pos2 + 1);
                            photo2 = sdf.format(Calendar.getInstance().getTimeInMillis()) + "_p2." + ext2;
                            fileNameArray.add(photo2);

                        }
                        dutyDetail.setPhoto1(photo1);
                        dutyDetail.setPhoto2(photo2);
                        dutyDetail.setRemark(strRemark);
                        //getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), photo1, photo2, "", "", "", strRemark, 1);

                        AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Do you want to save remark ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // saveVisitor(visitor);
                                // Log.e("VISITOR", "-----------------------" + visitor);

                                sendImage(pathArray, fileNameArray, dutyDetail);

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
                        Toast.makeText(RemarkActivity.this, "Please Attach photo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void showCameraDialog(final String type) {

        Log.e("Photo","-----------------"+type);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (type.equalsIgnoreCase("Photo1")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p1.jpg");
                    String authorities = BuildConfig.APPLICATION_ID + ".provider";
                    Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 102);
                } else if (type.equalsIgnoreCase("Photo2")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p2.jpg");
                    String authorities = BuildConfig.APPLICATION_ID + ".provider";
                    Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 202);
                }

            } else {

                if (type.equalsIgnoreCase("Photo1")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis() + "_p1.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 102);
                } else if (type.equalsIgnoreCase("Photo2")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(folder + File.separator, "" + Calendar.getInstance().getTimeInMillis()+ "_p2.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 202);
                }

            }
        } catch (Exception e) {
            ////Log.e("select camera : ", " Exception : " + e.getMessage());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String realPath;
        Bitmap bitmap = null;

        if (resultCode == RESULT_OK && requestCode == 102) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPhoto1.setImageBitmap(myBitmap1);

                    myBitmap1 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath1 = f.getAbsolutePath();
                tvPhoto1.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap1 = getBitmapFromCameraData(data, getApplicationContext());

                ivPhoto1.setImageBitmap(myBitmap1);
                imagePath1 = uriFromPath.getPath();
                tvPhoto1.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 202) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPhoto2.setImageBitmap(myBitmap2);

                    myBitmap2 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath2 = f.getAbsolutePath();
                tvPhoto2.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 201) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap2 = getBitmapFromCameraData(data, getApplicationContext());

                ivPhoto2.setImageBitmap(myBitmap2);
                imagePath2 = uriFromPath.getPath();
                tvPhoto2.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        }
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        path1 = picturePath;
        cursor.close();

        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);
        Log.e("Image Size : ---- ", " " + bitm.getByteCount());

        return bitm;
        // return BitmapFactory.decodeFile(picturePath, options);
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    private  void getTaskDone(Integer taskDoneDetailId, Integer taskDoneHeaderId, Integer photoReq, Integer remarkReq, String photo1, String photo2, String photo3, String photo4, String photo5, String strRemark, int status) {

        Log.e("PARAMETER", "                 DETAIL ID     " + taskDoneDetailId + "              HEADER ID       " + taskDoneHeaderId + "       PHOTO REQ      " + photoReq + "      REMARK REQ         " + remarkReq + "      PHOTO 1         " + photo1 + "      PHOTO 2         " + photo2 + "      PHOTO 3         " + photo3 + "      PHOTO 4         " + photo4 + "      PHOTO 5         " + photo5 + "      REMARK         " + strRemark + "      STATUS      " + status);
        if (Constants.isOnline(RemarkActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(RemarkActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateTaskStatus(taskDoneDetailId, taskDoneHeaderId, photoReq, remarkReq, photo1, photo2, photo3, photo4, photo5, strRemark, status);
            listCall.enqueue(new retrofit2.Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DONE TASK: ", " - " + response.body());

                            if (!response.body().getError()) {

                                RemarkActivity activity = (RemarkActivity) context;
                                Toast.makeText(getApplicationContext(), "Task Done Successfully", Toast.LENGTH_SHORT).show();
                                //activity.finish();

//                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                //intent.putExtra("model", "Close Meeting");
//                                intent.putExtra("model", "RemarkActivity");
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);

                                commonDialog.dismiss();
                            } else {
                                Toast.makeText(RemarkActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            }

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(RemarkActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(RemarkActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(RemarkActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(RemarkActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
}

    private void sendImage(final ArrayList<String> filePath, final ArrayList<String> fileName, final DutyDetail dutyDetail) {

        Log.e("PARAMETER : ", "   FILE PATH : " + filePath + "            FILE NAME : " + fileName);

        final CommonDialog commonDialog = new CommonDialog(RemarkActivity.this, "Loading", "Please Wait...");
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

                imagePath1 = null;
                imagePath2 = null;


                Log.e("Response : ", "--" + response.body());
                getTaskDone(dutyDetail.getTaskDoneDetailId(), dutyDetail.getTaskDoneHeaderId(), dutyDetail.getPhotoReq(), dutyDetail.getRemarkReq(), dutyDetail.getPhoto1(), dutyDetail.getPhoto2(), "", "", "", dutyDetail.getRemark(), 1);
                finish();
                commonDialog.dismiss();

            }
            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(RemarkActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
