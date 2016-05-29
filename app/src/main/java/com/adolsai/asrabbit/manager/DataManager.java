package com.adolsai.asrabbit.manager;

import android.content.Context;

import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.model.Partition;

import java.util.List;

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
     * @param context  上下文
     * @param listener 结果回调
     */
    public static void getPartition(final Context context, final RequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Partition.BoardListBean> results = DataSourceManager.getPartitionData(context);
                if (listener != null && results != null) {
                    listener.getResult(results);
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

    /**
     * 根据关键字搜索
     *
     * @param key      关键字
     * @param listener 回调
     */
    public static void searchByKey(final String key, final RequestListener listener) {

    }

}
