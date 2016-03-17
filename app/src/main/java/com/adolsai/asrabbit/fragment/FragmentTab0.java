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
import com.adolsai.asrabbit.activity.SettingActivity;
import com.adolsai.asrabbit.adapter.PartitionAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Partition;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;
import com.adolsai.asrabbit.views.InnerSwipeListView;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.LocalDisplay;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.SoftInputMethodUtils;
import com.ht.baselib.views.swipemenulistview.SwipeMenu;
import com.ht.baselib.views.swipemenulistview.SwipeMenuCreator;
import com.ht.baselib.views.swipemenulistview.SwipeMenuItem;
import com.ht.baselib.views.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentTab0 extends AsRabbitBaseFragment implements
        View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.as_rabbit_title_bar)
    AsRabbitTitleBar asRabbitTitleBar;
    @Bind(R.id.inner_swipe_list_view_fragment0_favourite)
    InnerSwipeListView innerSwipeListViewFragment0Favourite;
    @Bind(R.id.inner_swipe_list_view_fragment0_other)
    InnerSwipeListView innerSwipeListViewFragment0Other;
    @Bind(R.id.et_item_post_url)
    EditText etItemPostUrl;
    @Bind(R.id.tv_item_go)
    TextView tvItemGo;

    private PartitionAdapter partitionFavouriteAdapter;
    private PartitionAdapter partitionOtherAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_tab0);
        ButterKnife.bind(this, mMainView);
        return mMainView;

    }

    @Override
    protected void initViews() {
        //初始化标题栏
        asRabbitTitleBar.setIvBarLeftIcon(R.mipmap.icon_carrot);
        asRabbitTitleBar.setTvBarCenterTitle(getString(R.string.fragment0_title));
        asRabbitTitleBar.setIvBarRightIcon(R.mipmap.icon_setting);
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


        asRabbitTitleBar.setAsRabbitTitleBarClick(new AsRabbitTitleBar.AsRabbitTitleBarClick() {
            @Override
            public void barLeftIconClick(View v) {

            }

            @Override
            public void barLeftTipsClick(View v) {

            }

            @Override
            public void barCenterTitleClick(View v) {

            }

            @Override
            public void barRightTipsClick(View v) {

            }

            @Override
            public void barRightIconClick(View v) {
                //跳转到设置界面
                ActivityUtil.startActivityByAnim(getActivity(), SettingActivity.class);

            }

            @Override
            public void barRightIconExpandClick(View v) {

            }
        });

    }

    @Override
    protected void initData() {
        partitionFavouriteAdapter = new PartitionAdapter(mContext, new ArrayList<Partition>());
        partitionOtherAdapter = new PartitionAdapter(mContext, new ArrayList<Partition>());
        innerSwipeListViewFragment0Favourite.setAdapter(partitionFavouriteAdapter);
        innerSwipeListViewFragment0Other.setAdapter(partitionOtherAdapter);
        initSwipeMenuListView();
        refreshDate();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void refreshDate() {
        DataManager.getFavouritePartition(new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    partitionFavouriteAdapter.clear();
                    partitionFavouriteAdapter.addAll((List) result);
                }

            }
        });

        DataManager.getOtherPartition(new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    partitionOtherAdapter.clear();
                    partitionOtherAdapter.addAll((List) result);
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
        // step 1. create a MenuCreator，添加左滑操作
        SwipeMenuCreator creator2 = new SwipeMenuCreator() {
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
                        SoftInputMethodUtils.showSoftInputMethod(mContext, etItemPostUrl);
                    }
                });

                tvItemGo.setText("取消");

                break;
            case R.id.tv_item_go:
                LogUtils.e("sharing", "tv_item_go click2");
                tvItemGo.setText("");
                etItemPostUrl.setText("");
                etItemPostUrl.setCursorVisible(false);
                SoftInputMethodUtils.hideSoftInputMethod(mContext, etItemPostUrl);

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


}
