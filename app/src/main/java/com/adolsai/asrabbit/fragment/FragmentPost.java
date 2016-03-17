package com.adolsai.asrabbit.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.PostAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.model.Post;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.ht.baselib.utils.LocalDisplay;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.views.swipemenulistview.SwipeMenu;
import com.ht.baselib.views.swipemenulistview.SwipeMenuCreator;
import com.ht.baselib.views.swipemenulistview.SwipeMenuItem;
import com.ht.baselib.views.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>FragmentPost类 1、提供给fragment1和fragment2继承</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-15 10:58)<br/>
 */
public class FragmentPost extends AsRabbitBaseFragment implements
        View.OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {
    @Bind(R.id.as_rabbit_title_bar)
    AsRabbitTitleBar asRabbitTitleBar;
    @Bind(R.id.lv_history)
    InnerSwipeListView lvHistory;

    protected PostAdapter postAdapter;
    protected List<Post> lists;
    protected List<Post> tempLists;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_post);
        ButterKnife.bind(this, mMainView);
        return mMainView;
    }


    @Override
    protected void initViews() {
        lvHistory.setTextFilterEnabled(true);
        lvHistory.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {
        tempLists = new ArrayList<>();
        lists = new ArrayList<>();
        postAdapter = new PostAdapter(mContext, lists);
        lvHistory.setAdapter(postAdapter);
        initSwipeMenuListView();
        refreshDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 刷新数据
     */
    public void refreshDate() {
    }

    private List<Post> searchDate(String value) {
        tempLists.clear();
        for (int i = 0; i < lists.size(); i++) {
            Post currPost = lists.get(i);
            if (currPost != null && currPost.getContent().contains(value)) {
                tempLists.add(currPost);
            }
        }
        return tempLists;

    }

    /**
     * 初始化左滑删除的一些监听和设置
     */
    private void initSwipeMenuListView() {
        // step 1. create a MenuCreator，添加左滑操作
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //创建撤销认证item
                // create "revoke" item
                SwipeMenuItem revokeItem = new SwipeMenuItem(mContext);
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
        lvHistory.setMenuCreator(creator);
        // step 2. listener item click event
        lvHistory.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Post itemInfo = postAdapter.getAllItem().get(position);//确认取的是adapte中的动态值
                switch (index) {
                    case 0: {//删除
                        //TODO 更新数据源
                        updateAfterDelete(itemInfo);
                        //刷新界面
                        postAdapter.remove(itemInfo);
                    }
                    break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 删除数据后，刷新数据源
     *
     * @param post
     */
    protected void updateAfterDelete(Post post) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post currPost = (Post) parent.getItemAtPosition(position);//确认取的是adapte中的动态值
        LogUtils.e("sharing", "currPost is " + currPost.getContent());

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
//                    lvHistory.clearTextFilter();
            postAdapter.replaceAll(lists);
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤,根据输入，加载数据源
            List<Post> queryList = searchDate(newText);
            postAdapter.replaceAll(queryList);
//                    lvHistory.setFilterText(newText);
        }
        return false;
    }


}
