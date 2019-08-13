package com.ats.dutyapp.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.DutyList;
import com.ats.dutyapp.model.TaskList;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.File;
import java.util.ArrayList;

public class DutyListReportAdapter extends RecyclerView.Adapter<DutyListReportAdapter.MyViewHolder>  {
    private ArrayList<DutyList> dutyList;
    private static Context context;

    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");


    public DutyListReportAdapter(ArrayList<DutyList> dutyList, Context context) {
        this.dutyList = dutyList;
        this.context = context;
        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "DocumentSuperwiser" + File.separator + "Report");
        if (!dir.exists()) {
            dir.mkdirs();
        }

    }

    @NonNull
    @Override
    public DutyListReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_duty_list_report, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DutyListReportAdapter.MyViewHolder myViewHolder, int i) {
        final DutyList model=dutyList.get(i);
        myViewHolder.tvTaskStartTime.setText("From Time : "+model.getShiftFromTime());
        myViewHolder.tvTaskEndTime.setText("To Time : "+model.getShiftToTime());
        myViewHolder.tvDutyWeight.setText("Wgt "+model.getTotalTaskWt());
        myViewHolder.tvTaskName.setText(""+model.getDutyName());
        myViewHolder.tvShift.setText("Shift : "+model.getShiftName());

        if (model.getTaskList() != null) {
            ArrayList<TaskList> taskList = new ArrayList<>();
            for (int j = 0; j < model.getTaskList().size(); j++) {
                taskList.add(model.getTaskList().get(j));
            }

            TaskListReportAdapter adapter = new TaskListReportAdapter(taskList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            myViewHolder.recyclerViewTask.setLayoutManager(mLayoutManager);
            myViewHolder.recyclerViewTask.setItemAnimator(new DefaultItemAnimator());
            myViewHolder.recyclerViewTask.setAdapter(adapter);
        }

        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItemsTask.setVisibility(View.VISIBLE);
            myViewHolder.imageViewTask.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
        } else {
            myViewHolder.llItemsTask.setVisibility(View.GONE);
            myViewHolder.imageViewTask.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
        }
//        tvItemsTask
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItemsTask.setVisibility(View.VISIBLE);
                    myViewHolder.imageViewTask.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItemsTask.setVisibility(View.GONE);
                    myViewHolder.imageViewTask.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
                }
            }
        });

        myViewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_pdf, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.action_pdf){
                         //   createPDF(model);

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dutyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTaskStartTime,tvTaskEndTime,tvDutyWeight,tvTaskName,tvItemsTask,tvShift;
        public RecyclerView recyclerViewTask;
        public CardView cardView;
        public LinearLayout llItemsTask;
        public ImageView ivMenu,imageViewTask;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTaskStartTime=itemView.findViewById(R.id.tvTaskStartTime);
            tvTaskEndTime=itemView.findViewById(R.id.tvTaskEndTime);
            tvDutyWeight=itemView.findViewById(R.id.tvDutyWeight);
            tvTaskName=itemView.findViewById(R.id.tvTaskName);
            tvShift=itemView.findViewById(R.id.tvShift);
            tvItemsTask=itemView.findViewById(R.id.tvItemsTask);
            llItemsTask=itemView.findViewById(R.id.llItemsTask);
            recyclerViewTask=itemView.findViewById(R.id.recyclerViewTask);
            ivMenu=itemView.findViewById(R.id.ivMenu);
            imageViewTask=itemView.findViewById(R.id.imageViewTask);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
