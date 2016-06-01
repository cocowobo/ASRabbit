package com.ht.baselib.views.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ht.baselib.R;
import com.ht.baselib.views.wheelview.OnWheelScrollListener;
import com.ht.baselib.views.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;


/**
 * Msg: 二级分类选择器
 * Version: 1.1
 * Created by chenchao on 2015/11/26
 */
public class ClassifySelectorDialog extends Dialog implements View.OnClickListener, OnWheelScrollListener {
    /**
     * context
     */
    private Context mContext;
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 指示图标
     */
    private ImageView ivFlag;

    /**
     * 一级类目滚轮控件
     */
    private WheelView firstCategoryView;
    /**
     * 二级类目滚轮控件
     */
    private WheelView secondCategoryView;
    /**
     * 一级类目滚轮适配器
     */
    private ClassifyAdapter firstCategoryAdapter;
    /**
     * 二级类目滚轮适配器
     */
    private ClassifyAdapter secondCategoryAdapter;
    /**
     * 类目选择监听器
     */
    private OnClassifySelectListener listener;

    /**
     * 一级类目列表
     */
    private List<String> firstCategoryList = new ArrayList<String>();
    /**
     * 二级类目列表
     */
    private List<List<String>> secondCategoryList = new ArrayList<List<String>>();

    /**
     * 设置标题
     */
    private String title;
    /**
     * 当前日期索引
     */
    private int firstCategoryIndex;
    /**
     * 当前时间索引
     */
    private int secondCategoryIndex;
    /**
     * 显示item数
     */
    private int visibleItems = 3;

    /***
     * 开始时间日期选择构造
     *
     * @param context  上下文
     * @param listener 接口
     */
    public ClassifySelectorDialog(Context context, OnClassifySelectListener listener) {
        super(context, R.style.TimeDialog);
        this.listener = listener;
        this.mContext = context;
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        // window.setWindowAnimations(R.style.TimeDialogStyle);
        setCancelable(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_classify_selector);
        initLayoutParams();
        initView();
        initTimeSelectView();
    }

    /**
     * findView
     *
     * @param resId 资源ID
     * @param <V>   view
     * @return view
     */
    public <V extends View> V findView(int resId) {
        return (V) findViewById(resId);
    }

    /**
     * 设置天数
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置一级分类数据
     *
     * @param firstCategoryList 一级分类数据
     */
    public void setFirstCategoryList(List<String> firstCategoryList) {
        this.firstCategoryList = firstCategoryList;
    }

    /**
     * 设置二级分类数据
     *
     * @param secondCategoryList 二级分类数据
     */
    public void setSecondCategoryList(List<List<String>> secondCategoryList) {
        this.secondCategoryList = secondCategoryList;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_tilte);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        ivFlag = findView(R.id.iv_flag);

        firstCategoryView = findView(R.id.wheelView_first_category);
        secondCategoryView = findView(R.id.wheelView_second_category);

        findView(R.id.button_selector_confirm).setOnClickListener(this);
        findView(R.id.button_selector_cancel).setOnClickListener(this);
    }

    /**
     * 初始化布局参数
     */
    private void initLayoutParams() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        LinearLayout layout = findView(R.id.layout_classify_select);
        LayoutParams params = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
    }

    /**
     * 初始化滚轮控件
     */
    private void initTimeSelectView() {
        if (firstCategoryList != null && !firstCategoryList.isEmpty()) {
            firstCategoryAdapter = new ClassifyAdapter(mContext);
            firstCategoryAdapter.setDataList(firstCategoryList);
            firstCategoryView.setViewAdapter(firstCategoryAdapter);
            firstCategoryView.setCurrentItem(0);
            firstCategoryView.setVisibleItems(visibleItems);
            firstCategoryView.addScrollingListener(this);
            firstCategoryView.setCyclic(false);
            firstCategoryView.setShadowColor(Color.parseColor("#ffffff"),
                    Color.parseColor("#00ffffff"),
                    Color.parseColor("#00ffffff"));
//            firstCategoryView.setDrawTriangle(true);
            firstCategoryIndex = firstCategoryView.getCurrentItem();
        }

        if (secondCategoryList != null && !secondCategoryList.isEmpty()) {
            secondCategoryAdapter = new ClassifyAdapter(mContext);
            secondCategoryAdapter.setDataList(secondCategoryList.get(0));
            secondCategoryView.setViewAdapter(secondCategoryAdapter);
            secondCategoryView.setCyclic(false);
            secondCategoryView.setVisibleItems(visibleItems);
            secondCategoryView.addScrollingListener(this);
            secondCategoryView.setShadowColor(Color.parseColor("#ffffff"),
                    Color.parseColor("#00ffffff"),
                    Color.parseColor("#00ffffff"));
            if (secondCategoryAdapter.getItemsCount() > 0) {
                secondCategoryView.setCurrentItem(secondCategoryAdapter.getItemsCount() / 2);
                secondCategoryIndex = secondCategoryView.getCurrentItem();
//                ivFlag.setVisibility(View.VISIBLE);
            } else {
//                ivFlag.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_selector_confirm) {
            callbackSeleteTime();
        } else if (v.getId() == R.id.button_selector_cancel) {
            if (listener != null) {
                listener.onUnSelect();
            }
            dismiss();
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
    }

    /***
     * 选择回调
     */
    private void callbackSeleteTime() {
//        String firstCategory = null;
//        String secondCategory = null;
//
//        if (firstCategoryList != null && !firstCategoryList.isEmpty()) {
//            firstCategory = firstCategoryList.get(firstCategoryIndex);
//        }
//
//        if (secondCategoryList != null && !secondCategoryList.isEmpty()) {
//            secondCategory = secondCategoryList.get(firstCategoryIndex).get(secondCategoryIndex);
//        }

        if (listener != null) {
            listener.onSelected(firstCategoryIndex, secondCategoryIndex);
        }
        dismiss();
    }


    /***
     * 滚动结束
     *
     * @param wheel 滚条
     */
    @Override
    public void onScrollingFinished(WheelView wheel) {
        if (wheel.getId() == R.id.wheelView_first_category) {
            firstCategoryIndex = firstCategoryView.getCurrentItem();

            secondCategoryAdapter.setDataList(secondCategoryList.get(firstCategoryIndex));
            secondCategoryView.setViewAdapter(secondCategoryAdapter);
            secondCategoryView.setVisibleItems(visibleItems);
            if (secondCategoryAdapter.getItemsCount() > 0) {
                secondCategoryView.setCurrentItem(secondCategoryAdapter.getItemsCount() / 2);
                secondCategoryIndex = secondCategoryView.getCurrentItem();
//                    ivFlag.setVisibility(View.VISIBLE);
            } else {
//                    ivFlag.setVisibility(View.GONE);
            }
        } else if (wheel.getId() == R.id.wheelView_second_category) {
            secondCategoryIndex = secondCategoryView.getCurrentItem();
        }

    }
}
