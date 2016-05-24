package com.adolsai.asrabbit.manager;

import com.adolsai.asrabbit.model.FavouritePost;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>DataSourceManager类 数据源接口</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-23 16:48)<br/>
 */
public class DataSourceManager {

    /**
     * 获取首页数据源
     *
     * @return
     */
    public static List<Partition> getPartitionData() {
        List<Partition> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new Partition("1", "创作交流", true));
        list.add(new Partition("2", "风雨读书声", true));
        list.add(new Partition("3", "自由交易区", true));
        list.add(new Partition("4", "评论专区", true));
        list.add(new Partition("5", "连载文库", true));
        list.add(new Partition("6", "网友留言区", true));
        list.add(new Partition("11", "图画乐园", false));
        list.add(new Partition("12", "包月论坛", false));
        list.add(new Partition("13", "妈咪宝贝", false));
        list.add(new Partition("14", "名将传说", false));
        list.add(new Partition("15", "凤凰觉", false));
        list.add(new Partition("16", "流光水社", false));
        return list;
    }

    /**
     * 获取历史业数据源
     *
     * @return
     */
    public static List<Post> getHistoryPostData() {
        List<Post> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new Post("闲情", "15分钟前", " @1L", "闲聊", "不响说什么了这是个杂谈"));
        list.add(new Post("闲情", "30分钟前", " @2L", "闲聊", "不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈"));
        list.add(new Post("闲情", "60分钟前", " @3L", "闲聊", "堂主你这个渣渣"));
        list.add(new Post("闲情", "一天前", " @4L", "闲聊", "自己造数据什么的烦死了，最讨厌了，麻烦"));
        list.add(new Post("闲情", "一周前", " @5L", "闲聊", "不知道写什么数据好了"));
        list.add(new Post("闲情", "一个月前", " @6L", "闲聊", "算了，哎。。。"));
        return list;
    }

    /**
     * 获取收藏页数据源
     *
     * @return
     */
    public static List<FavouritePost> getFavouritePostData() {
        List<FavouritePost> list = new ArrayList<>();
        //TODO 获取数据源
        list.add(new FavouritePost("闲情", "15分钟前", " @1L", "收藏", "不响说什么了这是个杂谈"));
        list.add(new FavouritePost("闲情", "30分钟前", " @2L", "收藏", "不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈，不响说什么了这是个杂谈"));
        list.add(new FavouritePost("闲情", "60分钟前", " @3L", "收藏", "堂主你这个渣渣"));
        list.add(new FavouritePost("闲情", "一天前", " @4L", "收藏", "自己造数据什么的烦死了，最讨厌了，麻烦"));
        list.add(new FavouritePost("闲情", "一周前", " @5L", "收藏", "不知道写什么数据好了"));
        list.add(new FavouritePost("闲情", "一个月前", " @6L", "收藏", "算了，哎。。。"));
        return list;
    }

}
