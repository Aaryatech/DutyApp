package com.ats.dutyapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.ats.dutyapp.R;
import com.ats.dutyapp.constant.Constants;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageZoomActivity extends AppCompatActivity {

    private ZoomageView zoomageView;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        zoomageView = findViewById(R.id.myZoomageView);

        try {

            image = getIntent().getExtras().getString("image");
            Log.e("IMAGE PATH : ", " " + image);

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME + File.separator + image);
            Log.e("IMAGE FILE : ", " " + file);


            Picasso.with(this)
                    .load(file)
                    .placeholder(getResources().getDrawable(R.drawable.progress_animation))
                    .error(getResources().getDrawable(R.drawable.progress_animation))
                    //.resize(400,400)
                    .into(zoomageView);

           /* Glide.with(this)  //2
                    .load(image) //3
                    .centerCrop() //4
                    .placeholder(R.drawable.loader) //5
                    .error(R.drawable.loader) //6
                    .fallback(R.drawable.loader) //7
                    .into(zoomageView); //8*/


        } catch (Exception e) {
        }


    }
}
