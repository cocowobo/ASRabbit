package com.adolsai.asrabbit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <p>TabViewPagerAdapter类</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/1 11:29)<br/>
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {
    /**
     * fragment列表
     */
    private List<Fragment> fragmentList;

    /**
     * 构造器
     *
     * @param fm fragment管理器
     */
    public TabViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}

