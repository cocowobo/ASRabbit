
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.LogEx;

/**
 * Msg:默认下载进度监听器
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DefaultProgressListener implements IProgressListener {

    private long t1, t2;

    @Override
    public void onProgressChanged(DownloadFile file, int state) {
        String status;
        switch (state) {
            case FileDownloader.READY:
                t1 = System.currentTimeMillis();
                status = "准备就绪";
                break;
            case FileDownloader.DOWNLOADING:
                status = "正在下载";
                break;
            case FileDownloader.FINISH:
                t2 = System.currentTimeMillis();
                status = "下载完成";
                print("下载用时：" + (t1 > 0 ? (t2 - t1) : -1));
                break;
            case FileDownloader.INTERUPT:
                status = "用户中断下载";
                break;
            default:
                status = "未知状态";
                break;
        }
        print("当前状态：" + status + " " + file.toString());
    }

    @Override
    public void onError(DownloadFile file, int errorType) {
        String error;
        switch (errorType) {
            case FileDownloader.CREATE_FILE_ERROR:
                error = "创建文件出错";
                break;
            case FileDownloader.TIMEOUT_ERROR:
                error = "下载超时";
                break;
            case FileDownloader.NETWORK_ERROR:
                error = "网络中断";
                break;
            case FileDownloader.FILE_NOT_FOUND:
                error = "文件不存在";
                break;
            default:
                error = "未知错误";
                break;
        }
        print("出错原因：" + error + " " + file.toString() + " errorType=" + errorType);
    }

    private void print(Object obj) {
        LogEx.d("state:" + obj.toString());
    }

}
