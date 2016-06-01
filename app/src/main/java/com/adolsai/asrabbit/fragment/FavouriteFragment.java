package com.adolsai.asrabbit.fragment;

import android.graphics.Color;
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
import com.ht.baselib.utils.LocalDisplay;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.views.dialog.CustomDialog;
import com.ht.baselib.views.swipemenulistview.SwipeMenu;
import com.ht.baselib.views.swipemenulistview.SwipeMenuCreator;
import com.ht.baselib.views.swipemenulistview.SwipeMenuItem;
import com.ht.baselib.views.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/10/9.
 */
public class FavouriteFragment extends AsRabbitBaseFragment implements AdapterView.OnItemClickListener {
    private static FavouriteFragment FavFragment;
    @Bind(R.id.lv_favourite)
    InnerSwipeListView lvFavourite;


    private CustomDialog customDialog;
    private PostAdapter postAdapter;
    private List<Post> postLists;


    /**
     * 构造方法
     *
     * @return HistoryFragment
     */
    public static FavouriteFragment getInstance() {
        FavFragment = new FavouriteFragment();
        return FavFragment;
    }

    //*********************生命周期******************************************************************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_favourite);
        return mMainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void initViews() {
        customDialog = CustomDialog.newLoadingInstance(activity);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postLists);
        lvFavourite.setAdapter(postAdapter);
        initSwipeMenuListView();
        lvFavourite.setOnItemClickListener(this);
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
                if (customDialog != null) {
                    customDialog.dismiss();
                }
            }
        });

    }

    /**
     * 初始化左滑删除的一些监听和设置
     */
    private void initSwipeMenuListView() {
        // step 1. create a MenuCreator，添加左滑操作
        SwipeMenuCreator creator1 = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建撤销认证item
                // create "revoke" item
                SwipeMenuItem revokeItem = new SwipeMenuItem(context);
                // set item background
                revokeItem.setBackground(R.color.global_price_orange);
                // set item width
                revokeItem.setWidth(LocalDisplay.dp2px(80));
                // set item title
                revokeItem.setTitle("删除");
                // set item title fontsize
                revokeItem.setTitleSize(16);
                // set item title font color
                revokeItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(revokeItem);
            }
        };
        // set creator
        lvFavourite.setMenuCreator(creator1);
        // step 2. listener item click event
        lvFavourite.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Post itemInfo = postAdapter.getAllItem().get(position);//确认取的是adapte中的动态值
                switch (index) {
                    case 0: {//删除

                    }
                    break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}
