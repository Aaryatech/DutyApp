package com.ats.dutyapp.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by V2STech on 26-07-2017.
 */

public class Constant
{




    //---- your language
    public static void yourLanguage(Context context, String language)
    {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        //getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
    }




}