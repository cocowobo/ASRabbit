package com.ht.baselib.manager;

import android.content.Context;
import android.util.Log;

import com.ht.baselib.helper.download.android.DefaultDownloadConfig;
import com.ht.baselib.helper.download.android.IDownloadService;
import com.ht.baselib.helper.download.android.IDownloadServiceDestoryListener;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.entity.ParamsWrapper;
import com.ht.baselib.helper.download.interfaces.IConfig;
import com.ht.baselib.helper.download.interfaces.ILog;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.manager.DownloadInternalManager;
import com.ht.baselib.helper.download.util.LogEx;

import java.util.ArrayList;

/**
 * Msg: 下载方法调用类，这是个单例
 * Update:  2015-11-6
 * Version: 1.0
 * Created by laijiacai on 2015-11-6 14:39.
 */

public final class DownloadManager implements IDownloadService, DownloadInternalManager.OnDownloadingCountChangeListener {

    private static DownloadManager instance;

    private DownloadInternalManager mDownloadInternalManager;

    private IDownloadServiceDestoryListener mDestoryListener;

    private static ArrayList<IProgressListener> sWattingTasks = new ArrayList<IProgressListener>();

    /**
     *
     * @return 获取下载实体
     */
    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    private DownloadManager() {

    }

    /**
     * 在退出app时记得调用 onDestory()方法
     * @param context     上下文
     * @param operator    下载信息保存类
     * @param isDebug     是否打印log
     * @param config      设置下载参数配置
     * @param needSaveLog 是否保存日志在sdcard
     * @param logFilePath 保存在sdcard的日志文件路径
     */
    public void init(Context context, IOperator operator, boolean isDebug, IConfig config, boolean needSaveLog, String logFilePath) {
        mDownloadInternalManager = new DownloadInternalManager(operator, false);
        mDownloadInternalManager.setOnDownloadingCountChangeListener(this);
        if (config == null) {
            config = new DefaultDownloadConfig(context.getApplicationContext());
        }
        mDownloadInternalManager.setConfig(config);

        // 设置使用android日志类输出日志信息。
        LogEx.setCustomLog(new ILog() {
            @Override
            public void d(String tag, String msg) {
                Log.d(tag, msg);
            }
        });
        // 设置是否保存日志在sdcard
        LogEx.setSaveLog(needSaveLog);
        // 设置日志文件路径
        LogEx.setLogFilePath(logFilePath);
    }

    /**
     * 退出app时候调用
     */
    public void onDestory() {
        mDownloadInternalManager.stopAll();
        if (mDestoryListener != null) {
            mDestoryListener.onDownloadServiceDestory();
        }
    }

    @Override
    public boolean download(String resUrl, String filePath, IProgressListener listener) {
        return mDownloadInternalManager.download(resUrl, filePath, listener);
    }

    @Override
    public boolean download(String resUrl, String filePath, int classId,
                            IProgressListener listener) {
        return mDownloadInternalManager.download(resUrl, filePath, classId, listener);
    }

    @Override
    public boolean download(String key, String resUrl, String filePath, int classId,
                            IProgressListener listener) {
        return mDownloadInternalManager.download(key, resUrl, filePath, classId, listener);
    }

    @Override
    public boolean download(String key, String resUrl, String filePath, long fileSize,
                            int classId, IProgressListener listener) {
        return mDownloadInternalManager.download(key, resUrl, filePath, fileSize, classId, listener);
    }

    @Override
    public boolean download(String key, String resUrl, String filePath, String fileName,
                            long fileSize, int classId, IProgressListener listener) {
        return mDownloadInternalManager.download(key, resUrl, filePath, fileName, fileSize, classId,
                listener);
    }

    @Override
    public boolean download(ParamsWrapper paramsWrapper, IProgressListener listener) {
        return mDownloadInternalManager.download(paramsWrapper, listener);
    }

    @Override
    public boolean addToWaittingQueue(ParamsWrapper paramsWrapper, IProgressListener listener) {
        return mDownloadInternalManager.addToWaittingQueue(paramsWrapper, listener);
    }

    @Override
    public void stopDownload(String key) {
        mDownloadInternalManager.stopDownload(key);
    }

    @Override
    public void removeFromWaittingQueue(String key) {
        mDownloadInternalManager.removeFromWaittingQueue(key);
    }

    @Override
    public boolean isDownloading(String key) {
        return mDownloadInternalManager.isDownloading(key);
    }

    @Override
    public boolean isInWaittingQueue(String key) {
        return mDownloadInternalManager.isInWaittingQueue(key);
    }

    @Override
    public DownloadFile getDownloadFile(String key) {
        return mDownloadInternalManager.getDownloadFile(key);
    }

    @Override
    public int getProgress(String key) {
        return mDownloadInternalManager.getProgress(key);
    }

    @Override
    public void removeListener(IProgressListener listener) {
        mDownloadInternalManager.removeListener(listener);
    }

    @Override
    public void addListener(IProgressListener listener) {
        mDownloadInternalManager.addListener(listener);
    }

    @Override
    public void setGolbalListener(IProgressListener listener) {
        mDownloadInternalManager.setGlobalProgressListener(listener);
    }

    @Override
    public void cancelDownload(DownloadFile downloadFile, boolean realDelete) {
        mDownloadInternalManager.cancelDownload(downloadFile, realDelete);
    }

    @Override
    public void stopAll() {
        mDownloadInternalManager.stopAll();
    }

    @Override
    public void setOnDownloadServiceDestoryListener(IDownloadServiceDestoryListener listener) {
        mDestoryListener = listener;
    }

    @Override
    public DownloadFile getDownloadFileFromCache(String key) {
        return mDownloadInternalManager.getDownloadFileFromCache(key);
    }

    @Override
    public int getDownloadingSize() {
        return mDownloadInternalManager.getDownloadingSize();
    }

    @Override
    public int getWaittingSize() {
        return mDownloadInternalManager.getWaittingSize();
    }

    @Override
    public void setMaxDownloadingNum(int maxDownloadingNum) {
        mDownloadInternalManager.setMaxDownloadingNum(maxDownloadingNum);
    }

    @Override
    public ArrayList<DownloadFile> getDownloadingFiles() {
        return mDownloadInternalManager.getDownloadingFiles();
    }

    @Override
    public ArrayList<DownloadFile> getWaittingFiles() {
        return mDownloadInternalManager.getWaittingFiles();
    }

    @Override
    public ArrayList<DownloadFile> getDownloadFilesFromCache() {
        return mDownloadInternalManager.getDownloadFilesFromCache();
    }

    @Override
    public void onDownloadingCountChange(int downloadingCount, int waittingCount) {

    }
}
