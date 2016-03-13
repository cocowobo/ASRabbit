
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.entity.ConfigWrapper;

/**
 * Msg:数据统计类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class Statistics {

    private long startTime;

    private long finishTime;

    private long receivedLen;

    private long refreshTime;

    private long downloadLen;

    private int currentSpeed;

    private StringBuilder errorMsg = new StringBuilder();

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        this.refreshTime = startTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    synchronized void addReceivedLen(long readLen) {
        this.receivedLen += readLen;
    }

    public void setDownloadLen(long downloadLen) {
        this.downloadLen = downloadLen;
    }

    synchronized boolean canNotify() {
        long duration = System.currentTimeMillis() - refreshTime;
        // 根据配置的刷新频率决定是否通知客户端刷新
        boolean canNotify = duration > ConfigWrapper.getInstance().getRefreshInterval();
        if (canNotify) {
            currentSpeed = (int) (receivedLen / duration);
            refreshTime = System.currentTimeMillis();
            receivedLen = 0;
        }
        return canNotify;
    }

    /**
     * 下载开始时间
     * 
     * @return
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 下载用时
     * 
     * @return
     */
    public long getDownloadTime() {
        long duration = finishTime - startTime;
        return duration < 0 ? 0 : duration;
    }

    /**
     * 下载大小
     * 
     * @return
     */
    public long getDownloadSize() {
        return downloadLen;
    }

    /**
     * 当前下载速度
     * 
     * @return
     */
    public synchronized int getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * 平均速度
     * 
     * @return
     */
    public int getDownloadSpeed() {
        long duration = finishTime - startTime;
        int speed = 0;
        if (duration > 0) {
            speed = (int) (downloadLen / duration);
        }
        return speed;
    }

    public StringBuilder getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("downloadSize=" + getDownloadSize());
        sb.append(" currentSpeed=" + getCurrentSpeed());
        sb.append(" downloadTime=" + getDownloadTime());
        sb.append(" downloadSpeed=" + getDownloadSpeed());
        return sb.toString();
    }

    /**
     * 获取下载异常报告
     * 
     * @param e
     * @return
     */
    public static String getCrashReport(Exception e) {
        StringBuffer exceptionStr = new StringBuffer();
        if (e != null) {
            exceptionStr.append(e.toString() + "[n]");
            StackTraceElement[] elements = e.getStackTrace();
            if (elements != null) {
                for (int i = 0; i < elements.length; i++) {
                    if (elements[i] != null) {
                        exceptionStr.append(elements[i].toString() + "[n]");
                    }
                }
            }
        } else {
            exceptionStr.append("no exception. [n]");
        }
        // \n \t _ \r 替换成 [n] [t] [_] [r]
        return exceptionStr.toString();
    }
}
