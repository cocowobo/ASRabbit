package com.ht.baselib.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Msg: 软键盘帮助类
 * Update:  2015-9-11
 * Version: 1.0
 * Created by zengyaping on 2015-9-11 10:40.
 */
public final class SoftInputMethodUtils {
    /**
     * 私有化构造函数
     */
    private SoftInputMethodUtils(){

    }
    /**
     * 显示输入法键盘
     *
     * @param context 上下文环境
     * @param editText 在这个edittext上显示输入法
     *
     */
    public static void showSoftInputMethod(Context context, EditText editText) {
        if (context != null && editText != null) {
            editText.requestFocus();
            InputMethodManager inputManager =

                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.showSoftInput(editText, 0);
        }
    }

    /**
     * 隐藏输入法
     *
     * @param context 上下文环境
     * @param editText 在这个edittext上隐藏输入法
     */
    public static void hideSoftInputMethod(Context context, EditText editText) {
        if (context != null && editText != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(),
                        0);
            }
        }
    }
}
