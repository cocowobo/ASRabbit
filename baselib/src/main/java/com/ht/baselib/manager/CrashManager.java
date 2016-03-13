package com.ht.baselib.manager;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.ht.baselib.utils.FileUtils;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.SDCardUtils;
import com.ht.baselib.utils.TimeFormater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Msg: 崩溃处理类 1、提供记录和提交崩溃信息功能
 * Update:  2015/10/13
 * Version: 1.0
 * Created by hxm on 2015/10/13 14:42.
 */
public final class CrashManager implements Thread.UncaughtExceptionHandler {

    /**
     * 是否写入崩溃日志到文件夹 by hxm
     */
    public static boolean isWriteCrashLog = true;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashManager instance = null;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 睡眠时间，单位毫秒
     */
    private static final int SLEEP_TIME_IN_MILLISECONDS = 3000;
    /**
     * 秒转换为毫秒的单位
     */
    private static final int SECONDS_TO_MILLISECONDS = 1000;
    private static final int CAPACITY = 128;

    /**
     * 保证只有一个CrashManager实例
     */
    private CrashManager() {
    }

    /**
     * CrashManager ,单例模式
     *
     * @return CrashManager对象
     */
    public static CrashManager getInstance() {
        if (instance == null) {
            instance = new CrashManager();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this); // 设置该CrashHandler为程序的默认处理器
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread 发生异常的线程
     * @param ex     异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        handleException(ex);
        try {
            Thread.sleep(SLEEP_TIME_IN_MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 退出程序

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            logReportToFile(mContext, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            logReportToRemote(mContext, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /****
     * 保存错误信息到文件
     *
     * @param context 上下文对象
     * @param ex      异常信息
     */
    private void logReportToFile(Context context, Throwable ex) {
        //增加一个配置参数，用来决定崩溃日志要不要写入到文件
        if (isWriteCrashLog) {
            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                File logFolder = new File(SDCardUtils.LOG_FOLDER);
                if (logFolder != null && !logFolder.exists()) {
                    logFolder.mkdirs();
                }
                File appFolder = new File(logFolder, context.getPackageName());
                if (appFolder != null && !appFolder.exists()) {
                    appFolder.mkdirs();
                }
                String currentTimeInSeconds = String.valueOf(Calendar.getInstance().getTime().getTime() /
                        SECONDS_TO_MILLISECONDS);
                String currentTime = TimeFormater.timeStamp2Date(currentTimeInSeconds, "yyyy-MM-dd HH-mm-ss");//文件名不能使用":",因此时分秒使用"-"分隔
                String logFileName = appFolder.getAbsolutePath() + File.separator + currentTime + ".log";
                try {
                    File logFile = new File(logFileName);
                    logFile.createNewFile();
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logFileName)), true);
                    pw.println(getPhoneInfo());
                    ex.printStackTrace(pw);
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

    }

    /*****
     * 保存错误信息到服务器：自己的服务器，或友盟
     *
     * @param context 上下文对象
     * @param ex      异常信息
     */
    private void logReportToRemote(Context context, Throwable ex) {
/*        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        //保存错误信息到服务器：自己的服务器，或友盟*/

    }

    /**
     * @return 返回手机的一些硬件信息
     */
    public static String getPhoneInfo() {
        StringBuilder sb = new StringBuilder(CAPACITY);
        sb.append("Build.BOARD=").append(Build.BOARD).append("\n");
        sb.append("Build.BRAND=").append(Build.BRAND).append("\n");
        sb.append("Build.DEVICE=").append(Build.DEVICE).append("\n");
        sb.append("Build.HARDWARE=").append(Build.HARDWARE).append("\n");
        sb.append("Build.ID=").append(Build.ID).append("\n");
        sb.append("Build.MODEL=").append(Build.MODEL).append("\n");
        sb.append("Build.MANUFACTURER=").append(Build.MANUFACTURER).append("\n");
        sb.append("Build.CPU_ABI=").append(Build.CPU_ABI).append("\n");
        sb.append("Build.CPU_ABI2=").append(Build.CPU_ABI2).append("\n");
        sb.append("Build.VERSION.SDK_INT=").append("" + Build.VERSION.SDK_INT).append("\n");
        sb.append("Build.VERSION.RELEASE=").append(Build.VERSION.RELEASE).append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] array = Build.SUPPORTED_ABIS;
            if(array!=null && array.length>0){
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0;i<array.length;i++){
                    stringBuilder.append(array[i]).append(" , ");
                }
                sb.append("Build.SUPPORTED_ABIS=").append(stringBuilder.toString()).append("\n");
            }

        }
        return sb.toString();
    }

    /**
     * 获取崩溃日志信息
     *
     * @param context 上下文
     * @return key:文件名 value:崩溃日志
     */
    public HashMap<String, String> getLog(Context context) {
        HashMap<String, String> logMap = new HashMap<String, String>();

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File logFolder = new File(SDCardUtils.LOG_FOLDER);
            if (logFolder != null && !logFolder.exists()) {//不存在崩溃日志
                return logMap;
            }

            File appFolder = new File(logFolder, context.getPackageName());
            if (appFolder != null && !appFolder.exists()) {//不存在崩溃日志
                return logMap;
            }

            String dir = appFolder.getAbsolutePath() + File.separator;
            String[] fileNames = appFolder.list();
            for (String fileName : fileNames) {//存在崩溃日志文件
                String path = dir + fileName;
                LogUtils.v("--------------- >>> 崩溃日志路径:" + path);
                String content = FileUtils.readFileToString(path);
                LogUtils.v("--------------- >>> 崩溃日志:" + content);
                if (!"".equals(content)) {//崩溃日志不为空
                    logMap.put(fileName, content);
                }
            }
        }

        return logMap;
    }

    /**
     * 删除日志
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return true:删除成功(崩溃日志路径不存在,当删除成功) false:删除失败
     */
    public boolean deleteLog(Context context, String fileName) {

        boolean isDelete = false;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File logFolder = new File(SDCardUtils.LOG_FOLDER);
            if (logFolder != null && !logFolder.exists()) {//不存在崩溃日志
                isDelete = true;
            }

            File appFolder = new File(logFolder, context.getPackageName());
            if (appFolder != null && !appFolder.exists()) {//不存在崩溃日志
                isDelete = true;
            }

            String path = appFolder.getAbsolutePath() + File.separator + fileName;
            LogUtils.v("--------------- >>> 删除崩溃日志路径:" + path + "\r\n\r\n");
            isDelete = FileUtils.deleteFile(path);
        }
        return isDelete;
    }

}
