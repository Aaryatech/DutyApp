package com.ats.dutyapp.utils;

import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;

public class ItextPageEvent extends PdfPageEventHelper {
    private PdfTemplate t;
    private Image total;
    private String StrHeader, strTitle, strDate, footerText,headerText;

    public ItextPageEvent(String footerText, String headerText) {
        this.footerText = footerText;
        this.headerText = headerText;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("Hello"), 30, 800, 0);
        Log.e("ON START","-----------------------------ON START------------------------------");

        PdfContentByte pdfContByte=writer.getDirectContent();
        try {
            pdfContByte.setFontAndSize(BaseFont.createFont(), 16);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //ColumnText.showTextAligned(writer.getDirectContent().setFontAndSize(BaseColor.LIGHT_GRAY, 10), Element.ALIGN_RIGHT, new Phrase(""+footerText), 550, 800,

        //0);
        Phrase p=new Phrase();
        //p.setFont(Font.FontStyle.OBLIQUE);

        FontSelector selector = new FontSelector();
        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16,Font.BOLD);
        f1.setColor(BaseColor.BLACK);
        //Font f2 = FontFactory.getFont("MSung-Light",
        //   "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
        //f2.setColor(BaseColor.RED);
        selector.addFont(f1);
        //selector.addFont(f2);
        Phrase ph = selector.process(footerText);
        ColumnText.showTextAligned(pdfContByte, Element.ALIGN_CENTER, ph, 300, 800,

                0);

        Phrase ph1 = selector.process(headerText);
        ColumnText.showTextAligned(pdfContByte, Element.ALIGN_CENTER, ph1, 290, 780,

                0);



//        x=550;
//        y=800;

    }


}

