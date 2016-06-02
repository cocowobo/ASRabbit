package com.adolsai.asrabbit.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.app.SharePreferenceKey;
import com.ht.baselib.base.BaseActivity;
import com.ht.baselib.manager.SystemBarTintManager;
import com.orhanobut.hawk.Hawk;

import butterknife.ButterKnife;

/**
 * <p>AsRabbitBaseActivity类 </p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/1 11:04)<br/>
 */
public abstract class AsRabbitBaseActivity extends BaseActivity {
    private ProgressDialog progressDialog;

    //=======================================生命周期区===============================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        if (Hawk.get(SharePreferenceKey.SETTING_NIGHT_MODEL, true)) {
            this.setTheme(R.style.NightTheme);
        } else {
            this.setTheme(R.style.DayTheme);
        }
    }

    //=======================================抽象方法区===============================================

    /**
     * 初始化界面，需要强制重写
     */
    protected abstract void initViews();

    /**
     * 初始化数据，需要强制重写
     */
    protected abstract void initData();

    //=======================================实用方法区===============================================

    /**
     * 界面初始化操作  在上层 onCreate 中调用
     *
     * @param id            布局文件id
     * @param savedInstance 保存数据
     */
    protected void initActivity(int id, Bundle savedInstance) {
        setContentView(id);
        ButterKnife.bind(this);
        initViews();
        //预防Home键后，数据被第三方清掉后，回来界面需要重新刷新
        if (savedInstance == null) {
            initData();
        }
        changeStateBarColor(R.color.base_sys_bar_bg);
    }


    /**
     * 更改状态栏背景色
     * <p>
     * 需要在activity的跟布局添加     android:fitsSystemWindows="true"
     * </p>
     *
     * @param colorId 颜色值的id
     */
    protected void changeStateBarColor(int colorId) {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(getResources().getColor(colorId));
        }
    }

    public void closeProDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showProDialog(String str) {
        if (progressDialog != null) {
            progressDialog.setMessage(str);
            progressDialog.show();
        }
    }

    public void showProDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

}
