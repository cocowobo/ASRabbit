
package com.ht.baselib.helper.download.manager;

import com.ht.baselib.helper.download.DefaultProgressListener;
import com.ht.baselib.helper.download.FileDownloader;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IProgressListener;

/**
 * Msg:下载回调
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class InternalProgressListener extends DefaultProgressListener {

    private DownloadInternalManager mDownloadInternalManager;

    private IProgressListener mCustomListener;

    private IProgressListener mGolbalListener;

    /**
     *
     * @param manager  下载管理器
     */
    public InternalProgressListener(DownloadInternalManager manager) {
        mDownloadInternalManager = manager;
        mGolbalListener = manager.getGlobalProgressListener();
    }

    @Override
    public void onProgressChanged(DownloadFile file, int state) {
        super.onProgressChanged(file, state);
        mDownloadInternalManager.putDownloadFileCache(file.getKey(), file);
        if (state == FileDownloader.FINISH || state == FileDownloader.INTERUPT) {
            removeFromDownloadingSet(file);
        }
        if (mCustomListener != null) {
            mCustomListener.onProgressChanged(file, state);
        }
        mDownloadInternalManager.invokeListeners(file, state, DownloadInternalManager.NOTIFY_PROGRESS);
        if (mGolbalListener != null) {
            mGolbalListener.onProgressChanged(file, state);
        }
    }

    @Override
    public void onError(DownloadFile file, int errorType) {
        super.onError(file, errorType);
        mDownloadInternalManager.putDownloadFileCache(file.getKey(), file);
        removeFromDownloadingSet(file);
        if (mCustomListener != null) {
            mCustomListener.onError(file, errorType);
        }
        mDownloadInternalManager.invokeListeners(file, errorType, DownloadInternalManager.NOTIFY_ERROR);
        if (mGolbalListener != null) {
            mGolbalListener.onError(file, errorType);
        }
    }

    private void removeFromDownloadingSet(DownloadFile file) {
        String key = file.getKey();
        mDownloadInternalManager.removeFromDownloadingSet(key);
    }

    public void setCallback(IProgressListener callback) {
        this.mCustomListener = callback;
    }

}
