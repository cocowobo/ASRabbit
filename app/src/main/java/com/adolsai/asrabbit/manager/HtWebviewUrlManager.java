package com.adolsai.asrabbit.manager;

import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

import com.adolsai.asrabbit.utils.LocalAppUtil;

import java.util.Locale;

/**
 * <p>
 *     HtWebviewUrlManager类 webview 内url统一拦截处理分发器
 * </p>
 *
 * @author zmingchun
 * @version 1.0 (2016/1/18 17:32)
 */
public class HtWebviewUrlManager {
    //变量区 start===================================================================================
    /**webview控件*/
    private WebView mWebview;
    /**拦截处理的链接*/
    private String mUrl;
    /**
     * 构造方法
     * @param view webview对象
     * @param url 拦截的url
     */
    public HtWebviewUrlManager(WebView view, String url) {
        this.mWebview = view;
        this.mUrl = url;
    }

    //方法区 start===================================================================================
    /**
     *
     * @return
     * <p>
     *   true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载
     *   (只有当不需要加载网址而是拦截做其他处理，如拦截tel:xxx等特殊url做拨号处理的时候，才应该返回true)
     *   false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
     *   (如果不需要对url进行拦截做处理，而是简单的继续加载此网址。
         则建议采用返回false的方式而不是loadUrl的方式进行加载网址。)
     * </p>
     */
    public boolean dealUrl(){
        if(null != mUrl && !"".equals(mUrl)){
            if(mUrl.contains("tel:")){
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri contentUrl = Uri.parse(mUrl);
                intent.setData(contentUrl);
                mWebview.getContext().startActivity(intent);
                return true;
            }else if(mUrl.contains("mailto:")){
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri contentUrl = Uri.parse(mUrl);
                intent.setData(contentUrl);
                mWebview.getContext().startActivity(intent);
                return true;
            }else if(LocalAppUtil.isUrl(mUrl)){
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(mUrl);//识别文件扩展名
                String  strUrl = mUrl.toLowerCase(Locale.getDefault());
                if(!strUrl.contains(".doc&")&&(strUrl.contains(".doc")||strUrl.contains(".docx")||strUrl.contains(".xls")||strUrl.contains(".xlsx")
                        ||strUrl.contains(".ppt")||strUrl.contains(".pptx")||strUrl.contains(".pdf")||strUrl.contains(".rar")||strUrl.contains(".zip"))){
                    //TODO 提示是否去下载
                    return true;
                }else{
                    //点击网页里面的链接还是在当前的webview里跳转
                    mWebview.loadUrl(mUrl);
                    return false;
                }
            }
        }
        return true;
    }

}