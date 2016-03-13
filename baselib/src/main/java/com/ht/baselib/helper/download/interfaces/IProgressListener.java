
package com.ht.baselib.helper.download.interfaces;

import com.ht.baselib.helper.download.entity.DownloadFile;

/**
 * Msg: 下载进度监听接口
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public interface IProgressListener {

    /**
     * 下载进度变化
     * 
     * @param file 下载文件
     * @param state 当前状态({@link com.ht.baselib.helper.download.FileDownloader#READY
     *            FileDownloader#DOWNLOADING...} )
     */
    public void onProgressChanged(DownloadFile file, int state);

    /**
     * 下载出错
     * 
     * @param file 下载文件
     * @param errorType 错误类型 {@link com.ht.baselib.helper.download.FileDownloader#CREATE_FILE_ERROR ...}
     */
    public void onError(DownloadFile file, int errorType);

}
