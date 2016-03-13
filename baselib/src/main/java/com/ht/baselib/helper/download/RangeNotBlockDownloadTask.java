
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.entity.ConfigWrapper;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.LogEx;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Msg:下载线程（支持断点下载，但不分块下载），父类是{@link AbstractDownloadTask}
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class RangeNotBlockDownloadTask extends AbstractDownloadTask {

    public RangeNotBlockDownloadTask(FileAccess fileAccess, IProgressListener progressListener,
            FileDownloader downloader, IOperator operator, DownloadFile downloadFile) {
        super(fileAccess, progressListener, downloader, operator, downloadFile);
        LogEx.d("断点续传但不分块下载");
    }

    @Override
    public void run() {
        String resUrl = downloadFile.getResUrl();
        LogEx.d("resUrl=" + resUrl);

        StringBuilder errorMsg = downloadFile.getStatis().getErrorMsg();

        while (!stop) {
            try {
                errorMsg.setLength(0);
                errorMsg.append("RangeNotBlockDownloadTask: [n]");
                long start = downloadFile.getHaveRead();
                LogEx.d("start=" + start);
                KGHttpResponse httpResponse = httpConnector.getHttpResponse(resUrl, start);
                if (httpResponse != null) {
                    int responseCode = httpResponse.getResponseCode();
                    if (responseCode == 206 || (responseCode == 200 && start == 0)) {
                        long contentLength = -1;
                        if (httpResponse.containsHeader(KGHttpResponse.CONTENT_LENGTH)) {
                            contentLength = (Long) httpResponse
                                    .getHeader(KGHttpResponse.CONTENT_LENGTH);
                        }
                        LogEx.d("contentLength=" + contentLength);

                        String contentType = null;
                        if (httpResponse.containsHeader(KGHttpResponse.CONTENT_TYPE)) {
                            contentType = (String) httpResponse
                                    .getHeader(KGHttpResponse.CONTENT_TYPE);
                        }
                        LogEx.d("contentType=" + contentType);

                        if ("text/html".equalsIgnoreCase(contentType)) {
                            errorMsg.append("errorCode=1 [n]");

                            stopByContentError();
                            break;
                        }
                        if ("image/jpeg".equalsIgnoreCase(contentType)) {
                            errorMsg.append("errorCode=2 [n]");

                            stopByContentError();
                            break;
                        }

                        InputStream input = httpResponse.getInputStream();
                        long saveLenght = fileAccess.saveFile(input, start, mListener);
                        input.close();

                        // 保存文件数据成功
                        if (saveLenght != -1) {
                            if (contentLength > 0) {
                                if (downloadFile.getHaveRead() - start >= contentLength ) {
                                    // 文件流正常保存完毕
                                    finish();
                                    break;
                                } else {
                                    LogEx.d("error: 数据没下载完毕，继续下载...");

                                    errorMsg.append("errorCode=3 [n]");

                                    httpConnector = createHttpConnector(true);
                                    addFaileCounter();
                                }
                            } else {
                                long haveRead = downloadFile.getHaveRead();
                                if (haveRead - start > 0) {
                                    downloadFile.setFileSize(haveRead);
                                    finish();
                                    break;
                                } else {
                                    errorMsg.append("errorCode=4 [n]");

                                    downloadFile.setHaveRead(start);
                                    stopByContentError();
                                    break;
                                }
                            }
                        } else {
                            errorMsg.append("errorCode=5 [n]");

                            httpConnector = createHttpConnector(true);
                            addFaileCounter();
                        }
                    } else {
                        if (responseCode == 200 && start != 0) {
                            errorMsg.append("errorCode=6 [n]");

                            stopByServerRangeError();
                            break;
                        } else {
                            errorMsg.append("errorCode=7 [n]");

                            httpConnector = createHttpConnector(true);
                            addFaileCounter();
                        }
                    }
                } else {
                    boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
                    if (!isNetworkAvalid) {
                        errorMsg.append("errorCode=8 [n]");

                        stopByNetError();
                        break;
                    } else {
                        errorMsg.append("errorCode=9 [n]");

                        httpConnector = createHttpConnector(true);
                        addFaileCounter();
                    }
                }
            } catch (Exception e) {
                errorMsg.append("errorCode=10 [n]");
                errorMsg.append("errorInfo=" + e != null ? Statistics.getCrashReport(e) : "");
                errorMsg.append("[n]");

                e.printStackTrace();
                if (e instanceof FileNotFoundException) {
                    stopByFileNotFound();
                    break;
                } else {
                    boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
                    if (!isNetworkAvalid) {
                        stopByNetError();
                        break;
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
        downloadFile.setHaveRead(downloadFile.getFileSize());
        downloadFile.setState(FileDownloader.FINISH);
        operator.updateFile(downloadFile);
        downloadFile.getStatis().setFinishTime(System.currentTimeMillis());
        progressListener.onProgressChanged(downloadFile, FileDownloader.FINISH);
    }

}
