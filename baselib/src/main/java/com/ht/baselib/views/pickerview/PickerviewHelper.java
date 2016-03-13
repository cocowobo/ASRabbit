package com.ht.baselib.views.pickerview;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Msg: 选项选择器帮助类
 * Update:  2015/10/09
 * Version: 1.0
 * Created by zmingchun on 2015/10/09 15:15.
 *
 * 使用示例：
 * //性别选择
     sex = (TextView)findViewById(R.id.sex);
     OptionsPopupWindow pwOptions = PickerviewHelper.createSexPicker(mContext,null,
     new PickerviewHelper.PickerviewCallback(){
    @Override
    public void onResult(String result1) {
    super.onResult(result1);
    _sex = result1;
    sex.setText(result1);
    }
    });
     sex.setOnClickListener(v -> {
     //弹出性别选择器
     pwOptions.showAtLocation(sex, Gravity.BOTTOM, 0, 0);
     });

     //生日（出生日期）选择
     birthday = (TextView)findViewById(R.id.birthday);
     //时间选择器
     TimePopupWindow pwTime = PickerviewHelper.createDatePicker(mContext, new PickerviewHelper.PickerviewCallback() {
    @Override
    public void onResult(String result1) {
    super.onResult(result1);
    _brithday = result1;
    birthday.setText(_brithday);
    }
    });
     birthday.setOnClickListener(v -> {
     //弹出时间选择器
     pwTime.showAtLocation(birthday, Gravity.BOTTOM, 0, 0, new Date());
     });
 *
 */
