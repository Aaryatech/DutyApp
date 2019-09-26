package com.ats.dutyapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.AssignCheckListAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.AssignChecklist;
import com.ats.dutyapp.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignCheckListFragment extends Fragment {
    public RecyclerView recyclerView;
    ArrayList<AssignChecklist> assignList =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_check_list2, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);

       // prepareData();
        
        getAssigneChecklist();


        return view;
    }

    private void getAssigneChecklist() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<AssignChecklist>> listCall = Constants.myInterface.getAllAssignedChecklistDisplay();
            listCall.enqueue(new Callback<ArrayList<AssignChecklist>>() {
                @Override
                public void onResponse(Call<ArrayList<AssignChecklist>> call, Response<ArrayList<AssignChecklist>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGNE CHECK LIST : ", "------------------------------------------------- " + response.body());
                            assignList.clear();
                            assignList = response.body();

                            AssignCheckListAdapter adapter = new AssignCheckListAdapter(assignList,getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
                public void onFailure(Call<ArrayList<AssignChecklist>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

//    private void prepareData() {
//        AssignChecklist myListData = new AssignChecklist("Production & Quality","Another interesting feature .","Avinash Kachru,Archana Tarate,Kavita Sonawne");
//        assignList.add(myListData);
//
//        myListData = new AssignChecklist("Maintenance","Checklist Name","Avinash Kachru,Archana Tarate,Kavita Sonawne");
//        assignList.add(myListData);
//
//    }

}
