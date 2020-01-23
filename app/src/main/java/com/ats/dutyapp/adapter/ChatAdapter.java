package com.ats.dutyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.activity.ImageZoomActivity;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChatDisplay;
import com.ats.dutyapp.model.EmpChatReadModel;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.ImageDownloadAsync;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private ArrayList<ChatDisplay> chatDetails;
    private Context context;
    private int userId;
    private Activity activity;
    private String chatActType;

    public ChatAdapter(ArrayList<ChatDisplay> chatDetails, Context context, int userId, Activity activity, String chatActType) {
        this.chatDetails = chatDetails;
        this.context = context;
        this.userId = userId;
        this.activity = activity;
        this.chatActType = chatActType;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMsg, tvName, tvDate, tvReplyToMsg, tvReplyToName;
        public ImageView ivImg, ivTick, ivReplyToImg;
        public LinearLayout llParent, llBody, llName, llReply, llReplyAction;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvMsg = view.findViewById(R.id.tvMsg);
            tvName = view.findViewById(R.id.tvName);
            tvDate = view.findViewById(R.id.tvDate);
            llParent = view.findViewById(R.id.llParent);
            llBody = view.findViewById(R.id.llBody);
            llName = view.findViewById(R.id.llName);
            ivImg = view.findViewById(R.id.ivImg);
            ivTick = view.findViewById(R.id.ivTick);
            cardView = view.findViewById(R.id.cardView);

            tvReplyToMsg = view.findViewById(R.id.tvReplyToMsg);
            tvReplyToName = view.findViewById(R.id.tvReplyToName);
            ivReplyToImg = view.findViewById(R.id.ivReplyToImg);
            llReply = view.findViewById(R.id.llReply);
            llReplyAction = view.findViewById(R.id.llReplyAction);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ChatDisplay chat = chatDetails.get(position);
        // Log.e("CHAT", "---------------------------------------- " + chat);

        //holder.tvMsg.setTextIsSelectable(true);

        String dateDisplay = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

            //   Log.e("DATE CONVERT", "---------------------------------------- " + chat.getDateTime());

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(chat.getDateTime()));
            dateDisplay = sdf.format(cal.getTimeInMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (chat.getUserId() == userId) {

            holder.llName.setVisibility(View.GONE);

            holder.llParent.setGravity(Gravity.RIGHT);
            holder.llParent.setPadding(45, 0, 0, 0);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#e6ffe6"));
            holder.tvMsg.setText(chat.getTextValue());
            holder.tvDate.setText(dateDisplay);
            holder.tvName.setText(chat.getUserName());

            if (chat.getTypeOfText() == 1) {
                holder.tvMsg.setVisibility(View.VISIBLE);
                holder.ivImg.setVisibility(View.GONE);
            } else if (chat.getTypeOfText() == 2) {

                File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);

                File imgFile = new File(folder.getAbsolutePath() + File.separator + chat.getTextValue());
                if (!imgFile.exists()) {
                    // AltexImageDownloader.writeToDisk(context, Constants.CHAT_IMAGE_URL + chat.getTextValue(), "");

                    new ImageDownloadAsync(activity, chat.getTextValue()).execute(Constants.CHAT_IMAGE_URL + chat.getTextValue());

                }
                holder.tvMsg.setVisibility(View.GONE);
                holder.ivImg.setVisibility(View.VISIBLE);

                try {
                    final String image = Constants.CHAT_IMAGE_URL + chat.getTextValue();

                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME + File.separator + chat.getTextValue());

                    Picasso.with(context)
                            .load(file)
                            .placeholder(context.getResources().getDrawable(R.drawable.progress_animation))
                            .error(android.R.color.transparent)
                            //.resize(100, 100)
                            .into(holder.ivImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            //-----------------Reply-------------------

            if (chat.getReplyToId() > 0) {
                holder.llReply.setVisibility(View.VISIBLE);

                holder.tvReplyToName.setText("" + chat.getReplyToName());
                holder.tvReplyToMsg.setText("" + chat.getReplyToMsg());

                if (chat.getReplyToMsgType() == 1) {
                    holder.tvReplyToMsg.setVisibility(View.VISIBLE);
                    holder.ivReplyToImg.setVisibility(View.GONE);
                } else if (chat.getReplyToMsgType() == 2) {

                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);

                    File imgFile = new File(folder.getAbsolutePath() + File.separator + chat.getTextValue());
                    if (!imgFile.exists()) {
                        // AltexImageDownloader.writeToDisk(context, Constants.CHAT_IMAGE_URL + chat.getTextValue(), "");

                        new ImageDownloadAsync(activity, chat.getTextValue()).execute(Constants.CHAT_IMAGE_URL + chat.getTextValue());

                    }
                    holder.tvReplyToMsg.setVisibility(View.GONE);
                    holder.ivReplyToImg.setVisibility(View.VISIBLE);

                    try {

                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME + File.separator + chat.getReplyToMsg());

                        Picasso.with(context)
                                .load(file)
                                .placeholder(context.getResources().getDrawable(R.drawable.progress_animation))
                                .error(android.R.color.transparent)
                                //.resize(100, 100)
                                .into(holder.ivReplyToImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {
                holder.llReply.setVisibility(View.GONE);
            }

            //-----------------Reply-------------------

            holder.ivTick.setVisibility(View.VISIBLE);
            try {
                if (chat.getMarkAsRead() == 3) {
                    holder.ivTick.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_color_tick));
                } else if (chat.getMarkAsRead() == 2) {
                    holder.ivTick.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_two_tick));
                } else {
                    holder.ivTick.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_one_tick));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {

            holder.llName.setVisibility(View.VISIBLE);

            holder.llParent.setGravity(Gravity.LEFT);
            holder.llParent.setPadding(0, 0, 45, 0);

            if (chat.getTypeOfText() == 101 || chat.getTypeOfText() == 201) {
                holder.cardView.setCardBackgroundColor(Color.parseColor("#fedede"));
            } else {
                holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            }

            holder.tvMsg.setText(chat.getTextValue());
            holder.tvDate.setText(dateDisplay);
            holder.tvName.setText(chat.getUserName());

            if (chat.getTypeOfText() == 1) {
                holder.tvMsg.setVisibility(View.VISIBLE);
                holder.ivImg.setVisibility(View.GONE);
            } else if (chat.getTypeOfText() == 2) {

                File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);

                File imgFile = new File(folder.getAbsolutePath() + File.separator + chat.getTextValue());
                if (!imgFile.exists()) {
                    //  AltexImageDownloader.writeToDisk(context, Constants.CHAT_IMAGE_URL + chat.getTextValue(), "");

                    String url = Constants.CHAT_IMAGE_URL + chat.getTextValue();
                    Log.e("ASYNC", " **** ----------------------- ***** ----------- " + url);

                    new ImageDownloadAsync(activity, chat.getTextValue()).execute(Constants.CHAT_IMAGE_URL + chat.getTextValue());

                }

                holder.tvMsg.setVisibility(View.GONE);
                holder.ivImg.setVisibility(View.VISIBLE);


                try {
                    final String image = Constants.CHAT_IMAGE_URL + chat.getTextValue();

                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME + File.separator + chat.getTextValue());

                    Picasso.with(context)
                            .load(file)
                            .placeholder(context.getResources().getDrawable(R.drawable.progress_animation))
                            .error(context.getResources().getDrawable(R.drawable.progress_animation))
                            //.resize(100, 100)
                            .into(holder.ivImg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            holder.ivTick.setVisibility(View.GONE);


            //-----------------Reply-------------------

            if (chat.getReplyToId() > 0) {
                holder.llReply.setVisibility(View.VISIBLE);

                holder.tvReplyToName.setText("" + chat.getReplyToName());
                holder.tvReplyToMsg.setText("" + chat.getReplyToMsg());

                if (chat.getReplyToMsgType() == 1) {
                    holder.tvReplyToMsg.setVisibility(View.VISIBLE);
                    holder.ivReplyToImg.setVisibility(View.GONE);
                } else if (chat.getReplyToMsgType() == 2) {

                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Constants.FOLDER_NAME);

                    File imgFile = new File(folder.getAbsolutePath() + File.separator + chat.getTextValue());
                    if (!imgFile.exists()) {
                        // AltexImageDownloader.writeToDisk(context, Constants.CHAT_IMAGE_URL + chat.getTextValue(), "");

                        new ImageDownloadAsync(activity, chat.getTextValue()).execute(Constants.CHAT_IMAGE_URL + chat.getTextValue());

                    }
                    holder.tvReplyToMsg.setVisibility(View.GONE);
                    holder.ivReplyToImg.setVisibility(View.VISIBLE);

                    try {

                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FOLDER_NAME + File.separator + chat.getReplyToMsg());

                        Picasso.with(context)
                                .load(file)
                                .placeholder(context.getResources().getDrawable(R.drawable.progress_animation))
                                .error(android.R.color.transparent)
                                //.resize(100, 100)
                                .into(holder.ivReplyToImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {
                holder.llReply.setVisibility(View.GONE);
            }

            //-----------------Reply-------------------

        }

        holder.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", chat.getTextValue());
                context.startActivity(intent);
            }
        });

        holder.ivReplyToImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", chat.getReplyToMsg());
                context.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                if (chat.getUserId() == userId) {

                    showDialog(holder.cardView, chat);

                }else{
                    Intent pushNotificationIntent = new Intent();
                    pushNotificationIntent.setAction("CHAT_REPLY");
                    pushNotificationIntent.putExtra("msg", "");
                    pushNotificationIntent.putExtra("replyToId", chat.getChatTaskDetailId());
                    pushNotificationIntent.putExtra("replyToMsgType", chat.getTypeOfText());
                    pushNotificationIntent.putExtra("replyToMsg", chat.getTextValue());
                    pushNotificationIntent.putExtra("replyToName", chat.getUserName());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);
                }


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatDetails.size();
    }


    public void showDialog(View view, final ChatDisplay chat) {
      /*  final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_chat_reply);

        dialog.show();*/

        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater()
                .inflate(R.menu.menu_chat_reply, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (R.id.action_reply == menuItem.getItemId()) {
                    Intent pushNotificationIntent = new Intent();
                    pushNotificationIntent.setAction("CHAT_REPLY");
                    pushNotificationIntent.putExtra("msg", "");
                    pushNotificationIntent.putExtra("replyToId", chat.getChatTaskDetailId());
                    pushNotificationIntent.putExtra("replyToMsgType", chat.getTypeOfText());
                    pushNotificationIntent.putExtra("replyToMsg", chat.getTextValue());
                    pushNotificationIntent.putExtra("replyToName", chat.getUserName());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);
                } else if (R.id.action_read == menuItem.getItemId()) {

                    getEmpChatRead(chat.getChatTaskDetailId());
                }

                return false;
            }
        });

        popup.show();


    }


    //-------------------------------------------------------------------------

    private void getEmpChatRead(int detailId) {
        if (Constants.isOnline(context)) {

            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<EmpChatReadModel>> listCall = Constants.myInterface.getEmpChatRead(detailId);
            listCall.enqueue(new Callback<ArrayList<EmpChatReadModel>>() {
                @Override
                public void onResponse(Call<ArrayList<EmpChatReadModel>> call, Response<ArrayList<EmpChatReadModel>> response) {

                    try {
                        if (response.body() != null) {

                            ArrayList<EmpChatReadModel> empList = response.body();

                            //final String[] items = {"Apple", "Banana", "Orange", "Grapes","Apple", "Banana", "Orange", "Grapes","Apple", "Banana", "Orange", "Grapes","Apple", "Banana", "Orange", "Grapes","Apple", "Banana", "Orange", "Grapes","Apple", "Banana", "Orange", "Grapes","Apple", "Banana", "Orange", "Grapes"};

                            ArrayList<String> empArr=new ArrayList<>();
                            if (empList.size()>0){
                                for (int i=0;i<empList.size();i++){
                                    empArr.add(""+empList.get(i).getEmpName());
                                }
                            }
                            String[] array = empArr.toArray(new String[empArr.size()]);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
                            builder.setTitle("Chat read by -")
                                    .setItems(array, null);

                            builder.setNegativeButton("CANCEL", null);

                            AlertDialog alertDialog = builder.create();

                            alertDialog.show();

                            commonDialog.dismiss();



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "oops someting went wrong!", Toast.LENGTH_SHORT).show();
                        commonDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<EmpChatReadModel>> call, Throwable t) {
                    t.printStackTrace();
                    commonDialog.dismiss();
                    Toast.makeText(context, "oops someting went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
            builder.setTitle("Alert");
            builder.setMessage("No internet connection available!");
            builder.setNegativeButton("CANCEL", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


}
