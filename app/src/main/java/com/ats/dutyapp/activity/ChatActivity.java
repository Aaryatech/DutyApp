package com.ats.dutyapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.BuildConfig;
import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.ChatAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fcm.MyNotificationManager;
import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatDetailIdListByReadStatus;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.ats.dutyapp.utils.PermissionsUtil;
import com.ats.dutyapp.utils.RealPathUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.dutyapp.activity.HomeActivity.imagePath5;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LinearLayout llSend,llChatReply;
    private EditText edMsg;
    ImageView ivToolbar, ivBack, ivMenu,ivChatReplyClose,ivReplyToImg;
    TextView tvTitle, tvLastDate;
    TextView tvToolbarTitle, tvToolbarUser, tvAttach,tvReplyToId,tvReplyToMsg,tvReplyToName,tvReplyToMsgType;
    DatabaseHandler db;
    CircleImageView ivPic;

    Login loginUser;

    ArrayList<ChatDisplay> chatList = new ArrayList<>();
    ChatAdapter chatAdapter;

    private BroadcastReceiver broadcastReceiver, bRRefresh, bRRefreshImage, brRefreshRead, brReplyChat;

    ChatTask header;
    int cHeaderId = 0;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);
    File f;

    Bitmap myBitmap = null;
    public static String path, imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        createFolder();

        recyclerView = findViewById(R.id.recyclerView);
        llSend = findViewById(R.id.llSend);
        edMsg = findViewById(R.id.edMsg);
        ivToolbar = findViewById(R.id.ivToolbar);
        tvToolbarTitle = findViewById(R.id.tvToolbar_Title);
        tvToolbarUser = findViewById(R.id.tvToolbar_User);
        tvAttach = findViewById(R.id.tvAttach);

        ivBack = findViewById(R.id.ivBack);
        ivPic = findViewById(R.id.ivPic);
        ivMenu = findViewById(R.id.ivMenu);
        tvTitle = findViewById(R.id.tvTitle);
        tvLastDate = findViewById(R.id.tvLastDate);

        llChatReply = findViewById(R.id.llChatReply);
        tvReplyToId = findViewById(R.id.tvReplyToId);
        tvReplyToMsg = findViewById(R.id.tvReplyToMsg);
        tvReplyToMsgType = findViewById(R.id.tvReplyToMsgType);
        tvReplyToName = findViewById(R.id.tvReplyToName);
        ivChatReplyClose = findViewById(R.id.ivChatReplyClose);
        ivReplyToImg = findViewById(R.id.ivReplyToImg);

        tvReplyToId.setText("0");

        llSend.setOnClickListener(this);
        tvAttach.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPic.setOnClickListener(this);

        ivChatReplyClose.setOnClickListener(this);

        db = new DatabaseHandler(this);

        try {
            String userStr = CustomSharedPreference.getString(getApplication(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            cHeaderId = getIntent().getIntExtra("headerId", 0);
            if (cHeaderId > 0) {
                header = db.getChatHeaderById(cHeaderId);
            } else {
                String json = getIntent().getStringExtra("header");
                Gson gson = new Gson();
                header = gson.fromJson(json, ChatTask.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (header != null) {
            tvTitle.setText("" + header.getHeaderName());

            if (header.getLastDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date d = sdf.parse(header.getLastDate());
                    tvLastDate.setText("Completion Date : " + sdf1.format(d.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            try {
                final String image = Constants.CHAT_IMAGE_URL + header.getImage();

                Picasso.with(this)
                        .load(image)
                        .placeholder(getResources().getDrawable(R.drawable.profile))
                        .error(getResources().getDrawable(R.drawable.profile))
                        .into(ivPic);
            } catch (Exception e) {
                e.printStackTrace();
            }

            chatList = db.getAllSQLiteChat(header.getHeaderId());

            if (chatAdapter != null) {
                int position = chatAdapter.getItemCount() - 1;
                if (chatList != null) {
                    if (chatList.size() > 0) {
                        for (int i = 0; i < chatList.size(); i++) {
                            if (chatList.get(i).getMarkAsRead() == 0) {
                                position = i;
                            }
                        }
                    }
                }

                if (chatAdapter.getItemCount() > 0)
                    recyclerView.scrollToPosition(position);
            }
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("CHAT_DETAIL")) {
                    handlePushNotification(intent);
                }
            }
        };

        bRRefresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_NOTIFICATION")) {
                    handlePushNotiRefresh(intent);
                }
            }
        };

        bRRefreshImage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_CHAT_IMAGE")) {
                    handlePushNotiRefreshImage(intent);
                }
            }
        };

        brRefreshRead = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_READ_STATUS")) {
                    handlePushNotiRefreshReadStatus(intent);
                }
            }
        };

        brReplyChat = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("CHAT_REPLY")) {
                    handlePushNotifyReplyChat(intent);
                }
            }
        };


    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bRRefresh);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bRRefreshImage);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brRefreshRead);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(brReplyChat);
    }

    @Override
    protected void onResume() {
        super.onResume();

        chatList.clear();
        chatList = db.getAllSQLiteChat(header.getHeaderId());
        Log.e("SQLITE DATA ------- ","---------- "+chatList);

        chatAdapter = new ChatAdapter(chatList, this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatAdapter);

        if (chatAdapter != null) {
            int position = chatAdapter.getItemCount() - 1;
            if (chatList != null) {
                if (chatList.size() > 0) {
                    for (int i = 0; i < chatList.size(); i++) {
                        if (chatList.get(i).getMarkAsRead() == 0) {
                            position = i;
                            break;
                        }
                    }
                }
            }

            if (chatAdapter.getItemCount() > 0)
                recyclerView.scrollToPosition(position);
        }

        db.updateChatDetailReadStatus(header.getHeaderId(), 1);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("CHAT_DETAIL"));
        LocalBroadcastManager.getInstance(this).registerReceiver(bRRefresh,
                new IntentFilter("REFRESH_NOTIFICATION"));
        LocalBroadcastManager.getInstance(this).registerReceiver(bRRefreshImage,
                new IntentFilter("REFRESH_CHAT_IMAGE"));
        LocalBroadcastManager.getInstance(this).registerReceiver(brRefreshRead,
                new IntentFilter("REFRESH_READ_STATUS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(brReplyChat,
                new IntentFilter("CHAT_REPLY"));

        db.updateChatDetailReadStatus(header.getHeaderId(), 1);

        sendUnreadChatToServerForReadStatus();
    }

    public void sendUnreadChatToServerForReadStatus() {
        //----------- STATUS -----------
        //---- 0 - unread
        //---- 1 - read
        //---- 2 - user id send to server
        //---- 3 - All read(blue tick)

        ArrayList<ChatDetail> detailList = new ArrayList<>();
        ArrayList<ChatDisplay> displayList = db.getAllChatNotRead(header.getHeaderId());

        if (displayList != null) {
            for (int i = 0; i < displayList.size(); i++) {
                ChatDisplay chat = displayList.get(i);
                ChatDetail detail = new ChatDetail(chat.getChatTaskDetailId(), chat.getHeaderId(), chat.getTypeOfText(), chat.getTextValue(), chat.getDateTime(), chat.getDateTime(), chat.getUserId(), chat.getUserName(), 1, chat.getMarkAsRead(),chat.getReplyToId());
                detailList.add(detail);
            }
            if (detailList.size() > 0) {
                saveUserChatReadStatus(detailList, loginUser.getEmpId());
            }

        }

        ArrayList<Integer> ids = db.getChatDetailIdsByReadStatus1And3(header.getHeaderId());
        getMarkDetailIds(ids);
    }

    public void saveUserChatReadStatus(final ArrayList<ChatDetail> chatDetail, int userId) {
        if (Constants.isOnline(ChatActivity.this)) {

            Call<Info> infoCall = Constants.myInterface.saveUserIdChatReadToServer(chatDetail, userId);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            for (int i = 0; i < chatDetail.size(); i++) {
                                db.updateChatDetailReadStatusByDetailId(chatDetail.get(i).getChatTaskDetailId(), 2);
                            }

                            ArrayList<Integer> ids = db.getChatDetailIdsByReadStatus1And3(header.getHeaderId());
                            getMarkDetailIds(ids);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getMarkDetailIds(final ArrayList<Integer> detailIds) {
        if (Constants.isOnline(ChatActivity.this)) {

            Call<ArrayList<ChatDetailIdListByReadStatus>> listCall = Constants.myInterface.getChatDetailIdsRead(detailIds);
            listCall.enqueue(new Callback<ArrayList<ChatDetailIdListByReadStatus>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatDetailIdListByReadStatus>> call, Response<ArrayList<ChatDetailIdListByReadStatus>> response) {

                    try {
                        if (response.body() != null) {

                            ArrayList<ChatDetailIdListByReadStatus> idList = response.body();

                            for (int i = 0; i < idList.size(); i++) {

                                db.updateChatDetailReadStatusByDetailId(idList.get(i).getChatTaskDetailId(), 3);

                                for (int j = 0; j < chatList.size(); j++) {
                                    if (chatList.get(j).getChatTaskDetailId() == idList.get(i).getChatTaskDetailId()) {
                                        chatList.get(j).setMarkAsRead(3);
                                        break;
                                    }
                                }

                            }
                            chatAdapter.notifyDataSetChanged();

                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatDetailIdListByReadStatus>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ChatActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePushNotification(Intent intent) {

        Log.e("CHAT ACT", "-----------handlePushNotification---------------");

        String type = intent.getStringExtra("type");
        String msg = intent.getStringExtra("message");
        String title = intent.getStringExtra("title");

        try {
            Gson gson11 = new Gson();
            ChatDetail chatDetail = gson11.fromJson(msg, ChatDetail.class);
            db.updateChatDetailReadStatusByDetailId(chatDetail.getChatTaskDetailId(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();
        ChatDisplay chatDisplay = gson.fromJson(intent.getStringExtra("message"), ChatDisplay.class);
        int res = db.updateChatDetailReadStatusByDetailId(chatDisplay.getChatTaskDetailId(), 1);
        Log.e("CHAT ACT", "--------*****--------- RES -UPDATE - " + res);
        int hId = intent.getIntExtra("headerId", 0);
        if (header != null) {

            if (chatDisplay != null && hId == header.getHeaderId()) {
                Log.e("CHAT ACT", "-----------ChatDisplay---------------" + chatDisplay);
                chatList.add(chatDisplay);
                sendUnreadChatToServerForReadStatus();

                chatList = db.getAllSQLiteChat(header.getHeaderId());
                chatAdapter = new ChatAdapter(chatList, this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(chatAdapter);

                if (chatAdapter.getItemCount() > 1) {
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                }
            }
        }

        db.updateChatDetailReadStatus(header.getHeaderId(), 1);


    }


    private void handlePushNotiRefresh(Intent intent) {

        String type = intent.getStringExtra("type");
        String msg = intent.getStringExtra("message");
        String title = intent.getStringExtra("title");

        if (type.equalsIgnoreCase("chat")) {

            try {
                Gson gson = new Gson();
                ChatDetail chatDetail = gson.fromJson(msg, ChatDetail.class);
                ChatDisplay chat = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getServerDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), 0, 0, 0);

                MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                String text = "";
                if (chat.getTypeOfText() == 1) {
                    text = chat.getTextValue();
                } else if (chat.getTypeOfText() == 2) {
                    text = chat.getUserName() + " has send an image";
                } else if (chat.getTypeOfText() == 201) {
                    text = chat.getUserName() + " has requested to close the task";
                } else if (chat.getTypeOfText() == 101) {
                    text = chat.getUserName() + " has closed the task";
                }

                Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                resultIntent.putExtra("headerId", chat.getHeaderId());
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                if (chat.getHeaderId() != header.getHeaderId()) {

                    if (chat.getTypeOfText() == 101 || chat.getTypeOfText() == 201) {
                        Intent resultIntent1 = new Intent(getApplicationContext(), HomeActivity.class);
                        resultIntent1.putExtra("model", "ChatHeader");

                        mNotificationManager.showBigNotification(title, text, resultIntent1);
                    } else {
                        mNotificationManager.showBigNotification(title, text, resultIntent);
                    }

//                    MyNotificationManager notificationUtils = new MyNotificationManager(getApplicationContext());
//                    notificationUtils.playNotificationSound();

                } else {
                    // MyNotificationManager notificationUtils = new MyNotificationManager(getApplicationContext());
                    // notificationUtils.playNotificationSound();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("header")) {

            try {

                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("model", "ChatHeader");

                //clearExistingNotifications();

                MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                mNotificationManager.showBigNotification(title, "New task group created", resultIntent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("Refresh")) {

            chatList.clear();
            chatList = db.getAllSQLiteChat(header.getHeaderId());
            chatAdapter = new ChatAdapter(chatList, this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(chatAdapter);

            if (chatAdapter != null) {
                int position = chatAdapter.getItemCount() - 1;
                if (chatList != null) {
                    if (chatList.size() > 0) {
                        for (int i = 0; i < chatList.size(); i++) {
                            if (chatList.get(i).getMarkAsRead() == 0) {
                                position = i;
                                break;
                            }
                        }
                    }
                }

                if (chatAdapter.getItemCount() > 0)
                    recyclerView.scrollToPosition(position);
            }

            db.updateChatDetailReadStatus(header.getHeaderId(), 1);

        }

        sendUnreadChatToServerForReadStatus();
    }


    private void handlePushNotiRefreshImage(Intent intent) {
        Log.e("CHAT_ACT", " --------------------------   handlePushNotiRefreshImage    -----------------   ");
        chatList = db.getAllSQLiteChat(header.getHeaderId());
        chatAdapter.notifyDataSetChanged();
        sendUnreadChatToServerForReadStatus();
    }

    private void handlePushNotiRefreshReadStatus(Intent intent) {
        Log.e("CHAT_ACT", " --------------------------   handlePushNotiRefreshReadStatus    -----------------   ");
        Log.e("CHAT_ACT", " ----- handlePushNotiRefreshReadStatus    -----------------   " + chatList.get((chatList.size() - 1)));

        int detailId = 0;

        try {
            detailId = intent.getIntExtra("detailId", 0);

            if (chatList != null) {
                for (int i = 0; i < chatList.size(); i++) {
                    if (chatList.get(i).getChatTaskDetailId() == detailId) {
                        chatList.get(i).setMarkAsRead(3);
                    }
                }

                chatAdapter.notifyDataSetChanged();
            } else {

                chatList.clear();
                chatList = db.getAllSQLiteChat(header.getHeaderId());

                chatAdapter = new ChatAdapter(chatList, ChatActivity.this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(ChatActivity.this);
                recyclerView.setLayoutManager(mLayoutManager1);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(chatAdapter);

                if (chatAdapter.getItemCount() > 0)
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

            }


        } catch (Exception e) {
            e.printStackTrace();

            chatList.clear();
            chatList = db.getAllSQLiteChat(header.getHeaderId());


            chatAdapter = new ChatAdapter(chatList, ChatActivity.this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(ChatActivity.this);
            recyclerView.setLayoutManager(mLayoutManager1);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(chatAdapter);

            if (chatAdapter.getItemCount() > 0)
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

        }


        sendUnreadChatToServerForReadStatus();
    }


    private void handlePushNotifyReplyChat(Intent intent) {

        Log.e("CHAT ACT", "-----------handlePushNotifyReplyChat---------------");

        int replyToMsgType = intent.getIntExtra("replyToMsgType",1);
        String msg = intent.getStringExtra("msg");
        int replyToId = intent.getIntExtra("replyToId", 0);
        String replyToName = intent.getStringExtra("replyToName");
        String replyToMsg = intent.getStringExtra("replyToMsg");

        llChatReply.setVisibility(View.VISIBLE);

        tvReplyToName.setText(""+replyToName);
        tvReplyToMsg.setText(""+replyToMsg);
        tvReplyToMsgType.setText(""+replyToMsgType);
        tvReplyToId.setText(""+replyToId);

        if (replyToMsgType==2){
            ivReplyToImg.setVisibility(View.VISIBLE);
            tvReplyToMsg.setVisibility(View.GONE);

            try {

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME + File.separator + replyToMsg);

                Picasso.with(ChatActivity.this)
                        .load(file)
                        .placeholder(ChatActivity.this.getResources().getDrawable(R.drawable.progress_animation))
                        .error(android.R.color.transparent)
                        //.resize(100, 100)
                        .into(ivReplyToImg);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
            tvReplyToMsg.setVisibility(View.VISIBLE);
            ivReplyToImg.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvAttach) {
            showCameraDialog();

        } else if (v.getId() == R.id.llSend) {

            String msg = edMsg.getText().toString().trim();
            if (!msg.isEmpty()) {

                Calendar cal = Calendar.getInstance();
                long localDate = cal.getTimeInMillis();

                String userName = loginUser.getEmpFname() + " " + loginUser.getEmpMname() + " " + loginUser.getEmpSname();

                int replyToId=0;
                int replyToMsgType=1;
                String replyToMsg=tvReplyToMsg.getText().toString().trim();
                String replyToName=tvReplyToName.getText().toString().trim();

                try{
                    replyToId=Integer.parseInt(tvReplyToId.getText().toString());
                    replyToMsgType=Integer.parseInt(tvReplyToMsgType.getText().toString());

                }catch (Exception e){
                    e.printStackTrace();
                }

                ChatDetail chatDetail;
                if (replyToId>0){
                    chatDetail = new ChatDetail(0, header.getHeaderId(), 1, msg, "" + localDate, "" + localDate, loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),replyToId,replyToMsgType,replyToMsg,replyToName);
                }else{
                    chatDetail = new ChatDetail(0, header.getHeaderId(), 1, msg, "" + localDate, "" + localDate, loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),0);
                }



                if (Constants.isOnline(ChatActivity.this)) {
                    ChatDisplay chatDisplay;
                    if (replyToId>0){
                       chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0,chatDetail.getExInt1(),chatDetail.getReplyToMsgType(),chatDetail.getReplyToMsg(),chatDetail.getReplyToName());
                    }else{
                       chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                    }

                    long id = db.addChatDetailReturnId(chatDisplay);
                    chatList.add(chatDisplay);
                    clearChatReply();

                    edMsg.setText("");

                    chatAdapter = new ChatAdapter(chatList, ChatActivity.this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(chatAdapter);

                    if (chatAdapter.getItemCount() > 0)
                        recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                    saveChatToServer(chatDetail, id);

                } else {
                    ChatDisplay chatDisplay;
                    if (replyToId>0){
                        chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0,chatDetail.getExInt1(),chatDetail.getReplyToMsgType(),chatDetail.getReplyToMsg(),chatDetail.getReplyToName());
                    }else{
                        chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                    }
                    //ChatDisplay chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 1);
                    db.addChatDetail(chatDisplay);
                    chatList.add(chatDisplay);
                    clearChatReply();

                    edMsg.setText("");

                    chatAdapter.notifyDataSetChanged();

                    if (chatAdapter.getItemCount() > 0)
                        recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                }


            }

        } else if (v.getId() == R.id.ivPic) {

            Intent intent = new Intent(ChatActivity.this, ImageZoomActivity.class);
            intent.putExtra("image", Constants.CHAT_IMAGE_URL + header.getImage());
            startActivity(intent);

        } else if (v.getId() == R.id.ivBack) {
            onBackPressed();
        } else if (v.getId() == R.id.ivMenu) {


            PopupMenu popup = new PopupMenu(ChatActivity.this,ivMenu);
            popup.getMenuInflater().inflate(R.menu.menu_chat_delete_and_detail, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    if (menuItem.getItemId()==R.id.action_clear_chat){

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("Caution");
                        builder.setMessage("Do you want to clear the chat ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteChatDetailByHeader(header.getHeaderId(),0);
                                onResume();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();



                    }else if (menuItem.getItemId()==R.id.action_detail){
                        if (Constants.isOnline(ChatActivity.this)) {

                            Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                            intent.putExtra("headerId", header.getHeaderId());
                            startActivity(intent);

                        } else {
                            Toast.makeText(ChatActivity.this, "you are offline!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    return false;
                }
            });
            popup.show();

        }else if (v.getId()==R.id.ivChatReplyClose){
            clearChatReply();
        }
    }

    public void clearChatReply(){
        llChatReply.setVisibility(View.GONE);
        tvReplyToId.setText("0");
        tvReplyToMsgType.setText("1");
        tvReplyToMsg.setText("");
        tvReplyToName.setText("");
    }

    public void saveChatToServer(final ChatDetail chatDetail, final long id) {
        if (Constants.isOnline(this)) {

            Log.e("CHAT", "   - PARAMETER -------***************************************--------- " + chatDetail);

            Call<ChatDetail> infoCall = Constants.myInterface.saveChatDetail(chatDetail);
            infoCall.enqueue(new Callback<ChatDetail>() {
                @Override
                public void onResponse(Call<ChatDetail> call, Response<ChatDetail> response) {
                    try {
                        if (response.body() != null) {
                            ChatDetail data = response.body();
                            Log.e("CHAT  : ", " *-*-*-*-*-*-*-*-*-*-*-*-*- RESPONSE : " + data);

                            db.updateChatDetailIdAndReadStatusByPKId((int) id, data.getChatTaskDetailId(), 2);
                            //db.updateChatDetailLastSyncStatus(data.getChatTaskDetailId(), 1);

                          /*  chatList = db.getAllSQLiteChat(header.getHeaderId());
                            chatAdapter.notifyDataSetChanged();*/

                            chatList = db.getAllSQLiteChat(header.getHeaderId());
                            chatAdapter = new ChatAdapter(chatList, ChatActivity.this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(chatAdapter);

                            if (chatAdapter.getItemCount() > 0)
                                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                        } else {
                            Log.e("CHAT  : ", " RESPONSE :  NULL");
                        }
                    } catch (Exception e) {
                        Log.e("CHAT  : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ChatDetail> call, Throwable t) {
                    Log.e("CHAT  : ", " RESPONSE :  onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void showCameraDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Choose");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureActionIntent, 101);
            }
        });
        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");

                        String authorities = BuildConfig.APPLICATION_ID + ".provider";
                        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 102);

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 102);

                    }
                } catch (Exception e) {
                    ////Log.e("select camera : ", " Exception : " + e.getMessage());
                }
            }
        });
        builder.show();
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    //--------------------------IMAGE-----------------------------------------

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
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //ivImage.setImageBitmap(myBitmap);

                    myBitmap = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath = f.getAbsolutePath();

                ArrayList<String> pathArray = new ArrayList<>();
                ArrayList<String> fileNameArray = new ArrayList<>();

                String photo = "";
                Calendar cal = Calendar.getInstance();

                if (imagePath != null) {

                    pathArray.add(imagePath);

                    File imgFile1 = new File(imagePath);
                    int pos = imgFile1.getName().lastIndexOf(".");
                    String ext = imgFile1.getName().substring(pos + 1);
                    photo = loginUser.getEmpId() + "_" + cal.getTimeInMillis() + "." + ext;
                    fileNameArray.add(photo);


                    Log.e("CHAT_ACT", " ****************************** --------- " + imgFile1);


                    File sourceFile = new File(imagePath);
                    File targetFile = new File(folder + File.separator + imgFile1.getName());

                    Log.e("CHAT_ACT", " ****************************** SOURCE --------- " + sourceFile);
                    Log.e("CHAT_ACT", " ****************************** TARGET --------- " + targetFile);

                    if (sourceFile.exists()) {

                        if (targetFile.exists()) {

                            File targetFile1 = new File(folder + File.separator + photo);

                            InputStream in = new FileInputStream(sourceFile);
                            OutputStream out = new FileOutputStream(targetFile1);

                            // Copy the bits from instream to outstream
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in.close();
                            out.close();

                            Log.e("CHAT_ACT", "Copy file successful.");

                        } else {


                            InputStream in = new FileInputStream(sourceFile);
                            OutputStream out = new FileOutputStream(targetFile);

                            // Copy the bits from instream to outstream
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in.close();
                            out.close();

                            Log.e("CHAT_ACT", "Copy file successful.");


                            File oldNameFile = new File(folder + File.separator + imgFile1.getName());
                            File newNameFile = new File(folder + File.separator + photo);

                            if (oldNameFile.renameTo(newNameFile)) {
                                Log.e("CHAT_ACT", "Rename successful.");
                            } else {
                                Log.e("CHAT_ACT", "Rename failed.");
                            }

                        }


                    } else {
                        Log.e("CHAT_ACT", "Copy file failed. Source file missing.");
                    }


                }

                String userName = loginUser.getEmpFname() + " " + loginUser.getEmpMname() + " " + loginUser.getEmpSname();

                int replyToId=0;
                int replyToMsgType=1;
                String replyToMsg=tvReplyToMsg.getText().toString().trim();
                String replyToName=tvReplyToName.getText().toString().trim();

                try{
                    replyToId=Integer.parseInt(tvReplyToId.getText().toString());
                    replyToMsgType=Integer.parseInt(tvReplyToMsgType.getText().toString());

                }catch (Exception e){
                    e.printStackTrace();
                }

                ChatDetail chatDetail;
                if (replyToId>0){
                    chatDetail = new ChatDetail(0, header.getHeaderId(), 2, photo, "" + cal.getTimeInMillis(), "" + cal.getTimeInMillis(), loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),replyToId,replyToMsgType,replyToMsg,replyToName);
                }else{
                    chatDetail = new ChatDetail(0, header.getHeaderId(), 2, photo, "" + cal.getTimeInMillis(), "" + cal.getTimeInMillis(), loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),0);
                }

                ChatDisplay chatDisplay;
                if (replyToId>0){
                    chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0,chatDetail.getExInt1(),chatDetail.getReplyToMsgType(),chatDetail.getReplyToMsg(),chatDetail.getReplyToName());
                }else{
                    chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                }

                //ChatDetail chatDetail = new ChatDetail(0, header.getHeaderId(), 2, photo, "" + cal.getTimeInMillis(), "" + cal.getTimeInMillis(), loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),0);

               //ChatDisplay chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                long id = db.addChatDetailReturnId(chatDisplay);
                chatList.add(chatDisplay);
                chatAdapter.notifyDataSetChanged();
                clearChatReply();

                if (chatAdapter.getItemCount() > 0)
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                sendImageToServer(pathArray, fileNameArray, chatDetail, id);

                //tvImageName.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap = getBitmapFromCameraData(data, this);

                //ivImage.setImageBitmap(myBitmap);
                imagePath = uriFromPath.getPath();
                //tvImageName.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

                ArrayList<String> pathArray = new ArrayList<>();
                ArrayList<String> fileNameArray = new ArrayList<>();

                String photo = "";
                Calendar cal = Calendar.getInstance();

                if (imagePath != null) {

                    pathArray.add(imagePath);

                    File imgFile1 = new File(imagePath);
                    int pos = imgFile1.getName().lastIndexOf(".");
                    String ext = imgFile1.getName().substring(pos + 1);
                    photo = loginUser.getEmpId() + "_" + cal.getTimeInMillis() + "." + ext;
                    fileNameArray.add(photo);

                    Log.e("CHAT_ACT", " ****************************** --------- " + imgFile1);


                    File sourceFile = new File(imagePath);
                    File targetFile = new File(folder + File.separator + imgFile1.getName());

                    Log.e("CHAT_ACT", " ****************************** SOURCE --------- " + sourceFile);
                    Log.e("CHAT_ACT", " ****************************** TARGET --------- " + targetFile);

                    if (sourceFile.exists()) {

                        if (targetFile.exists()) {

                            File targetFile1 = new File(folder + File.separator + photo);

                            InputStream in = new FileInputStream(sourceFile);
                            OutputStream out = new FileOutputStream(targetFile1);

                            // Copy the bits from instream to outstream
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in.close();
                            out.close();

                            Log.e("CHAT_ACT", "Copy file successful.");

                        } else {


                            InputStream in = new FileInputStream(sourceFile);
                            OutputStream out = new FileOutputStream(targetFile);

                            // Copy the bits from instream to outstream
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in.close();
                            out.close();

                            Log.e("CHAT_ACT", "Copy file successful.");


                            File oldNameFile = new File(folder + File.separator + imgFile1.getName());
                            File newNameFile = new File(folder + File.separator + photo);

                            if (oldNameFile.renameTo(newNameFile)) {
                                Log.e("CHAT_ACT", "Rename successful.");
                            } else {
                                Log.e("CHAT_ACT", "Rename failed.");
                            }

                        }


                    } else {
                        Log.e("CHAT_ACT", "Copy file failed. Source file missing.");
                    }
                }

                String userName = loginUser.getEmpFname() + " " + loginUser.getEmpMname() + " " + loginUser.getEmpSname();

                int replyToId=0;
                int replyToMsgType=1;
                String replyToMsg=tvReplyToMsg.getText().toString().trim();
                String replyToName=tvReplyToName.getText().toString().trim();

                try{
                    replyToId=Integer.parseInt(tvReplyToId.getText().toString());
                    replyToMsgType=Integer.parseInt(tvReplyToMsgType.getText().toString());

                }catch (Exception e){
                    e.printStackTrace();
                }

                ChatDetail chatDetail;
                if (replyToId>0){
                    chatDetail = new ChatDetail(0, header.getHeaderId(), 2, photo, "" + cal.getTimeInMillis(), "" + cal.getTimeInMillis(), loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),replyToId,replyToMsgType,replyToMsg,replyToName);
                }else{
                    chatDetail = new ChatDetail(0, header.getHeaderId(), 2, photo, "" + cal.getTimeInMillis(), "" + cal.getTimeInMillis(), loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),0);
                }

                ChatDisplay chatDisplay;
                if (replyToId>0){
                    chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0,chatDetail.getExInt1(),chatDetail.getReplyToMsgType(),chatDetail.getReplyToMsg(),chatDetail.getReplyToName());
                }else{
                    chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                }



               // ChatDetail chatDetail = new ChatDetail(0, header.getHeaderId(), 2, photo, "" + cal.getTimeInMillis(), "" + cal.getTimeInMillis(), loginUser.getEmpId(), userName, 1, 1, "" + loginUser.getEmpId(),0);
               // ChatDisplay chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);

                long id = db.addChatDetailReturnId(chatDisplay);
                chatList.add(chatDisplay);
                chatAdapter.notifyDataSetChanged();
                clearChatReply();

                if (chatAdapter.getItemCount() > 0)
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                sendImageToServer(pathArray, fileNameArray, chatDetail, id);


            } catch (Exception e) {
                e.printStackTrace();
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
        path = picturePath;
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


    private void sendImageToServer(final ArrayList<String> filePath, final ArrayList<String> fileName, final ChatDetail chatDetail, final long id) {

        Log.e("PARAMETER : ", "   FILE PATH : " + filePath + "            FILE NAME : " + fileName);

        final CommonDialog commonDialog = new CommonDialog(ChatActivity.this, "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = null;

        MultipartBody.Part[] uploadImagesParts = new MultipartBody.Part[filePath.size()];

        for (int index = 0; index < filePath.size(); index++) {
            Log.e("ATTACH ACT", "requestUpload:  image " + index + "  " + filePath.get(index));
            imgFile = new File(filePath.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            uploadImagesParts[index] = MultipartBody.Part.createFormData("file", "" + fileName.get(index), surveyBody);
        }

        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), "chat");

        Call<JSONObject> call = Constants.myInterface.imageUpload(uploadImagesParts, fileName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

                imagePath = null;

                Log.e("Response : ", "-IMAGE SAVED -" + response.body());

                chatList.clear();
                chatList = db.getAllSQLiteChat(header.getHeaderId());

                Log.e("CHAT_ACT", "-----SQLITE------- " + chatList);

                chatAdapter = new ChatAdapter(chatList, ChatActivity.this, loginUser.getEmpId(), ChatActivity.this,"chatAct");
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(chatAdapter);

                if (chatAdapter.getItemCount() > 0)
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                saveChatToServer(chatDetail, id);

                commonDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(ChatActivity.this, "Failed to send image, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
        intent.putExtra("model", "ChatHeader");
        startActivity(intent);
        finish();

    }
}
