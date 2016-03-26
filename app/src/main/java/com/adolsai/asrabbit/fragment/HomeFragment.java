package com.adolsai.asrabbit.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.PartitionAdapter;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.ht.baselib.utils.LocalDisplay;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.SoftInputMethodUtils;
import com.ht.baselib.views.materialview.MaterialRefreshLayout;
import com.ht.baselib.views.materialview.MaterialRefreshListener;
import com.ht.baselib.views.swipemenulistview.SwipeMenu;
import com.ht.baselib.views.swipemenulistview.SwipeMenuCreator;
import com.ht.baselib.views.swipemenulistview.SwipeMenuItem;
import com.ht.baselib.views.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cjj on 2015/10/9.
 */
public class HomeFragment extends Fragment implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private Context context;
    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout refreshLayout;
    @Bind(R.id.et_item_post_url)
    EditText etItemPostUrl;
    @Bind(R.id.tv_item_go)
    TextView tvItemGo;
    @Bind(R.id.inner_swipe_list_view_fragment0_favourite)
    InnerSwipeListView innerSwipeListViewFragment0Favourite;
    @Bind(R.id.inner_swipe_list_view_fragment0_other)
    InnerSwipeListView innerSwipeListViewFragment0Other;

    private PartitionAdapter partitionFavouriteAdapter;
    private PartitionAdapter partitionOtherAdapter;

    private List<Partition> favouriteLists;//喜欢的分区数据源
    private List<Partition> otherLists;//其他的分区数据源

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
        LogUtils.e("sharing", " HomeFragment onResume  ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        context = getContext();
        ButterKnife.bind(this, v);
        initView();
        getData(null);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e("sharing", "setUserVisibleHint isVisibleToUser is " + isVisibleToUser);
        if (isVisibleToUser) {
            //用户主动切回到该界面，相当于Activity的onResume

        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        favouriteLists = new ArrayList<>();
        otherLists = new ArrayList<>();
        partitionFavouriteAdapter = new PartitionAdapter(context, favouriteLists);
        partitionOtherAdapter = new PartitionAdapter(context, otherLists);
        innerSwipeListViewFragment0Favourite.setAdapter(partitionFavouriteAdapter);
        innerSwipeListViewFragment0Other.setAdapter(partitionOtherAdapter);
        initSwipeMenuListView();
        innerSwipeListViewFragment0Favourite.setOnItemClickListener(this);
        innerSwipeListViewFragment0Other.setOnItemClickListener(this);
        innerSwipeListViewFragment0Other.setOnItemClickListener(this);
        etItemPostUrl.setOnClickListener(this);
        tvItemGo.setOnClickListener(this);
        refreshLayout.setLoadMore(false);


        etItemPostUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //do something;
                    LogUtils.e("sharing", "搜索拉 ：" + etItemPostUrl.getText());
                    return true;
                }
                return false;
            }

        });
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新
                getData(new RequestListener() {
                    @Override
                    public void getResult(Object result) {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_item_post_url:
                LogUtils.e("sharing", "et_item_post_url click1");
                etItemPostUrl.post(new Runnable() {
                    @Override
                    public void run() {
                        etItemPostUrl.setFocusable(true);
                        etItemPostUrl.setFocusableInTouchMode(true);
                        etItemPostUrl.setCursorVisible(true);
                        SoftInputMethodUtils.showSoftInputMethod(context, etItemPostUrl);
                    }
                });

                tvItemGo.setText("取消");

                break;
            case R.id.tv_item_go:
                LogUtils.e("sharing", "tv_item_go click2");
                initThrough();

                break;

            default:

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Partition itemInfo = (Partition) parent.getItemAtPosition(position);//确认取的是adapte中的动态值
        LogUtils.e("sharing", "itemInfo is " + itemInfo.getTitle());
    }

    /**
     * 获取数据
     */
    public void getData(final RequestListener listener) {
        DataManager.getPartition(new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    dealDate(result);
                }
                if (listener != null) {
                    listener.getResult(true);
                }

            }
        });

    }

    /**
     * 处理数据源，把数据分成喜欢的和其他的
     *
     * @param result 数据源
     */
    private void dealDate(Object result) {
        List<Partition> tempLists = (List) result;
        favouriteLists.clear();
        otherLists.clear();
        for (int i = 0; i < tempLists.size(); i++) {
            if (tempLists.get(i).isFavourite()) {
                //喜欢的
                favouriteLists.add(tempLists.get(i));
                partitionFavouriteAdapter.replaceAll(favouriteLists);
            } else {
                //其他的
                otherLists.add(tempLists.get(i));
                partitionOtherAdapter.replaceAll(otherLists);
            }

        }

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
        // step 1. create a MenuCreator，添加左滑操作
        SwipeMenuCreator creator2 = new SwipeMenuCreator() {
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
                revokeItem.setTitle("喜欢");
                // set item title fontsize
                revokeItem.setTitleSize(16);
                // set item title font color
                revokeItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(revokeItem);
            }
        };
        // set creator
        innerSwipeListViewFragment0Favourite.setMenuCreator(creator1);
        innerSwipeListViewFragment0Other.setMenuCreator(creator2);
        // step 2. listener item click event
        innerSwipeListViewFragment0Favourite.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Partition itemInfo = partitionFavouriteAdapter.getAllItem().get(position);//确认取的是adapte中的动态值
                switch (index) {
                    case 0: {//删除
                        //TODO 更新数据源

                        //刷新界面
                        partitionFavouriteAdapter.remove(itemInfo);
                        partitionOtherAdapter.add(itemInfo);
                    }
                    break;
                    default:
                        break;
                }
                return false;
            }
        });
        innerSwipeListViewFragment0Other.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Partition itemInfo = partitionOtherAdapter.getAllItem().get(position);//确认取的是adapte中的动态值
                switch (index) {
                    case 0: {//添加
                        //TODO 更新数据源

                        //刷新界面
                        partitionOtherAdapter.remove(itemInfo);
                        partitionFavouriteAdapter.add(itemInfo);
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
     * 初始化直达
     */
    private void initThrough() {
        tvItemGo.setText("");
        etItemPostUrl.setText("");
        etItemPostUrl.setCursorVisible(false);
        SoftInputMethodUtils.hideSoftInputMethod(context, etItemPostUrl);
    }

}
