package com.adolsai.asrabbit.listener;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.adolsai.asrabbit.R;


/**
 * MSG：输入文本字数监听
 *
 * @author wenwei.chen<br/>
 * @version 1.0 (2015-11-26)
 */
public class OnTextWatcher implements TextWatcher {
    private CharSequence temp;
    private int selectionStart;
    private int selectionEnd;
    private int num = 140;
    private Activity activity;
    private EditText etWords;
    private TextView tvWords;
    private TextView tvTitlebarRight;
    private boolean isDec = true;
    private boolean isTitlebarRight = false;

    /**
     * 构造方法
     *
     * @param etWords 输入文本
     * @param tvWords 显示文本
     * @param num     最大输入数量
     * @param isDec   是否递减
     */
    public OnTextWatcher(EditText etWords, TextView tvWords, int num, boolean isDec) {
        this.etWords = etWords;
        this.tvWords = tvWords;
        this.num = num;
        this.isDec = isDec;
    }

    /**
     * 构造方法
     *
     * @param etWords 输入文本
     * @param tvWords 显示文本
     * @param num     最大输入数量
     */
    public OnTextWatcher(EditText etWords, TextView tvWords, int num) {
        this.etWords = etWords;
        this.tvWords = tvWords;
        this.num = num;
    }

    /**
     * 构造方法
     *
     * @param activity        上下文
     * @param etWords         输入文本
     * @param tvWords         显示文本
     * @param tvTitlebarRight 提交按钮
     * @param num             最大输入数量
     */
    public OnTextWatcher(Activity activity, EditText etWords, TextView tvWords, TextView tvTitlebarRight, int num) {
        this.activity = activity;
        this.etWords = etWords;
        this.tvWords = tvWords;
        this.tvTitlebarRight = tvTitlebarRight;
        this.num = num;
        this.isTitlebarRight = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        int number;
        if (isDec) {
            number = num - s.length();
        } else {
            number = s.length();
        }
        if (isTitlebarRight) {
            if (s.length() == 0) {
                tvTitlebarRight.setTextColor(activity.getResources().getColor(R.color.global_aux_black));
                tvTitlebarRight.setEnabled(false);
            } else {
                tvTitlebarRight.setTextColor(activity.getResources().getColor(R.color.global_price_orange));
                tvTitlebarRight.setEnabled(true);
            }
        }
        tvWords.setText("" + number);
        selectionStart = etWords.getSelectionStart();
        selectionEnd = etWords.getSelectionEnd();
        if (temp.length() > num) {
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionStart;
            etWords.setText(s);
            etWords.setSelection(tempSelection);
        }
    }
}