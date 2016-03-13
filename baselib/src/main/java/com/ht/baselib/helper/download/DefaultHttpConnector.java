
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.constants.NetType;
import com.ht.baselib.helper.download.entity.ConfigWrapper;
import com.ht.baselib.helper.download.interfaces.IHttpConnector;
import com.ht.baselib.helper.download.util.LogEx;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.Hashtable;


/**
 * Msg:默认连网类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DefaultHttpConnector implements IHttpConnector {

    private HttpURLConnection httpConn;

    private boolean isCmwap;

    /**
     * 默认连网类
     */
    public DefaultHttpConnector() {
        this(false);
    }

    /**
     *
     * @param isCmwap 是否是cmwap连接
     */
    public DefaultHttpConnector(boolean isCmwap) {
        this.isCmwap = isCmwap;
    }

    @Override
    public KGHttpResponse getHttpResponse(String resUrl) throws Exception {
        httpConn = create(resUrl);
        return handleResponse(httpConn);
    }

    @Override
    public KGHttpResponse getHttpResponse(String resUrl, long start) throws Exception {
        httpConn = create(resUrl);
        httpConn.setRequestProperty("RANGE", "bytes=" + start + "-");
        return handleResponse(httpConn);
    }

    @Override
    public KGHttpResponse getHttpResponse(String resUrl, long start, long end) throws Exception {
        httpConn = create(resUrl);
        httpConn.setRequestProperty("RANGE", "bytes=" + start + "-" + end);
        return handleResponse(httpConn);
    }

    @Override
    public long getContentLength(String resUrl) throws Exception {
        httpConn = create(resUrl);
        int contentLength = -1;
        int responseCode = httpConn.getResponseCode();
        LogEx.d("responseCode-->" + responseCode);
        if (responseCode == 200 || responseCode == 206) {
            contentLength = httpConn.getContentLength();
            httpConn.disconnect();
        }
        return contentLength;
    }

    @Override
    public void close() throws Exception {
        httpConn.disconnect();
    }

    private HttpURLConnection create(String resUrl) throws Exception {
        URL url = new URL(resUrl);
        HttpURLConnection httpConn;
        if (isCmwap) {
            // cmwap需要设置代理
            Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
            httpConn = (HttpURLConnection) url.openConnection(proxy);
        } else {
            httpConn = (HttpURLConnection) url.openConnection();
        }
        if (ConfigWrapper.getInstance().getNetType() == NetType.WIFI) {
            httpConn.setConnectTimeout(30 * 1000);
            httpConn.setReadTimeout(30 * 1000);
        } else {
            httpConn.setConnectTimeout(30 * 1000);
            httpConn.setReadTimeout(30 * 1000);
        }
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        Hashtable<String, String> requestHeaders = ConfigWrapper.getInstance().getRequestHeaders();
        if (requestHeaders != null) {
            for (String key : requestHeaders.keySet()) {
                httpConn.setRequestProperty(key, requestHeaders.get(key));
            }
        }
        return httpConn;
    }

    private KGHttpResponse handleResponse(HttpURLConnection httpConn) throws Exception {
        if (httpConn != null) {
            int responseCode = httpConn.getResponseCode();
            LogEx.d("responseCode-->" + responseCode);
            InputStream inputStream = httpConn.getInputStream();
            long contentLength = httpConn.getContentLength();
            String contentType = httpConn.getContentType();
            KGHttpResponse kgresp = new KGHttpResponse(responseCode, inputStream);
            kgresp.setHeader(KGHttpResponse.CONTENT_LENGTH, contentLength);
            if (contentType != null) {
                kgresp.setHeader(KGHttpResponse.CONTENT_TYPE, contentType);
            }
            return kgresp;
        }
        return null;
    }

}
