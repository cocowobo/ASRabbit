package com.adolsai.asrabbit.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.ht.baselib.utils.StringUtils;
import com.ht.baselib.views.slideswitch.ShSwitchView;


/**
 * <p>SettingItemView类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-13 14:34)<br/>
 */
public class SettingItemView extends RelativeLayout {
    private TextView tvItemSettingHead;
    private TextView tvItemSettingTitle;
    private TextView tvItemSettingContent;
    private ShSwitchView swItemSetting;
    private TextView tvItemSettingTail;
    private RelativeLayout rlItemSettingRoot;

    private SettingItemViewListener settingItemViewListener;


    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 加载界面UI
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_setting, this);
        tvItemSettingHead = (TextView) findViewById(R.id.tv_item_setting_head);
        tvItemSettingTitle = (TextView) findViewById(R.id.tv_item_setting_title);
        tvItemSettingContent = (TextView) findViewById(R.id.tv_item_setting_content);
        tvItemSettingTail = (TextView) findViewById(R.id.tv_item_setting_tail);
        swItemSetting = (ShSwitchView) findViewById(R.id.sw_item_setting);
        rlItemSettingRoot = (RelativeLayout) findViewById(R.id.rl_item_setting_root);
        swItemSetting.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(View v, boolean isOn) {
                if (settingItemViewListener != null) {
                    settingItemViewListener.swItemSettingChange(v, isOn);
                }
            }
        });

        rlItemSettingRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingItemViewListener != null) {
                    settingItemViewListener.rootOnClick(v);
                }
            }
        });
    }

    /**
     * 初始化控件配置
     *
     * @param head    头部字眼
     * @param title   title字眼
     * @param content 内容字眼
     * @param tail    尾巴字眼
     */
    public void initConfigs(String head, String title, String content, String tail, boolean hasArrow) {
        setTvItemSettingHeadText(head);
        setTvItemSettingTitleText(title);
        setTvItemSettingContentText(content, hasArrow);
        setTvItemSettingTailText(tail);
    }

    /**
     * 设置head的文字
     *
     * @param value 文字值
     */
    public void setTvItemSettingHeadText(String value) {
        if (tvItemSettingHead != null && !StringUtils.isBlank(value)) {
            tvItemSettingHead.setText(value);
            tvItemSettingHead.setVisibility(VISIBLE);

        }

    }

    /**
     * 设置标题的文字
     *
     * @param value 文字值
     */
    public void setTvItemSettingTitleText(String value) {
        if (tvItemSettingTitle != null && !StringUtils.isBlank(value)) {
            tvItemSettingTitle.setText(value);
            tvItemSettingTitle.setVisibility(VISIBLE);

        }

    }

    /**
     * 设置右边的文字
     *
     * @param value 文字值
     */
    public void setTvItemSettingContentText(String value, boolean hasRightArrow) {
        if (tvItemSettingContent != null) {
            tvItemSettingContent.setVisibility(VISIBLE);
            tvItemSettingContent.setText(value);
            if (hasRightArrow) {
                Drawable drawable = getResources().getDrawable(R.mipmap.icon_right_arrow);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvItemSettingContent.setCompoundDrawables(null, null, drawable, null);
            } else {
                tvItemSettingContent.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    /**
     * 设置下面尾巴的文字
     *
     * @param value 文字值
     */
    public void setTvItemSettingTailText(String value) {
        if (tvItemSettingTail != null && !StringUtils.isBlank(value)) {
            tvItemSettingTail.setText(value);
            tvItemSettingTail.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置开关状态
     *
     * @param value 是否打开 true为打开，false为关闭
     */
    public void setSwItemSettingStatus(boolean value) {
        if (swItemSetting != null) {
            swItemSetting.setOn(value, true);
            swItemSetting.setVisibility(VISIBLE);
        }
    }

    public void setSettingItemViewListener(SettingItemViewListener settingItemViewListener) {
        this.settingItemViewListener = settingItemViewListener;
    }


    public interface SettingItemViewListener {
        void rootOnClick(View v);

        void swItemSettingChange(View v, boolean value);
    }


}
