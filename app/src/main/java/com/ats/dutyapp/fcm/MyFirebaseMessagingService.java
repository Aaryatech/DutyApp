package com.ats.dutyapp.fcm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ats.dutyapp.activity.ChatActivity;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.adapter.TaskListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.ats.dutyapp.utils.BackgroundService;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0 || !remoteMessage.getNotification().equals(null)) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                Log.e("JSON DATA", "-----------------------------" + json);

                sendPushNotification(json);

            } catch (Exception e) {
                Log.e(TAG, "----------Exception: " + e.getMessage());
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            DatabaseHandler db = new DatabaseHandler(this);


            if (type.equalsIgnoreCase("chat")) {

                Gson gson = new Gson();
                ChatDetail chatDetail = gson.fromJson(message, ChatDetail.class);
                ChatDisplay chat = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getServerDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), 0, 0, 0,chatDetail.getExInt1(),chatDetail.getReplyToMsgType(),chatDetail.getReplyToMsg(),chatDetail.getReplyToName());

                if (mNotificationManager.isAppIsInBackground(getApplicationContext())) {
                    Log.e("APP BACKGROUND", "--------------------------------------------------");
                    db.addChatDetail(chat);

                    Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                    resultIntent.putExtra("headerId", chat.getHeaderId());
                    resultIntent.putExtra("refresh", 1);

                    String msg = "";
                    if (chat.getTypeOfText() == 1) {
                        msg = chat.getTextValue();
                    } else if (chat.getTypeOfText() == 2) {
                        msg = chat.getUserName() + " has send an image";
                    }

                    mNotificationManager.showBigNotification(title, msg, resultIntent);

                } else {
                    Log.e("APP RUNNING", "--------------------------------------------------");

                    ChatDisplay chat1 = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getServerDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), 0, 1, 0,chatDetail.getExInt1(),chatDetail.getReplyToMsgType(),chatDetail.getReplyToMsg(),chatDetail.getReplyToName());

                    Gson gson1 = new Gson();
                    String chatToString = gson1.toJson(chat1);
                    db.addChatDetail(chat1);

                    Intent pushNotification = new Intent();
                    pushNotification.setAction("CHAT_DETAIL");
                    pushNotification.putExtra("message", chatToString);
                    pushNotification.putExtra("headerId", chat1.getHeaderId());
                    pushNotification.putExtra("refresh", 1);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    MyNotificationManager notificationUtils = new MyNotificationManager(getApplicationContext());
                    notificationUtils.playNotificationSound();


                }

                Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH_NOTIFICATION");
                pushNotificationIntent.putExtra("message", message);
                pushNotificationIntent.putExtra("title", title);
                pushNotificationIntent.putExtra("type", type);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificationIntent);

            } else if (type.equalsIgnoreCase("header")) {

                try {
                    String userStr = CustomSharedPreference.getString(this, CustomSharedPreference.MAIN_KEY_USER);
                    Gson gson = new Gson();
                    Login loginUser = gson.fromJson(userStr, Login.class);
                    Log.e("FCM SERVICE : ", "--------USER - PREF-------------" + loginUser);

                    saveDataToSqlite(loginUser.getEmpId());

                    if (mNotificationManager.isAppIsInBackground(getApplicationContext())) {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("model", "ChatHeader");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        mNotificationManager.showBigNotification(title, "New task group created", intent);

                    } else {

                        Log.e("APP RUNNING", "--------------------------------------------------");

                        /* LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                         */

                        MyNotificationManager notificationUtils = new MyNotificationManager(getApplicationContext());
                        notificationUtils.playNotificationSound();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH_NOTIFICATION");
                pushNotificationIntent.putExtra("message", message);
                pushNotificationIntent.putExtra("title", title);
                pushNotificationIntent.putExtra("type", type);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificationIntent);

            } else if (type.equalsIgnoreCase("background")) {

                Log.e("FCM", "-----------------BACKGROUND-----------------------------");

                if (mNotificationManager.isAppIsInBackground(getApplicationContext())) {

                    Log.e("FCM", "-----------------BACKGROUND------APP-----------------------");
                    startService(new Intent(MyFirebaseMessagingService.this, BackgroundService.class));

                } else {

                    BackgroundService backgroundService = new BackgroundService(getApplicationContext());
                    if (!isMyServiceRunning(backgroundService.getClass())) {
                        startService(new Intent(getApplicationContext(), BackgroundService.class));
                    }

                }

            } else if (type.equalsIgnoreCase("reminder")) {

                try {

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("model", "ChatHeader");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    mNotificationManager.showBigNotification(title, message, intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (type.equalsIgnoreCase("memo")) {

                try {

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("model", "Memo");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    mNotificationManager.showBigNotification(title, message, intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (type.equalsIgnoreCase("readTag")){

                int detailId=0;
                try{ detailId=Integer.parseInt(title);
                    int res=db.updateChatDetailReadStatusByDetailId(detailId,3);
                    Log.e("FIREBASE","--------------- REG_STATUS ---------- result - +detailId="+detailId+"      res="+res);
                }catch (Exception e){e.printStackTrace();}


                Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH_READ_STATUS");
                pushNotificationIntent.putExtra("detailId", detailId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificationIntent);

            }else {

                if (mNotificationManager.isAppIsInBackground(getApplicationContext())) {


                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("model", type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    mNotificationManager.showBigNotification(title, message, intent);

                } else {

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("model", type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    mNotificationManager.showBigNotification(title, message, intent);

                }

                Intent pushNotificationIntent = new Intent();
                pushNotificationIntent.setAction("REFRESH_NOTIFICATION");
                pushNotificationIntent.putExtra("message", message);
                pushNotificationIntent.putExtra("title", title);
                pushNotificationIntent.putExtra("type", type);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificationIntent);

            }


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: -----------" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: ------------" + e.getMessage());
            e.printStackTrace();
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "-----------FCM--------------");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "---------------FCM---------------");
        return false;
    }


    public void saveDataToSqlite(int empId) {

        if (Constants.isOnline(this)) {

            Call<ArrayList<ChatTask>> listCall = Constants.myInterface.getAllChatHeaderDisplayByUser(empId);
            listCall.enqueue(new Callback<ArrayList<ChatTask>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatTask>> call, Response<ArrayList<ChatTask>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("FCM  : ", "------------------HEADER LIST------------ " + response.body());

                            if (response.body().size() > 0) {

                                DatabaseHandler db = new DatabaseHandler(MyFirebaseMessagingService.this);

                                db.removeAllChatHeader();

                                for (int i = 0; i < response.body().size(); i++) {
                                    db.addChatHeader(response.body().get(i));
                                }

//                                Intent pushNotificationIntent = new Intent();
//                                pushNotificationIntent.setAction("HEADER_REFRESH");
//                                LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(pushNotificationIntent);
                            }

                        } else {
                            Log.e("FCM : ", "-------Data Null----");
                        }
                    } catch (Exception e) {
                        Log.e("FCM : ", "------Exception-----" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatTask>> call, Throwable t) {
                    Log.e("FCM : ", "-----onFailure------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        }

    }


}
