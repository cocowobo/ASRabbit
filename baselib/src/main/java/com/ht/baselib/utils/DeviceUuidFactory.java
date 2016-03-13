package com.ht.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

/**
 * <p>
 * 设备唯一标识UUID生成器
 * <br/>用于生成设备唯一标识,避免无法获取deviceId,androidId的问题
 * <p/>
 * @author chenchao
 * @version 1.0 (2015-10-22)
 */
public class DeviceUuidFactory {
    /**本地缓存文件*/
    private static final String PREFS_FILE = "device_uuid.xml";
    /**本地缓存文件中uuid对应key*/
    private static final String PREFS_DEVICE_UUID = "device_uuid";
    /**设备唯一标识*/
    private static String uuid;

    /**
     * 按照规则生成设备唯一标识
     *<br/>UUID格式形式例如:
     * 550E8400-E29B-11D4-A716-446655440000
     * @param context 上下文对象
     * @return uuid
     */
    public static synchronized String generateUUID(Context context) {
        if (TextUtils.isEmpty(uuid)) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
            String id = prefs.getString(PREFS_DEVICE_UUID, null);

            if (!TextUtils.isEmpty(id)) {
                // Use the ids previously computed and stored in the prefs file
                uuid = UUID.fromString(id).toString();
            } else {
                // Use the deviceIdv unless it's broken, in which case
                // fallback on Android ID,
                // unless it's not available, then fallback on a random
                // number which we store
                // to a prefs file

                try {
                    String deviceId = DeviceUtils.getDeviceID(context);
                    if(!TextUtils.isEmpty(deviceId)) {
                        uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString();
                    } else {
                        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        if(!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e("generateUUID exception", e.toString());
                }

                // 获取deviceId,androidId失败，则根据UUID算法随机生成UUID
                if(TextUtils.isEmpty(uuid)) {
                    uuid = UUID.randomUUID().toString();
                }

                // Write the value out to the prefs file
                prefs.edit().putString(PREFS_DEVICE_UUID, uuid).commit();
            }
        }
        return uuid;
    }
}
