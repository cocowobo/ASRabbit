package com.ht.baselib.views.pickerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.ht.baselib.R;
import com.ht.baselib.views.pickerview.subview.ScreenInfo;
import com.ht.baselib.views.pickerview.subview.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>时间选择器
 * <br/>源自https://github.com/saiwu-bigkoo/Android-PickerView
 * <p>
 *
 * @author Sai，Updated by zmingchun
 * @version 1.0(2015/9/28)
 */
public class TimePopupWindow extends PopupWindow implements OnClickListener {
    /**
     * 选择模式枚举:四种选择模式，年月日时分，年月日，时分，月日时分
     */
    public enum Type {
        /**年月日时分*/
        ALL
        /**年月日*/
        , YEAR_MONTH_DAY
        /**时分*/
        , HOURS_MINS
        /**月日时分*/
        , MONTH_DAY_HOUR_MIN
    }

    private View rootView; // 总的布局
    WheelTime wheelTime;
    private View btnSubmit, btnCancel;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnTimeSelectListener timeSelectListener;

    /**
     * 构造器
     * @param context 上下文
     * @param type 选择类型
     */
    public TimePopupWindow(Context context, Type type) {
        super(context);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.timepopwindow_anim_style);

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.pickerview_time, null);
        // -----确定和取消按钮
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        // ----时间转轮
        final View timepickerview = rootView.findViewById(R.id.timepicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        wheelTime = new WheelTime(timepickerview, type);

        wheelTime.screenheight = screenInfo.getHeight();

        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);

        setContentView(rootView);
    }

    /**
     * 设置可以选择的时间范围
     *
     * @param startYear 开始年
     * @param endYear 结束年
     */
    public void setRange(int startYear, int endYear) {
        WheelTime.setSTART_YEAR(startYear);
        WheelTime.setEND_YEAR(endYear);
    }

    /**
     * 设置选中时间
     *
     * @param date 选中的日期
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }else {
            calendar.setTime(date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 指定选中的时间，显示选择器
     *
     * @param parent 父级view，也即在哪个view显示
     * @param gravity 显示的位置
     * @param x x坐标
     * @param y y坐标
     * @param date 默认选中的日期
     */
    public void showAtLocation(View parent, int gravity, int x, int y, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }else {
            calendar.setTime(date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
        update();
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic true是，false否
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    timeSelectListener.onTimeSelect(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        }
    }

    /**
     * 时间选择监听
     */
    public interface OnTimeSelectListener {
        /**
         * 选择时间后回调方法
         * @param date 选中的时间
         */
        public void onTimeSelect(Date date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

}
