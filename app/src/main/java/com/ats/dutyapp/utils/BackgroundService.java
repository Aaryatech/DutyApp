package com.ats.dutyapp.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fcm.MyNotificationManager;
import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundService extends Service {

    private boolean mActive;
    private Thread mThread;
    private Handler mHandler;
    public int counter = 0;
    Context context;
    Login loginUser;

    public BackgroundService(Context context) {
        super();
        Log.e("HERE", "----------------------------------------here I am!");
        this.context = context;

        syncChat();


    }

    public BackgroundService() {
        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Checking",
                    NotificationManager.IMPORTANCE_MIN);
            channel.setShowBadge(false);
            channel.setSound(null, null);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            NotificationCompat.Builder b = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true);
            b.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

            Notification notification = b.build();

            startForeground(100, notification);


        }

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        context.startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, BackgroundBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        if (isConnectedToInternet()) {
            start();
        }

       /* if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Checking",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
*/
        return Service.START_STICKY;
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 60000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.e("in timer", "in timer ++++  " + (counter++));

                syncChat();

                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.e("BG SERVICE", " ------------- mHandler-----------------");
                        //   syncChat();
                    }
                };


            }
        };
    }

    /**
     * not needed
     */
    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }


    public void start() {
        if (!mActive) {
            mActive = true;
            Log.e("BG SERVICE", "-----------------------------------------START");
            // Create ConnectionThread Loop
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mHandler = new Handler();
                        //initConnection();
                        Log.e("start", "----- --------------------------");
                        //   syncChat();
                        Looper.loop();
                    }
                });
                mThread.start();
            }
        }
    }

    public void stop() {
        mActive = false;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }
        }
        return false;
    }

    private void syncChat() {
        Log.e("Background Service", "------------- syncChat ");

        try {
            String userStr = CustomSharedPreference.getString(context, CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            getAllTask(loginUser.getEmpId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        syncChatDetail();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void syncChatDetail() {

        DatabaseHandler db = new DatabaseHandler(context);

        ArrayList<ChatDisplay> notSyncChat = db.getAllNotSyncTypedChat();
        Log.e("Background Service : ", "------------- NOT SYNC CHAT ----------- " + notSyncChat);
        if (notSyncChat != null) {
            if (notSyncChat.size() > 0) {

                ArrayList<ChatDetail> detailList = new ArrayList<>();
                for (int i = 0; i < notSyncChat.size(); i++) {

                    ChatDisplay chat = notSyncChat.get(i);

                    ChatDetail detail = new ChatDetail(0, chat.getHeaderId(), chat.getTypeOfText(), chat.getTextValue(), chat.getDateTime(), chat.getDateTime(), chat.getUserId(), chat.getUserName(), 1, 1,chat.getReplyToId());
                    detailList.add(detail);
                }

                if (detailList.size() > 0) {
                    saveChatToServer(detailList);
                }
            } else {

                //--------get all from server
                try {
                    String userStr = CustomSharedPreference.getString(context, CustomSharedPreference.MAIN_KEY_USER);
                    Gson gson = new Gson();
                    loginUser = gson.fromJson(userStr, Login.class);

                    int lastSyncId = db.getChatDetailLastSyncId();
                    getLastSyncChatFromServer(lastSyncId, loginUser.getEmpId());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {

            //--------get all from server
            try {
                String userStr = CustomSharedPreference.getString(BackgroundService.this, CustomSharedPreference.MAIN_KEY_USER);
                Gson gson = new Gson();
                loginUser = gson.fromJson(userStr, Login.class);
                Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser.getEmpId());

                int lastSyncId = db.getChatDetailLastSyncId();
                getLastSyncChatFromServer(lastSyncId, loginUser.getEmpId());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void getLastSyncChatFromServer(int lastSyncId, final int userId) {
        if (isConnectedToInternet()) {
            Log.e("Background Service", "   - PARAMETER ----------------LAST_SYNC_ID : " + lastSyncId + "      USER_ID : " + userId);

            Call<ArrayList<ChatDisplay>> infoCall = Constants.myInterface.getAllChatByLastSyncAndUserId(lastSyncId, userId);
            infoCall.enqueue(new Callback<ArrayList<ChatDisplay>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatDisplay>> call, Response<ArrayList<ChatDisplay>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<ChatDisplay> data = response.body();
                            Log.e("Background Service  : ", "  RESPONSE :---------------------------**************************************--------------------------------------------------------------------------------- " + data);



                            if (data.size() > 0) {

                                for (int i=0;i<data.size();i++){
                                    Log.e("DATA ","---------------- "+data.get(i));
                                }

                                int flag = 0;

                                DatabaseHandler db = new DatabaseHandler(context);

                                for (int i = 0; i < data.size(); i++) {
                                    if (db.getChatDetailIdPresentOrNot(data.get(i).getChatTaskDetailId()) > 0) {
                                        Log.e("Backgroung Service : ", "------ID MATCH------- " + data.get(i).getChatTaskDetailId());

                                        db.updateChatDetailLastSyncStatus(data.get(i).getChatTaskDetailId(), 1);
                                    } else {
                                        Log.e("Backgroung Service : ", "------NOT MATCH------- " + data.get(i).getChatTaskDetailId());

                                        ChatDisplay disp = data.get(i);
                                        disp.setSyncStatus(1);

                                        if (disp.getMarkAsRead() != 3) {

                                            if (userId == disp.getUserId()) {
                                                disp.setMarkAsRead(2);
                                            } else {
                                                disp.setMarkAsRead(0);
                                                flag = 1;
                                            }
                                        }
                                        db.addChatDetail(disp);


                                    }
                                }

                                db.deleteChatDetailRecordByDetailId(0);
                                Log.e("Background Service : ", " --- AFTER DELETE SQLITE ---- " + db.getAllChatDetail());

                                if (flag == 1) {
                                    Intent resultIntent1 = new Intent(context, HomeActivity.class);
                                    resultIntent1.putExtra("model", "ChatHeader");
                                    MyNotificationManager mNotificationManager = new MyNotificationManager(context);
                                    mNotificationManager.showBigNotification("New Messages", "Check the new messages", resultIntent1);

                                    Intent pushNotificationIntent = new Intent();
                                    pushNotificationIntent.setAction("REFRESH_NOTIFICATION");
                                    pushNotificationIntent.putExtra("message", "Check the new messages");
                                    pushNotificationIntent.putExtra("title", "New Messages");
                                    pushNotificationIntent.putExtra("type", "Refresh");

                                    LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);


                                }

                            }

                        } else {
                            Log.e("Background Service  : ", " RESPONSE :  NULL");
                        }
                    } catch (Exception e) {
                        Log.e("Background Service  : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatDisplay>> call, Throwable t) {
                    Log.e("Background Service  : ", " RESPONSE :  onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }


    public void saveChatToServer(ArrayList<ChatDetail> chatDetail) {

        if (isConnectedToInternet()) {

            final DatabaseHandler db = new DatabaseHandler(BackgroundService.this);
            db.updateChatDetailOfflineStatus(0, 0);

            Call<ArrayList<ChatDetail>> listCall = Constants.myInterface.saveChatDetailList(chatDetail);
            listCall.enqueue(new Callback<ArrayList<ChatDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatDetail>> call, Response<ArrayList<ChatDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Background Service  : ", " ---------saveChatToServer------RESPONSE--- " + response.body());


                            int lastSyncId = db.getChatDetailLastSyncId();

                            try {
                                String userStr = CustomSharedPreference.getString(BackgroundService.this, CustomSharedPreference.MAIN_KEY_USER);
                                Gson gson = new Gson();
                                loginUser = gson.fromJson(userStr, Login.class);

                                getLastSyncChatFromServer(lastSyncId, loginUser.getEmpId());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("Background Service  : ", "---saveChatToServer----------------NULL------");

                        }
                    } catch (Exception e) {
                        Log.e("Exception : ", "-----saveChatToServer------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatDetail>> call, Throwable t) {
                    Log.e("onFailure : ", "-----saveChatToServer------" + t.getMessage());
                    t.printStackTrace();


                }
            });
        }
    }


    private void getAllTask(int userId) {
        if (isConnectedToInternet()) {

            Call<ArrayList<ChatTask>> listCall = Constants.myInterface.getAllChatHeaderDisplayByUser(userId);
            listCall.enqueue(new Callback<ArrayList<ChatTask>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatTask>> call, Response<ArrayList<ChatTask>> response) {
                    try {
                        if (response.body() != null) {

                            if (response.body().size() > 0) {

                                DatabaseHandler db = new DatabaseHandler(context);

                                db.removeAllChatHeader();

                                for (int i = 0; i < response.body().size(); i++) {
                                    db.addChatHeader(response.body().get(i));
                                }
                            }

                        } else {
                        }
                    } catch (Exception e) {
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatTask>> call, Throwable t) {
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }


}
