package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.PostAdapter;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.ErrorModel;
import com.adolsai.asrabbit.model.Post;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.views.materialview.MaterialRefreshLayout;
import com.ht.baselib.views.materialview.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/9.
 */
public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout mMaterialRefreshLayout;
    @Bind(R.id.lv_history)
    InnerSwipeListView lvHistory;

    private PostAdapter postAdapter;
    private List<Post> postLists;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, null);
        ButterKnife.bind(this, v);
        initView();
        getDate(null);
        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getDate(final RequestListener listener) {
        DataManager.getHistoryPost(new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    if (result instanceof ErrorModel) {
                        //错误处理
                    } else {
                        dealDate(result);
                    }

                }
                if (listener != null) {
                    listener.getResult(true);
                }
            }
        });
    }

    private void initView() {
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postLists);
        lvHistory.setAdapter(postAdapter);
        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mMaterialRefreshLayout.finishRefresh();
            }
        });
        lvHistory.setOnItemClickListener(this);
    }


    private void dealDate(Object result) {
        postLists.clear();
        postLists.addAll((List) result);
        postAdapter.replaceAll(postLists);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post currPost = (Post) parent.getItemAtPosition(position);
        LogUtils.e("onItemClick currPost is " + currPost.getContent());

    }
}
