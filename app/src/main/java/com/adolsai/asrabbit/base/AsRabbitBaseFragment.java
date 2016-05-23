package com.adolsai.asrabbit.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>AsRabbitBaseFragment类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/1 18:08)<br/>
 */
public abstract class AsRabbitBaseFragment extends Fragment {
    protected Handler mHandler = new Handler();

    protected abstract void initActions(View paramView);

    protected abstract void initData();

    protected abstract View initViews(LayoutInflater paramLayoutInflater);

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initData();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        View localView = initViews(paramLayoutInflater);
        initActions(localView);
        return localView;
    }

}
