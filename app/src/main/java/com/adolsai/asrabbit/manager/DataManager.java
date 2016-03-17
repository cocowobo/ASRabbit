package com.adolsai.asrabbit.manager;

import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>PartitionManager类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/7 11:30)<br/>
 */
public class DataManager {

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

    /**
     * 获取历史数据
     *
     * @param listener 回调函数
     */
    public static void getHistoryPost(RequestListener listener) {
        List<Post> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new Post("闲情", "15分钟前", " @1L", "闲聊", "不响说什么了这是个杂谈"));
        list.add(new Post("闲情", "30分钟前", " @2L", "闲聊", "不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈"));
        list.add(new Post("闲情", "60分钟前", " @3L", "闲聊", "堂主你这个渣渣"));
        list.add(new Post("闲情", "一天前", " @4L", "闲聊", "自己造数据什么的烦死了，最讨厌了，麻烦"));
        list.add(new Post("闲情", "一周前", " @5L", "闲聊", "不知道写什么数据好了"));
        list.add(new Post("闲情", "一个月前", " @6L", "闲聊", "算了，哎。。。"));
        if (listener != null) {
            listener.getResult(list);
        }

    }

    /**
     * 获取收藏数据
     *
     * @param listener 回调函数
     */
    public static void getCollectionPost(RequestListener listener) {
        List<Post> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new Post("闲情", "15分钟前", " @1L", "收藏", "不响说什么了这是个杂谈"));
        list.add(new Post("闲情", "30分钟前", " @2L", "收藏", "不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈"));
        list.add(new Post("闲情", "60分钟前", " @3L", "收藏", "堂主你这个渣渣"));
        list.add(new Post("闲情", "一天前", " @4L", "收藏", "自己造数据什么的烦死了，最讨厌了，麻烦"));
        list.add(new Post("闲情", "一周前", " @5L", "收藏", "不知道写什么数据好了"));
        list.add(new Post("闲情", "一个月前", " @6L", "收藏", "算了，哎。。。"));
        if (listener != null) {
            listener.getResult(list);
        }

    }
}
