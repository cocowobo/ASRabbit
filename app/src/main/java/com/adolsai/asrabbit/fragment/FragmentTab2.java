package com.adolsai.asrabbit.fragment;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Post;

import java.util.List;

public class FragmentTab2 extends FragmentPost {


    @Override
    protected void initViews() {
        super.initViews();
        asRabbitTitleBar.setTvBarCenterTitle(getString(R.string.fragment2_title));
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshDate() {
        DataManager.getCollectionPost(new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (lists != null) {
                    lists.clear();
                    lists.addAll((List) result);
                    postAdapter.replaceAll(lists);
                }
            }
        });
    }

    @Override
    protected void updateAfterDelete(Post post) {
        super.updateAfterDelete(post);
    }
}
