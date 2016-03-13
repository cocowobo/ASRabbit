package com.ht.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>应用程序工具类 1、提供查找App信息的方法；</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class AppUtils {

    /**
     * 获取包名
     *
     * @param ctx 上下文
     * @return 包名
     */
    public static String getPackageName(Context ctx) {
        final String packageName = ctx.getPackageName();
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(
                    packageName, 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
    private static String versionNameCache;
    /**
     * 获取版本名称，这个值在报文头里面经常用，缓存一下
     *
     * @param ctx 上下文
     * @return 版本名称
     */
    public static synchronized  String getVersionName(Context ctx) {
        if(versionNameCache==null){
            final String packageName = ctx.getPackageName();
            try {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(
                        packageName, 0);
                versionNameCache= info.versionName;
            } catch (Exception e) {
                return "";
            }
        }

        return versionNameCache;
    }
    private static int versionCodeCache;
    /**
     * 获取版本号，这个值在报文头里面经常用，缓存一下
     *
     * @param ctx 上下文
     * @return 版本号
     */
    public static synchronized int getVersionCode(Context ctx) {
        if(versionCodeCache<=0){
            final String packageName = ctx.getPackageName();
            try {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(
                        packageName, 0);
                versionCodeCache= info.versionCode;
            } catch (Exception e) {
                return 0;
            }
        }

        return versionCodeCache;
    }


    /**
     * 获取系统版本，如1.5,2.1
     *
     * @return 系统版本
     */
    public static String getSysVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本号
     *
     * @return SDK版本号
     */
    public static int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取安装包信息
     *
     * @param ctx      上下文
     * @param filePath 安装包路径
     * @return 安装包信息
     */
    public static PackageInfo getPackageInfo(Context ctx, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(filePath, 0);
            return pi;
        } else {
            return null;
        }
    }


    /**
     * 获取应用名称
     *
     * @param ctx         上下文
     * @param packageName 包名
     * @return 应用名称
     */
    public static String getAppName(Context ctx, String packageName) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = ctx.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }


    /**
     * 获取当前语言
     *
     * @param ctx 上下文
     * @return 当前语言
     */
    public static String getLanguage(Context ctx) {
        if (ctx != null) {
            return ctx.getResources().getConfiguration().locale.getLanguage();
        }
        return null;
    }


    /**
     * 创建桌面快捷方式
     *
     * @param ctx           上下文
     * @param shortCutName  快捷方式名
     * @param iconId        快捷方式图标资源ID
     * @param presentIntent 快捷方式激活的activity，需要执行的intent，自己定义
     */
    public static void createShortcut(Context ctx, String shortCutName,
                                      int iconId, Intent presentIntent) {
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra("duplicate", false);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(ctx, iconId));
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, presentIntent);

        ctx.sendBroadcast(shortcutIntent);
    }


    /**
     * 对外分享内容
     *
     * @param ctx   上下文
     * @param title 对外分享的主题
     * @param text  对外分享的内容
     */
    public static void shareText(Context ctx, String title, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        List<ResolveInfo> ris = getShareTargets(ctx);
        if (ris != null && ris.size() > 0) {
            ctx.startActivity(Intent.createChooser(intent, title));
        }
    }

    /**
     * 获取手机里具有"分享"功能的所有应用列表
     *
     * @param ctx 上下文
     * @return 手机里具有"分享"功能的所有应用列表
     */
    public static List<ResolveInfo> getShareTargets(Context ctx) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pm = ctx.getPackageManager();
        mApps = pm.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }
    private static String umengChannelCache;

    /**
     * 获取渠道号
     * @param context 上下文
     * @return
     */
    public static synchronized String getUmengChannel(Context context){
        if(umengChannelCache==null){
            if(context!=null){
                try {
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
                    // 请注意这个字段必须和Androidmanifest.xml文件里面的一致
                    umengChannelCache=applicationInfo.metaData.getString("UMENG_CHANNEL");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return umengChannelCache;
    }
}
