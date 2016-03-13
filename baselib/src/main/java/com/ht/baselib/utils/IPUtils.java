package com.ht.baselib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * <p>IPUtils类,获取ip地址</p>
 *
 * @author wuzuweng<br/>
 * @version 1.0 (15/11/24 下午2:30)<br/>
 */
public class IPUtils {

    /**
     * 获取ip地址
     *
     * @param ctx 上下文
     * @return
     */
    public static String getIP(Context ctx) {
        String ip = "";
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            ip = getWifiIP(wifiManager);
        } else {
            ip = getGPRSIP();
        }
        return ip;
    }

    /**
     * 获取wifi的ip地址
     *
     * @param wifiManager wifi服务
     * @return
     */
    private static String getWifiIP(WifiManager wifiManager) {

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        if (ip != null) {
            return ip;
        }

        return "";
    }

    /**
     * 转换ip地址
     *
     * @param i ip地址值
     * @return
     */
    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * 获取gprs的ip地址
     *
     * @return
     */
    private static String getGPRSIP() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {

        }
        return "";
    }

}
