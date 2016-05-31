package com.adolsai.asrabbit.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.activity.PartitionListActivity;
import com.adolsai.asrabbit.adapter.PartitionAdapter;
import com.adolsai.asrabbit.app.GlobalStaticData;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.LocalDisplay;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.SoftInputMethodUtils;
import com.ht.baselib.views.dialog.CustomDialog;
import com.ht.baselib.views.swipemenulistview.SwipeMenu;
import com.ht.baselib.views.swipemenulistview.SwipeMenuCreator;
import com.ht.baselib.views.swipemenulistview.SwipeMenuItem;
import com.ht.baselib.views.swipemenulistview.SwipeMenuListView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by cjj on 2015/10/9.
 */
public class HomeFragment extends AsRabbitBaseFragment implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private static HomeFragment homeFragment;
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

    private CustomDialog customDialog;

    private PartitionAdapter partitionFavouriteAdapter;
    private PartitionAdapter partitionOtherAdapter;

    private static Object dataObj;//数据源

    private ArrayList<Partition.BoardListBean> favouriteLists;//喜欢的分区数据源
    private ArrayList<Partition.BoardListBean> otherLists;//其他的分区数据源


    public static HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }


    //******************生命周期*******************************************************************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_home);
        return mMainView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishRefreshLoadMore();
        }
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }


    @Override
    protected void initData() {
        getData();

    }

    @Override
    protected void initViews() {
        LogUtils.e("home initview");
        customDialog = CustomDialog.newLoadingInstance(activity);
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
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                getData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //上拉加载更多...
            }
        });


    }

    @Override
    public void backToFragment() {

    }


    //************************事件区*****************************************************************
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
        Partition.BoardListBean itemInfo = (Partition.BoardListBean) parent.getItemAtPosition(position);//确认取的是adapte中的动态值
        LogUtils.e("sharing", "itemInfo is " + itemInfo.getId());
        ActivityUtil.startActivity(activity, PartitionListActivity.createIntent(activity, itemInfo));
    }

    //****************自定义方法区********************************************************************

    /**
     * 获取数据
     */
    public void getData() {
        if (customDialog != null) {
            customDialog.show();
        }
        DataManager.getPartition(context, new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    dataObj = result;
                    dealDate(dataObj);
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
        if (result != null) {
            List<String> tempFavouriteIds = Hawk.get(GlobalStaticData.IS_FAVOURITE_PARTITION_IDS, new ArrayList<String>());
            List<Partition.BoardListBean> tempLists = (List) result;
            favouriteLists.clear();
            otherLists.clear();
            for (int i = 0; i < tempLists.size(); i++) {
                if (tempFavouriteIds.contains(tempLists.get(i).getId())) {
                    //喜欢的
                    favouriteLists.add(tempLists.get(i));
                } else {
                    //其他的
                    otherLists.add(tempLists.get(i));
                }

            }
            updateUI();
        }

    }

    /**
     * 更新UI
     */
    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (partitionFavouriteAdapter != null) {
                    partitionFavouriteAdapter.replaceAll(favouriteLists);
                }
                if (partitionOtherAdapter != null) {
                    partitionOtherAdapter.replaceAll(otherLists);
                }
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishRefreshLoadMore();
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
                Partition.BoardListBean itemInfo = partitionFavouriteAdapter.getAllItem().get(position);//确认取的是adapte中的动态值
                switch (index) {
                    case 0: {//删除
                        dealItemInfo(false, itemInfo);

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
                Partition.BoardListBean itemInfo = partitionOtherAdapter.getAllItem().get(position);//确认取的是adapte中的动态值
                switch (index) {
                    case 0: {//添加
                        dealItemInfo(true, itemInfo);
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


    /**
     * 处理添加和删除喜欢
     *
     * @param value true 喜欢 false 删除喜欢
     */
    /**
     * 处理添加和删除喜欢
     *
     * @param value true 喜欢 false 删除喜欢
     * @param item  要处理的数据
     */
    private void dealItemInfo(boolean value, Partition.BoardListBean item) {
        List<String> tempInfo = Hawk.
                get(GlobalStaticData.IS_FAVOURITE_PARTITION_IDS, new ArrayList<String>());
        if (value) {
            if (!tempInfo.contains(item.getId())) {
                tempInfo.add(item.getId());
            }
        } else {
            tempInfo.remove(item.getId());
        }

        Hawk.put(GlobalStaticData.IS_FAVOURITE_PARTITION_IDS, tempInfo);

        dealDate(dataObj);
    }

}
