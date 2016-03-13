
package com.ht.baselib.helper.download.interfaces;

/**
 * Msg:流量监听接口
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public interface IBytesListener {

    /**
     * 流量监听
     * 
     * @param receivedLength 接收到的数据长度，单位字节
     */
    public void onBytesReceived(long receivedLength);
}
