
package com.ht.baselib.helper.download.android;


import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.entity.ParamsWrapper;
import com.ht.baselib.helper.download.interfaces.IProgressListener;

import java.util.ArrayList;

/**
 * Msg:后台下载服务接口
 * Update:  2015-11-06
 * Version: 1.0
 * Created by laijiacai on 2015-11-06 11:19.
 */
public interface IDownloadService {

    boolean download(String resUrl, String filePath, IProgressListener listener);

    boolean download(String resUrl, String filePath, int classId, IProgressListener listener);

    boolean download(String key, String resUrl, String filePath, int classId,
                     IProgressListener listener);

    boolean download(String key, String resUrl, String filePath, long fileSize, int classId,
                     IProgressListener listener);

    boolean download(String key, String resUrl, String filePath, String fileName, long fileSize,
                     int classId, IProgressListener listener);

    boolean download(ParamsWrapper paramsWrapper, IProgressListener listener);

    boolean addToWaittingQueue(ParamsWrapper paramsWrapper, IProgressListener listener);

    void stopDownload(String key);

    void removeFromWaittingQueue(String key);

    boolean isDownloading(String key);

    boolean isInWaittingQueue(String key);

    DownloadFile getDownloadFile(String key);

    int getProgress(String key);

    void removeListener(IProgressListener listener);

    void addListener(IProgressListener listener);

    void setGolbalListener(IProgressListener listener);

    void cancelDownload(DownloadFile downloadFile, boolean realDelete);

    void stopAll();

    void setOnDownloadServiceDestoryListener(IDownloadServiceDestoryListener listener);

    DownloadFile getDownloadFileFromCache(String key);

    int getDownloadingSize();

    int getWaittingSize();

    void setMaxDownloadingNum(int maxDownloadingNum);

    ArrayList<DownloadFile> getDownloadingFiles();

    ArrayList<DownloadFile> getWaittingFiles();

    ArrayList<DownloadFile> getDownloadFilesFromCache();

}
