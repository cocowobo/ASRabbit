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
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.getResult(DataSourceManager.getPartitionData());
                }
            }
        }).start();


    }


    /**
     * 获取历史数据
     *
     * @param listener 回调函数
     */
    public static void getHistoryPost(final RequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (listener != null) {
                    listener.getResult(DataSourceManager.getHistoryPostData());
                }
            }
        }).start();


    }

    /**
     * 获取收藏数据
     *
     * @param listener 回调函数
     */
    public static void getFavouritePost(final RequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.getResult(DataSourceManager.getFavouritePostData());
                }
            }
        }).start();
    }
}
