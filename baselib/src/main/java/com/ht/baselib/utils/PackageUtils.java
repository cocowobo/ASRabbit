package com.ht.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * <p>安装包信息工具类</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class PackageUtils {
    /**
     * 判断是否具有某种权限,参数不合法会返回false,只有真正有相应权限才会返回true
     *
     * @param context    上下文
     * @param permission 权限
     * @return true 有权限， false 无权限
     */
    public static boolean hasPermission(Context context, String permission) {
        if (context != null && !TextUtils.isEmpty(permission)) {
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(permission, context
                            .getPackageName())) {
                        return true;
                    }
                    LogUtils.d("Have you  declared permission " + permission + " in AndroidManifest.xml ?");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 判断应用是否已经安装
     *
     * @param context     上下文对象
     * @param packageName 包名
     * @return packageName代表的app存在，返回true.否则返回false
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (StringUtils.isBlank(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return info != null;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 正常安装
     *
     * @param context  上下文
     * @param filePath file path of package
     * @return true 成功，false 失败
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * 正常卸载
     *
     * @param context     上下文
     * @param packageName package name of app
     * @return true 成功，false 失败
     */
    public static boolean uninstallNormal(Context context, String packageName) {
        int stringBuilderLength = 32;
        if (packageName == null || packageName.length() == 0) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(stringBuilderLength).append
                ("package:").append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * 判断应用是否是系统应用
     *
     * @param context 上下文
     * @return true 系统应用，false 非系统应用
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * 根据包名，判断应用是否为系统应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true 系统应用，false 非系统应用
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * 根据包名，判断应用是否为系统应用
     *
     * @param packageManager 包管理者
     * @param packageName    包名
     * @return rue 系统应用，false 非系统应用
     */
    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
