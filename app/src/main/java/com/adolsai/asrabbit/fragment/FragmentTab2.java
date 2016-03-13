package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;

import butterknife.Bind;

public class FragmentTab2 extends AsRabbitBaseFragment {
    @Bind(R.id.as_rabbit_title_bar_fragment2)
    AsRabbitTitleBar asRabbitTitleBarFragment2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_tab2);
        return mMainView;
    }


    @Override
    protected void initViews() {
        asRabbitTitleBarFragment2.setTvBarCenterTitle(getString(R.string.fragment2_title));
    }

    @Override
    protected void initData() {

    }
}
