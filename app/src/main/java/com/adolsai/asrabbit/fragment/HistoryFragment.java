package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.PostAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Post;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.views.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/10/9.
 */
public class HistoryFragment extends AsRabbitBaseFragment implements AdapterView.OnItemClickListener {
    private static HistoryFragment HisFragment;
    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout mMaterialRefreshLayout;
    @Bind(R.id.lv_history)
    InnerSwipeListView lvHistory;

    private CustomDialog customDialog;
    private PostAdapter postAdapter;
    private List<Post> postLists;


    /**
     * 构造方法
     *
     * @return HistoryFragment
     */
    public static HistoryFragment getInstance() {
        if (HisFragment == null) {
            HisFragment = new HistoryFragment();
        }
        return HisFragment;
    }

    //*********************生命周期******************************************************************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_history);
        return mMainView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMaterialRefreshLayout != null) {
            mMaterialRefreshLayout.finishRefresh();
            mMaterialRefreshLayout.finishRefreshLoadMore();
        }
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }


    @Override
    protected void initData() {
        getDate();
    }

    @Override
    protected void initViews() {
        LogUtils.e("history initview");
        customDialog = CustomDialog.newLoadingInstance(activity);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postLists);
        lvHistory.setAdapter(postAdapter);
        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getDate();

            }
        });
        lvHistory.setOnItemClickListener(this);
    }

    @Override
    public void backToFragment() {
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
    private void getDate() {
        if (customDialog != null) {
            customDialog.show();
        }
        DataManager.getHistoryPost(new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    dealDate(result);
                }
            }
        });
    }


    /**
     * 数据处理
     *
     * @param result result
     */
    private void dealDate(Object result) {
        postLists.clear();
        postLists.addAll((List) result);
        updateUI();

    }

    /**
     * 更新UI
     */
    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (postAdapter != null) {
                    postAdapter.replaceAll(postLists);
                }
                if (mMaterialRefreshLayout != null) {
                    mMaterialRefreshLayout.finishRefresh();
                }
                if (customDialog != null) {
                    customDialog.dismiss();
                }
            }
        });

    }

}