public class PickerviewHelper {
    /**
     * 构造时间选择器
     * @param mContext 上下文
     * @param pCallback 选择结果回调接口
     * @return
     */
    public static TimePopupWindow createDatePicker(Context mContext, final PickerviewCallback pCallback){
        //时间选择器
        TimePopupWindow pwTime = new TimePopupWindow(mContext, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1949, 2100);
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener(){

                                           @Override
                                           public void onTimeSelect(Date date) {
                                               if (null != date) {
                                                   pCallback.onResult(date);
                                               }
                                           }
                                       });
        return pwTime;
    }

    /**
     * 构造人数选择器（范围：1~5）
     * @param mContext 上下文
     * @param defaultVal 默认选中值
     * @param pCallback 选择结果回调接口
     * @return
     */
    public static OptionsPopupWindow createNumberPicker(Context mContext, String defaultVal, final PickerviewCallback pCallback) {
        final ArrayList<String> options1Items = new ArrayList<>();
        //选项选择器
        final OptionsPopupWindow tPwOptions = new OptionsPopupWindow(mContext);
        //默认选中项目的位置
        int defaultKey = 0;
        //选项
        for (int j=0,i=1;i<=5;j++,i++){
            options1Items.add(""+i);
            if (!TextUtils.isEmpty(defaultVal) && defaultVal.equals(""+i)) {
                defaultKey = j;
            }else if (i==1){
                defaultKey = j;
            }
        }

        //联动效果
        tPwOptions.setPicker(options1Items);
        //设置选择的单位
        //tPwOptions.setLabels("人");
        //设置默认选中的项目
        tPwOptions.setSelectOptions(defaultKey);
        //监听确定选择按钮
        tPwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的是选中的位置
                String tx = options1Items.get(options1);
                pCallback.onResult(tx);
            }
        });
        return tPwOptions;
    }

    /**
     * 构造性别选择器
     * @param mContext 上下文
     * @param defaultVal 默认选中值
     * @param pCallback 选择结果回调接口
     * @return
     */
    public static OptionsPopupWindow createSexPicker(Context mContext, String defaultVal, final PickerviewCallback pCallback) {
        final ArrayList<String> options1Items = new ArrayList<>();
        //选项选择器
        final OptionsPopupWindow tPwOptions = new OptionsPopupWindow(mContext);
        //默认选中项目的位置
        int defaultKey = 0;

        //选项
        options1Items.add("男");
        options1Items.add("女");

        if (!TextUtils.isEmpty(defaultVal) && defaultVal.equals("女")){
            defaultKey = 1;
        }
        //联动效果
        tPwOptions.setPicker(options1Items);
        //设置选择的label
        tPwOptions.setLabels("选择性别");
        //设置默认选中的项目
        tPwOptions.setSelectOptions(defaultKey);
        //监听确定选择按钮
        tPwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的是选中的位置
                String tx = options1Items.get(options1);
                pCallback.onResult(tx);
            }
        });
        return tPwOptions;
    }

    /**
     * 构造身高选择器
     * @param mContext 上下文
     * @param defaultVal 默认选中值
     * @param pCallback 选择结果回调接口
     * @return
     */
    public static OptionsPopupWindow createHeightPicker(Context mContext, String defaultVal, final PickerviewCallback pCallback) {
        final ArrayList<String> options1Items = new ArrayList<>();
        //选项选择器
        final OptionsPopupWindow tPwOptions = new OptionsPopupWindow(mContext);
        //默认选中项目的位置
        int defaultKey = 0;
        //选项
        for (int j=0,i=150;i<=210;j++,i++){
            options1Items.add(""+i);
            if (!TextUtils.isEmpty(defaultVal) && defaultVal.equals(""+i)) {
                defaultKey = j;
            }else if (i==160){
                defaultKey = j;
            }
        }

        //联动效果
        tPwOptions.setPicker(options1Items);
        //设置选择的单位
        tPwOptions.setLabels("cm");
        //设置默认选中的项目
        tPwOptions.setSelectOptions(defaultKey);
        //监听确定选择按钮
        tPwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的是选中的位置
                String tx = options1Items.get(options1);
                pCallback.onResult(tx);
            }
        });
        return tPwOptions;
    }

    /**
     * 构造体重选择器
     * @param mContext 上下文
     * @param defaultVal 默认选中值
     * @param pCallback 选择结果回调接口
     * @return
     */
    public static OptionsPopupWindow createWeightPicker(Context mContext, String defaultVal, final PickerviewCallback pCallback) {
        final ArrayList<String> options1Items = new ArrayList<>();
        //选项选择器
        final OptionsPopupWindow tPwOptions = new OptionsPopupWindow(mContext);
        //默认选中项目的位置
        int defaultKey = 0;
        //选项
        for (int j=0,i=35;i<100;j++,i++){
            options1Items.add(""+i);
            if (!TextUtils.isEmpty(defaultVal) && defaultVal.equals(""+i)) {
                defaultKey = j;
            }else if (i==50){
                defaultKey = j;
            }
        }
        //联动效果
        tPwOptions.setPicker(options1Items);
        //设置选择的label
        tPwOptions.setLabels("kg");
        //设置默认选中的项目
        tPwOptions.setSelectOptions(defaultKey);
        //监听确定选择按钮
        tPwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的是选中的位置
                String tx = options1Items.get(options1);
                pCallback.onResult(tx);
            }
        });
        return tPwOptions;
    }

    /**
     * 构造行业选择器
     * @param mContext 上下文
     * @param defaultVal 默认选中值
     * @param pCallback 选择结果回调接口
     * @return
     */
    public static OptionsPopupWindow createIndustryPicker(Context mContext, String defaultVal, final PickerviewCallback pCallback) {
        final ArrayList<String> options1Items = new ArrayList<>();
        //选项选择器
        final OptionsPopupWindow tPwOptions = new OptionsPopupWindow(mContext);
        //默认选中项目的位置
        int defaultKey = 0;
        //选项
        options1Items.add("无");
        options1Items.add("娱乐/艺术/表演");
        options1Items.add("文化/广告/传媒");
        options1Items.add("计算机/互联网/通信");
        options1Items.add("公务员/事业单位");
        options1Items.add("金融/银行/投资");
        options1Items.add("商业/服务业");
        options1Items.add("律师/财务/咨询");
        options1Items.add("医药/护理/制药");
        options1Items.add("教育/培训");
        options1Items.add("自由职业/学生");
        for (int i = 0; i < options1Items.size(); i++){
            if (!TextUtils.isEmpty(defaultVal) && defaultVal.equals(options1Items.get(i))){
                defaultKey = i;
            }
        }

        //联动效果
        tPwOptions.setPicker(options1Items);
        //设置默认选中的项目
        tPwOptions.setSelectOptions(defaultKey);
        //监听确定选择按钮
        tPwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的是选中的位置
                String tx = options1Items.get(options1);
                pCallback.onResult(tx);
            }
        });
        return tPwOptions;
    }

    /**
     * 自定义选择器-选择回调方法
     */
    public abstract static class PickerviewCallback {
        /**
         * 单列选中结果回调方法
         * @param result1 date类型结果
         */
        public void onResult(Date result1) {
        }

        /**
         * 单列选中结果回调方法
         * @param result1 字符串类型结果
         */
        public void onResult(String result1) {
        }

        /**
         * 双列选中结果回调方法
         * @param result1 第一列字符串类型结果
         * @param result2 第二列字符串类型结果
         */
        public void onResult(String result1, String result2) {
        }

        /**
         * 三列选中结果回调方法
         * @param result1 第一列字符串类型结果
         * @param result2 第二列字符串类型结果
         * @param result3 第三列字符串类型结果
         */
        public void onResult(String result1, String result2, String result3) {
        }

        /**
         * 默认构造方法
         */
        public PickerviewCallback() {
        }

        /**
         * 克隆方法
         * @return
         * @throws CloneNotSupportedException 不支持clone导致异常
         */
        protected final Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        /**
         * equals方法
         * @param o 被比较对象
         * @return
         */
        public final boolean equals(Object o) {
            return super.equals(o);
        }

        /**
         * finalize方法
         * @throws Throwable 抛出的异常
         */
        protected final void finalize() throws Throwable {
            super.finalize();
        }

        /**
         * hashCode方法
         * @return
         */
        public final int hashCode() {
            return super.hashCode();
        }

        /**
         * toString方法
         * @return
         */
        public final String toString() {
            return super.toString();
        }
    }
}
