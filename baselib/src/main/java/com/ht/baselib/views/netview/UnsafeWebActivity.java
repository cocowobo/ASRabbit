package com.ht.baselib.views.netview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 测试webview 的注入漏洞（Android4.2以下系统存在）
 */
public class UnsafeWebActivity extends Activity {
    /**本地测试html链接*/
    public static final String HTML = "file:///android_asset/unsafe_test.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView wv = new WebView(this);
        setContentView(wv);

        WebSettings webView = wv.getSettings();
        webView.setJavaScriptEnabled(true);
        wv.setWebChromeClient(new InnerChromeClient());

        wv.loadUrl(HTML);
    }

    /**
     * 拓展WebChromeClient类
     */
    public class InnerChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
                    .create()
                    .show();
            return true;
        }
    }
}