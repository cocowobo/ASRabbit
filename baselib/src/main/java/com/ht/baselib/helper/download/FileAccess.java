
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IBytesListener;
import com.ht.baselib.helper.download.interfaces.IProgressListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Msg:文件保存类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class FileAccess {

    public static final int DEFAULT_BUFFER_SIZE = 16 * 1024;

    private IProgressListener progressListener;

    private IBytesListener bytesListener;

    private DownloadFile downloadFile;

    private Statistics statis;

    private int bufferSize;

    private boolean stop;

    private StringBuilder errorMsg;

    /**
     * 构造函数
     *
     * @param downloadFile 下载文件
     * @param listener     下载进度监听器
     */
    public FileAccess(DownloadFile downloadFile, IProgressListener listener) {
        this(downloadFile, listener, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 构造函数
     *
     * @param downloadFile 下载文件
     * @param listener     下载进度监听器
     * @param bufferSize   缓冲区大小
     */
    FileAccess(DownloadFile downloadFile, IProgressListener listener, int bufferSize) {
        this.downloadFile = downloadFile;
        this.progressListener = listener;
        this.bufferSize = bufferSize;
        this.statis = downloadFile.getStatis();
        if (statis != null) {
            this.errorMsg = statis.getErrorMsg();
        }
    }

    /**
     * 设置下载进度监听器
     *
     * @param listener 监听器
     */
    public void setProgressListener(IProgressListener listener) {
        this.progressListener = listener;
    }

    /**
     * 设置流量监听器
     *
     * @param listener 监听器
     */
    public void setBytesListener(IBytesListener listener) {
        this.bytesListener = listener;
    }

    /**
     * @param stop 是否停止吸入
     */
    public void setStop(boolean stop) {
        synchronized (progressListener) {
            this.stop = stop;
            progressListener = null;
        }
    }

    /**
     * 保存文件
     *
     * @param input 　输入流
     * @param listener 监听器
     * @return 返回成功保存的字节数长度。-1保存失败
     */
    public long saveFile(InputStream input, FileSaveProgressListener listener) {
        // 需要每次都创建一个RandomAccessFile对象，否则需要加synchronized来同步saveFile()方法
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(downloadFile.getFilePath(), "rw");
        } catch (Exception e) {
            return -1;
        }
        return saveFile(input, randomAccessFile, listener);
    }

    /**
     * 保存文件
     *
     * @param input    输入流
     * @param seekTo   跳转位置
     * @param listener 监听器
     * @return 返回成功保存的字节数长度。-1保存失败
     */
    public long saveFile(InputStream input, long seekTo, FileSaveProgressListener listener) {
        // 需要每次都创建一个RandomAccessFile对象，否则需要加synchronized来同步saveFile()方法
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(downloadFile.getFilePath(), "rw");
            randomAccessFile.seek(seekTo);
        } catch (Exception e) {
            if (errorMsg != null && e != null) {
                errorMsg.append(Statistics.getCrashReport(e));
            }
            return -1;
        }
        return saveFile(input, randomAccessFile, listener);
    }

    /**
     * 保存文件
     *
     * @param input            输入流
     * @param randomAccessFile RandomAccessFile对象
     * @return 是否保存成功
     */
    private long saveFile(InputStream input, RandomAccessFile randomAccessFile,
                          FileSaveProgressListener listener) {
        try {
            BufferedInputStream bis = new BufferedInputStream(input);
            int read = 0;
            byte[] buffer = new byte[bufferSize];
            long haveRead = 0;
            while ((read = bis.read(buffer)) != -1) {
                // 写入数据
                long saveLen = saveFile(buffer, 0, read, randomAccessFile, listener);
                if (saveLen != -1) {
                    haveRead += read;
                }
                if (saveLen == -1 || stop) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    }
                    return -1;
                }
            }
            return haveRead;
        } catch (Exception e) {
            if (errorMsg != null && e != null) {
                errorMsg.append(Statistics.getCrashReport(e));
            }
            return -1;
        } finally {
            try {
                randomAccessFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param data     字节数组
     * @param off      the index of the first byte in buffer to write.
     * @param len      the number of bytes from the buffer to write.
     * @param listener 保存监听器
     * @return 写入失败返回-1，成功返回保存的字节数长度
     */
    public long saveFile(byte[] data, int off, int len, FileSaveProgressListener listener) {
        // 需要每次都创建一个RandomAccessFile对象，否则需要加synchronized来同步saveFile()方法
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(downloadFile.getFilePath(), "rw");
        } catch (Exception e) {
            return -1;
        }
        return saveFile(data, off, len, randomAccessFile, listener);
    }

    /**
     * 保存文件
     *
     * @param data     字节数组
     * @param off      the index of the first byte in buffer to write.
     * @param len      the number of bytes from the buffer to write.
     * @param seekTo   跳转位置
     * @param listener 保存监听器
     * @return 返回成功保存的字节数长度。-1保存失败
     */
    public long saveFile(byte[] data, int off, int len, long seekTo,
                         FileSaveProgressListener listener) {
        // 需要每次都创建一个RandomAccessFile对象，否则需要加synchronized来同步saveFile()方法
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(downloadFile.getFilePath(), "rw");
            randomAccessFile.seek(seekTo);
        } catch (Exception e) {
            return -1;
        }
        return saveFile(data, off, len, randomAccessFile, listener);
    }

    /**
     * @param data             the buffer to write.
     * @param off              the index of the first byte in buffer to write.
     * @param len              the number of bytes from the buffer to write.
     * @param randomAccessFile RandomAccessFile对象
     * @param listener         监听器
     * @return 写入失败返回-1，其他返回写入长度
     */
    private long saveFile(byte[] data, int off, int len, RandomAccessFile randomAccessFile,
                          FileSaveProgressListener listener) {
        try {
            randomAccessFile.write(data, off, len);
            if (listener != null) {
                listener.onProgressChanged(len);
            }
            if (bytesListener != null) {
                // 流量监听
                bytesListener.onBytesReceived(len);
            }
            return len;
        } catch (Exception e) {
            if (errorMsg != null && e != null) {
                errorMsg.append(Statistics.getCrashReport(e));
            }
            return -1;
        }
    }

    /**
     * 文件保存监听器
     *
     * @author laijiacai
     */
    interface FileSaveProgressListener {

        /**
         * 正在保存
         *
         * @param savedLength 当次保存成功的字节数
         */
        void onProgressChanged(long savedLength);

    }

    // // 保存数据
    // private void saveFile(ByteArrayBuffer bab, RandomAccessFile
    // randomAccessFile) throws Exception {
    // byte[] receivedData = bab.toByteArray();
    // int receivedLen = receivedData.length;
    // randomAccessFile.write(receivedData, 0, receivedLen);
    // statis.addReceivedLen(receivedLen);
    // // 进度设置
    // if (downloadFile instanceof BlockedDownloadFile) {
    // // 分块下载通知的haveRead是连续块的字节数
    // BlockedDownloadFile blockedDownloadFile = (BlockedDownloadFile)
    // downloadFile;
    // int index = blockedDownloadFile.getBufferedIndex() + 1;
    // long bufferedRead = index * blockedDownloadFile.getBlockSize();
    // if (bufferedRead >= blockedDownloadFile.getFileSize()) {
    // bufferedRead = blockedDownloadFile.getFileSize();
    // }
    // blockedDownloadFile.setHaveRead(bufferedRead);
    // } else {
    // downloadFile.addHaveRead(receivedLen);
    // }
    // if (statis.canNotify()) {
    // notifyProgress(downloadFile);
    // }
    // if (bytesListener != null) {
    // // 流量监听
    // bytesListener.onBytesReceived(receivedLen);
    // }
    // bab.clear();
    // }
    //
    // // 通知进度
    // private void notifyProgress(DownloadFile file) {
    // synchronized (progressListener) {
    // if (!stop && progressListener != null) {
    // // 通知进度
    // progressListener.onProgressChanged(file, FileDownloader.DOWNLOADING);
    // }
    // }
    // }
}
