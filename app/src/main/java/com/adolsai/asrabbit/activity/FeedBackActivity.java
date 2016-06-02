package com.adolsai.asrabbit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.listener.OnTextWatcher;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;

import butterknife.Bind;

/**
 * <p>FeedBackActivity 1、建议反馈界面</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/8 14:03)<br/>
 */
public class FeedBackActivity extends AsRabbitBaseActivity implements AsRabbitTitleBar.AsRabbitTitleBarClick {
    @Bind(R.id.et_words)
    EditText etWords;
    @Bind(R.id.tv_words)
    TextView tvWords;
    @Bind(R.id.title_bar_free_back)
    AsRabbitTitleBar titleBarFreeBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_feed_back, savedInstanceState);
    }

    @Override
    protected void initViews() {
        titleBarFreeBack.setTvBarRightTips("发送");
        etWords.addTextChangedListener(new OnTextWatcher(activity, etWords, tvWords, titleBarFreeBack.getTvBarRightTips(), 140));
    }

    @Override
    protected void initData() {

    }

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
        //发送

    }

    @Override
    public void barRightIconClick(View v) {

    }

    @Override
    public void barRightIconExpandClick(View v) {

    }
}
