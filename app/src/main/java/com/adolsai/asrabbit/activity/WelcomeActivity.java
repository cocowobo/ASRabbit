package com.adolsai.asrabbit.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.ht.baselib.utils.ActivityUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * <p>WelcomeActivity类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/8 14:03)<br/>
 */
public class WelcomeActivity extends AsRabbitBaseActivity {

    @Bind(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_welcome, savedInstanceState);
    }

    @Override
    protected void initViews() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        ivSplash.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ActivityUtil.startActivityByAnim(activity, HomeActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
