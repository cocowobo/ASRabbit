package com.ht.baselib.views.materialview;

public abstract class MaterialRefreshListener {
    public void onFinish() {
    }

    public abstract void onRefresh(MaterialRefreshLayout materialRefreshLayout);

    public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
    }
}
