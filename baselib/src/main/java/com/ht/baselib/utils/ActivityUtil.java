package com.ht.baselib.utils;

import android.app.Activity;
import android.content.Intent;

import com.ht.baselib.R;


/**
 * Msg: Activity跳转帮助类 1、提供Activity跳转的方法；2、可通过配置，改变默认动画
 * Update:  2015/9/8
 * Version: 1.0
 * Created by hxm on 2015/9/8 11:47.
 */
public class ActivityUtil {
    private static int startInAnimId = R.anim.activity_in_from_right;//打开界面进入Activity的动画的id
    private static int startOutAnimId = R.anim.activity_out_to_left;//打开界面离开Activity的动画id

    private static int stopInAnimId = R.anim.activity_in_from_left;//关闭界面进入Activity的动画的id
    private static int stopOutAnimId = R.anim.activity_out_to_right;//关闭界面离开Activity的动画id

    /**
     * 初始化默认的启动动画
     *
     * @param startInAnimIdValue  startInAnimIdValue
     * @param startOutAnimIdValue startOutAnimIdValue
     */
    public static void initStartAnim(int startInAnimIdValue, int startOutAnimIdValue) {
        startInAnimId = startInAnimIdValue;
        startOutAnimId = startOutAnimIdValue;
    }

    /**
     * 初始化默认的关闭动画
     *
     * @param stopInAnimIdValue  stopInAnimIdValue
     * @param stopOutAnimIdValue stopOutAnimIdValue
     */
    public static void initStopAnim(int stopInAnimIdValue, int stopOutAnimIdValue) {
        stopInAnimId = stopInAnimIdValue;
        stopOutAnimId = stopOutAnimIdValue;
    }

    /**
     * 正常启动Activity
     *
     * @param activity 上下文
     * @param clazz    待启动activity
     */
    public static void startActivity(Activity activity, Class<? extends Activity> clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    /**
     * 正常启动Activity
     *
     * @param activity 上下文
     * @param i        intent
     */
    public static void startActivity(Activity activity, Intent i) {
        activity.startActivity(i);
    }

    /**
     * 正常启动Activity
     *
     * @param activity 上下文
     * @param i        intent
     * @param result   回调结果标识
     */
    public static void startActivity(Activity activity, Intent i, int result) {
        activity.startActivityForResult(i, result);
    }

    /**
     * 正常结束Activity
     *
     * @param activity 上下文
     */
    public static void stopActivity(Activity activity) {
        activity.finish();
    }

    /**
     * 使用默认动画启动Activity
     *
     * @param activity 上下文
     * @param clazz    待启动activity
     */
    public static void startActivityByAnim(Activity activity, Class<? extends Activity> clazz) {
        startActivity(activity, clazz);
        activity.overridePendingTransition(startInAnimId, startOutAnimId);
    }

    /**
     * 使用默认动画启动Activity
     *
     * @param activity 上下文
     * @param i        intent
     */
    public static void startActivityByAnim(Activity activity, Intent i) {
        startActivity(activity, i);
        activity.overridePendingTransition(startInAnimId, startOutAnimId);
    }

    /**
     * 使用默认动画启动Activity
     *
     * @param activity 上下文
     * @param i        itent
     * @param result   回调结果标识
     */
    public static void startActivityByAnim(Activity activity, Intent i, int result) {
        startActivity(activity, i, result);
        activity.overridePendingTransition(startInAnimId, startOutAnimId);
    }

    /**
     * 使用自定义动画启动Activity
     *
     * @param activity       上下文
     * @param clazz          待启动activity
     * @param startInAnimId  进入动画资源Id
     * @param startOutAnimId 退出动画资源Id
     */
    public static void startActivityByAnim(Activity activity, Class<? extends Activity> clazz, int startInAnimId, int startOutAnimId) {
        startActivity(activity, clazz);
        activity.overridePendingTransition(startInAnimId, startOutAnimId);
    }

    /**
     * 使用自定义动画启动Activity
     *
     * @param activity       上下文
     * @param i              intent
     * @param startInAnimId  进入动画资源Id
     * @param startOutAnimId 退出动画资源Id
     */
    public static void startActivityByAnim(Activity activity, Intent i, int startInAnimId, int startOutAnimId) {
        startActivity(activity, i);
        activity.overridePendingTransition(startInAnimId, startOutAnimId);
    }

    /**
     * 使用自定义动画启动Activity
     *
     * @param activity       上下文
     * @param i              intent
     * @param resultcode     回调结果标识
     * @param startInAnimId  进入动画资源Id
     * @param startOutAnimId 退出动画资源Id
     */
    public static void startActivityByAnim(Activity activity, Intent i, int resultcode, int startInAnimId, int startOutAnimId) {
        startActivity(activity, i, resultcode);
        activity.overridePendingTransition(startInAnimId, startOutAnimId);
    }

    /**
     * 启动默认动画关闭Activity
     *
     * @param activity 上下文
     */
    public static void stopActivityByAnim(Activity activity) {
        stopActivity(activity);
        activity.overridePendingTransition(stopInAnimId, stopOutAnimId);
    }

    /**
     * 启动自定义动画关闭Activity
     *
     * @param activity     上下文
     * @param stopInAnimId 进入动画资源Id
     * @param stoOutAnimId 退出动画资源Id
     */
    public static void stopActivityByAnim(Activity activity, int stopInAnimId, int stoOutAnimId) {
        stopActivity(activity);
        activity.overridePendingTransition(stopInAnimId, stoOutAnimId);
    }


}
