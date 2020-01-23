package com.ats.dutyapp.activity;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.EmployeeListAdapter;
import com.ats.dutyapp.adapter.TaskListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fcm.MyNotificationManager;
import com.ats.dutyapp.fragment.DashboardFragment;
import com.ats.dutyapp.model.ChatDetail;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.ChatHeader;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.GroupEmp;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.sqlite.DatabaseHandler;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public RecyclerView recyclerView;
    public CardView cvCloseTask, cvCloseRequest, cvCloseReqName;
    public TextView tvTaskName, tvCompletionDate, tvRemark, tvDesc, tvCloseReq;
    ArrayList<GroupEmp> empList = new ArrayList<>();
    ArrayList<Employee> employeeList = new ArrayList<>();
    EmployeeListAdapter adapter;
    public Login loginUser;
    public ChatTask model;
    String strModel;
    CircleImageView ivImg;
    public ImageView ivEdit;

    DatabaseHandler db;
    int hId,editType;//editType - 0-chatActivity, 1-groupwiseChatActivity
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        recyclerView = findViewById(R.id.recyclerView);

        cvCloseTask = findViewById(R.id.cvCloseTask);
        cvCloseRequest = findViewById(R.id.cvCloseRequest);
        cvCloseReqName = findViewById(R.id.cvCloseReqName);

        tvTaskName = findViewById(R.id.tvTaskName);
        tvCompletionDate = findViewById(R.id.tvCompletionDate);
        tvRemark = findViewById(R.id.tvRemark);
        tvDesc = findViewById(R.id.tvDesc);
        tvCloseReq = findViewById(R.id.tvCloseReq);
        ivImg = findViewById(R.id.ivImg);

        ivEdit = findViewById(R.id.ivEdit);

        db = new DatabaseHandler(ChatDetailActivity.this);

        try {
            String userStr = CustomSharedPreference.getString(this, CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser.getEmpId());

            loadAllTaskFromServer(loginUser.getEmpId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            hId = getIntent().getIntExtra("headerId", 0);

            editType = getIntent().getIntExtra("groupWise", 0);


        } catch (Exception e) {
            e.printStackTrace();
        }

        cvCloseTask.setOnClickListener(this);
        cvCloseRequest.setOnClickListener(this);
        ivImg.setOnClickListener(this);
        ivEdit.setOnClickListener(this);

    }

    public void loadData() {

        if (model != null) {
            if (model.getPrivilege() == 3) {
                ivEdit.setVisibility(View.INVISIBLE);
            }
        }

        tvTaskName.setText("" + model.getHeaderName());
        tvDesc.setText("" + model.getTaskDesc());

        try {
            final String image = Constants.CHAT_IMAGE_URL + model.getImage();

            Picasso.with(this)
                    .load(image)
                    .placeholder(getResources().getDrawable(R.drawable.profile))
                    .error(getResources().getDrawable(R.drawable.profile))
                    .into(ivImg);
        } catch (Exception e) {
            Log.e("CHAT_ACT", "----------- EXCEPTION --------- " + e.getMessage());
            e.printStackTrace();
        }

        if (model.getStatus() == 1) {
            cvCloseReqName.setVisibility(View.VISIBLE);
            tvCloseReq.setText("Close Request by - " + model.getRequestUserName());

            cvCloseRequest.setVisibility(View.GONE);
        }


        List<String> adminIds = new ArrayList<>();
        if (model.getAdminUserIds() != null) {
            if (!model.getAdminUserIds().isEmpty()) {
                adminIds = Arrays.asList(model.getAdminUserIds().split("\\s*,\\s*"));
            }
        }

        if (model.getPrivilege() == 1) {
            cvCloseTask.setVisibility(View.VISIBLE);
            cvCloseRequest.setVisibility(View.GONE);
        } else if (model.getPrivilege() == 2) {
            cvCloseTask.setVisibility(View.VISIBLE);
            cvCloseRequest.setVisibility(View.GONE);
        } else {
            if (model.getStatus() == 1) {
                cvCloseRequest.setVisibility(View.GONE);
                cvCloseTask.setVisibility(View.GONE);
            } else {
                cvCloseRequest.setVisibility(View.VISIBLE);
                cvCloseTask.setVisibility(View.GONE);
            }
        }

        getAllEmp(model.getHeaderId());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvCloseRequest) {

            Log.e("MODEL ", " --------------------- " + model);

            AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to make request to close the task?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateCloseRequest(loginUser.getEmpId(), 1, model.getHeaderId());

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

        } else if (v.getId() == R.id.cvCloseTask) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to complete task ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    updateChatClose(loginUser.getEmpId(), 2, model.getHeaderId(), "remark");

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
        } else if (v.getId() == R.id.ivImg) {

            Intent intent = new Intent(ChatDetailActivity.this, ImageZoomActivity.class);
            intent.putExtra("image", Constants.CHAT_IMAGE_URL + model.getImage());
            startActivity(intent);

        } else if (v.getId() == R.id.ivEdit) {

            if (editType==0){
                Intent intent = new Intent(ChatDetailActivity.this, HomeActivity.class);
                intent.putExtra("model", "EditChat");
                intent.putExtra("json", strModel);
                intent.putExtra("type", 0);
                startActivity(intent);
                finish();

            }else{
                Intent intent = new Intent(ChatDetailActivity.this, HomeActivity.class);
                intent.putExtra("model", "EditChat");
                intent.putExtra("json", strModel);
                intent.putExtra("type", 1);
                startActivity(intent);
                finish();
            }



        }
    }


    private void getAllEmp(int headerId) {
        if (Constants.isOnline(ChatDetailActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<GroupEmp>> listCall = Constants.myInterface.getChatEmpListByHeader(headerId);
            listCall.enqueue(new Callback<ArrayList<GroupEmp>>() {
                @Override
                public void onResponse(Call<ArrayList<GroupEmp>> call, Response<ArrayList<GroupEmp>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("EMPLOYEE LIST : ", " -----------------------------------EMPLOYEE LIST---------------------------- " + response.body());
                            empList.clear();
                            empList = response.body();

                            Log.e("BIN", "---------------------------------Model-----------------" + employeeList);

                            adapter = new EmployeeListAdapter(empList, ChatDetailActivity.this, model, loginUser);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatDetailActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            commonDialog.dismiss();

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
                public void onFailure(Call<ArrayList<GroupEmp>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ChatDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


  /*  private void saveHeader(ChatHeader chatHeader) {
        Log.e("PARAMETER", "---------------------------------------UPDATE STATUS--------------------------" + chatHeader);

        if (Constants.isOnline(ChatDetailActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ChatHeader> listCall = Constants.myInterface.saveChatHeader(chatHeader);
            listCall.enqueue(new Callback<ChatHeader>() {
                @Override
                public void onResponse(Call<ChatHeader> call, Response<ChatHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE STATUS : ", " ------------------------------UPDATE STATUS------------------------ " + response.body());
                            Toast.makeText(ChatDetailActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            commonDialog.dismiss();
                            getAllTask(loginUser.getEmpId());

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                            builder.setTitle("" + getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("" + getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<ChatHeader> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                    builder.setTitle("" + getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(ChatDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void updateCloseRequest(int empId, int status, int headerId) {

        if (Constants.isOnline(ChatDetailActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateChatCloseRequest(headerId, status, empId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            if (!response.body().getError()) {

                                Log.e("UPDATE STATUS : ", " ------------------------------UPDATE STATUS------------------------ " + response.body());
                                Toast.makeText(ChatDetailActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();

                                String username = loginUser.getEmpFname() + " " + loginUser.getEmpMname() + " " + loginUser.getEmpSname();

                                String msg = username + " has requested to close the task";

                                Calendar cal = Calendar.getInstance();
                                long localDate = cal.getTimeInMillis();

                                ChatDetail chatDetail = new ChatDetail(0, model.getHeaderId(), 201, msg, "" + localDate, "" + localDate, loginUser.getEmpId(), username, 1, 1,0);

                                ChatDisplay chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                                db.addChatDetail(chatDisplay);

                                saveChatToServer(chatDetail);


                                //getAllTask(loginUser.getEmpId());

                            } else {
                                commonDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                                builder.setTitle("" + getResources().getString(R.string.app_name));
                                builder.setMessage("Unable to process! please try again.");

                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }


                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                            builder.setTitle("" + getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("" + getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                    builder.setTitle("" + getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(ChatDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateChatClose(int empId, int status, int headerId, String remark) {

        if (Constants.isOnline(ChatDetailActivity.this)) {
            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateChatHeaderClose(headerId, status, empId, remark);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            if (!response.body().getError()) {

                                Log.e("UPDATE STATUS : ", " ------------------------------UPDATE STATUS------------------------ " + response.body());
                                Toast.makeText(ChatDetailActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();


                                String username = loginUser.getEmpFname() + " " + loginUser.getEmpMname() + " " + loginUser.getEmpSname();

                                String msg = username + " has closed the task";

                                Calendar cal = Calendar.getInstance();
                                long localDate = cal.getTimeInMillis();

                                ChatDetail chatDetail = new ChatDetail(0, model.getHeaderId(), 101, msg, "" + localDate, "" + localDate, loginUser.getEmpId(), username, 1, 1,0);

                                ChatDisplay chatDisplay = new ChatDisplay(chatDetail.getChatTaskDetailId(), chatDetail.getHeaderId(), chatDetail.getTypeOfText(), chatDetail.getTextValue(), chatDetail.getLocalDate(), chatDetail.getUserId(), chatDetail.getUserName(), chatDetail.getDelStatus(), chatDetail.getMarkAsRead(), 0, 0);
                                db.addChatDetail(chatDisplay);

                                saveChatToServer(chatDetail);

                                // getAllTask(loginUser.getEmpId());

                            } else {
                                commonDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                                builder.setTitle("" + getResources().getString(R.string.app_name));
                                builder.setMessage("Unable to process! please try again.");

                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                            builder.setTitle("" + getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("" + getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatDetailActivity.this, R.style.AlertDialogTheme);
                    builder.setTitle("" + getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(ChatDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveChatToServer(final ChatDetail chatDetail) {
        if (Constants.isOnline(this)) {

            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Log.e("CHAT", "   - PARAMETER ---------------- " + chatDetail);

            Call<ChatDetail> infoCall = Constants.myInterface.saveChatDetail(chatDetail);
            infoCall.enqueue(new Callback<ChatDetail>() {
                @Override
                public void onResponse(Call<ChatDetail> call, Response<ChatDetail> response) {
                    try {
                        if (response.body() != null) {
                            ChatDetail data = response.body();
                            Log.e("CHAT  : ", "  RESPONSE : " + data);

                            commonDialog.dismiss();
                            //getAllTask(loginUser.getEmpId());
                            Intent intent = new Intent(ChatDetailActivity.this, HomeActivity.class);
                            intent.putExtra("model", "ChatHeader");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            commonDialog.dismiss();

                            Log.e("CHAT  : ", " RESPONSE :  NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();

                        Log.e("CHAT  : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ChatDetail> call, Throwable t) {
                    commonDialog.dismiss();

                    Log.e("CHAT  : ", " RESPONSE :  onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    private void getAllTask(int userId) {
        if (Constants.isOnline(ChatDetailActivity.this)) {

            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ChatTask>> listCall = Constants.myInterface.getAllChatHeaderDisplayByUser(userId);
            listCall.enqueue(new Callback<ArrayList<ChatTask>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatTask>> call, Response<ArrayList<ChatTask>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER LIST : ", "------------------------------------------------- " + response.body());

                            if (response.body().size() > 0) {

                                commonDialog.dismiss();

                                db.removeAllChatHeader();

                                for (int i = 0; i < response.body().size(); i++) {
                                    db.addChatHeader(response.body().get(i));
                                }

                                Intent intent = new Intent(ChatDetailActivity.this, HomeActivity.class);
                                intent.putExtra("model", "ChatHeader");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }

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
                public void onFailure(Call<ArrayList<ChatTask>> call, Throwable t) {
                    commonDialog.dismiss();

                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ChatDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadAllTaskFromServer(int userId) {
        if (Constants.isOnline(ChatDetailActivity.this)) {

            final CommonDialog commonDialog = new CommonDialog(ChatDetailActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ChatTask>> listCall = Constants.myInterface.getAllChatHeaderDisplayByUser(userId);
            listCall.enqueue(new Callback<ArrayList<ChatTask>>() {
                @Override
                public void onResponse(Call<ArrayList<ChatTask>> call, Response<ArrayList<ChatTask>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER LIST : ", "------------------------------------------------- " + response.body());

                            if (response.body().size() > 0) {

                                commonDialog.dismiss();

                                db.removeAllChatHeader();

                                for (int i = 0; i < response.body().size(); i++) {
                                    db.addChatHeader(response.body().get(i));

                                    if (response.body().get(i).getHeaderId() == hId) {

                                        Gson g = new Gson();
                                        strModel = g.toJson(response.body().get(i));

                                    }
                                }

                                model = db.getChatHeaderById(hId);
                                loadData();

                            }

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
                public void onFailure(Call<ArrayList<ChatTask>> call, Throwable t) {
                    commonDialog.dismiss();

                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(ChatDetailActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("REFRESH_NOTIFICATION"));

    }


    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "-------------------CHAT DETAIL ACT-----------------");
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
                } else if (chat.getTypeOfText() == 101) {
                    text = chat.getTextValue();
                }

                Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                resultIntent.putExtra("headerId", chat.getHeaderId());
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                if (chat.getHeaderId() == model.getHeaderId()) {

                    if (chat.getTypeOfText() == 101) {
                        Intent intent1 = new Intent(ChatDetailActivity.this, HomeActivity.class);
                        intent1.putExtra("model", "ChatHeader");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                    } else {
                        mNotificationManager.showBigNotification(title, text, resultIntent);
                        loadAllTaskFromServer(loginUser.getEmpId());
                    }

                } else {
                    if (chat.getTypeOfText() != 101) {
                        mNotificationManager.showBigNotification(title, text, resultIntent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("header")) {
            try {
                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("model", "ChatHeader");

                MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                mNotificationManager.showBigNotification("You are added to the new group", "please check for more information", resultIntent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
