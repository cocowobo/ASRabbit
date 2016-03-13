package com.adolsai.asrabbit.activity;

import android.os.Bundle;
import android.view.View;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.views.AsRabbitTitleBar;
import com.adolsai.asrabbit.views.SettingItemView;
import com.ht.baselib.utils.AppUtils;
import com.ht.baselib.views.dialog.CustomToast;

import butterknife.Bind;

/**
 * <p>SettingActivity类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-13 14:26)<br/>
 */
public class SettingActivity extends AsRabbitBaseActivity implements SettingItemView.SettingItemViewListener {

    @Bind(R.id.as_rabbit_title_bar)
    AsRabbitTitleBar asRabbitTitleBar;
    @Bind(R.id.setting_item_view_model)
    SettingItemView settingItemViewModel;
    @Bind(R.id.setting_item_view_net)
    SettingItemView settingItemViewNet;
    @Bind(R.id.setting_item_view_picture)
    SettingItemView settingItemViewPicture;
    @Bind(R.id.setting_item_view_rich_text)
    SettingItemView settingItemViewRichText;
    @Bind(R.id.setting_item_view_rich_text1)
    SettingItemView settingItemViewRichText1;
    @Bind(R.id.setting_item_view_content)
    SettingItemView settingItemViewContent;
    @Bind(R.id.setting_item_view_content1)
    SettingItemView settingItemViewContent1;
    @Bind(R.id.setting_item_view_about)
    SettingItemView settingItemViewAbout;
    @Bind(R.id.setting_item_view_about_app)
    SettingItemView settingItemViewAboutApp;
    @Bind(R.id.setting_item_view_user_agreement)
    SettingItemView settingItemViewUserAgreement;
    @Bind(R.id.setting_item_view_support)
    SettingItemView settingItemViewSupport;

    private String dnsValue = "bbs.jjwxc.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity(R.layout.activity_setting, savedInstanceState);
        CustomToast.showToast(mContext, "SettingActivity");
    }

    @Override
    protected void initViews() {
        //初始化标题栏
        asRabbitTitleBar.setTvBarCenterTitle("Settings");
        asRabbitTitleBar.setTvBarRightTips("完成");
        //初始化下面那些奇奇怪怪的item
        settingItemViewModel.initConfigs("显示", "夜间模式", "", "我想妈妈保证睡觉前不玩手机", false);
        settingItemViewModel.setSwItemSettingStatus(true);
        settingItemViewNet.initConfigs("网络设置", "访问域名", dnsValue,
                "仅在联网后不能打开任何帖子的时候修改，修改该选项会清空登录信息", true);
        settingItemViewPicture.initConfigs("上传", "压缩上传图片", "",
                "压缩并限制图片的最大宽度为755px，GIF格式图片不压缩", false);
        settingItemViewPicture.setSwItemSettingStatus(true);
        settingItemViewRichText.initConfigs("富文本", "无图模式", "", "", false);
        settingItemViewRichText.setSwItemSettingStatus(true);
        settingItemViewRichText1.initConfigs("", "仅在使用流量时无图", "", "", false);
        settingItemViewRichText1.setSwItemSettingStatus(true);
        settingItemViewContent.initConfigs("内容", "层叠回复", "", "", false);
        settingItemViewContent.setSwItemSettingStatus(true);
        settingItemViewContent1.initConfigs("", "贴内导航条", "", "", false);
        settingItemViewContent1.setSwItemSettingStatus(true);
        settingItemViewAbout.initConfigs("关于此APP", "版本号", AppUtils.getVersionName(mContext), "", false);
        settingItemViewAboutApp.initConfigs("", "关于此APP", "", "", true);
        settingItemViewUserAgreement.initConfigs("", "用户许可协议", "", "", true);
        settingItemViewSupport.initConfigs("", "建议/反馈", "", "", true);

        //注册各种事件
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
                //点击完成，保存好配置信息，然后结束界面咯
                finish();
            }

            @Override
            public void barRightIconClick(View v) {

            }

            @Override
            public void barRightIconExpandClick(View v) {

            }
        });

        settingItemViewNet.setSettingItemViewListener(this);
        settingItemViewAboutApp.setSettingItemViewListener(this);
        settingItemViewUserAgreement.setSettingItemViewListener(this);
        settingItemViewSupport.setSettingItemViewListener(this);

        settingItemViewModel.setSettingItemViewListener(this);
        settingItemViewPicture.setSettingItemViewListener(this);
        settingItemViewRichText.setSettingItemViewListener(this);
        settingItemViewRichText1.setSettingItemViewListener(this);
        settingItemViewContent.setSettingItemViewListener(this);
        settingItemViewContent1.setSettingItemViewListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void rootOnClick(View v) {
        switch (v.getId()) {
            case R.id.setting_item_view_net:
                CustomToast.showToast(mContext, "点击访问域名");
                break;
            case R.id.setting_item_view_about_app:
                CustomToast.showToast(mContext, "点击关于此APP");
                break;
            case R.id.setting_item_view_user_agreement:
                CustomToast.showToast(mContext, "点击用户许可协议");
                break;
            case R.id.setting_item_view_support:
                CustomToast.showToast(mContext, "点击建议反馈");
                break;
            default:
                break;
        }
    }

    @Override
    public void swItemSettingChange(View v, boolean value) {
        switch (v.getId()) {
            case R.id.setting_item_view_model:
                CustomToast.showToast(mContext, "model isOn is " + value);
                break;
            case R.id.setting_item_view_picture:
                CustomToast.showToast(mContext, "picture isOn is " + value);
                break;
            case R.id.setting_item_view_rich_text:
                CustomToast.showToast(mContext, "rich_text isOn is " + value);
                break;
            case R.id.setting_item_view_rich_text1:
                CustomToast.showToast(mContext, "rich_text1 isOn is " + value);
                break;
            case R.id.setting_item_view_content:
                CustomToast.showToast(mContext, "content isOn is " + value);
                break;
            case R.id.setting_item_view_content1:
                CustomToast.showToast(mContext, "content1 isOn is " + value);
                break;
            default:
                break;
        }
    }
}
