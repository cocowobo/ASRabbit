package com.adolsai.asrabbit.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * <p>AsRabbitBaseFragment类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/1 18:08)<br/>
 */
public abstract class AsRabbitBaseFragment extends Fragment {
    /**
     * fragment的 mainView
     */
    protected View mMainView;

    protected Context mContext;

    //=======生命周期区===============================================================================
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    //======自定义方法区==============================================================================

    /**
     * 初始化fragment
     *
     * @param inflater 布局填充器
     * @param id       布局文件id
     */

    protected void initFragment(LayoutInflater inflater, int id) {
        mContext = getActivity();
        if (null != mMainView) {
            ViewGroup parent = (ViewGroup) mMainView.getParent();
            if (null != parent) {
                parent.removeView(mMainView);
            }
            ButterKnife.bind(this, mMainView);
        } else {
            mMainView = inflater.inflate(id, null);
            ButterKnife.bind(this, mMainView);
            initViews();
        }
        initData();
    }


    //======抽象方法区==============================================================================

    /**
     * 初始化界面
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract void initData();


}
