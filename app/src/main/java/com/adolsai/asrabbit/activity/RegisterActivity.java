package com.adolsai.asrabbit.activity;

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
import com.adolsai.asrabbit.event.ParamsBean;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.ht.baselib.network.ErrorModel;
import com.ht.baselib.utils.FormatVerificationUtils;
import com.ht.baselib.utils.StringUtils;
import com.ht.baselib.views.dialog.CustomToast;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * <p>
 * 注册界面
 *
 * @author hxm
 * @version 1.0 (2015-11-20)
 */
public class RegisterActivity extends AsRabbitBaseActivity implements View.OnClickListener {
    @Bind(R.id.et_user_email)
    EditText et_user_email;
    @Bind(R.id.et_user_password)
    EditText et_user_password;
    @Bind(R.id.et_user_re_password)
    EditText et_user_password_re;
    @Bind(R.id.btn_sure)
    Button btn_sure;

    //*************生命周期**************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_register, savedInstanceState);
    }

    @Override
    protected void initViews() {
//        StatusBarCompat.compat(this, getResources().getColor(R.color.base_sys_bar_bg));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_sure.setOnClickListener(this);

    }

    @Override
    protected void initData() {

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

    //**********************事件处理******************************************************************
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_sure:
                registerEvent(et_user_email.getText().toString().trim(),
                        et_user_password.getText().toString().trim(),
                        et_user_password_re.getText().toString().trim());
                break;

        }
    }

    //************私有方法**************************************************************************

    /**
     * 检测输入内容是否符合规范
     *
     * @param account         账号
     * @param password        密码
     * @param comparePassword 重复密码
     * @return true 符合规范  false 不符合规范
     */
    private boolean checkInputFindPassword(String account, String password, String comparePassword) {
        if (StringUtils.isBlank(account)) {
            CustomToast.showToast(mContext, "邮箱不能为空");
            return false;
        } else if (!FormatVerificationUtils.matcherEmail(account)) {
            CustomToast.showToast(mContext, "请输入正确的邮箱");
            return false;
        } else if (StringUtils.isBlank(password)) {
            CustomToast.showToast(mContext, "新密码不能为空");
            return false;
        } else if (StringUtils.isBlank(comparePassword)) {
            CustomToast.showToast(mContext, "重复密码不能为空");
            return false;
        } else if (!StringUtils.isEquals(password, comparePassword)) {
            CustomToast.showToast(mContext, "两次密码输入不一致");
            return false;
        } else if (!FormatVerificationUtils.checkInputRangeIsConform(password, 3, 15) ||
                !FormatVerificationUtils.checkInputRangeIsConform(comparePassword, 3, 15)) {
            CustomToast.showToast(mContext, "请填写3-15位密码");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 注册
     *
     * @param account         账号
     * @param password        密码
     * @param comparePassword 确认密码
     */
    private void registerEvent(final String account, final String password, final String comparePassword) {
        if (checkInputFindPassword(account, password, comparePassword)) {
            DataManager.toRegister(account, password, new RequestListener() {
                @Override
                public void getResult(Object result) {
                    if (result instanceof ErrorModel) {
                        //错误了
                        CustomToast.showToast(mContext, "注册发生错误了");
                    } else if (result instanceof Boolean) {
                        if ((boolean) result) {
                            //注册成功，发送通知，关闭界面
                            ParamsBean paramsBean = new ParamsBean();
                            paramsBean.setDealType(GlobalStaticData.REGISTER_SUCCESS);
                            paramsBean.setObjectContent(account);
                            MainParamEvent mainParamEvent = new MainParamEvent(paramsBean);
                            EventBus.getDefault().post(mainParamEvent);
                            finish();

                        } else {
                            //登陆失败
                            CustomToast.showToast(mContext, "注册失败");
                        }
                    }
                    closeProDialog();
                }
            });
        }

    }

}
