package com.adolsai.asrabbit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.app.GlobalStaticData;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.event.MainParamEvent;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.UserInfo;
import com.adolsai.asrabbit.utils.StatusBarCompat;
import com.ht.baselib.network.ErrorModel;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.StringUtils;
import com.ht.baselib.views.dialog.CustomToast;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * <p>
 * 登陆界面
 *
 * @author hxm
 * @version 1.0 (2015-11-20)
 */
public class LoginActivity extends AsRabbitBaseActivity implements View.OnClickListener {
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.et_user_email)
    EditText et_user_eamil;
    @Bind(R.id.et_user_password)
    EditText et_user_pass;
    @Bind(R.id.btn_login)
    Button btn_login;

    //*****************************生命周期***********************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_login, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //**********************方法重载******************************************************************
    @Override
    protected void initViews() {
        StatusBarCompat.compat(this, getResources().getColor(R.color.brownness));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }


    //****************事件处理区**********************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                showProDialog("登录中...");
                login();
                break;
        }
    }

    public void onEventMainThread(MainParamEvent paramEvent) {
        if (GlobalStaticData.REGISTER_SUCCESS.equals(paramEvent.paramsBean.getDealType())) {
            String email = paramEvent.paramsBean.getObjectContent();
            if (!StringUtils.isBlank(email) && et_user_eamil != null) {
                et_user_eamil.setText(email);
            }
        }

    }

    //*******************自定义方法*******************************************************************

    /**
     * 登陆
     */
    private void login() {
        final String email = et_user_eamil.getText().toString().trim();
        final String pass = et_user_pass.getText().toString().trim();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(pass)) {
            CustomToast.showToast(mContext, "邮箱或者密码不能为空");
            closeProDialog();
            return;
        }

        DataManager.toLogin(email, pass, new RequestListener() {
            @Override
            public void getResult(Object result) {
                if (result instanceof ErrorModel) {
                    //错误了
                    CustomToast.showToast(mContext, "登陆发生错误了");
                } else if (result instanceof Boolean) {
                    if ((boolean) result) {
                        //登陆成功，保存用户信息，跳转主界面
                        Hawk.put(GlobalStaticData.LOGOUT_USER_INFO, new UserInfo(email, pass));
                        Hawk.put(GlobalStaticData.IS_LOGIN, true);
                        ActivityUtil.startActivity(activity, HomeActivity.class);

                    } else {
                        //登陆失败
                        CustomToast.showToast(mContext, "登陆失败");
                    }
                }
                closeProDialog();
            }
        });
    }
}
