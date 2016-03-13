package com.adolsai.asrabbit.app;

import com.adolsai.asrabbit.BuildConfig;
import com.ht.baselib.base.BaseApplication;
import com.ht.baselib.utils.LogUtils;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

/**
 * <p>AsrabbitApplication类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/1 9:13)<br/>
 */
public class AsRabbitApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initAppConfig();
    }

    /**
     * 初始化应用设置
     */
    private void initAppConfig() {
        //初始化日志LogUtils
        LogUtils.isDebug = BuildConfig.IS_SHOW_LOG;
        /**初始化键值加密存储工具*/
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setPassword(BuildConfig.ENCRYPT_SEED)
                .setStorage(HawkBuilder.newSharedPrefStorage(this)) //以SharedPreferences方式存储
                .setLogLevel(LogLevel.FULL)
                .build();
    }

}
