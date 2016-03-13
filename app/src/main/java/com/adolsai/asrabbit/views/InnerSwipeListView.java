package com.adolsai.asrabbit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ht.baselib.views.swipemenulistview.SwipeMenuListView;

/**
 * <p>继承自自定义ListView,实现了Item侧滑且不可收缩，一般放在listview或者scrollview内部，防止滑动冲突</p>
 *
 * @author zmingchun
 * @version 1.0 (2015-12-04)
 */
public class InnerSwipeListView extends SwipeMenuListView {
    /**
     * @param context  上下文
     * @param attrs    属性
     * @param defStyle 样式
     */
    public InnerSwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param context 上下文
     * @param attrs   属性
     */
    public InnerSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    /**
     * @param context 上下文
     */
    public InnerSwipeListView(Context context) {
        super(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heighSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heighSpec);
    }

    private float downX, downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                //禁止了向下滑动
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - downX) > Math.abs(ev.getY() - downY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);// 这句话的作用
                    // 告诉父view，我的事件我自行处理，不要阻碍我。
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                //禁止了向上滑动
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
