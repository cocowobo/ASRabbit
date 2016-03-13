
package com.ht.baselib.helper.download.interfaces;

import com.ht.baselib.helper.download.KGHttpResponse;

/**
 * Msg:连网类接口
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public interface IHttpConnector {

    /**
     * 获取响应
     * 
     * @param resUrl 下载URL
     * @return 返回文件输入流
     */
    public KGHttpResponse getHttpResponse(String resUrl) throws Exception;

    /**
     * 获取响应，支持断点续传
     * 
     * @param resUrl 下载URL
     * @param start 起始位置
     * @return
     */
    public KGHttpResponse getHttpResponse(String resUrl, long start) throws Exception;

    /**
     * 获取响应，支持断点续传
     * 
     * @param resUrl 下载URL
     * @param start 起始位置
     * @param end 结束位置
     * @return
     */
    public KGHttpResponse getHttpResponse(String resUrl, long start, long end) throws Exception;

    /**
     * 获取文件长度
     * 
     * @param resUrl 下载URL
     * @return 文件长度
     */
    public long getContentLength(String resUrl) throws Exception;

    /**
     * 关闭连接
     */
    public void close() throws Exception;

}
