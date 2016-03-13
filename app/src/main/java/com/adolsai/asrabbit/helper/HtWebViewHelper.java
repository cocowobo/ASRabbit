package com.adolsai.asrabbit.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.manager.HtWebviewUrlManager;
import com.ht.baselib.views.dialog.CustomDialog;
import com.ht.baselib.views.dialog.CustomToast;
import com.ht.baselib.views.netview.HtSafeWebview;


/**
 * <p>HtWebviewHelper类 Webview帮助类，统一配置</p>
 *
 * @author hxm
 * @version 1.0 (2016/1/14 15:45)
 */
public class HtWebViewHelper {

    /**
     * Activity 上下文
     */
    private Activity activity;
    /**
     * webview控件的资源id
     */
    private int mWebViewId;
    /**
     * webview控件
     */
    private HtSafeWebview mWebView;
    /**
     * 加载框
     */
    private CustomDialog cDialog;
    /**
     * 加载的js方法
     */
    private String jsMethod = null;

    /**
     * 构造方法
     *
     * @param activity  上下文
     * @param webviewId webview控件的资源id
     */
    public HtWebViewHelper(Activity activity, int webviewId) {
        super();
        this.activity = activity;
        this.mWebViewId = webviewId;
        mWebView = this.createWebView(mWebViewId);
    }

    /**
     * 构造方法
     *
     * @param activity  上下文
     * @param htSafeWebview webview控件的资源
     */
    public HtWebViewHelper(Activity activity, HtSafeWebview htSafeWebview) {
        super();
        this.activity = activity;
        mWebView = this.createWebView(htSafeWebview);
    }

    /**
     * 加载网络链接
     *
     * @param webView webview控件
     * @param url     html网络链接
     */
    private void loadUrl(final HtSafeWebview webView, final String url) {
        webView.loadUrl(url);
    }

    //对外方法区 start===============================================================================

    /**
     * 获取webview控件
     *
     * @return
     */
    public HtSafeWebview getWebview() {
        return mWebView;
    }

    /**
     * 销毁
     */
    public void destroyView() {
        if (null != mWebView) {
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    /**
     * 加载url
     *
     * @param url html网络链接
     */
    public void loadUrl(String url) {
        loadUrl(mWebView, url);
    }

    /**
     * 加载方法，网页加载完毕时加载方法，防止加载不到方法
     *
     * @param method js方法
     */
    public void loadMethod(String method) {
        this.jsMethod = method;
    }

    /**
     * 刷新页面-重新加载
     */
    public void reload() {
        if (null != mWebView) {
            mWebView.reload();
        }
    }
//创建WebView相关配置 start
//==============================

    /**
     * 初始化webview
     *
     * @param webviewId webview控件id
     * @return
     */
    @SuppressLint("SetJavaScriptEnabled")
    private HtSafeWebview createWebView(int webviewId) {
        try {
            final HtSafeWebview webView = (HtSafeWebview) activity.findViewById(webviewId);
            return createWebView(webView);
        }catch (Exception e) {
            return null;
        }
    }
    /**
     * 初始化webview
     *
     * @param webView webview控件
     * @return
     */
    @SuppressLint("SetJavaScriptEnabled")
    private HtSafeWebview createWebView(HtSafeWebview webView) {
        try {
            /********************webView 设置相关 start************************/
            WebSettings settings = webView.getSettings();
            settings.setSupportZoom(true);            //将图片调整到适合webview的大小
            settings.setLoadWithOverviewMode(true);  //页面自动缩放
            settings.setBuiltInZoomControls(false);    //设置显示缩放按钮
            settings.setJavaScriptEnabled(true);    //启用JS脚本
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setAllowFileAccess(true); // 允许访问文件

            /**
             * 用WebView显示图片，可使用这个参数
             * 设置网页布局类型：
             * 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
             * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
             */
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setDefaultTextEncodingName("utf-8"); //设置文本编码
            settings.setAppCacheEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式

//        settings.setBlockNetworkImage(true);
//        settings.setGeolocationEnabled(true);

            webView.setHapticFeedbackEnabled(false);
            webView.setBackgroundColor(0xffeeeeee);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            webView.requestFocus();
            webView.setFocusable(true);

            webView.setWebViewClient(new WebViewClientUtil());
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress >= 100) {
                        view.loadUrl(jsMethod);
                    }
                    super.onProgressChanged(view, newProgress);
                }

                @Override
                public boolean onJsAlert(WebView view, String url, String message,
                                         final JsResult result) {
                    CustomDialog customDialog = CustomDialog.newConfirmInstance(view.getContext());
                    customDialog.setContent(message);
                    customDialog.setConfirmBtnText(view.getContext().getString(R.string.custom_dialog_btn_confirm));
                    customDialog.setCancelBtnText(view.getContext().getString(R.string.custom_dialog_btn_cancel));
                    customDialog.setIsCancelable(false);
                    customDialog.setBtnCallback(new CustomDialog.ButtonCallback() {
                        @Override
                        public void onNegative(CustomDialog dialog) {
                            super.onNegative(dialog);
                        }

                        @Override
                        public void onPositive(CustomDialog dialog) {
                            super.onPositive(dialog);
                            result.confirm();
                        }
                    });
                    customDialog.show();
                    return true;
                }
            });
            /********************webView 设置相关 end************************/
            return webView;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 处理404
     */
    final ThreadLocal<Handler> webhandler = new ThreadLocal<Handler>() {
        @Override
        protected Handler initialValue() {
            return new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:

                            break;
                        case 2:
                            CustomToast.showShortToastCenter(activity, "当前链接可能已损坏，请求失败！");
                            break;
                        default:
                            break;
                    }
                }
            };
        }
    };

    /**
     * 配置链接处理方式
     */
    private class WebViewClientUtil extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.pauseTimers();
            Message msg = webhandler.get().obtainMessage();//发送通知，加入线程
            msg.what = 2;//通知加载自定义404页面
            webhandler.get().sendMessage(msg);//通知发送！
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (null != cDialog) {
                cDialog.dismiss();
            }
            if (null != activity && (
                    (null != view.getContext() && !activity.isFinishing())
                    ||(Build.VERSION.SDK_INT >= 17 && !activity.isDestroyed())
            )) {
                cDialog = CustomDialog.newLoadingInstance(view.getContext());
                cDialog.setIsCancelable(true);
                cDialog.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (cDialog != null) {
                cDialog.dismiss();
            }
        }

        /**
         * url重定向会执行此方法以及点击页面某些链接也会执行此方法
         *
         * @param view 当前webview
         * @param url  即将重定向的url
         * @return <p>
         * true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载
         * (只有当不需要加载网址而是拦截做其他处理，如拦截tel:xxx等特殊url做拨号处理的时候，才应该返回true)
         * false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
         * (如果不需要对url进行拦截做处理，而是简单的继续加载此网址。
         * 则建议采用返回false的方式而不是loadUrl的方式进行加载网址。)
         * </p>
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            HtWebviewUrlManager htUrlManager = new HtWebviewUrlManager(view, url);
            return htUrlManager.dealUrl();
        }
    }

//创建WebView相关 end
//==============================


}
