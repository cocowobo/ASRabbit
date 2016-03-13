package com.adolsai.asrabbit.activity;

import android.os.Bundle;
import android.os.Handler;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.ht.baselib.utils.ActivityUtil;

/**
 * <p>WelcomeActivity类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/8 14:03)<br/>
 */
public class WelcomeActivity extends AsRabbitBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_welcome, savedInstanceState);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        //延时1秒跳转到主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.startActivityByAnim(activity, MainActivity.class);
            }
        }, 1000);

    }
}
