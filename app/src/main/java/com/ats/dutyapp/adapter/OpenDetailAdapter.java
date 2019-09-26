package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.fragment.OpenDetailFragment;
import com.ats.dutyapp.model.ChecklistActionDetail;

import java.util.ArrayList;

public class OpenDetailAdapter extends RecyclerView.Adapter<OpenDetailAdapter.MyViewHolder> {

    private ArrayList<ChecklistActionDetail> detailList;
    private static Context context;

    public OpenDetailAdapter(ArrayList<ChecklistActionDetail> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;

    }

    @NonNull
    @Override
    public OpenDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_open_detail_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenDetailAdapter.MyViewHolder myViewHolder, final int i) {
        ChecklistActionDetail model=detailList.get(i);
        final int pos = i;
        myViewHolder.tvChecklist.setText(""+model.getChecklist_desc());

        myViewHolder.checkBox.setChecked(detailList.get(i).isChecked());

        myViewHolder.checkBox.setTag(detailList.get(i));

        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ChecklistActionDetail detail = (ChecklistActionDetail) cb.getTag();

                detail.setChecked(cb.isChecked());
                detailList.get(pos).setChecked(cb.isChecked());

               new OpenDetailFragment().onClickActivity(i,context);

//                Intent pushNotificationIntent = new Intent();
//                pushNotificationIntent.putExtra("pos",pos);
//                pushNotificationIntent.setAction("CHECKLIST_DETAIL");
//                LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotificationIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvChecklist;
        public CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChecklist=(itemView).findViewById(R.id.tvChecklist);
            checkBox=(itemView).findViewById(R.id.checkbox);
        }
    }
}
