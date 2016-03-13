/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ht.baselib.views.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.ht.baselib.R;
import com.ht.baselib.views.netview.HtSafeWebview;

public class PullToRefreshHtSafeWebView extends PullToRefreshBase<HtSafeWebview> {

    private static final OnRefreshListener<HtSafeWebview> defaultOnRefreshListener = new OnRefreshListener<HtSafeWebview>() {

        @Override
        public void onRefresh(PullToRefreshBase<HtSafeWebview> refreshView) {
            refreshView.getRefreshableView().reload();
        }

    };

    public PullToRefreshHtSafeWebView(Context context) {
        super(context);
        // Added so that by default, Pull-to-Refresh refreshes the page
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public PullToRefreshHtSafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Added so that by default, Pull-to-Refresh refreshes the page
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public PullToRefreshHtSafeWebView(Context context, Mode mode) {
        super(context, mode);
        // Added so that by default, Pull-to-Refresh refreshes the page
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public PullToRefreshHtSafeWebView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        // Added so that by default, Pull-to-Refresh refreshes the page
        setOnRefreshListener(defaultOnRefreshListener);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected HtSafeWebview createRefreshableView(Context context, AttributeSet attrs) {
        HtSafeWebview webView = new HtSafeWebview(context,attrs);
        webView.setId(R.id.webview);
        return webView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        float exactContentHeight = (float) Math.floor(mRefreshableView.getContentHeight()
                * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }

    @Override
    protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
        super.onPtrRestoreInstanceState(savedInstanceState);
        mRefreshableView.restoreState(savedInstanceState);
    }

    @Override
    protected void onPtrSaveInstanceState(Bundle saveState) {
        super.onPtrSaveInstanceState(saveState);
        mRefreshableView.saveState(saveState);
    }
}
