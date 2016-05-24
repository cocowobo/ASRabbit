package com.adolsai.asrabbit.manager;

import com.adolsai.asrabbit.listener.RequestListener;

/**
 * <p>PartitionManager类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/7 11:30)<br/>
 */
public class DataManager {

    /**
     * 获取分区列表数据
     *
     * @param listener 结果回调
     */
    public static void getPartition(final RequestListener listener) {
        if (listener != null) {
            listener.getResult(DataSourceManager.getPartitionData());
        }

    }


    /**
     * 获取历史数据
     *
     * @param listener 回调函数
     */
    public static void getHistoryPost(final RequestListener listener) {
        if (listener != null) {
            listener.getResult(DataSourceManager.getHistoryPostData());
        }
    }

    /**
     * 获取收藏数据
     *
     * @param listener 回调函数
     */
    public static void getFavouritePost(final RequestListener listener) {
        if (listener != null) {
            listener.getResult(DataSourceManager.getFavouritePostData());
        }

    }

    /**
     * 登陆方法
     *
     * @param account  账号
     * @param pwd      密码
     * @param listener 回调
     */
    public static void toLogin(final String account, final String pwd, final RequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

    }

    /**
     * 注册方法
     *
     * @param account  账号
     * @param pwd      密码
     * @param listener 回调
     */
    public static void toRegister(final String account, final String pwd, final RequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
