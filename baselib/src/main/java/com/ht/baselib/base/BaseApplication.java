package com.ht.baselib.base;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ht.baselib.helper.ResourcesHelper;
import com.ht.baselib.manager.CrashManager;
import com.ht.baselib.manager.ImageLoaderManager;
import com.ht.baselib.utils.LocalDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>基础Application类 1、提供获取全局context方法；2、完成初始化配置</p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class BaseApplication extends Application {
    /**
     * 管理Activity的List,主要用于退出应用程序时（完全退出），Finish所有Activity;
     * 需要在每一个Activity的OnCreate,OnDestory中，正确调用addActivity和removeActivity
     */
    public static ArrayList<Activity> mActivityList = new ArrayList<>();

    private static BaseApplication instance;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dealMessage(msg);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化视图显示相关工具类
        LocalDisplay.init(this);
        //初始化资源帮助类
        ResourcesHelper.init(getResources());
        //初始化图片加载工具
        ImageLoaderManager.getInstance().init(this);
        CrashManager.getInstance().init(this);
    }


    /**
     * 获取全局Application实例
     *
     * @return Application实例
     */
    public static BaseApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return instance;
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity 当前实例
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            mActivityList.add(activity);
        }
    }

    /**
     * 移除容器中的Activity
     *
     * @param activity 当前实例
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityList.remove(activity);
        }
    }

    /**
     * 退出应用程序
     *
     * @param mContext 上下文
     * @param killOrNo 是否真退出：true是，false否
     */
    public static void baseExitApp(Context mContext, boolean killOrNo) {
        //若有多个桌面应用，会提示回到哪个桌面
        Intent intent9 = new Intent(Intent.ACTION_MAIN);
        intent9.addCategory(Intent.CATEGORY_HOME);
        intent9.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//结束历史栈
        mContext.startActivity(intent9);

        if (killOrNo) {//真退出
            List<Activity> lists = mActivityList;
            for (Activity a : lists) {
                a.finish();
            }

            //清除所有通知
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            System.exit(0);//真退出-结束整个java虚拟机
        }
    }

    /**
     * 获取句柄
     *
     * @return
     */
    public Handler getHandler() {

        return this.handler;
    }

    /**
     * 处理消息
     *
     * @param msg 消息
     */
    protected void dealMessage(Message msg) {
    }

}
