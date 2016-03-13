
package com.ht.baselib.helper.download;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.ht.baselib.helper.download.entity.ConfigWrapper;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.LogEx;

/**
 * Msg:普通下载线程（不支持断点续传，不支持分块下载），父类是{@link AbstractDownloadTask}
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class NormalDownloadTask extends AbstractDownloadTask {

    public NormalDownloadTask(FileAccess fileAccess, IProgressListener progressListener,
            FileDownloader downloader, IOperator operator, DownloadFile downloadFile) {
        super(fileAccess, progressListener, downloader, operator, downloadFile);
        LogEx.d("普通下载");
    }

    @Override
    public void run() {
        String resUrl = downloadFile.getResUrl();
        LogEx.d("resUrl=" + resUrl);
        while (!stop) {
            try {
                KGHttpResponse httpResponse = httpConnector.getHttpResponse(resUrl);
                if (httpResponse != null) {
                    int responseCode = httpResponse.getResponseCode();
                    if (responseCode == 200 || responseCode == 206) {
                        long contentLength = -1;
                        if (httpResponse.containsHeader(KGHttpResponse.CONTENT_LENGTH)) {
                            contentLength = (Long) httpResponse
                                    .getHeader(KGHttpResponse.CONTENT_LENGTH);
                        }
                        LogEx.d("contentLength=" + contentLength);
                        InputStream input = httpResponse.getInputStream();
                        long saveLength = fileAccess.saveFile(input, mListener);
                        if (contentLength != -1) {
                            if (saveLength == contentLength) {
                                finish();
                                break;
                            } else {
                                // 普通下载没有断点续传，必须从重开始下载，所以haveRead要清零
                                downloadFile.resetHaveRead();
                                addFaileCounter();
                            }
                        } else {
                            if (saveLength != -1) {
                                finish();
                            } else {
                                // 普通下载没有断点续传，必须从重开始下载，所以haveRead要清零
                                downloadFile.resetHaveRead();
                                addFaileCounter();
                            }
                        }
                    } else {
                        // 普通下载没有断点续传，必须从重开始下载，所以haveRead要清零
                        downloadFile.resetHaveRead();
                        addFaileCounter();
                    }
                } else {
                    // 普通下载没有断点续传，必须从重开始下载，所以haveRead要清零
                    downloadFile.resetHaveRead();
                    boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
                    if (!isNetworkAvalid) {
                        stopByNetError();
                    } else {
                        addFaileCounter();
                    }
                }
            } catch (Exception e) {
                // 普通下载没有断点续传，必须从重开始下载，所以haveRead要清零
                downloadFile.resetHaveRead();

                if (e instanceof FileNotFoundException) {
                    stopByFileNotFound();
                } else {
                    boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
                    if (!isNetworkAvalid) {
                        stopByNetError();
                    } else {
                        httpConnector = createHttpConnector(true);
                        addFaileCounter();
                    }
                }
            }
        }
    }

    private void finish() {
        // 下载完成
        stopDownload();
        // 普通下载是不知道fileSize的，所有下载结束后把fileSize设置成已下载的大小
        downloadFile.setFileSize(downloadFile.getHaveRead());
        downloadFile.setState(FileDownloader.FINISH);
        operator.updateFile(downloadFile);
        downloadFile.getStatis().setFinishTime(System.currentTimeMillis());
        progressListener.onProgressChanged(downloadFile, FileDownloader.FINISH);
    }

}
