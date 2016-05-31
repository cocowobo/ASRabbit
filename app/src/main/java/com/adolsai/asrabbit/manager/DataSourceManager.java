package com.adolsai.asrabbit.manager;

import android.content.Context;

import com.adolsai.asrabbit.app.GlobalUrl;
import com.adolsai.asrabbit.app.GlobalUrlId;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.model.FavouritePost;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.model.Post;
import com.ht.baselib.network.Command;
import com.ht.baselib.network.JsonCommand;
import com.ht.baselib.network.RspListener;
import com.ht.baselib.network.TaskManager;
import com.ht.baselib.utils.GsonUtil;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.ResourceUtils;

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
    public static List<Partition.BoardListBean> getPartitionData(Context context) {
        List<Partition.BoardListBean> list = new ArrayList<>();
        //从本地json中获取数据源
        String result = ResourceUtils.getFileFromAssets(context, "BoardList.json");
        //解析数据源
        Partition currPartition = GsonUtil.jsonToBean(result, Partition.class);
        if (currPartition != null) {
            list.addAll(currPartition.getBoardList());
        }
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

    /**
     * 清除个人收藏数据
     *
     * @param listener 回调
     */
    public static void cleanFavouriteData(RequestListener listener) {

    }

    /**
     * 清除历史数据
     *
     * @param listener 回调
     */
    public static void cleanHistoryData(RequestListener listener) {

    }


    public static void getBroadListData(String broadId, int page, final RequestListener listener) {
        JsonCommand jsonCommand = new JsonCommand(GlobalUrlId.BroadListDataId,
                GlobalUrl.getBoardUrlByIdAndPage(broadId, page), new RspListener() {
            @Override
            public void onSuccess(Command cmd, Object obj) {
                LogUtils.e("getBroadListData onSuccess obj is " + obj.toString());


            }

            @Override
            public void onFailure(Command cmd) {
                LogUtils.e("getBroadListData onFailure");
            }
        });
        TaskManager.getInstance().addCommand(jsonCommand);

    }


}
