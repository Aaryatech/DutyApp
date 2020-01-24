package com.ats.dutyapp.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.ChatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GeneratePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

    private Context mContext;
    private String title, message, imageUrl;

    public GeneratePictureStyleNotification(Context context, String title, String message, String imageUrl) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        Log.e("GEN_IMAGE","--------------ASYNC----------------- "+result);

        Intent resultIntent = new Intent(mContext, ChatActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        /*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            int NOTIFICATION_ID = 1;

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(id, title, importance);
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);

            NotificationCompat.BigTextStyle bigPictureStyle = new NotificationCompat.BigTextStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.bigText(message);


            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 123, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "id_product")
                    .setSmallIcon(R.drawable.ic_monginis_chat) //your app icon
                    .setBadgeIconType(R.drawable.ic_monginis_chat) //your app icon
                    .setLargeIcon(result)
                    .setChannelId(id)
                    .setContentTitle(title)
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setNumber(1)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_round))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                    .setColor(255)
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis());


            notificationManager.notify(1, notificationBuilder.build());
        }else{

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_monginis_chat).setDefaults(Notification.DEFAULT_ALL)
                    .setLargeIcon(result)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent);
            NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


        }*/


        final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.custom_image_notification);

        remoteViews.setImageViewBitmap(R.id.ivImg, result);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 123, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "id_product")
                .setSmallIcon(R.drawable.ic_monginis_chat) //your app icon
                .setBadgeIconType(R.drawable.ic_monginis_chat) //your app icon
                .setLargeIcon(result)
                .setChannelId("id_product")
                .setContentTitle(title)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_round))
                .setColor(255)
                .setContentText(message)
                .setContent(remoteViews)
                .setWhen(System.currentTimeMillis());

        notificationManager.notify(1, notificationBuilder.build());



    }



}
