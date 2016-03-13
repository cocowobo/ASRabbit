
package com.ht.baselib.helper.download.interfaces;

import java.util.Hashtable;

import com.ht.baselib.helper.download.constants.NetType;

/**
 * Msg:下载配置接口
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public interface IConfig {

    /**
     * 块大小
     * 
     * @return
     */
    public int getBlockSize();

    /**
     * 下载线程数
     * 
     * @return
     */
    public int getTaskNum();

    /**
     * 是否支持断点续传
     * 
     * @return
     */
    public boolean isRange();

    /**
     * 是否需要分块下载
     * 
     * @return
     */
    public boolean isBlock();

    /**
     * 获取网络类型
     * 
     * @return 返回{@link NetType}
     */
    public NetType getNetType();

    /**
     * 是否cmwap网络
     * 
     * @return
     */
    public boolean isCmwap();

    /**
     * 刷新频率
     * 
     * @return
     */
    public long getRefreshInterval();

    /**
     * 缓冲块数量
     * 
     * @return
     */
    public int getBufferBlockNum();

    /**
     * 网络是否可用
     * 
     * @return
     */
    public boolean isNetworkAvalid();

    /**
     * 头部参数
     * 
     * @return
     */
    public Hashtable<String, String> getRequestHeaders();

    /**
     * 2G网络是否需要强制转成分块下载
     * 
     * @return
     */
    public boolean is2GNeedToForceBlock();

}
