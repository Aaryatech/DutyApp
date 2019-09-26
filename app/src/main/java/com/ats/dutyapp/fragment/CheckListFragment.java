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
import com.ats.dutyapp.adapter.CheckListHeaderAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.ChecklistHeader;
import com.ats.dutyapp.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListFragment extends Fragment {
    public RecyclerView recyclerView;
    ArrayList<ChecklistHeader> checkListHeader = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_check_list2, container, false);
        getActivity().setTitle("CheckList List");
        recyclerView=view.findViewById(R.id.recyclerView);

        getCheckListHeader();

        return view;
    }

    private void getCheckListHeader() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<ChecklistHeader>> listCall = Constants.myInterface.getAllChecklistHeaderAndDetail();
            listCall.enqueue(new Callback<ArrayList<ChecklistHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<ChecklistHeader>> call, Response<ArrayList<ChecklistHeader>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CHEACK LIST HEADER : ", " -------------------------- " + response.body());
                            checkListHeader = response.body();

                            CheckListHeaderAdapter adapter = new CheckListHeaderAdapter(checkListHeader, getContext());
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
                public void onFailure(Call<ArrayList<ChecklistHeader>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
