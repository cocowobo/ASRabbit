package com.ht.baselib.helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.ht.baselib.R;

import java.util.Random;


/**
 * <p>
 * 静态资源引用帮助类
 * <br/>一般用于不方便获取context的地方
 * <p/>
 * @author zmingchun
 * @version 1.0 (2015-05-30)
 */
public final class ResourcesHelper {
    /**图片占位图视图设置-头像类的*/
    public static Drawable sPlaceholderHeadImage;
    /**图片占位图视图设置-截图类的-红色*/
    public static Drawable sPlaceholderDrawable0;
    /**图片占位图视图设置-截图类的-青色*/
    public static Drawable sPlaceholderDrawable1;
    /**图片占位图视图设置-截图类的-橘色*/
    public static Drawable sPlaceholderDrawable2;
    /**图片占位图视图设置-截图类的-蓝色*/
    public static Drawable sPlaceholderDrawable3;
    /**图片加载失败视图设置*/
    public static Drawable sErrorDrawable;

    /**
     * 初始化资源
     *
     * @param resources 资源管理类
     */
    public static void init(final Resources resources) {
        if (sPlaceholderDrawable0 == null) {
            sPlaceholderDrawable0 = resources.getDrawable(R.color.ht_imageView_load_placeholder0);
        }
        if (sPlaceholderDrawable1 == null) {
            sPlaceholderDrawable1 = resources.getDrawable(R.color.ht_imageView_load_placeholder1);
        }
        if (sPlaceholderDrawable2 == null) {
            sPlaceholderDrawable2 = resources.getDrawable(R.color.ht_imageView_load_placeholder2);
        }
        if (sPlaceholderDrawable3 == null) {
            sPlaceholderDrawable3 = resources.getDrawable(R.color.ht_imageView_load_placeholder3);
        }

        if (sPlaceholderHeadImage == null) {
//            sPlaceholderHeadImage = resources.getDrawable(R.drawable.b_login_head);
            sPlaceholderHeadImage = resources.getDrawable(R.color.ht_gray_purple9);
        }

        if (sErrorDrawable == null) {
            sErrorDrawable = resources.getDrawable(R.color.ht_gray_purple9);
        }

    }


    /**
     * 获取随机的占位图
     */
    public static Drawable getRadomPlaceHolder() {
        Drawable drawable;
        //生成0~4之间的随机数
        int result = new Random().nextInt(4);
        switch (result) {
            case 0:
                drawable = sPlaceholderDrawable0;
                break;
            case 1:
                drawable = sPlaceholderDrawable1;
                break;
            case 2:
                drawable = sPlaceholderDrawable2;
                break;
            case 3:
                drawable = sPlaceholderDrawable3;
                break;
            default:
                drawable = sPlaceholderDrawable0;
                break;
        }
        return drawable;
    }

    /**
     * 在API16以前使用setBackgroundDrawable，在API16以后使用setBackground
     * API16<---->4.1
     * @param view 设置背景的view
     * @param drawable 背景资源
     */
    public static void setBackgroundOfVersion(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Android系统大于等于API16，使用setBackground
            view.setBackground(drawable);
        } else {
            //Android系统小于API16，使用setBackground
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Drawable → Bitmap
     *
     * @param drawable 背景资源
     */
    public static Bitmap drawable2Bitmap(Drawable drawable){
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }
}
