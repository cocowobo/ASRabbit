package com.ht.baselib.utils;

import android.util.Log;

import com.ht.baselib.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>统一日志打印类</p>
 *
 * @author 王多新
 * @version 1.0 (2015-10-19)
 */
public class LogUtils {

    /**
     * 是否打印日志  调试过程中设置为 true , 发包的时候设置为false
     * <br/>应用层在application初始化
     */
    public static boolean isDebug = BuildConfig.DEBUG;

    /**
     * 默认TAG
     */
    private static final String TAG = "---LogUtils---";

    /**
     * d级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * i级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    /**
     * v级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }


    /**
     * e级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    /**
     * d级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    /**
     * i级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * v级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    /**
     * e级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }


    /**
     * e级别  在控制台输出并写入SD卡log日志
     *
     * @param exc Exception
     */
    public static void we(Exception exc) {
      
        if (isDebug) {
            exc.printStackTrace();

            StringWriter sw = new StringWriter();
            exc.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String content = time + System.getProperty("line.separator") + str + System.getProperty("line.separator");
            FileOutputStream fos = null;

            try {
                File file = new File(SDCardUtils.LOG_FOLDER, "log.txt");
                fos = new FileOutputStream(file, true);
                fos.write(content.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * v级别  在控制台输出并写入SD卡log日志
     *
     * @param info 显示信息
     */
    public static void wv(String info) {
        if (isDebug) {
            v(info);
            
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String content = time + System.getProperty("line.separator") + info + System.getProperty("line.separator");
            FileOutputStream fos = null;

            try {
                File file = new File(SDCardUtils.LOG_FOLDER, "log.txt");
                fos = new FileOutputStream(file, true);
                fos.write(content.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
