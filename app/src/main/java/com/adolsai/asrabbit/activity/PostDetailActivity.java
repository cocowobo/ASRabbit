package com.adolsai.asrabbit.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.adolsai.asrabbit.R;
import com.ht.baselib.utils.LogUtils;
import com.ht.baselib.utils.StringUtils;
import com.ht.baselib.views.dialog.CustomDialog;

/**
 * Created by Administrator on 2015/11/9.
 */
public class PostDetailActivity extends BrowserActivity implements
        AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    private CustomDialog customDialog;


    //******************生命周期区*******************************************************************

    @Override
    protected void initViews() {
        super.initViews();
//        StatusBarCompat.compat(this, getResources().getColor(R.color.base_sys_bar_bg));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("帖子详情");
        toolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customDialog = CustomDialog.newConfirmInstance(activity, false);
        customDialog.setConfirmBtnText("跳转");
        customDialog.setTitle("请输入页码");
        customDialog.setHintText("最多可跳转到50页");
        customDialog.setInputType(InputType.TYPE_CLASS_NUMBER);

        customDialog.setBtnCallback(new CustomDialog.ButtonCallback() {
            @Override
            public void onPositive(CustomDialog dialog, EditText editText) {
                super.onPositive(dialog, editText);
                LogUtils.e("需要跳转的是 " + editText.getText().toString());
            }
        });
    }

    @Override
    protected void initData() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。 返回true则显示该menu,false 则不显示;
         * (只会在第一次初始化菜单时调用) Inflate the menu; this adds items to the action bar
         * if it is present.
         */
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //*************************事件区****************************************************************
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            //点击跳页，弹出页码选择框
            customDialog.show();
            return true;
        } else if (id == R.id.action_last_page) {
            //点击跳页，弹出页码选择框
            return true;
        } else if (id == R.id.action_next_page) {
            //点击跳页，弹出页码选择框
            return true;
        }
        return false;
    }

    //******************自定义方法区******************************************************************

    /**
     * 开放方法（供其他类调用）——跳转至当前类（拓展页面标题栏右侧按钮）
     *
     * @param context 上下文
     * @param title   页面标题
     * @param url     请求的h5链接
     * @return intent
     */
    public static Intent createIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        if (!StringUtils.isBlank(title)) {
            intent.putExtra(INTENT_PARAMS_KEY_TITLE, title);
        }
        if (!StringUtils.isBlank(url)) {
            intent.putExtra(INTENT_PARAMS_KEY_LOAD_URL, url);
        }

        return intent;
    }

}
