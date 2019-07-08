package com.ats.dutyapp.utils;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class CommonDialog {

    Context context;
    String Title;
    String Msg;

    KProgressHUD hud;

    public CommonDialog(final Context context, String title, String msg) {
        this.context = context;
        Title = title;
        Msg = msg;

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setDimAmount(0.5f);

    }


    public void show() {
        try {
            hud.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void dismiss() {
        try {
            if (hud.isShowing()) {
                hud.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
