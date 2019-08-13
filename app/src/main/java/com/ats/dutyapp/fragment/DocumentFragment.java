package com.ats.dutyapp.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ats.dutyapp.BuildConfig;
import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.DocumentAdapter;
import com.ats.dutyapp.constant.Constants;
import com.ats.dutyapp.model.Document;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CommonDialog;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.ats.dutyapp.utils.ItextPageEvent;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment {
    private RecyclerView recyclerView;
    DocumentAdapter adapter;
    ArrayList<Document> docList = new ArrayList<>();
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

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUserMain = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUserMain);
        } catch (Exception e) {
            e.printStackTrace();
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
                float[] colWidth = new float[]{5,35,50,30,50};
                ptName.setWidths(colWidth);
                ptName.setTotalWidth(colWidth);
                ptName.setWidthPercentage(100);

                for (int i = 0; i < docList.size(); i++) {

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

                    for (int j = 0; j < docList.get(i).getDutyList().size(); j++) {

                        Log.e("CYCLE of J", "-------------------------------" + j);

                        cell = new PdfPCell(new Paragraph("" + (j + 1) + ")  ", textFont));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptName.addCell(cell);

                        cell = new PdfPCell(new Paragraph( "Duty Name: ", textFontLarge));
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
                            if(language==1) {

                                cell = new PdfPCell(new Paragraph("" + (k + 1) + ")  ", textFont));
                                cell.setBorder(Rectangle.NO_BORDER);
                                cell.setHorizontalAlignment(0);
                                ptName.addCell(cell);

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

                            }else {
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

                //--------------------------------------------------------------------------------------

               // doc.add(pt);
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

}
