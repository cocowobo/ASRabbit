
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.entity.BlockedDownloadFile;
import com.ht.baselib.helper.download.entity.ConfigWrapper;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IHttpConnector;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.LogEx;

/**
 * Msg:下载线程，有三个子类{@link NormalDownloadTask
 * ,RangeBlockDownloadTask,RangeNotBlockDownloadTask}
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public abstract class AbstractDownloadTask extends Thread {

    protected IHttpConnector httpConnector;

    protected DownloadFile downloadFile;

    protected FileAccess fileAccess;

    protected IProgressListener progressListener;

    protected FileDownloader downloader;

    protected IOperator operator;

    protected boolean stop;

    /**
     * 构造函数
     * 
     * @param fileAccess 文件操作类
     * @param progressListener 下载进度监听器
     * @param downloader 下载类
     * @param operator 下载信息保存类
     * @param downloadFile 文件
     */
    public AbstractDownloadTask(FileAccess fileAccess, IProgressListener progressListener,
            FileDownloader downloader, IOperator operator, DownloadFile downloadFile) {
        this.fileAccess = fileAccess;
        this.progressListener = progressListener;
        this.downloader = downloader;
        this.operator = operator;
        this.downloadFile = downloadFile;
        this.httpConnector = createHttpConnector(false);
    }

    /**
     * 停止下载
     */
    public void stopDownload() {
        stop = true;
        // 这个可能会很耗时
        // try {
        // httpConnector.close();
        // } catch (Exception e) {
        // }
    }

    /**
     * 失败计数清零
     */
    protected void clearFaileCounter() {
        downloader.resetTryNum();
    }

    /**
     * 失败计数，达到最大尝试次数时会停止所有下载线程
     */
    protected void addFaileCounter() {
        LogEx.d(getName() + ": ------重试--------");
        downloader.addTryNum();
    }

    /**
     * 因网络中断而停止下载
     */
    protected void stopByNetError() {
        LogEx.d(getName() + ": ------stopByNetError--------");
        downloader.stopByNetError();
    }

    /**
     * 因文件不存在而停止下载
     */
    protected void stopByFileNotFound() {
        LogEx.d(getName() + ": ------stopByFileNotFound--------");
        downloader.stopByFileNotFound();
    }

    /**
     * 服务器断点数据有误
     */
    protected void stopByServerRangeError() {
        LogEx.d(getName() + ": ------stopByServerRangeError--------");
        downloader.stopByRangeError();
    }

    /**
     * 服务器断点数据有误
     */
    protected void stopByContentError() {
        LogEx.d(getName() + ": ------stopByContentError--------");
        downloader.stopByContentError();
    }

    private long preTime;

    /**
     * 文件保存监听器
     */
    protected FileAccess.FileSaveProgressListener mListener = new FileAccess.FileSaveProgressListener() {

        @Override
        public void onProgressChanged(long savedLength) {
            Statistics statis = downloadFile.getStatis();
            statis.addReceivedLen(savedLength);
            // 进度设置
            if (downloadFile instanceof BlockedDownloadFile) {
                // 分块下载通知的haveRead是连续块的字节数
                BlockedDownloadFile blockedDownloadFile = (BlockedDownloadFile) downloadFile;
                int index = blockedDownloadFile.getBufferedIndex() + 1;
                long bufferedRead = index * blockedDownloadFile.getBlockSize();
                if (bufferedRead >= blockedDownloadFile.getFileSize()) {
                    bufferedRead = blockedDownloadFile.getFileSize();
                }
                blockedDownloadFile.setHaveRead(bufferedRead);
            } else {
                downloadFile.addHaveRead(savedLength);
            }
            // 如果下载时间超过十秒，就更新一次数据库数据
            long curTime = System.currentTimeMillis();
            if (preTime == 0) {
                preTime = curTime;
            }
            if ((curTime - preTime) >= 10000) {
                operator.updateFile(downloadFile);
                preTime = curTime;
            }
            if (statis.canNotify()) {
                notifyProgress(downloadFile, FileDownloader.DOWNLOADING);
            }
        }
    };

    /**
     * 通知进度
     * 
     * @param file 文件下载实体
     * @param state 下载状态，查看FileDownloader中的几种下载状态
     */
    protected void notifyProgress(DownloadFile file, int state) {
        synchronized (progressListener) {
            if (!stop && progressListener != null) {
                // 通知进度
                progressListener.onProgressChanged(file, state);
            }
        }
    }

    /**
     *
     * @param isError  出错重新创建时，isError需要设置为true，避免手机网络状态出错比如设成了cmwap，导致httpConnector被设置了代理而下载失败
     * @return 网络连接
     */
    protected IHttpConnector createHttpConnector(boolean isError) {
        boolean isCmwap = ConfigWrapper.getInstance().isCmwap();
        boolean isReallyCmwap = isCmwap && !isError;
        return DefaultHttpConnectorFactory.create(isReallyCmwap);
    }

    /**
     *
     *
     * @param msg 用来提醒用户的一些相关信息
     * @return 该线程和下载文件相关的一些信息
     */
    protected String getMessage(String msg) {
        return getName() + " <-> " + downloadFile.getFileName() + ": " + msg;
    }
}
