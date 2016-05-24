package com.ht.baselib.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.ht.baselib.R;
import com.ht.baselib.views.slidefinish.SlideFinishLayout;

/**
 * <p>基础Activity类 ，统一规范Activity并提供通用方法
 * <br/>1.所有Activity的基类，保证全部是该类的子类
 * <br/>2.管理通用功能比如，Activity队列(完全退出程序使用)
 * </p>
 *
 * @author zmingchun
 * @version 1.0（2015-10-19）
 */
public class BaseActivity extends AppCompatActivity {
    //变量区 start======================================
    protected Context mContext;
    protected Activity activity;


    //执行区 start======================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //禁止显示默认的title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (enableSlideFinish()) {
            SlideFinishLayout layout = (SlideFinishLayout) LayoutInflater.from(this).inflate(
                    R.layout.activity_container, null);
            layout.attachToActivity(this);
        }
        activity = this;
        mContext = this;

        //Activity进入自定义栈
        BaseApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity移除自定义栈
        BaseApplication.getInstance().removeActivity(this);
    }

    //方法区 start========================================

    /**
     * 是否允许滑动删除
     *
     * @return 是否允许
     */
    protected boolean enableSlideFinish() {
        return true;
    }
    //事件区 start========================================
}