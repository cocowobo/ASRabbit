package com.adolsai.asrabbit.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <p>ViewPagerNoSlide类 </p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/2/29 15:02)<br/>
 */
public class ViewPagerNoSlide extends ViewPager {
    /**
     * 是否可以滑动
     * 默认不可以滑动
     */
    private boolean isScrollable = false;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public ViewPagerNoSlide(Context context) {
        super(context);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public ViewPagerNoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置是否可以滑动
     *
     * @param isScroll 是否可以滑动
     */
    public void setScroll(boolean isScroll) {
        this.isScrollable = isScroll;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
