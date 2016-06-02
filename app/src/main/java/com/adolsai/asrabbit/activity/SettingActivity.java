package com.adolsai.asrabbit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.app.GlobalUrl;
import com.adolsai.asrabbit.app.SharePreferenceKey;
import com.adolsai.asrabbit.base.AsRabbitBaseActivity;
import com.adolsai.asrabbit.views.SettingItemView;
import com.ht.baselib.utils.ActivityUtil;
import com.ht.baselib.utils.AppUtils;
import com.ht.baselib.views.dialog.CustomToast;
import com.ht.baselib.views.pickerview.ActionSheetDialogBuilder;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;

/**
 * <p>SettingActivity类 </p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-13 14:26)<br/>
 */
public class SettingActivity extends AsRabbitBaseActivity implements SettingItemView.SettingItemViewListener {
    @Bind(R.id.setting_item_view_model)
    SettingItemView settingItemViewModel;//模式选择
    @Bind(R.id.setting_item_view_net)
    SettingItemView settingItemViewNet;//网络设置
    @Bind(R.id.setting_item_view_picture)
    SettingItemView settingItemViewPicture;//图片压缩
    @Bind(R.id.setting_item_view_rich_text)
    SettingItemView settingItemViewRichText;//富文本无图模式
    @Bind(R.id.setting_item_view_rich_text1)
    SettingItemView settingItemViewRichText1;//富文本使用流量时无图
    @Bind(R.id.setting_item_view_content)
    SettingItemView settingItemViewContent;//层叠回复
    @Bind(R.id.setting_item_view_content1)
    SettingItemView settingItemViewContent1;//贴内导航条
    @Bind(R.id.setting_item_view_about)
    SettingItemView settingItemViewAbout;//版本号
    @Bind(R.id.setting_item_view_about_app)
    SettingItemView settingItemViewAboutApp;//关于APP
    @Bind(R.id.setting_item_view_user_agreement)
    SettingItemView settingItemViewUserAgreement;//用户许可协议
    @Bind(R.id.setting_item_view_support)
    SettingItemView settingItemViewSupport;//建议反馈
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String dnsValue;

