package com.ht.baselib.views.PullToRefreshView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 这个类封装了下拉刷新的布局
 *
 * @author chenchao
 * @since 2015-11-17
 */
public class EmptyLoadingLayout extends LoadingLayout {
    /**
     * 构造方法
     *
     * @param context context
     */
    public EmptyLoadingLayout(Context context) {
        super(context);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public EmptyLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return new FrameLayout(context);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
    }

    @Override
    protected void onPullToRefresh() {
    }

    @Override
    protected void onReleaseToRefresh() {
    }

    @Override
    protected void onRefreshing() {
    }
}
