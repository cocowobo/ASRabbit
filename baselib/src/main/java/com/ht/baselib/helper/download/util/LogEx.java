
package com.ht.baselib.helper.download.util;

import com.ht.baselib.helper.download.interfaces.ILog;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Msg:日志类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class LogEx {

    private static boolean isDebug = true;

    /**
     * 设置是否打印调试信息
     * 
     * @param debug 
     */
    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    private static boolean isSaveLog;

    /**
     * 设置是否保存日志
     * 
     * @param saveLog 是否保存日志
     */
    public static void setSaveLog(boolean saveLog) {
        isSaveLog = saveLog;
    }

    private static String logFilePath;

    public static void setLogFilePath(String filePath) {
        logFilePath = filePath;
    }

    private static ILog mCustomLog;

    public static void setCustomLog(ILog customLog) {
        mCustomLog = customLog;
    }

    /**
     * 打印信息
     * 
     * @param msg 要打印的信息
     */
    public static void d(String msg) {
        final String tag = "download";
        d(tag, msg);
    }

    private static void d(String tag, String msg) {
        if (isDebug) {
            if (mCustomLog != null) {
                mCustomLog.d(tag, msg);
            } else {
                System.out.println(tag + ":" + msg);
            }

            if (isSaveLog) {
                if (logFilePath != null && !"".equals(logFilePath)) {
                    try {
                        File file = new File(logFilePath);
                        if (!file.exists()) {
                            // 如果路径不存在，先创建路径
                            File mFile = file.getParentFile();
                            if (!mFile.exists()) {
                                mFile.mkdirs();
                            }
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file, true);
                        fos.write((msg + "\n").getBytes());
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
