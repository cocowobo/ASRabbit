
package com.ht.baselib.helper.download.manager;

import com.ht.baselib.helper.download.FileDownloader;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.entity.ParamsWrapper;
import com.ht.baselib.helper.download.interfaces.IConfig;
import com.ht.baselib.helper.download.constants.FileColumns;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.LogEx;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Msg:下载管理器
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class DownloadInternalManager {

    /**
     * 下载数量监听器
     */
    public interface OnDownloadingCountChangeListener {

        /**
         * 下载数变化
         *
         * @param downloadingCount 正在下载的数量
         * @param waittingCount    正在等待下载的数量
         */
        void onDownloadingCountChange(int downloadingCount, int waittingCount);
    }

    /**
     * 通知下载进度
     */
    public static final int NOTIFY_PROGRESS = 1;

    /**
     * 通知下载出错
     */
    public static final int NOTIFY_ERROR = 2;

    private ExecutorService mExecutorService;

    // 正在下载的文件
    private HashMap<String, FileDownloader> mDownloadingSet;

    // 正在等待下载的文件
    private Queue<WaitingTask> mWaittingQueue;

    // 文件下载信息保存类（操作数据库）
    private IOperator mOperator;

    private IConfig mConfig;

    private int mMaxDownloadingNum;

    private ArrayList<IProgressListener> mListeners;

    private IProgressListener mGlobalListener;

    private HashMap<String, DownloadFile> mDownloadFileCache;

    private OnDownloadingCountChangeListener mOnDownloadingCountChangeListener;

    /**
     * 下载管理器
     *
     * @param operator 下载信息保存类
     * @param isDebug  是否打印下载信息
     */
    public DownloadInternalManager(IOperator operator, boolean isDebug) {
        // 下载内核是否打印下载信息
        LogEx.setDebug(isDebug);

        mExecutorService = Executors.newCachedThreadPool();
        mDownloadingSet = new HashMap<String, FileDownloader>() {
            @Override
            public FileDownloader put(String key, FileDownloader value) {
                FileDownloader downloader = super.put(key, value);
                notifyOnDownloadingCountChange();
                return downloader;
            }

            @Override
            public FileDownloader remove(Object key) {
                FileDownloader downloader = super.remove(key);
                notifyOnDownloadingCountChange();
                return downloader;
            }

            @Override
            public void putAll(Map<? extends String, ? extends FileDownloader> m) {
                super.putAll(m);
                notifyOnDownloadingCountChange();
            }

            @Override
            public void clear() {
                super.clear();
                notifyOnDownloadingCountChange();
            }

        };
        mWaittingQueue = new LinkedList<WaitingTask>() {
            @Override
            public boolean offer(WaitingTask e) {
                boolean r = super.offer(e);
                notifyOnDownloadingCountChange();
                return r;
            }

            @Override
            public boolean remove(Object o) {
                boolean r = super.remove(o);
                notifyOnDownloadingCountChange();
                return r;
            }

            @Override
            public WaitingTask poll() {
                WaitingTask wt = super.poll();
                notifyOnDownloadingCountChange();
                return wt;
            }

            @Override
            public void clear() {
                super.clear();
                notifyOnDownloadingCountChange();
            }
        };
        mListeners = new ArrayList<IProgressListener>();
        mDownloadFileCache = new HashMap<String, DownloadFile>();
        mOperator = operator;
        // 默认支持2个同时下载
        mMaxDownloadingNum = 2;
        if (mOperator == null) {
            throw new IllegalArgumentException("operator can not be null.");
        }

        initDownloadFileState();
    }

    /**
     * 通知回调，下载数量发生变化
     */
    private void notifyOnDownloadingCountChange() {
        if (mOnDownloadingCountChangeListener != null) {
            int downloadingCount = mDownloadingSet.size();
            int waittingCount = mWaittingQueue.size();
            mOnDownloadingCountChangeListener.onDownloadingCountChange(downloadingCount,
                    waittingCount);
        }
    }

    /**
     * 初始化下载文件状态
     */
    private void initDownloadFileState() {
        mDownloadFileCache.clear();
        String selection = FileColumns.IS_DELETE + " != 1 ";
        List<DownloadFile> allFiles = mOperator.queryFile(selection, null);
        if (allFiles != null && allFiles.size() > 0) {
            for (DownloadFile file : allFiles) {
                putDownloadFileCache(file.getKey(), file);
            }
        }
    }

    /**
     * 存储下载文件
     *
     * @param key 文件唯一标识符
     * @param file 文件下载信息实体
     */
    void putDownloadFileCache(String key, DownloadFile file) {
        synchronized (mDownloadFileCache) {
            mDownloadFileCache.put(key, file);
        }
    }

    /**
     * 移除存储下载文件
     *
     * @param key 文件唯一标识符
     */
    void removeDownloadFileCache(String key) {
        synchronized (mDownloadFileCache) {
            mDownloadFileCache.remove(key);
        }
    }

    /**
     *
     * @param maxDownloadingNum 最大下载数
     */
    public void setMaxDownloadingNum(int maxDownloadingNum) {
        if (mMaxDownloadingNum > 0) {
            mMaxDownloadingNum = maxDownloadingNum;
        }
    }

    /**
     * 设置正在下载任务数变化监听接口
     *
     * @param listener 监听器
     */
    public void setOnDownloadingCountChangeListener(OnDownloadingCountChangeListener listener) {
        mOnDownloadingCountChangeListener = listener;
    }

    /**
     * 设置下载参数配置
     *
     * @param config 下载配置
     */
    public void setConfig(IConfig config) {
        mConfig = config;
    }

    /**
     * 设置全局监听器
     *
     * @param listener 监听器
     */
    public void setGlobalProgressListener(IProgressListener listener) {
        mGlobalListener = listener;
    }

    public IProgressListener getGlobalProgressListener() {
        return mGlobalListener;
    }

    /**
     * 下载文件。
     *
     * @param resUrl   下载地址
     * @param filePath 　文件保存路径
     * @param listener 　下载监听器
     */
    public boolean download(String resUrl, String filePath, IProgressListener listener) {
        ParamsWrapper paramsWrapper = new ParamsWrapper();
        paramsWrapper.setKey(resUrl);
        paramsWrapper.setResUrl(resUrl);
        paramsWrapper.setFilePath(filePath);
        paramsWrapper.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

        return download(paramsWrapper, listener);
    }

    /**
     * 下载文件。
     *
     * @param resUrl   下载地址
     * @param filePath 　文件保存路径
     * @param listener 　下载监听器
     * @param classId  　分类
     */
    public boolean download(String resUrl, String filePath, int classId, IProgressListener listener) {
        ParamsWrapper paramsWrapper = new ParamsWrapper();
        paramsWrapper.setKey(resUrl);
        paramsWrapper.setClassId(classId);
        paramsWrapper.setResUrl(resUrl);
        paramsWrapper.setFilePath(filePath);
        paramsWrapper.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

        return download(paramsWrapper, listener);
    }

    /**
     * 下载文件。
     *
     * @param key      文件唯一标识
     * @param resUrl   下载地址
     * @param filePath 　文件保存路径
     * @param listener 　下载监听器
     * @param classId  　分类
     */
    public boolean download(String key, String resUrl, String filePath, int classId,
                            IProgressListener listener) {
        ParamsWrapper paramsWrapper = new ParamsWrapper();
        paramsWrapper.setKey(key);
        paramsWrapper.setClassId(classId);
        paramsWrapper.setResUrl(resUrl);
        paramsWrapper.setFilePath(filePath);
        paramsWrapper.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

        return download(paramsWrapper, listener);
    }

    /**
     * 下载文件。
     *
     * @param key      文件唯一标识
     * @param resUrl   下载地址
     * @param filePath 　文件保存路径
     * @param fileSize 　文件大小
     * @param listener 　下载监听器
     * @param classId  　分类
     */
    public boolean download(String key, String resUrl, String filePath, long fileSize, int classId,
                            IProgressListener listener) {
        ParamsWrapper paramsWrapper = new ParamsWrapper();
        paramsWrapper.setKey(key);
        paramsWrapper.setClassId(classId);
        paramsWrapper.setResUrl(resUrl);
        paramsWrapper.setFilePath(filePath);
        paramsWrapper.setFileSize(fileSize);
        paramsWrapper.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

        return download(paramsWrapper, listener);
    }

    /**
     * 下载文件。
     *
     * @param key      文件唯一标识
     * @param resUrl   下载地址
     * @param filePath 　文件保存路径
     * @param fileName 　文件名
     * @param fileSize 　文件大小
     * @param listener 　下载监听器
     * @param classId  　分类
     */
    public boolean download(String key, String resUrl, String filePath, String fileName,
                            long fileSize, int classId, IProgressListener listener) {
        ParamsWrapper paramsWrapper = new ParamsWrapper();
        paramsWrapper.setKey(key);
        paramsWrapper.setClassId(classId);
        paramsWrapper.setResUrl(resUrl);
        paramsWrapper.setFilePath(filePath);
        paramsWrapper.setFileSize(fileSize);
        paramsWrapper.setFileName(fileName);

        return download(paramsWrapper, listener);
    }

    /**
     * 下载文件。
     *
     * @param paramsWrapper 　下载参数，具体查看{@link ParamsWrapper}
     * @param listener      　下载监听器
     */
    public boolean download(ParamsWrapper paramsWrapper, IProgressListener listener) {
        if (mMaxDownloadingNum > 0) {
            int downloadNum = getDownloadingSize();
            if (downloadNum >= mMaxDownloadingNum) {
                return addToWaittingQueue(paramsWrapper, listener);
            }
        }
        return downloadInternal(paramsWrapper, listener);
    }

    private boolean downloadInternal(ParamsWrapper paramsWrapper, IProgressListener listener) {
        String key = paramsWrapper.getKey();
        if (key == null || "".equals(key)) {
            return false;
        }

        boolean canDownload = false;
        synchronized (mDownloadingSet) {
            if (!mDownloadingSet.containsKey(key)) {
                canDownload = true;
            }
        }
        if (canDownload) {
            final FileDownloader downloader = createTask(paramsWrapper, listener);
            synchronized (mDownloadingSet) {
                if (!mDownloadingSet.containsKey(key)) {
                    mDownloadingSet.put(key, downloader);
                }
            }

            // 开启下载线程
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    downloader.startTask();
                }
            });
        }

        return true;
    }

    /**
     * 调用回调函数
     *
     * @param file       　下载文件
     * @param state      下载状态，它的意义由notifyType类型决定。<br />
     *                   NOTIFY_PROGRESS时，state代表下载状态<br />
     *                   NOTIFY_ERROR时，state代表出错类型
     * @param notifyType 通知类型。NOTIFY_PROGRESS代表通知下载进度，NOTIFY_ERROR代表通知出错原因
     */
    void invokeListeners(DownloadFile file, int state, int notifyType) {
        synchronized (mListeners) {
            int size = mListeners.size();
            LogEx.d("listener size:" + size);
            for (int i = 0; i < size; i++) {
                IProgressListener listener = (IProgressListener) mListeners.get(i);
                if (notifyType == NOTIFY_PROGRESS) {
                    listener.onProgressChanged(file, state);
                } else if (notifyType == NOTIFY_ERROR) {
                    listener.onError(file, state);
                }
            }
        }
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public void addListener(IProgressListener listener) {
        if (listener != null) {
            synchronized (mListeners) {
                if (!mListeners.contains(listener)) {
                    mListeners.add(listener);
                }
            }
        }
    }

    /**
     * 移除监听器
     *
     * @param listener 监听器
     */
    public void removeListener(IProgressListener listener) {
        synchronized (mListeners) {
            if (mListeners.contains(listener)) {
                mListeners.remove(listener);
            }
        }
    }

    /**
     * 添加到等待队列
     *
     * @param paramsWrapper 下载参数包装实体
     * @param listener      下载监听器
     * @return
     */
    public boolean addToWaittingQueue(ParamsWrapper paramsWrapper, IProgressListener listener) {
        boolean isAdd = false;
        synchronized (mWaittingQueue) {
            WaitingTask wt = new WaitingTask();
            wt.pw = paramsWrapper;
            wt.listener = listener;
            if (!mWaittingQueue.contains(wt)) {
                isAdd = mWaittingQueue.offer(wt);
            }
        }
        if (isAdd) {
            // 向数据库插入一条数据
            createTask(paramsWrapper, listener);
        }
        return isAdd;
    }

    /**
     * 停止下载
     *
     * @param key 下载文件唯一标识符
     */
    public void stopDownload(String key) {
        synchronized (mDownloadingSet) {
            FileDownloader downloader = mDownloadingSet.remove(key);
            if (downloader != null) {
                downloader.stop();
            }
        }
    }

    /**
     * 从等待队列中移除
     *
     * @param key 文件唯一标识符
     */
    public void removeFromWaittingQueue(String key) {
        if (key != null) {
            synchronized (mWaittingQueue) {
                WaitingTask targetWt = null;
                for (WaitingTask wt : mWaittingQueue) {
                    if (key.equals(wt.pw.getKey())) {
                        targetWt = wt;
                        break;
                    }
                }
                if (targetWt != null && mWaittingQueue.remove(targetWt)) {
                    ParamsWrapper pw = targetWt.pw;
                    DownloadFile file = new DownloadFile();
                    file.setId((int)pw.getId());
                    file.setKey(key);
                    file.setClassId(pw.getClassId());
                    file.setFileName(pw.getFileName());
                    file.setFilePath(pw.getFilePath());
                    file.setFileSize(pw.getFileSize());
                    file.setResUrl(pw.getResUrl());
                    file.setState(FileDownloader.INTERUPT);
                    file.setExt1(pw.getExt1());
                    file.setExt2(pw.getExt2());
                    file.setExt3(pw.getExt3());
                    file.setExt4(pw.getExt4());
                    file.setExt5(pw.getExt5());
                    file.setExt6(pw.getExt6());
                    file.setExt7(pw.getExt7());
                    file.setExt8(pw.getExt8());
                    file.setExt9(pw.getExt9());
                    file.setExt10(pw.getExt10());
                    file.setExt11(pw.getExt11());
                    file.setExt12(pw.getExt12());
                    file.setExt13(pw.getExt13());
                    file.setExt14(pw.getExt14());
                    file.setExt15(pw.getExt15());
                    file.setExt16(pw.getExt16());

                    putDownloadFileCache(key, file);

                    if (targetWt.listener != null) {
                        targetWt.listener.onProgressChanged(file, FileDownloader.INTERUPT);
                    }

                    if (mGlobalListener != null) {
                        mGlobalListener.onProgressChanged(file, FileDownloader.INTERUPT);
                    }
                }
            }
        }
    }

    /**
     * 从正在下载集合中移除
     *
     * @param key 文件唯一标识符
     */
    void removeFromDownloadingSet(String key) {
        synchronized (mDownloadingSet) {
            mDownloadingSet.remove(key);
        }
        startNext();
    }

    /**
     * 开始下一个下载
     */
    private void startNext() {
        WaitingTask wt = null;
        synchronized (mWaittingQueue) {
            if (!mWaittingQueue.isEmpty()) {
                wt = mWaittingQueue.poll();
            }
        }
        if (wt != null) {
            downloadInternal(wt.pw, wt.listener);
        }
    }

    /**
     *
     *
     * @return 正在下载中的文件数量
     */
    public int getDownloadingSize() {
        synchronized (mDownloadingSet) {
            return mDownloadingSet.size();
        }
    }

    /**
     *
     *
     * @return 正在等待中的文件数量
     */
    public int getWaittingSize() {
        synchronized (mWaittingQueue) {
            return mWaittingQueue.size();
        }
    }

    /**
     * 是否正在下载
     *
     * @param key 文件唯一标识符
     * @return
     */
    public boolean isDownloading(String key) {
        synchronized (mDownloadingSet) {
            return mDownloadingSet.containsKey(key);
        }
    }

    /**
     * 是否在等待队列中
     *
     * @param key 文件唯一标识符
     * @return
     */
    public boolean isInWaittingQueue(String key) {
        synchronized (mWaittingQueue) {
            WaitingTask wt = new WaitingTask();
            ParamsWrapper paramsWrapper = new ParamsWrapper();
            paramsWrapper.setKey(key);
            wt.pw = paramsWrapper;
            return mWaittingQueue.contains(wt);
        }
    }

    /**
     * 获取DownloadFile（下载文件）
     *
     * @param key 　文件标识符
     * @return
     */
    public DownloadFile getDownloadFile(String key) {
        FileDownloader downloader = null;
        synchronized (mDownloadingSet) {
            downloader = mDownloadingSet.get(key);
        }
        if (downloader != null) {
            return downloader.getDownloadFile();
        }
        return null;
    }

    /**
     * 获取下载进度
     *
     * @param key 　文件标识符
     * @return
     */
    public int getProgress(String key) {
        synchronized (mDownloadingSet) {
            if (mDownloadingSet.containsKey(key)) {
                FileDownloader downloader = mDownloadingSet.get(key);
                DownloadFile file = downloader.getDownloadFile();
                long haveRead = file.getHaveRead();
                long fileSize = file.getFileSize();
                if (fileSize > 0) {
                    int progress = (int) ((float) haveRead / fileSize * 100);
                    if (progress < 0) {
                        progress = 0;
                    }
                    if (progress > 100) {
                        progress = 100;
                    }
                    return progress;
                }
            }
        }
        return 0;
    }

    /**
     * 停止所有下载
     */
    public void stopAll() {
        synchronized (mWaittingQueue) {
            mWaittingQueue.clear();
        }
        // 销毁前把当前正在下载的任务全部停止
        synchronized (mDownloadingSet) {
            if (mDownloadingSet != null) {
                try {
                    ArrayList<FileDownloader> downloaders = new ArrayList<FileDownloader>();
                    for (String key : mDownloadingSet.keySet()) {
                        downloaders.add(mDownloadingSet.get(key));
                    }
                    for (FileDownloader downloader : downloaders) {
                        downloader.stop();
                    }
                    downloaders.clear();
                    // 清空所有下载任务
                    mDownloadingSet.clear();
                } catch (Exception e) {
                }
            }
        }
    }

    // 创建下载任务
    private FileDownloader createTask(ParamsWrapper paramsWrapper, IProgressListener listener) {
        // 参数
        int classId = paramsWrapper.getClassId();
        String key = paramsWrapper.getKey();
        String resUrl = paramsWrapper.getResUrl();
        String filePath = paramsWrapper.getFilePath();
        String fileName = paramsWrapper.getFileName();
        long fileSize = paramsWrapper.getFileSize();
        int isDelete = paramsWrapper.isDelete();
        String ext1 = paramsWrapper.getExt1();
        String ext2 = paramsWrapper.getExt2();
        String ext3 = paramsWrapper.getExt3();
        String ext4 = paramsWrapper.getExt4();
        String ext5 = paramsWrapper.getExt5();
        String ext6 = paramsWrapper.getExt6();
        String ext7 = paramsWrapper.getExt7();
        String ext8 = paramsWrapper.getExt8();
        String ext9 = paramsWrapper.getExt9();
        String ext10 = paramsWrapper.getExt10();
        String ext11 = paramsWrapper.getExt11();
        String ext12 = paramsWrapper.getExt12();
        String ext13 = paramsWrapper.getExt13();
        String ext14 = paramsWrapper.getExt14();
        String ext15 = paramsWrapper.getExt15();
        String ext16 = paramsWrapper.getExt16();
        // 创建downloader
        FileDownloader downloader = new FileDownloader(resUrl, filePath, fileName, fileSize, key,
                classId, isDelete, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8, ext9, ext10,
                ext11, ext12, ext13, ext14, ext15, ext16);

        downloader.setOperator(mOperator);
        InternalProgressListener internalListener = new InternalProgressListener(this);
        internalListener.setCallback(listener);
        downloader.setProgressListener(internalListener);
        if (mConfig != null) {
            downloader.setConfig(mConfig);
        }
        // 下载前的准备工作
        downloader.prepare();
        paramsWrapper.setId(downloader.getDownloadFile().getId());

        return downloader;
    }

    /**
     * 取消任务下载
     *
     * @param downloadFile 文件下载实体 
     * @param realDelete   是否真的删除
     */
    public void cancelDownload(DownloadFile downloadFile, boolean realDelete) {
        if (downloadFile != null) {
            String key = downloadFile.getKey();
            removeFromWaittingQueue(key);
            stopDownload(key);
            removeDownloadFileCache(key);
            if (realDelete) {
                mOperator.deleteFile(key);
                try {
                    File file = new File(downloadFile.getFilePath());
                    if (file.isFile()) {
                        file.delete();
                    }
                } catch (Exception e) {
                }
            } else {
                String selection = FileColumns.KEY + " = ? ";
                String[] selectionArgs = {
                        key
                };
                HashMap<String, Object> updateFileds = new HashMap<String, Object>();
                updateFileds.put(FileColumns.IS_DELETE, 1);
                mOperator.updateFile(updateFileds, selection, selectionArgs);
            }
            if (mGlobalListener != null) {
                mGlobalListener.onProgressChanged(downloadFile, FileDownloader.CANCEL);
            }
        }
    }

    /**
     * 获取下载文件的状态
     *
     * @param key 文件唯一标识符 
     * @return
     */
    public DownloadFile getDownloadFileFromCache(String key) {
        if (mDownloadFileCache.containsKey(key)) {
            return mDownloadFileCache.get(key);
        } else {
            return null;
        }
    }

    /**
     *
     * @return 正在下载中的列表
     */
    public ArrayList<DownloadFile> getDownloadingFiles() {
        ArrayList<DownloadFile> files = new ArrayList<DownloadFile>();
        synchronized (mDownloadingSet) {
            Set<String> keys = mDownloadingSet.keySet();
            for (String key : keys) {
                files.add(mDownloadingSet.get(key).getDownloadFile());
            }
        }
        return files;
    }

    /**
     *
     * @return 正在等待下载的列表
     */
    public ArrayList<DownloadFile> getWaittingFiles() {
        ArrayList<DownloadFile> files = new ArrayList<DownloadFile>();
        synchronized (mWaittingQueue) {
            Iterator<WaitingTask> wts = mWaittingQueue.iterator();
            while (wts.hasNext()) {
                ParamsWrapper pw = wts.next().pw;

                DownloadFile file = new DownloadFile();
                file.setResUrl(pw.getResUrl());
                file.setKey(pw.getKey());
                file.setFilePath(pw.getFilePath());
                file.setFileName(pw.getFileName());
                file.setFileSize(pw.getFileSize());
                file.setClassId(pw.getClassId());
                file.setIsDelete(pw.isDelete());
                file.setExt1(pw.getExt1());
                file.setExt2(pw.getExt2());
                file.setExt3(pw.getExt3());
                file.setExt4(pw.getExt4());
                file.setExt5(pw.getExt5());
                file.setExt6(pw.getExt6());
                file.setExt7(pw.getExt7());
                file.setExt8(pw.getExt8());
                file.setExt9(pw.getExt9());
                file.setExt10(pw.getExt10());
                file.setExt11(pw.getExt11());
                file.setExt12(pw.getExt12());
                file.setExt13(pw.getExt13());
                file.setExt14(pw.getExt14());
                file.setExt15(pw.getExt15());
                file.setExt16(pw.getExt16());

                files.add(file);
            }
        }
        return files;
    }

    /**
     * 获取缓存中的下载文件信息实体
     * @return
     */
    public ArrayList<DownloadFile> getDownloadFilesFromCache() {
        ArrayList<DownloadFile> files = new ArrayList<DownloadFile>();
        synchronized (mDownloadFileCache) {
            Set<String> keys = mDownloadFileCache.keySet();
            for (String key : keys) {
                files.add(mDownloadFileCache.get(key));
            }
        }
        return files;
    }
}
