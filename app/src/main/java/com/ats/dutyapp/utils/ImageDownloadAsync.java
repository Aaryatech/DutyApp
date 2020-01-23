package com.ats.dutyapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ats.dutyapp.constant.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class ImageDownloadAsync extends AsyncTask<String,Integer, Bitmap> {

    InputStream in = null;
    int responseCode = -1;
    Bitmap bitmap;
    Context context;
    String imgName;

    public ImageDownloadAsync(Context context) {
        this.context = context;
    }

    public ImageDownloadAsync(Context context, String imgName) {
        this.context = context;
        this.imgName = imgName;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        URL url = null;
        try {
            url = new URL(strings[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap data) {
        Log.e("ASYNC","/////////////////////// DOWNLOADED \\\\\\\\\\\\\\\\\\\\\\  ");
        saveImageToInternalStorage(data,context,imgName);
    }



    protected Uri saveImageToInternalStorage(Bitmap bitmap,Context context,String fileName){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(context);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);

        // Create a file to save the image
        file = new File(folder, fileName);

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            try{
                // Compress the bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            }catch (Exception e){
                e.printStackTrace();
            }

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();


            Intent pushNotificationIntent = new Intent();
            pushNotificationIntent.setAction("REFRESH_CHAT_IMAGE");
            LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

            Log.e("ASYNC","-------- IMAGE SAVED------- **************");

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }

}
