package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adolsai.asrabbit.R;
import com.ht.baselib.views.materialview.MaterialRefreshLayout;
import com.ht.baselib.views.materialview.MaterialRefreshListener;
import com.ht.baselib.views.viewselector.ReLoadCallbackListener;
import com.ht.baselib.views.viewselector.ViewSelectorLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by cjj on 2015/10/9.
 */
public class HomeFragment extends Fragment implements ReLoadCallbackListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout refreshLayout;
    private ViewSelectorLayout viewSelectorLayout;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, v);
        viewSelectorLayout = new ViewSelectorLayout(getActivity(), v);
        viewSelectorLayout.setReLoadCallbackListener(this);
        return viewSelectorLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Layout Managers:
        final StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Item Decorator:
//        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
//        recyclerView.setItemAnimator(new FadeInLeftAnimator());

//        /* Listeners */
//        recyclerView.setOnScrollListener(onScrollListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewSelectorLayout.show_LoadingView();
//        getBookData();

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
//                getBookData();
            }
        });
    }

    public void getBookData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };


    @Override
    public void onReLoadCallback() {
        viewSelectorLayout.show_LoadingView();
        getBookData();
    }
}
