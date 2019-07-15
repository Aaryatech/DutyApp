package com.ats.dutyapp.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0 || !remoteMessage.getNotification().equals(null)) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                // JSONObject json1 = new JSONObject(remoteMessage.getNotification().toString());

                Log.e("JSON DATA", "-----------------------------" + json);
                // Log.e("JSON NOTIFICATION", "-----------------------------" + json1);

                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "-----------------------------Exception: " + e.getMessage());
                e.printStackTrace();
            }

            super.onMessageReceived(remoteMessage);

        } else {
            Log.e("FIREBASE", "----------------------------------");
        }
    }

    private void sendPushNotification(JSONObject json) {

        Log.e(TAG, "--------------------------------JSON String" + json.toString());
        try {

            String title = json.getString("title");
            String message = json.getString("body");
            String type = json.getString("tag");
            String imageUrl = "";
            int tag = json.getInt("tag");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            DatabaseHandler db = new DatabaseHandler(this);

            if (mNotificationManager.isAppIsInBackground(getApplicationContext())) {

                db.addNotification(title, message, "" + sdf.format(Calendar.getInstance().getTimeInMillis()));

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("model", type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //intent.putExtra("Feedback","fcm");

                mNotificationManager.showSmallNotification(title, message, intent);

            } else {

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("model", type);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.putExtra("Feedback","fcm");

                mNotificationManager.showSmallNotification(title, message, intent);

//                MyNotificationManager notificationUtils = new MyNotificationManager(getApplicationContext());
//                notificationUtils.playNotificationSound();

                db.addNotification(title, message, "" + sdf.format(Calendar.getInstance().getTimeInMillis()));
            }

            Intent pushNotificationIntent = new Intent();
            pushNotificationIntent.setAction("REFRESH_NOTIFICATION");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificationIntent);


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: -----------" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: ------------" + e.getMessage());
            e.printStackTrace();
        }

    }


}
