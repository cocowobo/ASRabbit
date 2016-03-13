package com.ht.baselib.views.PullToRefreshView;

import android.content.Context;
import android.util.AttributeSet;

import com.ht.baselib.views.netview.HtSafeWebview;

/**
 * 封装了HtSafeWebview的下拉刷新
 * 
 * @author wenwei.chen
 * @since 2013-8-22
 */
public class PullToRefreshHtSafeWebView extends PullToRefreshBase<HtSafeWebview> {
    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshHtSafeWebView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshHtSafeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public PullToRefreshHtSafeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected HtSafeWebview createRefreshableView(Context context, AttributeSet attrs) {
        HtSafeWebview webView = new HtSafeWebview(context,attrs);
        return webView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullUp() {
        double exactContentHeight = Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }
}
