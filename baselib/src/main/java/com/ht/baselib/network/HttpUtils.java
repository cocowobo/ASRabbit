package com.ht.baselib.network;


import com.ht.baselib.utils.LogUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Msg: 网络请求类
 * <p>
 * Version: 1.0
 * Created by hxm on 2015/10/12 14:33.
 */

public class HttpUtils {

    /**
     * 连接超时
     */
    private static final int CONNECT_TIME = 10000;
    /**
     * 读取超时
     */
    private static final int READ_TIME = 10000;
    /**
     * 换行符
     */
    private static final String LINEND = "\r\n";
    /**
     * 编码格式
     */
    private static final String CHARSET = "utf-8";


    public String get(String actionUrl) throws Exception {
        LogUtils.v("get------------- actionUrl = " + actionUrl);
        HttpURLConnection urlConn = null;
        BufferedReader br = null;
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL url = new URL(actionUrl);//新建url对象
            urlConn = (HttpURLConnection) url.openConnection();//通过HttpURLConnection对象,向网络地址发送请求

            urlConn.setDoOutput(true);//设置该连接允许读取
            urlConn.setDoInput(true);//设置该连接允许写入
            urlConn.setUseCaches(false);//设置不能适用缓存
            urlConn.setConnectTimeout(CONNECT_TIME);   //设置连接超时时间
            urlConn.setReadTimeout(READ_TIME);   //读取超时
            urlConn.setRequestMethod("GET");//设置连接方法post
            urlConn.setRequestProperty("connection", "Keep-Alive");//设置维持长连接
            urlConn.setRequestProperty("Charset", CHARSET);//设置文件字符集

            //接收返回信息
            int code = urlConn.getResponseCode();
            LogUtils.v("get------------- code = " + code);
            if (code != 200) {
                urlConn.disconnect();
                Exception e = new Exception("connect fail");
                throw e;

            } else {
                String result = "";
                br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                LogUtils.v("get------------- result = " + result);
                return result;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (urlConn != null) {
                    urlConn.disconnect();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }


    /**
     * post网络请求
     *
     * @param actionUrl 请求地址
     * @param params    POST参数
     * @return
     * @throws Exception 网络请求异常,其中状码码非200时抛出异常
     */
    public String post(String actionUrl, Map<String, Object> params)
            throws Exception {
        LogUtils.v("post------------- actionUrl = " + actionUrl);
        HttpURLConnection urlConn = null;
        BufferedReader br = null;
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL url = new URL(actionUrl);//新建url对象
            urlConn = (HttpURLConnection) url.openConnection();//通过HttpURLConnection对象,向网络地址发送请求

            urlConn.setDoOutput(true);//设置该连接允许读取
            urlConn.setDoInput(true);//设置该连接允许写入
            urlConn.setUseCaches(false);//设置不能适用缓存
            urlConn.setConnectTimeout(CONNECT_TIME);   //设置连接超时时间
            urlConn.setReadTimeout(READ_TIME);   //读取超时
            urlConn.setRequestMethod("POST");//设置连接方法post
            urlConn.setRequestProperty("connection", "Keep-Alive");//设置维持长连接
            urlConn.setRequestProperty("Charset", CHARSET);//设置文件字符集

            /********************************************************************/
            //构建表单数据
            boolean bool = false;
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                Set<String> keySet = params.keySet();
                for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                    String name = it.next();
                    Object value = params.get(name);
                    if (bool) {
                        sb.append("&");
                    }
                    sb.append(name).append("=").append(value);
                    bool = true;
                }
                LogUtils.v("post------------- param = " + sb.toString());
                byte[] bypes = sb.toString().getBytes();
                DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                dos.write(bypes);// 输入参数
                dos.flush();
                dos.close();
            }

            //接收返回信息
            int code = urlConn.getResponseCode();
            LogUtils.v("post------------- code = " + code);
            if (code != 200) {
                urlConn.disconnect();
                Exception exception = new Exception();
                throw exception;
            } else {
                br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String result = "";
                String line = null;
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                LogUtils.v("post------------- result = " + result);
                return result;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (urlConn != null) {
                    urlConn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * HTTPS检验
     */
    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            LogUtils.v("post-------------RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }
    }


}
