package com.adolsai.asrabbit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.PostAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataSourceManager;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.model.Post;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ht.baselib.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <p>PartitionListActivity类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-30 9:59)<br/>
 */
public class PartitionListActivity extends AsRabbitBaseActivity implements
        AdapterView.OnItemClickListener {

    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout mMaterialRefreshLayout;
    @Bind(R.id.lv_partition_list)
    InnerSwipeListView lvPartitionList;

    private PostAdapter postAdapter;
    private List<Post> postLists;

    private static Partition.BoardListBean boardListBean;//点击后从上一个界面传递过来的对象

    private static int currPage = 1;//当前页数


    //********************生命周期********************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_partition_list, savedInstanceState);

    }

    @Override
    protected void initViews() {
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(mContext, postLists);
        lvPartitionList.setAdapter(postAdapter);
        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                getDate(1);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                //上拉加载更多...
                currPage = currPage + 1;
                getDate(currPage);
            }
        });

        lvPartitionList.setOnItemClickListener(this);


    }

    @Override
    protected void initData() {
        if (boardListBean != null) {
            DataSourceManager.getBroadListData(boardListBean.getId(), currPage, new RequestListener() {
                @Override
                public void getResult(Object result) {
                    LogUtils.d("getBroadListData result is " + result.toString());

                }
            });
        }
    }
    //********************对外方法区******************************************************************

    public static Intent createIntent(Context context, Partition.BoardListBean item) {
        Intent intent = new Intent(context, PartitionListActivity.class);
        boardListBean = item;
        return intent;
    }

    //********************事件区*********************************************************************
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post currPost = (Post) parent.getItemAtPosition(position);
        LogUtils.e("onItemClick currPost is " + currPost.getContent());

    }

    //*************自定义方法*************************************************************************

    /**
     * 获取数据
     */
    private void getDate(int page) {
        if (boardListBean != null) {
            //根据上面传递过来的获取数据
        }

    }


    /**
     * 数据处理
     *
     * @param result     result
     * @param isNextPage true 下一业数据
     */
    private void dealDate(Object result, boolean isNextPage) {
        if (!isNextPage) {
            postLists.clear();
        }
        postLists.addAll((List) result);
        updateUI();

    }

    /**
     * 更新UI
     */
    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (postAdapter != null) {
                    postAdapter.replaceAll(postLists);
                }
                if (mMaterialRefreshLayout != null) {
                    mMaterialRefreshLayout.finishRefresh();
                    mMaterialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });

    }

}
