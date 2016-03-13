package com.adolsai.asrabbit.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.TabViewPagerAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.fragment.FragmentTab0;
import com.adolsai.asrabbit.fragment.FragmentTab1;
import com.adolsai.asrabbit.fragment.FragmentTab2;
import com.adolsai.asrabbit.views.ViewPagerNoSlide;

import java.lang.reflect.Method;
import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends AsRabbitBaseActivity implements View.OnClickListener {
    @Bind(R.id.mainViewPager)
    ViewPagerNoSlide mainViewPager;
    @Bind(R.id.iv_tab0)
    ImageView ivTab0;
    @Bind(R.id.tv_tab0)
    TextView tvTab0;
    @Bind(R.id.tab0)
    RelativeLayout tab0;
    @Bind(R.id.iv_tab1)
    ImageView ivTab1;
    @Bind(R.id.tv_tab1)
    TextView tvTab1;
    @Bind(R.id.tab1)
    RelativeLayout tab1;
    @Bind(R.id.iv_tab2)
    ImageView ivTab2;
    @Bind(R.id.tv_tab2)
    TextView tvTab2;
    @Bind(R.id.tab2)
    RelativeLayout tab2;
    /**
     * Tab 3个fragment 标志位
     */
    private int flag = 0;
    /**
     * fragment列表
     */
    private ArrayList<Fragment> fragmentList;
    /**
     * 第一个界面
     */
    private FragmentTab0 fragmentTab0;
    /**
     * 第二个界面
     */
    private FragmentTab1 fragmentTab1;
    /**
     * 第三个界面
     */
    private FragmentTab2 fragmentTab2;
    /**
     * viewPage适配器
     */
    private TabViewPagerAdapter PagerAdapter;

    private static final String STATE_CUR_TAB = "STATE_CUR_TAB";
    private static final int TAB0 = 0;
    private static final int TAB1 = 1;
    private static final int TAB2 = 2;

    //==========回调方法区============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_main, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CUR_TAB, flag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        flag = savedInstanceState.getInt(STATE_CUR_TAB, TAB0);
        switch (flag) {
            case TAB1:
                tab1Event();
                break;
            case TAB2:
                tab2Event();
                break;
            default:
                tab0Event();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tab0:
                tab0Event();
                break;
            case R.id.tab1:
                tab1Event();
                break;
            case R.id.tab2:
                tab2Event();
                break;
            default:
                break;
        }

    }

    @Override
    protected void initViews() {
        generateAllFragment();
        PagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainViewPager.setOffscreenPageLimit(3);
        mainViewPager.setAdapter(PagerAdapter);

        tab0.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);

        tab0Event();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //==========自定义方法============================================================================/

    /**
     * 获取所有的Fragment
     */
    private void generateAllFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        Fragment fragment = generateFragment(TAB0);
        if (fragment instanceof FragmentTab0) {
            fragmentTab0 = (FragmentTab0) fragment;
            fragmentList.add(fragmentTab0);
        }
        fragment = generateFragment(TAB1);
        if (fragment instanceof FragmentTab1) {
            fragmentTab1 = (FragmentTab1) fragment;
            fragmentList.add(fragmentTab1);
        }
        fragment = generateFragment(TAB2);
        if (fragment instanceof FragmentTab2) {
            fragmentTab2 = (FragmentTab2) fragment;
            fragmentList.add(fragmentTab2);
        }
    }

    /**
     * 根据当前位置，返回相应的Fragment
     *
     * @param position 位置信息
     * @return 相应的Fragment
     */
    private Fragment generateFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag(mainViewPager.getId(), position));
            if (fragment != null) {
                return fragment;
            }
            switch (position) {
                case TAB0:
                    return new FragmentTab0();
                case TAB1:
                    return new FragmentTab1();
                case TAB2:
                    return new FragmentTab2();
                default:
                    return new FragmentTab0();
            }
        }
        return null;
    }

    /**
     * 获取指定位置的fragment的tag
     *
     * @param viewId   资源id
     * @param position 位置id
     * @return
     */
    private String getFragmentTag(int viewId, int position) {
        try {
            Method method = FragmentPagerAdapter.class.getDeclaredMethod("makeFragmentName", int.class, long.class);
            if (method != null) {
                method.setAccessible(true);
                Object result = method.invoke(null, viewId, position);
                if (result != null) {
                    return result.toString();
                }
            }
        } catch (Exception throwable) {
            throwable.printStackTrace();
        }
        return "";
    }

    /**
     * 第一个tab的点击事件
     */
    private void tab0Event() {
        tabStateClear();
        ivTab0.setSelected(true);
        tvTab0.setTextColor(Color.parseColor(getString(R.string.color_bab_s)));
        //设置当前页的标签
        flag = TAB0;
        mainViewPager.setCurrentItem(flag, false);
    }

    /**
     * 第二个tab的点击事件
     */
    private void tab1Event() {
        tabStateClear();
        ivTab1.setSelected(true);
        tvTab1.setTextColor(Color.parseColor(getString(R.string.color_bab_s)));
        //设置当前页的标签
        flag = TAB1;
        mainViewPager.setCurrentItem(flag, false);
    }

    /**
     * 第三个tab的点击事件
     */
    private void tab2Event() {
        tabStateClear();
        ivTab2.setSelected(true);
        tvTab2.setTextColor(Color.parseColor(getString(R.string.color_bab_s)));
        //设置当前页的标签
        flag = TAB2;
        mainViewPager.setCurrentItem(flag, false);
    }

    /**
     * 清除tab的状态
     */
    private void tabStateClear() {
        tvTab0.setTextColor(Color.parseColor(getString(R.string.color_tab_normal)));
        tvTab1.setTextColor(Color.parseColor(getString(R.string.color_tab_normal)));
        tvTab2.setTextColor(Color.parseColor(getString(R.string.color_tab_normal)));
        ivTab0.setSelected(false);
        ivTab1.setSelected(false);
        ivTab2.setSelected(false);
    }
}
