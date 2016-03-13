package com.ht.baselib.views.netview;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.ht.baselib.views.dialog.CustomToast;

/**
 * <p>
 *     HtJsScope类 js脚本所能执行的方法空间(提供被JS调用的方法)
 * </p>
 *
 * @author zmingchun
 * @version 1.0 (2016/1/15 17:11)
 */
public class HtJsScope {
    /**
     * 供JS调用的统一接口对象名称
     * <br/>如：HtAndroid.toast('我是气泡');
     * */
    public static final String HT_JS_INTERFACE_NAME = "HtAndroid";

    /**webview 对象*/
    private WebView mWebview;

    /**
     * 构造方法
     * @param webview webview对象
     */
    public HtJsScope(WebView webview) {
        this.mWebview = webview;
    }

    /**
     * 短暂气泡提醒
     *
     * @param message 提示信息
     */
    @android.webkit.JavascriptInterface
    public void toast(String message) {
        CustomToast.showToast(mWebview.getContext(), message);
    }

    /**
     * 弹出记录的测试JS层到Java层代码执行损耗时间差
     *
     * @param timeStamp js层执行时的时间戳
     */
    @android.webkit.JavascriptInterface
    public void testLossTime(long timeStamp) {
        timeStamp = (System.currentTimeMillis() - timeStamp)/1000;
        toast("耗时：" + String.valueOf(timeStamp) + "秒");
    }

    /**
     * 异步回调，传入js函数到Java方法,设定3秒后回调
     * @param ms 延时时间
     * @param backMsg 回调提示内容
     * @param jsCallback 回调方法
     */
    @android.webkit.JavascriptInterface
    public void delayJsCallBack(final int ms, final String backMsg, final HtJsCallback jsCallback) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //backMsg = "javascript:(" + mMethodName + ")('" + mJSONArgument + "')";
                    jsCallback.apply(backMsg);
                } catch (HtJsCallback.JsCallbackException je) {
                    je.printStackTrace();
                }
            }
        }, ms);
    }
}