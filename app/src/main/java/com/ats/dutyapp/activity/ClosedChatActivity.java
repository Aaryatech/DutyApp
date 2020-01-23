package com.ats.dutyapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.ChatAdapter;
import com.ats.dutyapp.adapter.ClosedTaskListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.ats.dutyapp.utils.PermissionsUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClosedChatActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LinearLayout llSend;
    private EditText edMsg;
    ImageView ivBack, ivMenu;
    TextView tvTitle;
    TextView tvToolbarTitle, tvToolbarUser, tvAttach;
    CircleImageView ivPic;

    Login loginUser;

    ArrayList<ChatDisplay> chatList = new ArrayList<>();
    ChatAdapter chatAdapter;

    ChatTask header;
    int cHeaderId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_chat);

        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        recyclerView = findViewById(R.id.recyclerView);
        llSend = findViewById(R.id.llSend);
        edMsg = findViewById(R.id.edMsg);
        ivBack = findViewById(R.id.ivBack);
        ivPic = findViewById(R.id.ivPic);
        ivMenu = findViewById(R.id.ivMenu);
        tvTitle = findViewById(R.id.tvTitle);

        ivMenu.setVisibility(View.GONE);

        ivBack.setOnClickListener(this);
        ivPic.setOnClickListener(this);

        try {
            String userStr = CustomSharedPreference.getString(getApplication(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String str = getIntent().getStringExtra("header");
            Gson gson1 = new Gson();
            header = gson1.fromJson(str, ChatTask.class);

            if (header != null) {
                tvTitle.setText("" + header.getHeaderName());

                try {
                    final String image = Constants.CHAT_IMAGE_URL + header.getImage();

                    Picasso.with(this)
                            .load(image)
                            .placeholder(getResources().getDrawable(R.drawable.profile))
                            .error(getResources().getDrawable(R.drawable.profile))
                            .into(ivPic);
                } catch (Exception e) {
                    Log.e("CHAT_ACT", "----------- EXCEPTION --------- " + e.getMessage());
                    e.printStackTrace();
                }

                getAllChat(header.getHeaderId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ivBack){
            finish();
        }else if (v.getId()==R.id.ivPic){

        }
    }



    private void getAllChat(int headerId) {
        if (Constants.isOnline(ClosedChatActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ClosedChatActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ChatDetail>> listCall = Constants.myInterface.getAllChatDetailByHeader(headerId);
            listCall.enqueue(new Callback<ArrayList<ChatDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatDetail>> call, Response<ArrayList<ChatDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CHAT LIST : ", "------------------------------------------------- " + response.body());
                            commonDialog.dismiss();

                            ArrayList<ChatDisplay> chatList=new ArrayList<>();
                            if (response.body().size()>0){
                                for (int i=0;i<response.body().size();i++){

                                    ChatDetail chat=response.body().get(i);

                                    String date="";
                                    if (loginUser.getEmpId()==chat.getUserId()){
                                        date=chat.getLocalDate();
                                    }else{
                                        date=chat.getServerDate();
                                    }

                                    ChatDisplay display=new ChatDisplay(chat.getChatTaskDetailId(),chat.getHeaderId(),chat.getTypeOfText(),chat.getTextValue(),date,chat.getUserId(),chat.getUserName(),chat.getDelStatus(),chat.getMarkAsRead());
                                    chatList.add(display);

                                }
                            }

                            ChatAdapter chatAdapter = new ChatAdapter(chatList, ClosedChatActivity.this, loginUser.getEmpId(),ClosedChatActivity.this,"closechat");
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ClosedChatActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(chatAdapter);

                            if (chatAdapter.getItemCount() > 0)
                                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ChatDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ClosedChatActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
