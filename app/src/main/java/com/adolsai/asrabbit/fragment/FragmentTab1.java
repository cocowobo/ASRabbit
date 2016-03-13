package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;
import com.ht.baselib.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FragmentTab1 extends AsRabbitBaseFragment {
    @Bind(R.id.as_rabbit_title_bar_fragment1)
    AsRabbitTitleBar asRabbitTitleBarFragment1;
    @Bind(R.id.sv_history)
    SearchView svHistory;
    @Bind(R.id.lv_history)
    ListView lvHistory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_tab1);
        ButterKnife.bind(this, mMainView);
        return mMainView;
    }


    @Override
    protected void initViews() {
        asRabbitTitleBarFragment1.setTvBarCenterTitle(getString(R.string.fragment1_title));
        asRabbitTitleBarFragment1.setIvBarRightIcon(R.drawable.selector_titlebar_delete);
        lvHistory.setTextFilterEnabled(true);
        svHistory.setSubmitButtonEnabled(false);
        svHistory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtils.e("sharing", "onQueryTextSubmit query is" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtils.e("sharing", "onQueryTextChange newText is" + newText);
                return false;
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
