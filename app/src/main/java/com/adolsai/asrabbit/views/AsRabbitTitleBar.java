package com.adolsai.asrabbit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolsai.asrabbit.R;

/**
 * <p>AsRabbitTitleBar类 1、提供基础的titlebar</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/2 9:42)<br/>
 */
public class AsRabbitTitleBar extends RelativeLayout implements View.OnClickListener {
    /**
     * 标题栏左侧图标
     */
    protected ImageView ivBarLeftIcon;
    /**
     * 标题栏左侧提示语
     */
    protected TextView tvBarLeftTips;
    /**
     * 标题栏中间的标题
     */
    protected TextView tvBarCenterTitle;
    /**
     * 标题栏右边的整体布局
     */
    protected RelativeLayout rlBarRight;
    /**
     * 标题栏右边提示语
     */
    protected TextView tvBarRightTips;
    /**
     * 标题栏右侧图标
     */
    protected ImageView ivBarRightIcon;
    /**
     * 标题栏右侧扩展图标
     */
    protected ImageView ivBarRightIconExpand;

    /**
     * 控件点击事件的回调对象
     */
    protected AsRabbitTitleBarClick asRabbitTitleBarClick;

    //=======================================构造方法区===============================================
    public AsRabbitTitleBar(Context context) {
        super(context);
        init(context);
    }

    public AsRabbitTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AsRabbitTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    //=======================================本地方法区===============================================

    /**
     * 初始化控件
     *
     * @param context 上下文
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.base_title_bar, this);
        //获取控件
        ivBarLeftIcon = (ImageView) findViewById(R.id.iv_bar_left_icon);
        tvBarLeftTips = (TextView) findViewById(R.id.tv_bar_left_tips);
        tvBarCenterTitle = (TextView) findViewById(R.id.tv_bar_center_title);
        rlBarRight = (RelativeLayout) findViewById(R.id.rl_bar_right);
        tvBarRightTips = (TextView) findViewById(R.id.tv_bar_right_tips);
        ivBarRightIcon = (ImageView) findViewById(R.id.iv_bar_right_icon);
        ivBarRightIconExpand = (ImageView) findViewById(R.id.iv_bar_right_icon_expand);

        //注册点击事件
        ivBarLeftIcon.setOnClickListener(this);
        tvBarLeftTips.setOnClickListener(this);
        tvBarCenterTitle.setOnClickListener(this);
        rlBarRight.setOnClickListener(this);
        tvBarRightTips.setOnClickListener(this);
        ivBarRightIcon.setOnClickListener(this);
        ivBarRightIconExpand.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (asRabbitTitleBarClick == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_bar_left_icon:
                asRabbitTitleBarClick.barLeftIconClick(v);
                break;
            case R.id.tv_bar_left_tips:
                asRabbitTitleBarClick.barLeftTipsClick(v);
                break;
            case R.id.tv_bar_center_title:
                asRabbitTitleBarClick.barCenterTitleClick(v);
                break;
            case R.id.tv_bar_right_tips:
                asRabbitTitleBarClick.barRightTipsClick(v);
                break;
            case R.id.iv_bar_right_icon:
                asRabbitTitleBarClick.barRightIconClick(v);
                break;
            case R.id.iv_bar_right_icon_expand:
                asRabbitTitleBarClick.barRightIconExpandClick(v);
                break;
            default:
                break;
        }
    }

    //=======================================控件属性设置方法区========================================

    /**
     * 设置左侧图标
     *
     * @param id 资源id
     */
    public void setIvBarLeftIcon(int id) {
        if (ivBarLeftIcon != null) {
            ivBarLeftIcon.setBackgroundResource(id);
            ivBarLeftIcon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置左边提醒字眼
     *
     * @param value 提醒字眼
     */
    public void setTvBarLeftTips(String value) {
        if (tvBarLeftTips != null) {
            tvBarLeftTips.setVisibility(View.VISIBLE);
            tvBarLeftTips.setText(value);
        }

    }

    /**
     * 设置中间标题
     *
     * @param value 标题值
     */
    public void setTvBarCenterTitle(String value) {
        if (tvBarCenterTitle != null) {
            tvBarCenterTitle.setVisibility(View.VISIBLE);
            tvBarCenterTitle.setText(value);
        }
    }

    /**
     * 设置右边提醒的字眼
     *
     * @param value 右边提醒值
     */
    public void setTvBarRightTips(String value) {
        if (tvBarRightTips != null) {
            ivBarRightIcon.setVisibility(View.GONE);
            tvBarRightTips.setVisibility(View.VISIBLE);
            tvBarRightTips.setText(value);
        }
    }

    /**
     * 设置右边的图标
     *
     * @param id 资源id
     */
    public void setIvBarRightIcon(int id) {
        if (ivBarRightIcon != null) {
            tvBarRightTips.setVisibility(View.GONE);
            ivBarRightIcon.setBackgroundResource(id);
            ivBarRightIcon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置右边扩展图标的值
     *
     * @param id 资源id
     */
    public void setIvBarRightIconExpand(int id) {
        if (ivBarRightIconExpand != null) {
            ivBarRightIconExpand.setBackgroundResource(id);
            ivBarRightIconExpand.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置点击事件回调
     *
     * @param asRabbitTitleBarClick
     */
    public void setAsRabbitTitleBarClick(AsRabbitTitleBarClick asRabbitTitleBarClick) {
        this.asRabbitTitleBarClick = asRabbitTitleBarClick;
    }

    public TextView getTvBarRightTips() {
        return tvBarRightTips;
    }


    /**
     * 控件点击事件接口
     */
    public interface AsRabbitTitleBarClick {
        /**
         * 左边图标的点击事件
         *
         * @param v
         */
        void barLeftIconClick(View v);

        /**
         * 左边提示语的点击事件
         *
         * @param v
         */
        void barLeftTipsClick(View v);

        /**
         * 中间标题的点击事件
         *
         * @param v
         */
        void barCenterTitleClick(View v);

        /**
         * 右边标题的点击事件
         *
         * @param v
         */
        void barRightTipsClick(View v);

        /**
         * 右边图标的点击事件
         *
         * @param v
         */
        void barRightIconClick(View v);

        /**
         * 右边扩展图标的点击事件
         *
         * @param v
         */
        void barRightIconExpandClick(View v);
    }
}
