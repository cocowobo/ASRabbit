
package com.ht.baselib.helper.download.entity;

import com.ht.baselib.helper.download.constants.NetType;
import com.ht.baselib.helper.download.interfaces.IConfig;

import java.util.Hashtable;

/**
 * Msg:默认配置类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DefaultConfig implements IConfig {

    private final int DEFAULT_BLOCK_SIZE = 100 * 1024;

    private final int DEFAULT_DOWNLOADTASK_NUM = 2;

    private final int DEFAULT_REFRESH_INTERVAL = 300;

    @Override
    public int getBlockSize() {
        return DEFAULT_BLOCK_SIZE;
    }

    @Override
    public int getTaskNum() {
        return DEFAULT_DOWNLOADTASK_NUM;
    }

    @Override
    public boolean isRange() {
        return true;
    }

    @Override
    public boolean isBlock() {
        return false;
    }

    @Override
    public NetType getNetType() {
        return NetType.WIFI;
    }

    @Override
    public boolean isCmwap() {
        return false;
    }

    @Override
    public long getRefreshInterval() {
        return DEFAULT_REFRESH_INTERVAL;
    }

    @Override
    public int getBufferBlockNum() {
        return getTaskNum();
    }

    @Override
    public boolean isNetworkAvalid() {
        return true;
    }

    @Override
    public Hashtable<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    public boolean is2GNeedToForceBlock() {
        return true;
    }

}
