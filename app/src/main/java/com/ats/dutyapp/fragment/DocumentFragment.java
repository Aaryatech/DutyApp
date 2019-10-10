package com.ats.dutyapp.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.dutyapp.BuildConfig;
import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.DocumentAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.Document;
import com.ats.dutyapp.model.Emp;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.ats.dutyapp.utils.ItextPageEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    DocumentAdapter adapter;
    ArrayList<Document> docList = new ArrayList<>();
    ArrayList<Sync> syncArray = new ArrayList<>();
    public static Login loginUserMain;
    public int language;

    public String FONT = "/assets/lohit_devanagari.ttf";

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        getActivity().setTitle("Document List");
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUserMain = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUserMain);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Gson gson = new Gson();
            String json = prefs.getString("Sync", null);
            Type type = new TypeToken<ArrayList<Sync>>() {}.getType();
            syncArray= gson.fromJson(json, type);

            Log.e("SYNC MAIN : ", "--------USER-------" + syncArray);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        for(int i=0;i<syncArray.size();i++)
        {
            Log.e("MY TAG","-----syncArray-------");
            if(syncArray.get(i).getSettingKey().equals("Admin"))
            {
                Log.e("MY TAG1","-----Admin-------");
                if(syncArray.get(i).getSettingValue().equals(String.valueOf(loginUserMain.getEmpCatId())))
                {
                    fab.setVisibility(View.VISIBLE);
                    Log.e("MY TAG","-----Admin-------");
                }else{
                    fab.setVisibility(View.GONE);
                    Log.e("MY TAG","-----Other-------");
                }
            }

    }

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DocumentSuperwiser/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        language = Integer.parseInt(CustomSharedPreference.getString(getActivity(),CustomSharedPreference.LANGUAGE_SELECTED));
        Log.e("LANGUAGE REPORT","----------------------------------------"+language);

        getDocument(loginUserMain.getEmpId(), language);

        return view;
    }

    private void getDocument(Integer empId, Integer langId) {
        Log.e("PARAMETER","                  EMP ID          "+empId+"                         LANG ID           "+langId);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Document>> listCall = Constants.myInterface.getDutyReportByEmp(empId, langId);
            listCall.enqueue(new Callback<ArrayList<Document>>() {
                @Override
                public void onResponse(Call<ArrayList<Document>> call, Response<ArrayList<Document>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DOCUMENT LIST : ", " - " + response.body());
                            docList.clear();
                            docList = response.body();

                            adapter = new DocumentAdapter(docList, getContext());
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
                public void onFailure(Call<ArrayList<Document>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_pdf);
        item.setVisible(true);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pdf:

               genratePdf(docList);

               return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void genratePdf(ArrayList<Document> docList) {
        Log.e("PDF LIST", "----------------------------------------" + docList);

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        doc.setMargins(10, 10, 80, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Font textFontLarge = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

        Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H,true);

       // List list = new List(List.UNORDERED);
        List list = new List(false, 20);

        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Duty_Detail_Report" + ".pdf";
            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            try {
                ItextPageEvent itextPageEvent=new ItextPageEvent("Galdhar Food","Duty Document for " + loginUserMain.getEmpFname() + " " + loginUserMain.getEmpMname() + " " + loginUserMain.getEmpSname()+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n");
                writer.setPageEvent(itextPageEvent);

            }catch (Exception e)
            {
                e.printStackTrace();
                Log.e("Exception Itext","------------------------------"+ e);
            }

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Galdhar Food", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Duty Document for " + loginUserMain.getEmpFname() + " " + loginUserMain.getEmpMname() + " " + loginUserMain.getEmpSname(), boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("", boldTextFont));
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);


                //-------------------------------------------------------------------------------------------------------------
                PdfPTable ptName = new PdfPTable(5);
                float[] colWidth = new float[]{5, 35, 50, 30, 50};
                ptName.setWidths(colWidth);
                ptName.setTotalWidth(colWidth);
                ptName.setWidthPercentage(100);


                for (int i = 0; i < docList.size(); i++) {

                    if(docList.get(i).getDutyList() != null)
                    {
                    cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyType()), boldFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(1);
                    cell.setColspan(5);
                    ptName.addCell(cell);

                    cell = new PdfPCell(new Paragraph(" ", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(1);
                    ptName.addCell(cell);

                    if (docList.get(i).getDutyList() != null) {
                        for (int j = 0; j < docList.get(i).getDutyList().size(); j++) {

                            Log.e("CYCLE of J", "-------------------------------" + j);

                            cell = new PdfPCell(new Paragraph("" + (j + 1) + ")  ", textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph("Duty Name: ", textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getDutyName()), textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            cell.setColspan(3);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(" ", textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(1);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph("Shift: ", textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getShiftName()), textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            cell.setColspan(3);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(" ", textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(1);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph("From Time: ", textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getShiftFromTime()), textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

//                        cell = new PdfPCell(new Paragraph(" ", textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(1);
//                        ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph("To Time: ", textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getShiftToTime()), textFontLarge));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph(" ", textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            cell.setColspan(5);
                            ptName.addCell(cell);

//                        cell = new PdfPCell(new Paragraph(" ", textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptName.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph(" ", textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(1);
//                        ptName.addCell(cell);


                            cell = new PdfPCell(new Paragraph(" ", textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(1);
                            ptName.addCell(cell);

                            cell = new PdfPCell(new Paragraph("Activities : ", boldFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            cell.setColspan(4);
                            ptName.addCell(cell);

//                        cell = new PdfPCell(new Paragraph(" ", textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(1);
//                        ptName.addCell(cell);


                            for (int k = 0; k < docList.get(i).getDutyList().get(j).getTaskList().size(); k++) {


//                            cell = new PdfPCell(new Paragraph(" ", textFont));
////                            cell.setBorder(Rectangle.NO_BORDER);
////                            cell.setHorizontalAlignment(0);
////                            ptName.addCell(cell);
                                if (language == 1) {


                                    Chunk bullet = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD));
                                    list.setListSymbol(bullet);

                                    // list.add(docList.get(i).getDutyList().get(j).getTaskList().get(k).getTaskNameEng());

                                    // (k + 1) + ") "

                                    cell = new PdfPCell(new Paragraph(String.valueOf(bullet) + " ", boldFont));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

//                                cell = new PdfPCell(new Paragraph("" + doc.add(list), textFont));
//                                cell.setBorder(Rectangle.NO_BORDER);
//                                cell.setHorizontalAlignment(0);
//                                ptName.addCell(cell);


                                    cell = new PdfPCell(new Paragraph("Task Name : ", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getTaskList().get(k).getTaskNameEng()), textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    cell.setColspan(3);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph(" ", textFont));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph("Task Description : ", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getTaskList().get(k).getTaskDescEng()), textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setColspan(3);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                } else {
                                    cell = new PdfPCell(new Paragraph("" + (k + 1) + ")  ", textFont));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph("Task Name : ", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getTaskList().get(k).getTaskNameEng()), font));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    cell.setColspan(3);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph(" ", textFont));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph("Task Description : ", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);

                                    cell = new PdfPCell(new Paragraph(String.valueOf(docList.get(i).getDutyList().get(j).getTaskList().get(k).getTaskDescEng()), font));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setColspan(3);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);
                                }

                                cell = new PdfPCell(new Paragraph(" ", textFont));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(0);
                                ptName.addCell(cell);

                                cell = new PdfPCell(new Paragraph("Photo Req : ", textFontLarge));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(0);
                                ptName.addCell(cell);

                                if (docList.get(i).getDutyList().get(j).getTaskList().get(k).getPhotoReq() == 0) {
                                    cell = new PdfPCell(new Paragraph("NO", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);
                                } else if (docList.get(i).getDutyList().get(j).getTaskList().get(k).getPhotoReq() == 1) {
                                    cell = new PdfPCell(new Paragraph("YES", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);
                                }

//                            cell = new PdfPCell(new Paragraph(" ", textFont));
//                            cell.setBorder(Rectangle.NO_BORDER);
//                            cell.setHorizontalAlignment(0);
//                            ptName.addCell(cell);

                                cell = new PdfPCell(new Paragraph("Remark Req : ", textFontLarge));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(0);
                                ptName.addCell(cell);

                                if (docList.get(i).getDutyList().get(j).getTaskList().get(k).getRemarkReq() == 0) {
                                    cell = new PdfPCell(new Paragraph("NO", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);
                                } else if (docList.get(i).getDutyList().get(j).getTaskList().get(k).getRemarkReq() == 1) {
                                    cell = new PdfPCell(new Paragraph("YES", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);
                                }

                                cell = new PdfPCell(new Paragraph(" ", textFont));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(0);
                                ptName.addCell(cell);

                                cell = new PdfPCell(new Paragraph("Time Req : ", textFontLarge));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(0);
                                ptName.addCell(cell);

                                if (docList.get(i).getDutyList().get(j).getTaskList().get(k).getTimeReq() == 0) {
                                    cell = new PdfPCell(new Paragraph("NO", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setColspan(3);
                                    cell.setHorizontalAlignment(0);
                                    ptName.addCell(cell);
                                } else if (docList.get(i).getDutyList().get(j).getTaskList().get(k).getTimeReq() == 1) {
                                    cell = new PdfPCell(new Paragraph("YES" + " (" + docList.get(i).getDutyList().get(j).getTaskList().get(k).getTaskTime() + ")", textFontLarge));
                                    cell.setBorder(Rectangle.NO_BORDER);
                                    cell.setHorizontalAlignment(0);
                                    cell.setColspan(3);
                                    ptName.addCell(cell);
                                }

                                cell = new PdfPCell(new Paragraph(" ", textFont));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(1);
                                cell.setColspan(5);
                                ptName.addCell(cell);

//                            cell = new PdfPCell(new Paragraph(" ", textFont));
//                            cell.setBorder(Rectangle.NO_BORDER);
//                            cell.setHorizontalAlignment(0);
//                            ptName.addCell(cell);
//
//                            cell = new PdfPCell(new Paragraph(" ", textFont));
//                            cell.setBorder(Rectangle.NO_BORDER);
//                            cell.setHorizontalAlignment(0);
//                            ptName.addCell(cell);


                            }


                            cell = new PdfPCell(new Paragraph(" ", textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(1);
                            cell.setColspan(5);
                            ptName.addCell(cell);

//                        cell = new PdfPCell(new Paragraph(" ", textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptName.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph(" ", textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptName.addCell(cell);


                        }
                    }

                    cell = new PdfPCell(new Paragraph(" ", textFont));
                    cell.setBorder(Rectangle.BOTTOM);
                    cell.setHorizontalAlignment(1);
                    cell.setColspan(5);
                    ptName.addCell(cell);

//                    cell = new PdfPCell(new Paragraph("", boldTextFont));
//                    cell.setBorder(Rectangle.BOTTOM);
//                    cell.setHorizontalAlignment(1);
//                    ptName.addCell(cell);
//
//                    cell = new PdfPCell(new Paragraph("", boldTextFont));
//                    cell.setBorder(Rectangle.BOTTOM);
//                    cell.setHorizontalAlignment(1);
//                    ptName.addCell(cell);

                    cell = new PdfPCell(new Paragraph(" ", textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(0);
                    cell.setColspan(5);
                    ptName.addCell(cell);

//                    cell = new PdfPCell(new Paragraph(" ", textFont));
//                    cell.setBorder(Rectangle.NO_BORDER);
//                    cell.setHorizontalAlignment(0);
//                    ptName.addCell(cell);
//
//                    cell = new PdfPCell(new Paragraph(" ", textFont));
//                    cell.setBorder(Rectangle.NO_BORDER);
//                    cell.setHorizontalAlignment(0);
//                    ptName.addCell(cell);

                }
            }

                //--------------------------------------------------------------------------------------

               // doc.add(pt);
              //  doc.add(list);
                doc.add(ptName);

              //  doc.add(pTable);

            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate", Toast.LENGTH_SHORT).show();
                    Log.e("Mytag1", "--------------------");
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Log.e("Mytag", "--------------------" + e);
            Toast.makeText(getActivity(), "Unable To Generate", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        new FilterDialog(getActivity()).show();
    }

    private class FilterDialog extends Dialog {
        public Button btnFilter;
        public ImageView ivClose;
        // public Spinner spDept,spEmp;
        public TextView spDept,spEmp,tvDeptId,tvEmpId;
        Login loginUser;
        int deptId,language;

        Dialog dialog;
        private BroadcastReceiver mBroadcastReceiver;
        DepartmentListDialogAdapter deptAdapter;
        EmployeeListDialogAdapter empAdapter;

        ArrayList<String> deptNameList = new ArrayList<>();
        ArrayList<Integer> deptIdList = new ArrayList<>();
        ArrayList<Department> deptList = new ArrayList<>();

        ArrayList<String> empNameList = new ArrayList<>();
        ArrayList<Integer> empIdList = new ArrayList<>();
        ArrayList<Emp> empList = new ArrayList<>();


        ArrayList<Integer> assignedMaterialIdArray = new ArrayList<>();

        public FilterDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_filter);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
//            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
//            wlp.x = 10;
//            wlp.y = 10;
//            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(wlp);

            wlp.gravity = Gravity.CENTER_VERTICAL;
            wlp.x = 5;
            wlp.y = 5;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);


            ivClose = (ImageView) findViewById(R.id.ivClose);
            btnFilter = (Button) findViewById(R.id.btnFilter);
            spDept=(TextView) findViewById(R.id.spDept);
            spEmp=(TextView)findViewById(R.id.spEmp);
            tvDeptId=(TextView)findViewById(R.id.tvDeptId);
            tvEmpId=(TextView)findViewById(R.id.tvEmpId);


            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("CUSTOMER_DATA")) {
                        handlePushNotification(intent);
                    }
                }
            };


            //getAllEmp();
            getAllDept();

            spEmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog1();
                }
            });

            spDept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String empName=spEmp.getText().toString();
                    String deptName=spDept.getText().toString();

                    boolean isValidEmp=false,isValidDept=false;
                    int empId = 0,deptId = 0;
                    try {
                        empId = Integer.parseInt(tvEmpId.getText().toString());
                        deptId = Integer.parseInt(tvDeptId.getText().toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    if (empName.isEmpty()) {
                        spEmp.setError("required");
                    } else {
                        spEmp.setError(null);
                        isValidEmp = true;
                    }

                    if (deptName.isEmpty()) {
                        spDept.setError("required");
                    } else {
                        spDept.setError(null);
                        isValidDept = true;
                    }

                    if(isValidDept && isValidEmp){

                        Toast.makeText(getActivity(), "Successfully", Toast.LENGTH_SHORT).show();
                        language = Integer.parseInt(CustomSharedPreference.getString(getActivity(),CustomSharedPreference.LANGUAGE_SELECTED));
                        Log.e("LANGUAGE REPORT","----------------------------------------"+language);

                        getDocument(empId, language);
                        dismiss();
                        //saveApprove()
                    }

                }

            });

        }


        private void handlePushNotification(Intent intent) {
            Log.e("handlePushNotification", "------------------------------------**********");
            dialog.dismiss();
            String name = intent.getStringExtra("name");
            int custId = intent.getIntExtra("id", 0);
            Log.e("CUSTOMER NAME : ", " " + name);
            spDept.setText("" + name);
            tvEmpId.setText("" + custId);

        }

        private void showDialog() {
            dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
            dialog.setContentView(v);
            dialog.setCancelable(true);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
            EditText edSearch = dialog.findViewById(R.id.edSearch);

            deptAdapter = new DepartmentListDialogAdapter(deptList, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvCustomerList.setLayoutManager(mLayoutManager);
            rvCustomerList.setItemAnimator(new DefaultItemAnimator());
            rvCustomerList.setAdapter(deptAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if (deptAdapter != null) {
                            filterDept(editable.toString());
                        }
                    } catch (Exception e) {
                    }
                }
            });

            dialog.show();
        }

        void filterDept(String text) {
            ArrayList<Department> temp = new ArrayList();
            for (Department d : deptList) {
                if (d.getEmpDeptName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            deptAdapter.updateList(temp);
        }


        private void showDialog1() {
            dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
            dialog.setContentView(v);
            dialog.setCancelable(true);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
            EditText edSearch = dialog.findViewById(R.id.edSearch);

            empAdapter = new EmployeeListDialogAdapter(empList, getContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvCustomerList.setLayoutManager(mLayoutManager);
            rvCustomerList.setItemAnimator(new DefaultItemAnimator());
            rvCustomerList.setAdapter(empAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        if (empAdapter != null) {
                            filterEmp(editable.toString());
                        }
                    } catch (Exception e) {
                    }
                }
            });

            dialog.show();
        }

        void filterEmp(String text) {
            ArrayList<Emp> temp = new ArrayList();
            for (Emp d : empList) {
                if (d.getEmpFname().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            empAdapter.updateList(temp);
        }


        public class DepartmentListDialogAdapter extends RecyclerView.Adapter<DepartmentListDialogAdapter.MyViewHolder> {

            private ArrayList<Department> custList;
            private Context context;

            public DepartmentListDialogAdapter(ArrayList<Department> custList, Context context) {
                this.custList = custList;
                this.context = context;
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {
                public TextView tvName, tvAddress;
                public LinearLayout linearLayout;

                public MyViewHolder(View view) {
                    super(view);
                    tvName = view.findViewById(R.id.tvName);
                    tvAddress = view.findViewById(R.id.tvAddress);
                    linearLayout = view.findViewById(R.id.linearLayout);
                }
            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.adapter_department_dialog, viewGroup, false);

                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
                final Department model = custList.get(i);

                myViewHolder.tvName.setText(model.getEmpDeptName());
                //holder.tvAddress.setText(model.getCustAddress());

                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent customerDataIntent = new Intent();
//                        customerDataIntent.setAction("CUSTOMER_DATA");
//                        customerDataIntent.putExtra("name", model.getEmpDeptName());
//                        customerDataIntent.putExtra("id", model.getEmpDeptId());
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);
                        dialog.dismiss();
                        spDept.setText(""+model.getEmpDeptName());
                        tvDeptId.setText(""+model.getEmpDeptId());
                        deptId= Integer.parseInt(tvDeptId.getText().toString());
                        getAllEmp(deptId);
                    }
                });
            }



            @Override
            public int getItemCount() {
                return custList.size();
            }

            public void updateList(ArrayList<Department> list) {
                custList = list;
                notifyDataSetChanged();
            }

        }

        public class EmployeeListDialogAdapter extends RecyclerView.Adapter<EmployeeListDialogAdapter.MyViewHolder> {

            private ArrayList<Emp> empList;
            private Context context;

            public EmployeeListDialogAdapter(ArrayList<Emp> empList, Context context) {
                this.empList = empList;
                this.context = context;
            }

            public class MyViewHolder extends RecyclerView.ViewHolder {
                public TextView tvName, tvAddress;
                public LinearLayout linearLayout;

                public MyViewHolder(View view) {
                    super(view);
                    tvName = view.findViewById(R.id.tvName);
                    tvAddress = view.findViewById(R.id.tvAddress);
                    linearLayout = view.findViewById(R.id.linearLayout);
                }
            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.adapter_department_dialog, viewGroup, false);

                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
                final Emp model = empList.get(i);

                myViewHolder.tvName.setText(model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
                //holder.tvAddress.setText(model.getCustAddress());

                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent customerDataIntent = new Intent();
//                        customerDataIntent.setAction("CUSTOMER_DATA");
//                        customerDataIntent.putExtra("name", model.getEmpDeptName());
//                        customerDataIntent.putExtra("id", model.getEmpDeptId());
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);
                        dialog.dismiss();
                        spEmp.setText(""+model.getEmpFname()+" "+model.getEmpMname()+" "+model.getEmpSname());
                        tvEmpId.setText(""+model.getEmpId());

                    }
                });
            }



            @Override
            public int getItemCount() {
                return empList.size();
            }

            public void updateList(ArrayList<Emp> list) {
                empList = list;
                notifyDataSetChanged();
            }

        }



        private void getAllEmp(final int deptId) {
            if (Constants.isOnline(getActivity())) {
                final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
                commonDialog.show();

                Call<ArrayList<Emp>> listCall = Constants.myInterface.allEmployees();
                listCall.enqueue(new Callback<ArrayList<Emp>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Emp>> call, Response<ArrayList<Emp>> response) {
                        try {
                            if (response.body() != null) {

                                Log.e("EMPLOYEE LIST : ", " -----------------------------------EMPLOYEE LIST---------------------------- " + response.body());
                                empNameList.clear();
                                empIdList.clear();
                                empList.clear();

                                empNameList.add("");
                                empIdList.add(0);

                                if (response.body().size() > 0) {
                                    for (int i = 0; i < response.body().size(); i++) {

                                        if (response.body().get(i).getEmpDeptId() == deptId) {

                                            // empIdList.add(response.body().get(i).getEmpDeptId());
                                            // empNameList.add(response.body().get(i).getEmpFname() + " " + response.body().get(i).getEmpMname() + " " + response.body().get(i).getEmpSname());

                                            Emp employee = new Emp(response.body().get(i).getEmpId(),response.body().get(i).getEmpDsc(),response.body().get(i).getEmpCode(),response.body().get(i).getCompanyId(),response.body().get(i).getEmpCatId(),response.body().get(i).getEmpTypeId(),response.body().get(i).getEmpDeptId(),response.body().get(i).getLocId(),response.body().get(i).getEmpFname(),response.body().get(i).getEmpMname(),response.body().get(i).getEmpSname(),response.body().get(i).getEmpPhoto(),response.body().get(i).getEmpMobile1(),response.body().get(i).getEmpMobile2(),response.body().get(i).getEmpEmail(),response.body().get(i).getEmpAddressTemp(),response.body().get(i).getEmpAddressPerm(),response.body().get(i).getEmpBloodgrp(),response.body().get(i).getEmpEmergencyPerson1(),response.body().get(i).getEmpEmergencyNo1(),response.body().get(i).getEmpEmergencyPerson2(),response.body().get(i).getEmpEmergencyNo2(),response.body().get(i).getEmpRatePerhr(),response.body().get(i).getEmpJoiningDate(),response.body().get(i).getEmpPrevExpYrs(),response.body().get(i).getEmpPrevExpMonths(),response.body().get(i).getEmpLeavingDate(),response.body().get(i).getEmpLeavingReason(),response.body().get(i).getLockPeriod(),response.body().get(i).getTermConditions(),response.body().get(i).getSalaryId(),response.body().get(i).getDelStatus(),response.body().get(i).getIsActive(),response.body().get(i).getMakerUserId(),response.body().get(i).getMakerEnterDatetime(),response.body().get(i).getExInt1(),response.body().get(i).getExInt2(),response.body().get(i).getExInt3(),response.body().get(i).getExVar1(),response.body().get(i).getExVar2(),response.body().get(i).getExVar3());
                                            empList.add(employee);
                                        }
                                    }
                                }
//
//                                    Log.e("EMP NAME","-------------------------------------------"+empNameList);
//                                    Log.e("EMP ID","-------------------------------------------"+empIdList);
//
//                                    ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, empNameList);
//                                    spEmp.setAdapter(projectAdapter);
//
//                                }

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
                    public void onFailure(Call<ArrayList<Emp>> call, Throwable t) {
                        commonDialog.dismiss();
                        Log.e("onFailure : ", "-----------" + t.getMessage());
                        t.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }

        private void getAllDept() {
            if (Constants.isOnline(getContext())) {
                final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
                commonDialog.show();

                Call<ArrayList<Department>> listCall = Constants.myInterface.allEmployeeDepartment();
                listCall.enqueue(new Callback<ArrayList<Department>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Department>> call, Response<ArrayList<Department>> response) {
                        try {
                            if (response.body() != null) {

                                Log.e("DEPT LIST : ", " - " + response.body());

                                deptNameList.clear();
                                deptIdList.clear();
                                deptList=response.body();

//                                deptNameList.add("Select Department");
//                                deptIdList.add(0);
//
//                                if (response.body().size() > 0) {
//                                    for (int i = 0; i < response.body().size(); i++) {
//                                        deptIdList.add(response.body().get(i).getEmpDeptId());
//                                        deptNameList.add(response.body().get(i).getEmpDeptName());
//                                    }
//
//                                    ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, deptNameList);
//                                    spDept.setAdapter(projectAdapter);
//
//                                }

//                                if (model != null) {
//                                    int position = 0;
//                                    if (purposeIdList.size() > 0) {
//                                        for (int i = 0; i < purposeIdList.size(); i++) {
//                                            if (model.getPurposeId() == purposeIdList.get(i)) {
//                                                position = i;
//                                                break;
//                                            }
//                                        }
//                                        spPurpose.setSelection(position);
//
//                                    }
//                                }
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
                    public void onFailure(Call<ArrayList<Department>> call, Throwable t) {
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
}
