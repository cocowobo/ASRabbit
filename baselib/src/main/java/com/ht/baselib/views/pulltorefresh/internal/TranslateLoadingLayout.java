package com.ht.baselib.views.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.ht.baselib.R;
import com.ht.baselib.views.pulltorefresh.PullToRefreshBase;

/**
 * @author laijiacai
 * @date 2016/1/18
 * @desc 平移动画加载布局
 */
public class TranslateLoadingLayout extends LoadingLayout {

    private AnimationDrawable animationDrawable;

    private TranslateAnimation animation_Translate;

    public TranslateLoadingLayout(Context context, PullToRefreshBase.Mode mode,
                                  PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        // 初始化
//        mHeaderImage.setImageResource(R.anim.translate_pulltorefresh);
//        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
        animation_Translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -0.025f, Animation.RELATIVE_TO_SELF, 0.025f);
        animation_Translate.setDuration(250);
        animation_Translate.setRepeatCount(Animation.INFINITE);
        animation_Translate.setRepeatMode(Animation.REVERSE);
    }

    // 默认图片
    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.default_ptr_translate;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        // NO-OP
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
    }

    // 下拉以刷新
    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    // 正在刷新时回调
    @Override
    protected void refreshingImpl() {
        // 播放动画
//        animationDrawable.start();
        mHeaderImage.startAnimation(animation_Translate);
    }

    // 释放以刷新
    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    // 重新设置
    @Override
    protected void resetImpl() {
        mHeaderImage.setVisibility(View.VISIBLE);
        mHeaderImage.clearAnimation();
    }

}