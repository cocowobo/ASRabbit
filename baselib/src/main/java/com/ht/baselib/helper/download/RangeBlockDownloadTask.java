
package com.ht.baselib.helper.download;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.ht.baselib.helper.download.entity.BlockedDownloadFile;
import com.ht.baselib.helper.download.entity.ConfigWrapper;
import com.ht.baselib.helper.download.entity.DataBlock;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.FreeBlockManager;
import com.ht.baselib.helper.download.util.LogEx;

/**
 * Msg:下载线程(支持断点下载、分块下载的线程)，父类是{@link AbstractDownloadTask}
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class RangeBlockDownloadTask extends AbstractDownloadTask {

    private BlockedDownloadFile blockedDownloadFile;

    private byte[] buffer;

    private byte[] temp = new byte[8 * 1024];

    private StringBuilder errorMsg;

    public RangeBlockDownloadTask(FileAccess fileAccess, IProgressListener progressListener,
            FileDownloader downloader, IOperator operator, DownloadFile downloadFile) {
        super(fileAccess, progressListener, downloader, operator, downloadFile);
        blockedDownloadFile = (BlockedDownloadFile) downloadFile;
        errorMsg = blockedDownloadFile.getStatis().getErrorMsg();
        ConfigWrapper.getInstance().getBlockSize();
        LogEx.d(getMessage("断点续传并分块下载"));
    }

    @Override
    public void run() {
        String resUrl = blockedDownloadFile.getResUrl();
        LogEx.d(getMessage("开始下载" + blockedDownloadFile.getFileName()));

        while (!stop) {
            errorMsg.setLength(0);
            errorMsg.append("RangeBlockDownloadTask: [n]");

            int freeBlockIndex = FreeBlockManager.getFreeBlockIndex(blockedDownloadFile);
            int bufferedIndex = blockedDownloadFile.getBufferedIndex();
            if (freeBlockIndex == -1) {
                if (bufferedIndex + 1 == blockedDownloadFile.getBlockNum()) {
                    // 下载完成
                    stopDownload();
                    synchronized (downloadFile) {
                        if (blockedDownloadFile.getState() != FileDownloader.FINISH) {
                            blockedDownloadFile.setHaveRead(blockedDownloadFile.getFileSize());
                            blockedDownloadFile.setState(FileDownloader.FINISH);
                            operator.updateFile(blockedDownloadFile);
                            downloadFile.getStatis().setFinishTime(System.currentTimeMillis());
                            progressListener.onProgressChanged(blockedDownloadFile,
                                    FileDownloader.FINISH);
                        }
                    }
                }
                LogEx.d(getMessage(blockedDownloadFile.getFileName() + " 下载完毕."));
                break;
            }
            DataBlock dataBlock = blockedDownloadFile.getDataBlocks()[freeBlockIndex];
            int bufferBlockNum = ConfigWrapper.getInstance().getBufferBlockNum();
            bufferBlockNum = Math.max(bufferBlockNum, ConfigWrapper.getInstance().getTaskNum());
            if (bufferBlockNum != -1 && (freeBlockIndex - (bufferedIndex + 1)) >= bufferBlockNum) {
                // 空闲块超出缓冲区范围，不去下载，休眠500毫秒。这样可以让缓冲区的数据尽快下载完，形成连续快
                dataBlock.setState(DataBlock.FREE);
                try {
                    sleep(500);
                } catch (Exception e) {
                }
                continue;
            }
            long start = dataBlock.getStart();
            long end = dataBlock.getEnd();
            long reqLength = end - start + 1;
            LogEx.d(getMessage("---->begin"));
            LogEx.d(getMessage("reqUrl=" + resUrl));
            LogEx.d(getMessage("freeBlockIndex=" + freeBlockIndex));
            LogEx.d(getMessage("start=" + start + " end=" + end));
            LogEx.d(getMessage("reqLength=" + reqLength));
            if (buffer == null || buffer.length != reqLength) {
                buffer = new byte[(int) reqLength];
            }
            try {
                LogEx.d(getMessage("--reading..."));
                KGHttpResponse httpResponse = httpConnector.getHttpResponse(resUrl, start, end);
                LogEx.d(getMessage("--done"));
                if (httpResponse != null) {
                    int responseCode = httpResponse.getResponseCode();
                    if (responseCode == 200 || responseCode == 206) {
                        if (httpResponse.containsHeader(KGHttpResponse.CONTENT_LENGTH)) {
                            long contentLength = (Long) httpResponse
                                    .getHeader(KGHttpResponse.CONTENT_LENGTH);
                            LogEx.d(getMessage("content-length=" + contentLength));
                            if (contentLength != reqLength) {
                                downloader.setTryNumMax();
                                faile(dataBlock, ERROR_DATA_LENGTH);
                                break;
                            }
                        } else if (httpResponse.containsHeader(KGHttpResponse.CONTENT_TYPE)) {
                            String contentType = (String) httpResponse
                                    .getHeader(KGHttpResponse.CONTENT_TYPE);
                            LogEx.d(getMessage("content-type=" + contentType));
                            // 临时处理方案(cdn服务器有可能返回出错图片)
                            if ("image/jpeg".equalsIgnoreCase(contentType)) {
                                downloader.setTryNumMax();
                                faile(dataBlock, ERROR_DATA_LENGTH);
                                break;
                            }
                        }

                        InputStream input = httpResponse.getInputStream();
                        byte[] data = readData(input, reqLength);
                        if (data != null && data.length == reqLength) {
                            long saveLength = fileAccess.saveFile(data, 0, data.length, start,
                                    mListener);
                            if (saveLength != -1) {
                                LogEx.d(getMessage("第" + freeBlockIndex + "块数据下载完毕"));
                                dataBlock.setState(DataBlock.FINISH);
                                clearFaileCounter();
                            } else {
                                faile(dataBlock, ERROR_SAVE_FILE);
                            }
                        } else {
                            LogEx.d(getMessage("error: reqLength=" + reqLength + " receiveLength="
                                    + data.length));
                            faile(dataBlock, ERROR_DATA_LENGTH);
                        }
                    } else {
                        faile(dataBlock, ERROR_RESPONSE_CODE);
                    }
                } else {
                    faile(dataBlock, ERROR_NO_RESPONSE);
                }
            } catch (Exception e) {
                if (e != null) {
                    LogEx.d(getMessage("error: " + e.getMessage()));

                    errorMsg.append("errorInfo=" + Statistics.getCrashReport(e));
                    errorMsg.append("[n]");
                }

                if (e instanceof FileNotFoundException) {
                    stopByFileNotFound();
                } else {
                    httpConnector = createHttpConnector(true);
                    faile(dataBlock, ERROR_EXCEPTION);
                }
            }
        }
        LogEx.d(getMessage("------线程结束--------"));
    }

    private byte[] readData(InputStream input, long reqLength) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(input);
        int read = 0;
        int haveRead = 0;
        while ((read = bis.read(temp)) != -1) {
            if (haveRead + read <= buffer.length) {
                System.arraycopy(temp, 0, buffer, haveRead, read);
                haveRead += read;
            } else {
                input.close();
                input = null;
                bis.close();
                bis = null;
                return null;
            }
        }
        input.close();
        input = null;
        bis.close();
        bis = null;

        if (haveRead == reqLength) {
            return buffer;
        } else {
            return null;
        }
    }

    private final int ERROR_DATA_LENGTH = 1;

    private final int ERROR_RESPONSE_CODE = 2;

    private final int ERROR_NO_RESPONSE = 3;

    private final int ERROR_EXCEPTION = 4;

    private final int ERROR_SAVE_FILE = 5;

    private void faile(DataBlock dataBlock, int errorType) {
        errorMsg.append("errorType=" + errorType + "[n]");

        String error = "unknown";
        switch (errorType) {
            case ERROR_DATA_LENGTH:
                error = "块大小不对";
                break;
            case ERROR_RESPONSE_CODE:
                error = "响应码错误（非200,206）";
                break;
            case ERROR_NO_RESPONSE:
                error = "服务器没响应";
                break;
            case ERROR_EXCEPTION:
                error = "发生异常";
                break;
            case ERROR_SAVE_FILE:
                error = "保存文件出错";
                break;
        }
        LogEx.d(getMessage("error=" + error));
        dataBlock.setState(DataBlock.FREE);

        boolean isNetworkAvalid = ConfigWrapper.getInstance().isNetworkAvalid();
        if (!isNetworkAvalid) {
            stopByNetError();
        } else {
            addFaileCounter();
        }
    }
}
