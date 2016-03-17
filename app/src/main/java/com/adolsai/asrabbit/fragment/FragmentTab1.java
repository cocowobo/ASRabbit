package com.adolsai.asrabbit.fragment;

import android.content.DialogInterface;
import android.view.View;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Post;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;
import com.ht.baselib.views.pickerview.ActionSheetDialogBuilder;

import java.util.List;


public class FragmentTab1 extends FragmentPost {

    /**
     * 清空所有消息的弹窗
     */
    protected ActionSheetDialogBuilder cleanAllBuilder;


    @Override
    protected void initViews() {
        super.initViews();
        asRabbitTitleBar.setTvBarCenterTitle(getString(R.string.fragment1_title));
        asRabbitTitleBar.setIvBarRightIcon(R.drawable.selector_titlebar_delete);
        asRabbitTitleBar.setAsRabbitTitleBarClick(new AsRabbitTitleBar.AsRabbitTitleBarClick() {
            @Override
            public void barLeftIconClick(View v) {

            }

            @Override
            public void barLeftTipsClick(View v) {

            }

            @Override
            public void barCenterTitleClick(View v) {

            }

            @Override
            public void barRightTipsClick(View v) {

            }

            @Override
            public void barRightIconClick(View v) {
                //弹出窗口清空
                if (cleanAllBuilder == null) {
                    cleanAllBuilder = new ActionSheetDialogBuilder(getActivity());
                    cleanAllBuilder.setTitleVisibility(true);
                    cleanAllBuilder.setTitleVisibility(false);
                    cleanAllBuilder.setTitleMessage("确定要删除所有历史记录吗？");
                    cleanAllBuilder.setButtons("删除", null, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case ActionSheetDialogBuilder.BUTTON1:
                                            //清除操作
                                            postAdapter.clear(true);
                                            break;

                                        default:
                                            break;
                                    }
                                }
                            });
                }
                cleanAllBuilder.create().show();
            }

            @Override
            public void barRightIconExpandClick(View v) {

            }
        });

    }

    @Override
    protected void updateAfterDelete(Post post) {
        super.updateAfterDelete(post);
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshDate() {
        DataManager.getHistoryPost(new RequestListener() {
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


}
