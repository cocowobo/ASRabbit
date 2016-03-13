package com.ht.baselib.views.netview;

import android.util.Log;
import android.webkit.WebView;

import com.ht.baselib.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * <p>JsCallback类 异步回调页面JS函数管理对象</p>
 *
 * @author zhangguojun （from net https://github.com/seven456/SafeWebView）
 * @version 1.0
 *          1.1 update by zmingchun on 2015/6/21
 */
public class HtJsCallback {
    private static final String CALLBACK_JS_FORMAT = "javascript:%s.callback(%d, %d %s);";
    private int mIndex;
    private boolean mCouldGoOn;
    private WeakReference<WebView> mWebViewRef;
    private int mIsPermanent;
    private String mInjectedName;

    /**
     * 构造方法
     * @param view webview对象
     * @param injectedName 注入的方法名
     * @param index 索引
     */
    public HtJsCallback(WebView view, String injectedName, int index) {
        mCouldGoOn = true;
        mWebViewRef = new WeakReference<WebView>(view);
        mInjectedName = injectedName;
        mIndex = index;
    }

    /**
     * 向网页执行js回调；
     * @param args 参数组
     * @throws JsCallbackException 回调异常
     */
    public void apply(Object... args) throws JsCallbackException {
        if (mWebViewRef.get() == null) {
            throw new JsCallbackException("the WebView related to the HtJsCallback has been recycled");
        }
        if (!mCouldGoOn) {
            throw new JsCallbackException("the HtJsCallback isn't permanent,cannot be called more than once");
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args){
            sb.append(",");
            boolean isStrArg = arg instanceof String;
            // 有的接口将Json对象转换成了String返回，这里不能加双引号，否则网页会认为是String而不是JavaScript对象；
            boolean isObjArg = isJavaScriptObject(arg);
            if (isStrArg && !isObjArg) {
                sb.append("\"");
            }
            sb.append(String.valueOf(arg));
            if (isStrArg && !isObjArg) {
                sb.append("\"");
            }
        }
        String execJs = String.format(CALLBACK_JS_FORMAT, mInjectedName, mIndex, mIsPermanent, sb.toString());
        if (BuildConfig.DEBUG) {
            Log.d("JsCallBack", execJs);
        }
        mWebViewRef.get().loadUrl(execJs);
        mCouldGoOn = mIsPermanent > 0;
    }

    /**
     * 是否是JSON(JavaScript Object Notation)对象；
     * @param obj 对象内容
     * @return
     */
    private boolean isJavaScriptObject(Object obj) {
        if (obj instanceof JSONObject || obj instanceof JSONArray) {
            return true;
        } else {
            String json = obj.toString();
            try {
                new JSONObject(json);
            } catch (JSONException e) {
                try {
                    new JSONArray(json);
                } catch (JSONException e1) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 一般传入到Java方法的js function是一次性使用的，即在Java层HtJsCallback.apply(...)之后不能再发起回调了；
     * 如果需要传入的function能够在当前页面生命周期内多次使用，请在第一次apply前setPermanent(true)；
     * @param value 是否多次调用：true是，false否
     */
    public void setPermanent(boolean value) {
        mIsPermanent = value ? 1 : 0;
    }

    /**
     * 回调异常处理类
     */
    public static class JsCallbackException extends Exception {
        /**
         * 异常处理方法
         * @param msg 异常信息
         */
        public JsCallbackException(String msg) {
            super(msg);
        }
    }
}
