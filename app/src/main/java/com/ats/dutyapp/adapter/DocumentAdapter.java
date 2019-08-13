package com.ats.dutyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.dutyapp.R;
import com.ats.dutyapp.model.Document;
import com.ats.dutyapp.model.DutyList;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder>  {
    private ArrayList<Document> docList;
    private static Context context;

    public DocumentAdapter(ArrayList<Document> docList, Context context) {
        this.docList = docList;
        this.context = context;

    }


    @NonNull
    @Override
    public DocumentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_document_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentAdapter.MyViewHolder myViewHolder, int i) {
        final Document model=docList.get(i);

        myViewHolder.tvDutyType.setText(""+model.getDutyType());

        if (model.getDutyList() != null) {
            ArrayList<DutyList> dutyList = new ArrayList<>();
            for (int j = 0; j < model.getDutyList().size(); j++) {
                dutyList.add(model.getDutyList().get(j));
            }

            DutyListReportAdapter adapter = new DutyListReportAdapter(dutyList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            myViewHolder.recyclerViewDuty.setLayoutManager(mLayoutManager);
            myViewHolder.recyclerViewDuty.setItemAnimator(new DefaultItemAnimator());
            myViewHolder.recyclerViewDuty.setAdapter(adapter);
        }

        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItemsDuty.setVisibility(View.VISIBLE);
            myViewHolder.imageViewDuty.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
        } else {
            myViewHolder.llItemsDuty.setVisibility(View.GONE);
            myViewHolder.imageViewDuty.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
        }

        myViewHolder.tvDutyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItemsDuty.setVisibility(View.VISIBLE);
                    myViewHolder.imageViewDuty.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItemsDuty.setVisibility(View.GONE);
                    myViewHolder.imageViewDuty.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDutyType;
        private ImageView imageViewDuty;
        private LinearLayout llItemsDuty;
        private RecyclerView recyclerViewDuty;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDutyType=itemView.findViewById(R.id.tvDutyType);
            imageViewDuty=itemView.findViewById(R.id.imageViewDuty);
            llItemsDuty=itemView.findViewById(R.id.llItemsDuty);
            recyclerViewDuty=itemView.findViewById(R.id.recyclerViewDuty);

        }
    }
}
