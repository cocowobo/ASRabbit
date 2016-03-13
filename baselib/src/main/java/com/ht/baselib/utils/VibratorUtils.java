package com.ht.baselib.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * <p>震动工具类</p>
 *
 * @author 王多新
 * @version 1.0 (2015-10-19)
 */
public class VibratorUtils {

    /**
     * 震动器
     */
    private static Vibrator mVibrator;

    /**
     * 默认震动间隔
     */
    private static final Integer VIBRATE_DURATION = 10;

    /**
     * 初始化震动器
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 默认 10ms
     */
    public static void vibrator() {
        mVibrator.vibrate(VIBRATE_DURATION);
    }

    /**
     * 自定与震动时间
     *
     * @param volue 时间/毫秒
     */
    public static void vibrator(Integer volue) {
        mVibrator.vibrate(volue);
    }

    /**
     * 重复次数震动
     *
     * @param l     震动组,节奏数组
     * @param volue 重复次数, -1不重复,非-1从pattern指定下标开始重复
     */
    public static void vibrator(long[] l, Integer volue) {
        mVibrator.vibrate(l, volue);
    }

    /**
     * 撤销震动
     */
    public static void cancel() {
        if (null != mVibrator) {
            mVibrator.cancel();
        }
    }
}
