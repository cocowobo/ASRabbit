package com.adolsai.asrabbit.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * <p>DialogUtils类 1、提供弹出窗口类；</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-24 15:19)<br/>
 */
public class DialogUtils {

    public static void showDialog(Context context, String title, String msg,
                                  DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes", listener);
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
