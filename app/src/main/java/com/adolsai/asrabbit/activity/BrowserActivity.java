package com.adolsai.asrabbit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.helper.HtWebViewHelper;
import com.adolsai.asrabbit.utils.LocalAppUtil;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.StringUtils;
import com.ht.baselib.views.dialog.CustomToast;
import com.ht.baselib.views.netview.HtSafeWebview;
import com.ht.baselib.views.pulltorefresh.PullToRefreshBase;
import com.ht.baselib.views.pulltorefresh.PullToRefreshHtSafeWebView;

import butterknife.Bind;


/**
 * <p>
 * 公用webview浏览器 Activity
 * <br/>
 * 本界面不提供原生交互,仅作浏览器使用
 * 启动本界面的源Activity 需要设置
 * intent.putExtra(BrowserActivity.INTENT_PARAMS_KEY_TITLE,url);
 * intent.putExtra(BrowserActivity.INTENT_PARAMS_KEY_LOAD_URL,title);
 * </p>
 *
 * @author hxm
 * @version 1.0 (2015-11-20)
 */
public class BrowserActivity extends AsRabbitBaseActivity {
    /**
     * 当前类标识
     */
    //常量区 start===================================================================================
    /**
     * intent 标题key
     */
    public static final String INTENT_PARAMS_KEY_TITLE = "INTENT_PARAMS_KEY_TITLE";

    /**
     * intent 链接key
     */
    public static final String INTENT_PARAMS_KEY_LOAD_URL = "INTENT_PARAMS_KEY_LOAD_URL";


    //变量区 start===================================================================================
    /**
     * 主视图
     */
    protected LinearLayout llMainView;
    /**
     * webview拓展类
     */
    protected HtWebViewHelper htWebviewHelper;
    /**
     * 状态保持
     */
    protected Bundle savedInstanceState;
    /**
     * intent
     */
    protected Intent intent;
    /**
     * 页面标题
     */
    protected String titleName;

    protected String loadUrl;
    protected PullToRefreshHtSafeWebView pullToRefreshHtSafeWebView;
    /**
     * 是否等网络返回后加载URL
     */
    protected boolean isDelay = false;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;


    //执行区 start===================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();

        initActivity(R.layout.activity_browser, savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != htWebviewHelper.getWebview()) {
            htWebviewHelper.getWebview().saveState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        if (null != llMainView) {
            llMainView.removeAllViews();
            llMainView = null;
        }
        if (htWebviewHelper != null) {
            htWebviewHelper.destroyView();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!LocalAppUtil.isContainMainActivity()) {
            ActivityUtil.startActivityByAnim(this, HomeActivity.class);
        }
        super.onBackPressed();
    }

    //方法区 start===================================================================================
    @Override
    protected void initViews() {
        titleName = intent.getStringExtra(INTENT_PARAMS_KEY_TITLE);
        if (!StringUtils.isBlank(titleName)) {
            toolbar.setTitle(titleName);
        }

        llMainView = (LinearLayout) findViewById(R.id.ll_main_view);
        pullToRefreshHtSafeWebView = (PullToRefreshHtSafeWebView) findViewById(R.id.pullToRefreshHtSafeWebView);
        htWebviewHelper = new HtWebViewHelper(this, pullToRefreshHtSafeWebView.getRefreshableView());
        if (null != savedInstanceState && null != htWebviewHelper.getWebview()) {
            //恢复状态
            htWebviewHelper.getWebview().restoreState(savedInstanceState);
        } else {
            loadWebUrl();
        }
        pullToRefreshHtSafeWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<HtSafeWebview>() {
            @Override
            public void onRefresh(PullToRefreshBase<HtSafeWebview> refreshView) {
                htWebviewHelper.reload();
                pullToRefreshHtSafeWebView.onRefreshComplete();
            }
        });
    }


    /**
     * 加载网页
     */
    protected void loadWebUrl() {
        if (!isDelay) {
            loadUrl = intent.getStringExtra(INTENT_PARAMS_KEY_LOAD_URL);
            if (StringUtils.isBlank(loadUrl)) {
                CustomToast.showToast(mContext, "路径不存在");
                this.finish();
            } else {
                LogUtils.e(loadUrl);
                htWebviewHelper.loadUrl(loadUrl);
            }
        }
    }

    @Override
    protected void initData() {

    }

    /**
     * 开放方法（供其他类调用）——跳转至当前类（拓展页面标题栏右侧按钮）
     *
     * @param context 上下文
     * @param title   页面标题
     * @param url     请求的h5链接
     * @return intent
     */
    public static Intent createIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        if (!StringUtils.isBlank(title)) {
            intent.putExtra(INTENT_PARAMS_KEY_TITLE, title);
        }
        if (!StringUtils.isBlank(url)) {
            intent.putExtra(INTENT_PARAMS_KEY_LOAD_URL, url);
        }

        return intent;
    }
}
