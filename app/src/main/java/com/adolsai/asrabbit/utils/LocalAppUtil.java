package com.adolsai.asrabbit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.adolsai.asrabbit.activity.HomeActivity;
import com.ht.baselib.base.BaseApplication;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.StringUtils;
import com.ht.baselib.views.imageselector.MultiImageBrowserActivity;
import com.ht.baselib.views.imageselector.bean.Image;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Msg: 一些公共操作方法，还不确定要放进base，以后再讨论是否放进base的apputil里
 * Update:  2015-11-26
 * Version: 1.0
 * Created by laijiacai on 2015-11-26 17:22.
 */

public class LocalAppUtil {
    /**
     * 检查应用是否已安装（包名一致即可，这里不检查版本）
     *
     * @param context     上下文
     * @param packageName 包名
     * @return
     */
    public static boolean checkAppExistSimple(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            if (pi != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取本地ip
     *
     * @param context 上下文
     * @return
     */
    public static String getLocalIp(Context context) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 是不是一个网址
     *
     * @param url 网址
     * @return
     */
    public static boolean isNetWorkUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
                + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
                + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
                + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
                + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
        return url.matches(regEx);
    }

    /**
     * 判断是否完整的URL
     *
     * @param strUrl 待检查字符串
     * @return true是，false 否
     */
    public static boolean isUrl(String strUrl) {
        if (StringUtils.isBlank(strUrl)) {
            return false;
        }
        String strPattern = "^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strUrl);
        return m.matches();
    }

    /**
     * 把分转为元，保留两位小数
     *
     * @param money 钱数量，分为单位
     * @return
     */
    public static String turnFenToYuan(int money) {
        StringBuilder sb = new StringBuilder();
        sb.append(money / 100 + "." + money % 100 / 10 + money % 100 % 10);
        return sb.toString();
    }

    /**
     * @param context   上下文
     * @param videoPath 要播放的视频文件所在路径
     */
    public static void playVideo(Context context, String videoPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri uri = Uri.fromFile(new File(videoPath));
        intent.setDataAndType(uri, "video/*");
        context.startActivity(intent);
    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception 异常
     */
    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
                new java.io.FileInputStream(zipFileString));
        java.util.zip.ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {

                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator
                        + szName);
                folder.mkdirs();

            } else {

                File file = new File(outPathString + File.separator
                        + szName);

                file.createNewFile();
                // get the output stream of the file
                java.io.FileOutputStream out = new java.io.FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }// end of while
        inZip.close();
    }

    /**
     * @param activity activity
     * @param images   图片对象列表
     */
    public static void startMultiImageBrowserActivity(Activity activity, ArrayList<Image> images) {
        if (images.size() > 0) {
            // 打开图片浏览器
            Intent browserIntent = new Intent(activity, MultiImageBrowserActivity.class);
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_BROWSER_TYPE, MultiImageBrowserActivity.BROWSER_TYPE_PREVIEW);
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_IMAGES, images);
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_CUR_POSITION, 0);
            ActivityUtil.startActivityByAnim(activity, browserIntent);
        }
    }

    /**
     * 去除html代码
     *
     * @param content 原始内容
     * @return 过滤后内容
     */
    public static String trimHtmlStyle(String content) {
        String result = clearHTMLToString(content);
        return splitAndFilterString(result, 50);
    }

    /**
     * 去掉字符串里面的html代码
     *
     * @param content 原内容
     * @return 去掉后的内容
     */
    public static String clearHTMLToString(String content) {
        if (null == content || "".equals(content)) {
            return "";
        }
        content = content.replaceAll("width&gt;this", "");//去除特定的字符

        // 去掉其它的<>之间的东西
        content = content.replaceAll("&lt;(.*?)&gt;", "");
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\s*|\t|\r|\n", "");
        content = content.replaceAll("&nbsp;", " ");
        content = content.replaceAll("nbsp;", " ");
        content = content.replaceAll("&amp;", "");
        return content;
    }

    /**
     * 删除input字符串中的html格式
     *
     * @param input  输入内容
     * @param length 长度
     * @return
     */
    public static String splitAndFilterString(String input, int length) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
                "<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        int len = str.length();
        if (len <= length) {
            return str;
        } else {
            str = str.substring(0, length);
            str += "......";
        }
        return str;
    }

    /**
     * 是否mainactivity已经启动
     *
     * @return
     */
    public static boolean isContainMainActivity() {
        ArrayList<Activity> activities = BaseApplication.mActivityList;
        if (activities != null && activities.size() > 0) {
            for (Activity a : activities) {
                if (a instanceof HomeActivity) {
                    return true;
                }
            }
        }
        return false;
    }

}
