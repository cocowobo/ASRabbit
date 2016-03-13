package com.adolsai.asrabbit.manager;

import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.model.Partition;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>PartitionManager类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/7 11:30)<br/>
 */
public class PartitionManager {

    /**
     * 获取喜欢的分区列表数据
     *
     * @param listener 结果回调
     */
    public static void getFavouritePartition(RequestListener listener) {
        List<Partition> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new Partition("1", "创作交流"));
        list.add(new Partition("2", "风雨读书声"));
        list.add(new Partition("3", "自由交易区"));
        list.add(new Partition("4", "评论专区"));
        list.add(new Partition("5", "连载文库"));
        list.add(new Partition("6", "网友留言区"));
        if (listener != null) {
            listener.getResult(list);
        }


    }

    /**
     * 获取其他数据集
     *
     * @param listener 结果回调
     */
    public static void getOtherPartition(RequestListener listener) {
        List<Partition> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new Partition("11", "图画乐园"));
        list.add(new Partition("12", "包月论坛"));
        list.add(new Partition("13", "妈咪宝贝"));
        list.add(new Partition("14", "名将传说"));
        list.add(new Partition("15", "凤凰觉"));
        list.add(new Partition("16", "流光水社"));
        if (listener != null) {
            listener.getResult(list);
        }
    }
}
