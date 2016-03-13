
package com.ht.baselib.helper.download;

import com.ht.baselib.helper.download.constants.NetType;
import com.ht.baselib.helper.download.entity.BlockedDownloadFile;
import com.ht.baselib.helper.download.entity.ConfigWrapper;
import com.ht.baselib.helper.download.entity.DefaultConfig;
import com.ht.baselib.helper.download.entity.DownloadFile;
import com.ht.baselib.helper.download.interfaces.IBytesListener;
import com.ht.baselib.helper.download.interfaces.IConfig;
import com.ht.baselib.helper.download.constants.FileColumns;
import com.ht.baselib.helper.download.interfaces.IHttpConnector;
import com.ht.baselib.helper.download.interfaces.IOperator;
import com.ht.baselib.helper.download.interfaces.IProgressListener;
import com.ht.baselib.helper.download.util.LogEx;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Msg:文件下载器.调用方法：创建一个FileDownloader对象，[设置参数与回调]，prepare(),startTask().
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class FileDownloader {

    /************************ 下载状态 *************************/

    /**
     * 准备状态
     */
    public static final int PREPARE = 1;

    /**
     * 准备就绪
     */
    public static final int READY = 2;

    /**
     * 正在下载
     */
    public static final int DOWNLOADING = 3;

    /**
     * 下载中断
     */
    public static final int INTERUPT = 4;

    /**
     * 下载完成
     */
    public static final int FINISH = 5;

    /**
     * 删除任务
     */
    public static final int CANCEL = 6;

    /************************ 出错原因 *************************/
    /**
     * 创建文件错误
     */
    public static final int CREATE_FILE_ERROR = 10;

    /**
     * 获取文件大小失败，在分块下载时，如果不能正确获取文件大小，会引发这个错误
     */
    public static final int GET_FILE_SIZE_ERROR = 11;

    /**
     * 超时
     */
    public static final int TIMEOUT_ERROR = 12;

    /**
     * 网络不可用
     */
    public static final int NETWORK_ERROR = 13;

    /**
     * 文件不存在
     */
    public static final int FILE_NOT_FOUND = 14;

    /**
     * 断点信息有误
     */
    public static final int RANGE_ERROR = 15;

    /**
     * 内容有误
     */
    public static final int CONTENT_ERROR = 16;

    /**
     * 文件不可读写
     */
    public static final int FILE_ACCESS_ERROR = 17;

    /**
     * 最大尝试次数
     */
    private int tryMaxNum = 3;

    private int tryNum;

    private FileAccess fileAccess;

    private IOperator operator;

    private IProgressListener progressListener;

    private IConfig config;

    private ConfigWrapper configWrapper;

    private String resUrl;

    private String filePath;

    private String newFilePath;

    private String fileName;

    private long fileSize = -1;

    private String key;

    private int classId;

    private int isDelete;

    private String ext1;

    private String ext2;

    private String ext3;

    private String ext4;

    private String ext5;

    private String ext6;

    private String ext7;

    private String ext8;

    private String ext9;

    private String ext10;

    private String ext11;

    private String ext12;

    private String ext13;

    private String ext14;

    private String ext15;

    private String ext16;

    // 待下载文件
    private DownloadFile downloadFile;

    private List<AbstractDownloadTask> tasks = new ArrayList<AbstractDownloadTask>();

    private boolean isStop;

    /**
     * 构造函数
     * 
     * @param resUrl 下载URL
     * @param filePath 文件保存路径（带文件名）
     */
    public FileDownloader(String resUrl, String filePath) {
        this(resUrl, filePath, new File(filePath).getName());
    }

    /**
     * 构造函数
     * 
     * @param resUrl 下载URL
     * @param filePath 文件路径（带文件名）
     * @param fileName 文件名
     */
    public FileDownloader(String resUrl, String filePath, String fileName) {
        this(resUrl, filePath, fileName, 0, resUrl, 0);
    }

    /**
     * 构造函数
     * 
     * @param resUrl 下载URL
     * @param filePath 文件路径（带文件名）
     * @param fileSize 文件长度
     */
    public FileDownloader(String resUrl, String filePath, long fileSize) {
        // 默认resUrl为标识符
        this(resUrl, filePath, new File(filePath).getName(), fileSize, resUrl, 0);
    }

    /**
     * 构造函数
     * 
     * @param resUrl 下载URL
     * @param filePath 文件路径（带文件名）
     * @param fileName 文件名
     * @param fileSize 文件长度
     * @param key 标识符
     * @param classId 分类ID
     */
    public FileDownloader(String resUrl, String filePath, String fileName, long fileSize,
            String key, int classId) {
        this(resUrl, filePath, fileName, fileSize, key, classId, 0, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * 构造函数
     * 
     * @param resUrl 下载URL
     * @param filePath 文件路径（带文件名）
     * @param fileName 文件名
     * @param fileSize 文件长度
     * @param key 标识符
     * @param classId 分类ID
     */
    public FileDownloader(String resUrl, String filePath, String fileName, long fileSize,
            String key, int classId, int isDelete, String ext1, String ext2, String ext3,
            String ext4, String ext5, String ext6, String ext7, String ext8, String ext9,
            String ext10, String ext11, String ext12, String ext13, String ext14, String ext15,
            String ext16) {
        this.resUrl = resUrl;
        this.filePath = filePath;
        this.newFilePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.key = key;
        this.classId = classId;
        this.isDelete = isDelete;
        this.ext1 = ext1;
        this.ext2 = ext2;
        this.ext3 = ext3;
        this.ext4 = ext4;
        this.ext5 = ext5;
        this.ext6 = ext6;
        this.ext7 = ext7;
        this.ext8 = ext8;
        this.ext9 = ext9;
        this.ext10 = ext10;
        this.ext11 = ext11;
        this.ext12 = ext12;
        this.ext13 = ext13;
        this.ext14 = ext14;
        this.ext15 = ext15;
        this.ext16 = ext16;
        this.config = new DefaultConfig();
        this.configWrapper = ConfigWrapper.getInstance();
        this.progressListener = new DefaultProgressListener();
        this.operator = new DefaultOperator();
        loadConfig();
    }

    /**
     * 设置文件信息保存类
     * 
     * @param operator 文件信息保存类对象
     */
    public void setOperator(IOperator operator) {
        this.operator = operator;
    }

    /**
     * 设置下载进度监听器
     * 
     * @param progressListener 下载进度监听器对象
     */
    public void setProgressListener(IProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * 设置参数配置
     * 
     * @param config 配置类对象
     */
    public void setConfig(IConfig config) {
        this.config = config;
        loadConfig();
    }

    public IConfig getConfig() {
        return config;
    }

    /**
     * 设置流量监听接口
     * 
     * @param listener
     */
    public void setBytesListener(IBytesListener listener) {
        fileAccess.setBytesListener(listener);
    }

    /**
     * 获取下载文件
     * 
     * @return 返回下载文件
     */
    public DownloadFile getDownloadFile() {
        return this.downloadFile;
    }

    /**
     * 这个方法会生成DownloadFile对象，并在数据库中创建一条记录。本方法不生成物理文件。调用这个方法之前，需要先调用loadConfig()
     */
    public void prepare() {
        LogEx.d("---------------" + fileName + " 准备下载-----------------");
        if (configWrapper.isBlock()) {
            downloadFile = new BlockedDownloadFile();// 需要分块下载
        } else {
            downloadFile = new DownloadFile();
        }
        downloadFile.setResUrl(resUrl);
        downloadFile.setFilePath(filePath);
        downloadFile.setFileName(fileName);
        downloadFile.setMimeType(getMimeType(filePath));
        downloadFile.setFileSize(fileSize);
        downloadFile.setKey(key);
        downloadFile.setClassId(classId);
        downloadFile.setIsDelete(isDelete);
        downloadFile.setExt1(ext1);
        downloadFile.setExt2(ext2);
        downloadFile.setExt3(ext3);
        downloadFile.setExt4(ext4);
        downloadFile.setExt5(ext5);
        downloadFile.setExt6(ext6);
        downloadFile.setExt7(ext7);
        downloadFile.setExt8(ext8);
        downloadFile.setExt9(ext9);
        downloadFile.setExt10(ext10);
        downloadFile.setExt11(ext11);
        downloadFile.setExt12(ext12);
        downloadFile.setExt13(ext13);
        downloadFile.setExt14(ext14);
        downloadFile.setExt15(ext15);
        downloadFile.setExt16(ext16);
        fileAccess = new FileAccess(downloadFile, progressListener);
        fileAccess.setProgressListener(progressListener);
        // 创建或恢复文件
        String selection = FileColumns.KEY + " = ? ";
        String[] selectionArgs = new String[] {
            key
        };
        List<DownloadFile> files = operator.queryFile(selection, selectionArgs);
        if (files != null && files.size() > 0) {
            // 以前有下载过
            DownloadFile tempFile = files.get(0);
            downloadFile.setId(tempFile.getId());
            downloadFile.setHaveRead(tempFile.getHaveRead());
            downloadFile.setState(tempFile.getState());
            downloadFile.setFilePath(tempFile.getFilePath());
            downloadFile.setFileSize(tempFile.getFileSize());
            downloadFile.setMimeType(tempFile.getMimeType());
            downloadFile.setClassId(tempFile.getClassId());
            downloadFile.setIsDelete(tempFile.getIsDelete());
            downloadFile.setIsNewDownload(tempFile.isNewDownload());

            filePath = tempFile.getFilePath();
            File file = new File(filePath);
            if (!file.exists()) {
                downloadFile.setState(0);
                downloadFile.setIsNewDownload(true);
                String selectionTemp = FileColumns.KEY + " = ? ";
                String[] selectionArgsTemp = {
                    key
                };
                HashMap<String, Object> updateFileds = new HashMap<String, Object>();
                updateFileds.put(FileColumns.STATE, 0);
                if (tempFile.getIsDelete() == 1) {
                    downloadFile.setIsDelete(0);
                    updateFileds.put(FileColumns.IS_DELETE, 0);
                }
                operator.updateFile(updateFileds, selectionTemp, selectionArgsTemp);
            } else {
                downloadFile.setIsNewDownload(false);
                if (tempFile.getIsDelete() == 1) {
                    downloadFile.setIsDelete(0);
                    operator.updateFile(downloadFile);
                }
                downloadFile.setExt1(tempFile.getExt1());
                downloadFile.setExt2(tempFile.getExt2());
                downloadFile.setExt3(tempFile.getExt3());
                downloadFile.setExt4(tempFile.getExt4());
                downloadFile.setExt5(tempFile.getExt5());
                downloadFile.setExt6(tempFile.getExt6());
                downloadFile.setExt7(tempFile.getExt7());
                downloadFile.setExt8(tempFile.getExt8());
                downloadFile.setExt9(tempFile.getExt9());
                downloadFile.setExt10(tempFile.getExt10());
                downloadFile.setExt11(tempFile.getExt11());
                downloadFile.setExt12(tempFile.getExt12());
                downloadFile.setExt13(tempFile.getExt13());
                downloadFile.setExt14(tempFile.getExt14());
                downloadFile.setExt15(tempFile.getExt15());
                downloadFile.setExt16(tempFile.getExt16());
            }
        } else {
            // 新增一条数据
            long id = operator.insertFile(downloadFile);
            downloadFile.setId((int)id);
            downloadFile.setIsNewDownload(true);
        }

        progressListener.onProgressChanged(downloadFile, PREPARE);
    }

    /**
     * 开始下载,这个方法需要在子线程中运行
     * 
     * @throws Exception
     */
    public void startTask() {
        if (isStop()) {
            // 已中断下载
            return;
        }
        // 全局变量的fileSize重新赋值，因为这个fileSize可能会在后期有改变
        fileSize = downloadFile.getFileSize();
        filePath = downloadFile.getFilePath();
        if (fileSize <= 0) {
            // 连网请求文件长度
            fileSize = getContentLength(resUrl);
            LogEx.d("get_file_size_from_web:" + fileSize);
            if (fileSize > 0) {
                // 设置文件大小
                downloadFile.setFileSize(fileSize);
            }
        }
        File file = new File(filePath);
        if (file.exists()) {
            if (downloadFile.getState() == FileDownloader.FINISH) {
                // 下载完成的，不需要继续下载
                progressListener.onProgressChanged(downloadFile, FileDownloader.FINISH);
                return;
            }
            if (!file.canWrite()) {
                // 不能写文件，不能继续下载
                progressListener.onError(downloadFile, FILE_ACCESS_ERROR);
                return;
            }
        } else {
            // 缓存物理文件被删除，重新创建
            // 使用传进来的保存路径
            filePath = newFilePath;
            downloadFile.setFilePath(filePath);
            downloadFile.setHaveRead(0);
            downloadFile.setState(0);
            downloadFile.setIsDelete(0);

            boolean createFile = createFile(downloadFile);
            if (!createFile) {
                // 创建文件失败，不能继续下载
                progressListener.onError(downloadFile, CREATE_FILE_ERROR);
                return;
            }
        }
        // ///////准备开始下载//////////
        // 是否断点续传
        boolean isRange = configWrapper.isRange();
        // 是否分块下载
        boolean isBlock = configWrapper.isBlock();
        // 每块大小
        int blockSize = configWrapper.getBlockSize();

        if (isStop()) {
            // 已中断下载
            return;
        }
        if (isRange) {
            if (isBlock) {
                // 断点续传并且分块下载时，必须事先知道fileSize大小
                if (fileSize <= 0) {
                    // 获取文件大小失败，不能继续下载
                    progressListener.onError(downloadFile, GET_FILE_SIZE_ERROR);
                    return;
                }

                // 需要分块下载
                BlockedDownloadFile blockedDownloadFile = (BlockedDownloadFile) downloadFile;
                long haveRead = blockedDownloadFile.getHaveRead();
                int bufferedIndex = (int) (haveRead / blockSize - 1);
                LogEx.d("bufferedIndex----->" + bufferedIndex);
                blockedDownloadFile.setBufferedIndex(bufferedIndex);
                blockedDownloadFile.setBlockSize(Math.min(blockSize, (int) fileSize));
                blockedDownloadFile.splitBlocks(bufferedIndex);
            }
        } else {
            // 不断点续传，从头开始下载
            downloadFile.setHaveRead(0);
        }

        // 设置统计数据
        Statistics statis = downloadFile.getStatis();
        // 设置下载开始时间
        statis.setStartTime(System.currentTimeMillis());
        // 设置数据下载长度
        statis.setDownloadLen(downloadFile.getFileSize() - downloadFile.getHaveRead());

        // 准备工作完毕 废弃这个状态回调
        // downloadFile.setState(READY);
        // operator.updateFile(downloadFile);
        // progressListener.onProgressChanged(downloadFile, READY);

        // 开始下载
        int taskNum = 1;
        if (isRange && isBlock) {
            // 分块下载
            taskNum = configWrapper.getTaskNum();
        }
        startDownloadTasks(taskNum);
    }

    /**
     * 停止下载
     */
    public void stop() {
        stopDownload(INTERUPT);
    }

    // 下载超时
    public void stopByTimeOut() {
        stopDownload(TIMEOUT_ERROR);
    }

    // 文件不存在
    public void stopByFileNotFound() {
        stopDownload(FILE_NOT_FOUND);
    }

    // 断点信息有误
    public void stopByRangeError() {
        stopDownload(RANGE_ERROR);
    }

    // 内容有误
    public void stopByContentError() {
        stopDownload(CONTENT_ERROR);
    }

    // 停止所有下载线程
    private synchronized void stopDownload(int stopReason) {
        if (!isStop()) {
            isStop = true;
        } else {
            return;
        }

        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            tasks.get(i).stopDownload();
        }

        fileAccess.setStop(true);
        LogEx.d("stop:" + " ---------" + fileName + " stopDownload----------");
        if (downloadFile.getState() != FileDownloader.FINISH) {
            // 非下载完成的，设置状态为中断
            downloadFile.setState(INTERUPT);
        }
        // 保存下载信息
        operator.updateFile(downloadFile);
        if (stopReason == INTERUPT) {
            progressListener.onProgressChanged(downloadFile, INTERUPT);
        } else if (stopReason == TIMEOUT_ERROR) {
            progressListener.onError(downloadFile, TIMEOUT_ERROR);
        } else if (stopReason == NETWORK_ERROR) {
            progressListener.onError(downloadFile, NETWORK_ERROR);
        } else if (stopReason == FILE_NOT_FOUND) {
            progressListener.onError(downloadFile, FILE_NOT_FOUND);
        } else if (stopReason == RANGE_ERROR) {
            progressListener.onError(downloadFile, RANGE_ERROR);
        } else if (stopReason == CONTENT_ERROR) {
            progressListener.onError(downloadFile, CONTENT_ERROR);
        } else {
            progressListener.onError(downloadFile, TIMEOUT_ERROR);
        }
        tasks.clear();
    }

    /**
     * 重置重试次数
     */
    public synchronized void resetTryNum() {
        this.tryNum = 0;
    }

    /**
     * 累加
     */
    public synchronized void addTryNum() {
        tryNum++;
        if (tryNum >= tryMaxNum) {
            stopByTimeOut();
        }
    }

    /**
     * 停止
     */
    public synchronized void setTryNumMax() {
        tryNum = tryMaxNum;
    }

    /**
     * 由于网络中断而停止
     */
    public synchronized void stopByNetError() {
        stopDownload(NETWORK_ERROR);
    }

    // 创建物理文件
    private boolean createFile(DownloadFile downloadFile) {
        try {
            String filePath = downloadFile.getFilePath();
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                // 创建父目录
                parent.mkdirs();
            }
            file.createNewFile();

            // if (fileSize > 0) {
            // RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            // //raf.setLength(fileSize);
            // raf.close();
            // }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private synchronized void startDownloadTasks(int taskNum) {
        if (isStop()) {
            return;
        }
        downloadFile.setState(DOWNLOADING);
        operator.updateFile(downloadFile);
        progressListener.onProgressChanged(downloadFile, DOWNLOADING);

        for (int i = 0; i < taskNum && !isStop(); i++) {
            startDownloadTask();
        }
    }

    private void startDownloadTask() {
        boolean isRange = configWrapper.isRange();
        boolean isBlock = configWrapper.isBlock();
        if (!isStop()) {
            AbstractDownloadTask task = createDownloadTask(isRange, isBlock);
            // 添加到下载线程管理器中
            tasks.add(task);
            // 开始下载
            task.start();
        }
    }

    private final int MAX_TASK_NUM = 3;

    /**
     * 加速
     */
    public synchronized void speedUp() {
        while (tasks.size() < MAX_TASK_NUM && !isStop()) {
            startDownloadTask();
            LogEx.d("speedUp-->");
        }
    }

    /**
     * 减速
     */
    public synchronized void speedDown() {
        while (tasks.size() > 1) {
            removeDownloadTask();
        }
    }

    /**
     * 加载配置文件
     * 
     */
    private void loadConfig() {
        configWrapper.setConfig(config);
        configWrapper.setBlockSize(config.getBlockSize());
        configWrapper.setTaskNum(config.getTaskNum());
        configWrapper.setNetType(config.getNetType());
        configWrapper.setRange(config.isRange());
        configWrapper.setCmwap(config.isCmwap());
        configWrapper.setRefreshInterval(config.getRefreshInterval());
        configWrapper.setBufferBlockNum(config.getBufferBlockNum());
        configWrapper.setRequestHeaders(config.getRequestHeaders());
        configWrapper.setNeedToForceBlock(config.is2GNeedToForceBlock());
        if (configWrapper.isRange()) {
            // 能断点续传，才需要判断是否分块下载
            configWrapper.setBlock(config.isBlock());
        } else {
            configWrapper.setBlock(false);
        }
        if (config.is2GNeedToForceBlock() && configWrapper.getNetType() == NetType.G2) {
            // 由于2g网络对数据块有限制，一般来说，文件长度超过100K时就不会响应，所以必须要分块下载
            configWrapper.setRange(true);
            configWrapper.setBlock(true);
        }
    }

    private int mThreadCounter;

    /**
     * 创建下载线程
     * 
     * @param isRange 是否支持断点下载
     * @param isBlock　是否需要分块下载
     * @return
     */
    private AbstractDownloadTask createDownloadTask(boolean isRange, boolean isBlock) {
        AbstractDownloadTask downloadTask;
        if (isRange) {
            if (isBlock) {
                downloadTask = new RangeBlockDownloadTask(fileAccess, progressListener, this,
                        operator, downloadFile);
            } else {
                downloadTask = new RangeNotBlockDownloadTask(fileAccess, progressListener, this,
                        operator, downloadFile);
            }
        } else {
            downloadTask = new NormalDownloadTask(fileAccess, progressListener, this, operator,
                    downloadFile);
        }
        downloadTask.setName("download" + (++mThreadCounter));
        return downloadTask;
    }

    // 移除下载线程
    private void removeDownloadTask() {
        int size = tasks.size();
        if (size > 0) {
            AbstractDownloadTask task = tasks.remove(size - 1);
            task.stopDownload();
            mThreadCounter--;
            LogEx.d("speedDown-->");
        }
    }

    // 获取文件长度
    private long getContentLength(String resUrl) {
        boolean isCmwap = configWrapper.isCmwap();
        int counter = 0;
        long contentLength = Integer.MIN_VALUE;
        while (counter++ < 2) {
            try {
                IHttpConnector httpConn = DefaultHttpConnectorFactory.create(isCmwap);
                contentLength = httpConn.getContentLength(resUrl);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentLength;
    }

    // 获取文件类型
    private String getMimeType(String filePath) {
        if (filePath != null) {
            if (filePath.length() > 0 && (filePath.lastIndexOf(".") != -1)) {
                return filePath.substring(filePath.lastIndexOf(".") + 1);
            }
        }
        return "UNKNOWN";
    }

    /**
     * 任务是否停止/取消下载
     * 
     * @return
     */
    private synchronized boolean isStop() {
        return isStop;
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        if (obj instanceof FileDownloader) {
            FileDownloader downloader = (FileDownloader) obj;
            return downloader.key != null && downloader.key.equalsIgnoreCase(this.key);
        }
        return false;
    }

}
