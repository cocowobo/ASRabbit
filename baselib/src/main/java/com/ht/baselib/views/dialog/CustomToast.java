package com.ht.baselib.views.dialog;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ht.baselib.R;

/**
 * Msg: 自定义Toast帮助类
 * Update:  2015/9/28
 * Version: 1.0
 * Created by zmc on 2015/9/28 10:09.
 */
public class CustomToast {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();
    private static int sysVersion = VERSION.SDK_INT;

    /**
     *
     * 显示提示消息
     *
     * @param mContext
     *            显示提示的activity
     * @param gravity
     *            显示位置
     * @param icon
     *            小图标资源id-提示消息左侧小图标（18*18）,若为-1则不显示
     * @param msg
     *            提示消息
     * @param yOffset
     *             垂直偏移距离（距离底部）
     * @param duration
     *            持续时间
     */
    public static void showToast(final Context mContext, final int gravity
            , final int icon, final String msg, final int yOffset, final int duration) {
        final View toastRoot = LayoutInflater.from(mContext).inflate(R.layout.custom_global_toast, null);
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(msg);
        if (icon != -1){
            ImageView toastIcon = (ImageView)toastRoot.findViewById(R.id.toast_icon);
            toastIcon.setBackgroundResource(icon);
            toastIcon.setVisibility(View.VISIBLE);
        }
        handler.post(new Runnable(){
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        // 4.0不用cancel
                        if (sysVersion <= 14) {
                            toast.cancel();
                        }
                        toast.setGravity(gravity, 0, yOffset);
                        toast.setDuration(duration);
                        toast.setView(toastRoot);
                    } else {
                        toast = new Toast(mContext);
                        toast.setGravity(gravity, 0, yOffset);
                        toast.setDuration(duration);
                        toast.setView(toastRoot);
                    }
                    toast.show();
                }
            }
        });
    }

    /**
     *
     * 显示提示消息(不带icon)
     *
     * @param mContext
     *            显示提示的activity
     * @param gravity
     *            显示位置
     * @param msg
     *            提示消息
     * @param yOffset
     *             垂直偏移距离（距离底部）
     * @param duration
     *            持续时间
     */
    public static void showToast(final Context mContext, final int gravity
            , final String msg, final int yOffset, final int duration) {
        showToast(mContext, gravity, -1, msg, yOffset, duration);
    }

    // 浮现在中间
    //===================================================

    /**
     *
     * 默认方法：瞬时显示提示消息(浮现在中间)
     *
     * @param mContext
     *            显示提示的activity
     * @param msg
     *            提示消息
     */
    public static void showToast(final Context mContext, final String msg) {
        showShortToastCenter(mContext, msg);
    }

    /**
     *
     * 显示提示消息(浮现在中间)
     *
     * @param mContext
     *            显示提示的activity
     * @param msg
     *            提示消息
     * @param duration
     *            持续时间
     */
    public static void showToastCenter(final Context mContext, final String msg, final int duration) {
        showToast(mContext, Gravity.CENTER, msg, 0, duration);
    }

    /**
     * 显示自定义Toast提示(来自String)
     * @param mContext 上下文
     * @param text 提示内容
     */
    public static void showLongToastCenter(Context mContext, String text) {
        showToastCenter(mContext, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示自定义Toast提示(来自String)
     * @param mContext 上下文
     * @param stringResId 提示内容资源Id
     */
    public static void showLongToastCenter(Context mContext, int stringResId) {
        String msg = mContext.getString(stringResId);
        showLongToastCenter(mContext, msg);
    }

    /**
     * 显示自定义Toast提示(来自String)
     * @param mContext 上下文
     * @param text 提示内容
     */
    public static void showShortToastCenter(Context mContext, String text) {
        showToastCenter(mContext, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示自定义Toast提示(来自String)
     * @param mContext 上下文
     * @param stringResId 提示内容资源Id
     */
    public static void showShortToastCenter(Context mContext, int stringResId) {
        String msg = mContext.getString(stringResId);
        showShortToastCenter(mContext, msg);
    }

    /**
     *
     * 显示提示消息(居中显示，提示内容左侧带小图标)
     *
     * @param mContext
     *            显示提示的activity
     * @param msg
     *            提示消息
     * @param icon
     *            小图标资源id-提示消息左侧小图标（18*18）,若为-1则不显示
     * @param duration
     *            持续时间
     */
    public static void showToastWithIcon(final Context mContext, final int icon, final String msg, final int duration) {
        showToast(mContext, Gravity.CENTER, icon, msg, 0, duration);
    }

    /**
     *
     * 显示提示消息(居中显示，提示内容左侧带小图标)
     *
     * @param mContext
     *            显示提示的activity
     * @param msg
     *            提示消息
     * @param icon
     *            小图标资源id-提示消息左侧小图标（18*18）,若为-1则不显示
     */
    public static void showShortToastWithIcon(final Context mContext, final int icon, final String msg) {
        showToast(mContext, Gravity.CENTER, icon, msg, 0, Toast.LENGTH_SHORT);
    }

    /**
     *
     * 显示提示消息(居中显示，提示内容左侧带小图标)
     *
     * @param mContext
     *            显示提示的activity
     * @param msg
     *            提示消息
     * @param icon
     *            小图标资源id-提示消息左侧小图标（18*18）,若为-1则不显示
     */
    public static void showLongToastWithIcon(final Context mContext, final int icon, final String msg) {
        showToast(mContext, Gravity.CENTER, icon, msg, 0, Toast.LENGTH_LONG);
    }

    // 浮现在底部
    //===================================================
    /**
     *
     * 显示提示消息(默认浮现在底部,偏移100)
     *
     * @param mContext
     *            显示提示的activity
     * @param msg
     *            提示消息
     * @param duration
     *            持续时间
     */
    public static void showToastBottom(final Context mContext, final String msg, final int duration) {
        showToast(mContext, Gravity.BOTTOM, msg, 100, duration);
    }

}

