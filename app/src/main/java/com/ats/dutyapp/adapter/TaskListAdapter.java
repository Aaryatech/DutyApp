package com.ats.dutyapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.ChatActivity;
import com.ats.dutyapp.activity.GroupwiseTaskChatActivity;
import com.ats.dutyapp.activity.HomeActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.fragment.EditTaskFragment;
import com.ats.dutyapp.fragment.TaskCommunicationlFragment;
import com.ats.dutyapp.fragment.TaskListFragment;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {
    private ArrayList<ChatTask> taskList;
    private static Context context;
    private String fragmentName;

   /* public TaskListAdapter(ArrayList<ChatTask> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;

    }*/

    public TaskListAdapter(ArrayList<ChatTask> taskList, Context context, String fragmentName) {
        this.taskList = taskList;
        this.context = context;
        this.fragmentName = fragmentName;
    }

    @NonNull
    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_task_header_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /*public TextView tvGenrateDate,tvCompletionDate,tvRemark,tvEmpName,tvStatus,tvTaskName;
        public CardView cardView;
        public ImageView ivEdit;
        */

        public LinearLayout llBack;
        private TextView tvName, tvCount, tvCloseReq;
        private CircleImageView ivPic;
        private View viewLine;
        private ImageView ivDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            llBack = (itemView).findViewById(R.id.llBack);
            tvName = (itemView).findViewById(R.id.tvName);
            tvCount = (itemView).findViewById(R.id.tvCount);
            ivPic = (itemView).findViewById(R.id.ivPic);
            tvCloseReq = (itemView).findViewById(R.id.tvCloseReq);
            viewLine = (itemView).findViewById(R.id.viewLine);
            ivDelete = (itemView).findViewById(R.id.ivDelete);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.MyViewHolder myViewHolder, int i) {

        String userStr = CustomSharedPreference.getString(context, CustomSharedPreference.MAIN_KEY_USER);
        Gson gson = new Gson();
        Login loginUser = gson.fromJson(userStr, Login.class);


        final ChatTask model = taskList.get(i);

        if (loginUser.getEmpId() .equals(model.getCreatedUserId()) ) {
            myViewHolder.ivDelete.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.ivDelete.setVisibility(View.GONE);
        }


        myViewHolder.tvName.setText("" + model.getHeaderName());

        if (model.getUnreadCount() == 0) {
            myViewHolder.tvCount.setVisibility(View.GONE);
        } else {
            myViewHolder.tvCount.setVisibility(View.VISIBLE);
            myViewHolder.tvCount.setText("" + model.getUnreadCount());
        }

        if (model.getStatus() == 1) {
            myViewHolder.viewLine.setVisibility(View.VISIBLE);
            myViewHolder.tvCloseReq.setVisibility(View.VISIBLE);
            myViewHolder.tvCloseReq.setText("Close Request by - " + model.getRequestUserName());
        } else {
            myViewHolder.viewLine.setVisibility(View.INVISIBLE);
            myViewHolder.tvCloseReq.setVisibility(View.GONE);
        }

        try {
            String image = Constants.CHAT_IMAGE_URL + "/" + model.getImage();
            Log.e("TASK ADAPTER", "---------- IMAGE ---------- " + image);
            Picasso.with(context).load(image).placeholder(context.getResources().getDrawable(R.drawable.profile)).error(context.getResources().getDrawable(R.drawable.profile)).into(myViewHolder.ivPic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolder.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                if (fragmentName.equalsIgnoreCase("group")) {
                    Intent intent = new Intent(context, GroupwiseTaskChatActivity.class);
                    intent.putExtra("header", json);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("header", json);
                    context.startActivity(intent);
                }
            }
        });
        
        myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder  builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete this task ?");
                builder.setCancelable(false);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(model.getHeaderId());
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setTitle("Delete Task");
                alert.show();
            }
        });

       /* myViewHolder.tvCompletionDate.setText(""+model.getLastDate());
        myViewHolder.tvGenrateDate.setText(""+model.getCreatedDate());
        myViewHolder.tvEmpName.setText(""+model.getAssignUserNames());
        myViewHolder.tvRemark.setText(""+model.getTaskCompleteRemark());
        myViewHolder.tvTaskName.setText(""+model.getHeaderName());

        if(model.getStatus()==0)
        {
            myViewHolder.tvStatus.setText("On Going");
        }else if(model.getStatus()==1)
        {
            myViewHolder.tvStatus.setText("Request Close");
        }else if(model.getStatus()==2)
        {
            myViewHolder.tvStatus.setText("Close Task");
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("header",json);
                context.startActivity(intent);


            }
        });

        */

        /*myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            Fragment adf = new EditTaskFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            args.putInt("type", 0);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "TaskListFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete task?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteTask(model.getHeaderId());
                                    dialog.dismiss();
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
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });*/

    }

    private void deleteTask(Integer headerId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteChatHeader(headerId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DELETE TASK: ", " - " + response.body());

                            if (!response.body().getError()) {

                                HomeActivity activity = (HomeActivity) context;

                               // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new TaskListFragment(), "MainFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


}
