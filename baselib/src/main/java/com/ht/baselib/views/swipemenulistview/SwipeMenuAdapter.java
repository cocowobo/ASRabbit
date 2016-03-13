package com.ht.baselib.views.swipemenulistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.WrapperListAdapter;


import com.ht.baselib.R;

import java.util.List;

/**
 * Msg: SwipeMenuListView 适配器
 * Update:  2015/9/2
 * Version: 1.0
 */
public class SwipeMenuAdapter implements WrapperListAdapter,
        SwipeMenuView.OnSwipeItemClickListener {

    private ListAdapter mAdapter;
    private Context mContext;
    private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;


    //是否需要动态改变菜单的数量  如果需要设置为true 取需要在自己的adapter中有一个getMenuList方法
    public boolean changeMenuCount = false;
    private List<String> menuList;  //判断菜单是否需要显示  装着需要显示的菜单的文本
    public String menuColor; //需要改变的view的背景
    public String leftMenuText;


    public SwipeMenuAdapter(Context context, ListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;

    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuLayout layout = null;
        if (convertView == null) {

            View contentView = mAdapter.getView(position, convertView, parent);
            SwipeMenu menu = new SwipeMenu(mContext);
            menu.setViewType(mAdapter.getItemViewType(position));
            createMenu(menu);
            SwipeMenuView menuView = new SwipeMenuView(menu, (SwipeMenuListView) parent);
            menuView.setOnSwipeItemClickListener(this);

            SwipeMenuListView listView = (SwipeMenuListView) parent;
            layout = new SwipeMenuLayout(contentView, menuView,
                    listView.getCloseInterpolator(),
                    listView.getOpenInterpolator());

            layout.setPosition(position);
        } else {

            layout = (SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            View view = mAdapter.getView(position, layout.getContentView(), parent);
        }

        if (changeMenuCount && menuList != null) {  //动态修改侧滑菜单数量
            setMenuStatus(layout, position);
        }


        return layout;
    }

    /**
     * 设置侧滑菜单的状态
     *
     * @param layout
     * @param position
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setMenuStatus(SwipeMenuLayout layout, int position) {
        SwipeMenuView swipeMenuView = (SwipeMenuView) layout.getChildAt(1);
        LinearLayout linearLayout = (LinearLayout) swipeMenuView.getChildAt(0);
        TextView view = (TextView) linearLayout.getChildAt(0);
        String menuStatus = menuList.get(position);

        if (menuStatus.equals(mContext.getString(R.string.no_slide))) {
            swipeMenuView.getChildAt(0).setVisibility(View.GONE);
            swipeMenuView.getChildAt(1).setVisibility(View.GONE);

        } else if (!menuStatus.equals(mContext.getString(R.string.show_single_menu))) {
            swipeMenuView.getChildAt(1).setVisibility(View.GONE);
            view.setText(menuStatus);
            if (menuColor != null) {
                linearLayout.setBackgroundColor(Color.parseColor(menuColor));
            }
            view.setTextColor(Color.WHITE);
        } else {
            swipeMenuView.getChildAt(0).setVisibility(View.VISIBLE);
            swipeMenuView.getChildAt(1).setVisibility(View.VISIBLE);
            if (leftMenuText != null) {
                view.setText(leftMenuText);
            }
            view.setTextColor(Color.parseColor("#ffa633"));
            linearLayout.setBackgroundColor(Color.parseColor("#fff2e1"));
        }

    }

    public void createMenu(SwipeMenu menu) {
        // Test Code
        SwipeMenuItem item = new SwipeMenuItem(mContext);
        item.setTitle("Item 1");
        item.setBackground(new ColorDrawable(Color.GRAY));
        item.setWidth(300);
        menu.addMenuItem(item);

        item = new SwipeMenuItem(mContext);
        item.setTitle("Item 2");
        item.setBackground(new ColorDrawable(Color.RED));
        item.setWidth(300);
        menu.addMenuItem(item);
    }

    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
                    index);
        }
    }

    public void setOnMenuItemClickListener(
            SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }

    public void setMenuList(List<String> menuList) {
        this.menuList = menuList;
    }

}
