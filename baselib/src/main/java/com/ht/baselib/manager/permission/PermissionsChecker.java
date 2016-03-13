package com.ht.baselib.manager.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * <p>PermissionsActivity类 检查权限的工具类</p>
 *
 * @author wangchenlong （from net https://github.com/SpikeKing/wcl-permission-demo）
 * @version 1.0
 *          1.1 update by zmingchun on 2016/2/17
 */
public class PermissionsChecker {
    private final Context mContext;

    /**
     * 构造器
     * @param context 上下文
     */
    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }
    /**
     * 判断权限集合
     * @param permissions 权限集合
     * @return
     */
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     * @param permission 权限码
     * @return
     */
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
