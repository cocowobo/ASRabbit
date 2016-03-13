package com.ht.baselib.views.swipemenulistview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Msg: 侧滑菜单管理类   提供了添加菜单  删除菜单
 * Update:  2015/8/27
 * Version: 1.0
 * Created by hxm on 2015/8/27 13:46.
 */
public class SwipeMenu {

    private Context mContext;
    private List<SwipeMenuItem> mItems;
    private int mViewType;

    public SwipeMenu(Context context) {
        mContext = context;
        mItems = new ArrayList<SwipeMenuItem>();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 添加侧滑的菜单
     *
     * @param item
     */
    public void addMenuItem(SwipeMenuItem item) {
        mItems.add(item);
    }

    public void removeMenuItem(SwipeMenuItem item) {
        mItems.remove(item);
    }

    public List<SwipeMenuItem> getMenuItems() {
        return mItems;
    }

    public SwipeMenuItem getMenuItem(int index) {
        return mItems.get(index);
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }

}
