package com.ht.baselib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Msg: 视图显示相关工具类（获取屏幕宽高）
 * Update:  2015/9/28
 * Version: 1.0
 * Created by zmc on 2015/9/28 14:03.
 * @author huqiu.lhq
 */
public class LocalDisplay {
    /**屏幕宽度，单位像素px*/
    public static int screenWidthPixels;
    /**屏幕高度，单位像素px*/
    public static int screenHeightPixels;
    /**屏幕像素*/
    public static float screenDensity;
    /**屏幕宽度，单位像素dp*/
    public static int screenWidthDp;
    /**屏幕高度，单位像素dp*/
    public static int screenHeightDp;
    private static boolean sInitialed;

    /**默认构造方法*/
    public LocalDisplay() {
    }

    /**
     * 初始化，可以在全局Application中调用，只需要实例化一次
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (!sInitialed && context != null) {
            sInitialed = true;
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            screenWidthPixels = dm.widthPixels;
            screenHeightPixels = dm.heightPixels;
            screenDensity = dm.density;
            screenWidthDp = (int) ((float) screenWidthPixels / dm.density);
            screenHeightDp = (int) ((float) screenHeightPixels / dm.density);
        }
    }

    /**
     * 根据手机的分辨率，将px(像素)值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue px值
     * @return
     */
    public static int px2dp(float pxValue) {
        final float scale = screenDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue dp值
     * @return
     */
    public static int dp2px(float dipValue) {
        final float scale = screenDensity;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue 像素值
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = screenDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue 字体sp值
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = screenDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
