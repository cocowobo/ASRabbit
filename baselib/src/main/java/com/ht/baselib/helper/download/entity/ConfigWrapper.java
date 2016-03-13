
package com.ht.baselib.helper.download.entity;

import com.ht.baselib.helper.download.constants.NetType;
import com.ht.baselib.helper.download.interfaces.IConfig;

import java.util.Hashtable;

/**
 * Msg:配置参数值，在生成FileDownloader对象或调用setConfig时会赋值
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public final class ConfigWrapper {

    private int blockSize;

    private int taskNum;

    private boolean isRange;

    private boolean isBlock;

    private NetType netType;

    private boolean cmwap;

    private long refreshInterval;

    private int bufferBlockNum;

    private static ConfigWrapper instance;

    private IConfig config;

    private Hashtable<String, String> requestHeaders;

    private boolean is2GNeedToForceBlock;

    private ConfigWrapper() {

    }

    /**
     * @return 配置参数实体
     */
    public static synchronized ConfigWrapper getInstance() {
        if (instance == null) {
            instance = new ConfigWrapper();
        }
        return instance;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public boolean isRange() {
        return isRange;
    }

    public void setRange(boolean isRange) {
        this.isRange = isRange;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public boolean isCmwap() {
        return cmwap;
    }

    public void setCmwap(boolean cmwap) {
        this.cmwap = cmwap;
    }

    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public long getRefreshInterval() {
        return refreshInterval;
    }

    public int getBufferBlockNum() {
        return bufferBlockNum;
    }

    /**
     * @param bufferBlockNum 数据块大小
     */
    public void setBufferBlockNum(int bufferBlockNum) {
        this.bufferBlockNum = bufferBlockNum;
    }

    /**
     * @return 网络是否可用
     */
    public boolean isNetworkAvalid() {
        if (config != null) {
            return config.isNetworkAvalid();
        } else {
            return true;
        }
    }

    public void setConfig(IConfig config) {
        this.config = config;
    }

    public Hashtable<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Hashtable<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public void setNeedToForceBlock(boolean is2GNeedToForceBlock) {
        this.is2GNeedToForceBlock = is2GNeedToForceBlock;
    }

    /**
     * @return 如果是2G网络，是否强制分块，一般超过100k都要
     */
    public boolean is2GNeedToForceBlock() {
        return is2GNeedToForceBlock;
    }
}