    private ActionSheetDialogBuilder DomainNameBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dnsValue = Hawk.get(SharePreferenceKey.SETTING_NET_DNS, GlobalUrl.DNS_COM);
        initActivity(R.layout.activity_setting, savedInstanceState);
    }

    @Override
    protected void initViews() {
        //初始化标题栏
        toolbar.setTitle("设置");
        toolbar.setNavigationIcon(R.drawable.selector_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化下面那些奇奇怪怪的item
        settingItemViewModel.initConfigs("显示", "夜间模式", "", "我想妈妈保证睡觉前不玩手机", false);
        settingItemViewNet.initConfigs("网络设置", "访问域名", dnsValue,
                "仅在联网后不能打开任何帖子的时候修改，修改该选项会清空登录信息", true);
        settingItemViewPicture.initConfigs("上传", "压缩上传图片", "",
                "压缩并限制图片的最大宽度为755px，GIF格式图片不压缩", false);
        settingItemViewRichText.initConfigs("富文本", "无图模式", "", "", false);
        settingItemViewRichText1.initConfigs("", "仅在使用流量时无图", "", "", false);
        settingItemViewContent.initConfigs("内容", "层叠回复", "", "", false);
        settingItemViewContent1.initConfigs("", "贴内导航条", "", "", false);
        settingItemViewAbout.initConfigs("关于此APP", "版本号", AppUtils.getVersionName(mContext), "", false);
        settingItemViewAboutApp.initConfigs("", "关于此APP", "", "", true);
        settingItemViewUserAgreement.initConfigs("", "用户许可协议", "", "", true);
        settingItemViewSupport.initConfigs("", "建议/反馈", "", "", true);

        //设置各个开关状态
        updateSwButtonStatus();

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
                //访问域名
                selectDomainName();
                break;
            case R.id.setting_item_view_about_app:
                //关于此APP
                Intent aboutIntent = BrowserActivity.createIntent(activity, "关于此APP", GlobalUrl.ABOUT_URL);
                ActivityUtil.startActivity(activity, aboutIntent);
                break;
            case R.id.setting_item_view_user_agreement:
                //用户许可协议
                Intent agreementIntent = BrowserActivity.createIntent(activity, "用户许可协议", GlobalUrl.AGREEMENT_URL);
                ActivityUtil.startActivity(activity, agreementIntent);
                break;
            case R.id.setting_item_view_support:
                //建议反馈
                ActivityUtil.startActivity(activity, FeedBackActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void swItemSettingChange(View v, boolean value) {
        switch (v.getId()) {
            case R.id.setting_item_view_model:
                //夜间模式
                CustomToast.showToast(mContext, "model isOn is " + value);
                Hawk.put(SharePreferenceKey.SETTING_NIGHT_MODEL, value);
                break;
            case R.id.setting_item_view_picture:
                //上传图片
                CustomToast.showToast(mContext, "picture isOn is " + value);
                Hawk.put(SharePreferenceKey.SETTING_UPLOAD_PICIURE, value);
                break;
            case R.id.setting_item_view_rich_text:
                //无图模式
                CustomToast.showToast(mContext, "rich_text isOn is " + value);
                Hawk.put(SharePreferenceKey.SETTING_NO_PICTURE_3G, value);
                break;
            case R.id.setting_item_view_rich_text1:
                //仅在使用流量时无图
                CustomToast.showToast(mContext, "rich_text1 isOn is " + value);
                Hawk.put(SharePreferenceKey.SETTING_NO_PICTURE, value);
                break;
            case R.id.setting_item_view_content:
                //层叠回复
                CustomToast.showToast(mContext, "content isOn is " + value);
                Hawk.put(SharePreferenceKey.SETTING_REPLY, value);
                break;
            case R.id.setting_item_view_content1:
                //贴内导航条
                CustomToast.showToast(mContext, "content1 isOn is " + value);
                Hawk.put(SharePreferenceKey.SETTING_NAVIGATION_BAR, value);
                break;
            default:
                break;
        }
    }

    /**
     * 更新各个选择开关的状态
     */
    private void updateSwButtonStatus() {
        settingItemViewModel.setSwItemSettingStatus(Hawk.get(SharePreferenceKey.SETTING_NIGHT_MODEL, false));
        settingItemViewPicture.setSwItemSettingStatus(Hawk.get(SharePreferenceKey.SETTING_UPLOAD_PICIURE, false));
        settingItemViewRichText.setSwItemSettingStatus(Hawk.get(SharePreferenceKey.SETTING_NO_PICTURE, false));
        settingItemViewRichText1.setSwItemSettingStatus(Hawk.get(SharePreferenceKey.SETTING_NO_PICTURE_3G, false));
        settingItemViewContent.setSwItemSettingStatus(Hawk.get(SharePreferenceKey.SETTING_REPLY, false));
        settingItemViewContent1.setSwItemSettingStatus(Hawk.get(SharePreferenceKey.SETTING_NAVIGATION_BAR, false));
    }

    /**
     * 选择域名
     */
    private void selectDomainName() {
        if (DomainNameBuilder == null) {
            DomainNameBuilder = new ActionSheetDialogBuilder(mContext);
            DomainNameBuilder.setTitleVisibility(true);
            DomainNameBuilder.setTitleMessage("请选择访问域名");
            DomainNameBuilder.setButtons(GlobalUrl.DNS_COM, GlobalUrl.DNS_NET, "取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case ActionSheetDialogBuilder.BUTTON1:
                                    Hawk.put(SharePreferenceKey.SETTING_NET_DNS, GlobalUrl.DNS_COM);
                                    settingItemViewNet.setTvItemSettingContentText(GlobalUrl.DNS_COM, true);
                                    break;

                                case ActionSheetDialogBuilder.BUTTON2:
                                    Hawk.put(SharePreferenceKey.SETTING_NET_DNS, GlobalUrl.DNS_NET);
                                    settingItemViewNet.setTvItemSettingContentText(GlobalUrl.DNS_NET, true);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
        DomainNameBuilder.create().show();
    }
}
