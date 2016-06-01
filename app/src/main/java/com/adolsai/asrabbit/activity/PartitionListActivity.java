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
import com.adolsai.asrabbit.views.AsRabbitTitleBar;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.views.dialog.ClassifySelectorDialog;
import com.ht.baselib.views.dialog.OnClassifySelectListener;

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
        AdapterView.OnItemClickListener, AsRabbitTitleBar.AsRabbitTitleBarClick,
        OnClassifySelectListener {

    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout mMaterialRefreshLayout;
    @Bind(R.id.lv_partition_list)
    InnerSwipeListView lvPartitionList;
    @Bind(R.id.partition_title_bar)
    AsRabbitTitleBar partitionTitleBar;

    private PostAdapter postAdapter;
    private List<Post> postLists;

    private static Partition.BoardListBean boardListBean;//点击后从上一个界面传递过来的对象

    private static int currPage = 1;//当前页数

    private ClassifySelectorDialog classifySelectorDialog;

    private static List<String> firstCategoryList;
    private static List<List<String>> secondCategoryList;
    private static List<String> specialList;
    private static List<String> categoryList;


    //********************生命周期********************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_partition_list, savedInstanceState);

    }

    @Override
    protected void initViews() {
        initSelectorData();
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(mContext, postLists);
        lvPartitionList.setAdapter(postAdapter);
        partitionTitleBar.setIvBarLeftIcon(R.drawable.selector_titlebar_back);
        partitionTitleBar.setTvBarCenterTitle(boardListBean.getName());
        partitionTitleBar.setTvBarRightTips("检索");
        partitionTitleBar.setIvBarRightIconExpand(R.mipmap.ic_add_white_24dp);
        classifySelectorDialog = new ClassifySelectorDialog(mContext, this);
        classifySelectorDialog.setTitle("请选择检索条件");
        classifySelectorDialog.setFirstCategoryList(firstCategoryList);
        classifySelectorDialog.setSecondCategoryList(secondCategoryList);
        //设置是否上拉加载更多，默认是false，要手动改为true，要不然不会出现上拉加载
        mMaterialRefreshLayout.setLoadMore(true);

        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                currPage = 1;
                getDate(currPage);

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
        partitionTitleBar.setAsRabbitTitleBarClick(this);


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

    /**
     * titlebar点击事件
     *
     * @param v v
     */
    @Override
    public void barLeftIconClick(View v) {
        finish();
    }

    @Override
    public void barLeftTipsClick(View v) {

    }

    @Override
    public void barCenterTitleClick(View v) {

    }

    @Override
    public void barRightTipsClick(View v) {
        //检索，弹出滚轮
        if (classifySelectorDialog != null) {
            classifySelectorDialog.show();
        }
    }

    @Override
    public void barRightIconClick(View v) {
        //发布帖子

    }

    @Override
    public void barRightIconExpandClick(View v) {

    }

    /**
     * 滚轮选择
     *
     * @param firstCategoryIndex  一级类目
     * @param secondCategoryIndex 二级类目
     */
    @Override
    public void onSelected(int firstCategoryIndex, int secondCategoryIndex) {
        String currSelect = secondCategoryList.get(firstCategoryIndex).get(secondCategoryIndex);
        LogUtils.e("onSelected currSelect is " + currSelect);


    }

    @Override
    public void onUnSelect() {

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

    /**
     * 初始化滚轮的数据
     */
    private void initSelectorData() {
        firstCategoryList = new ArrayList<>();
        secondCategoryList = new ArrayList<>();
        specialList = new ArrayList<>();
        categoryList = new ArrayList<>();

        firstCategoryList.add("特殊");
        firstCategoryList.add("分类");

        specialList.add("套红区");
        specialList.add("加*区");
        specialList.add("精华区");

        categoryList.add("资料");
        categoryList.add("时政");
        categoryList.add("公告");
        categoryList.add("辟谣");

        secondCategoryList.add(specialList);
        secondCategoryList.add(categoryList);

    }


}
