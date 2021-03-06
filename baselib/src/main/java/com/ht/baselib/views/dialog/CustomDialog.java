package com.ht.baselib.views.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ht.baselib.R;
import com.ht.baselib.utils.DeviceUtils;
import com.ht.baselib.utils.LocalDisplay;
import com.ht.baselib.utils.StringUtils;

/**
 * Msg: 自定义弹出框类 1、提供加载类弹出框功能；2、提供确认类弹出框功能
 * Update:  2015/9/29
 * Version: 1.0
 * Created by zmc on 2015/9/29 10:09.
 * 使用示例：
 * 1）确认类：
 CustomDialog cDialog = CustomDialog
 .newConfirmInstance(activity, "Notifications may include alerts, sounds and icon badges. These can be configured in Setting.");
 cDialog.setIsCancelable(true);
 cDialog.setTitle("我是标题也");
 cDialog.setCancelBtnText("取消");
 cDialog.setConfirmBtnText("确定");
 cDialog.setBtnCallback(new CustomDialog.ButtonCallback() {
@Override
public void onNegative(CustomDialog dialog) {
CustomToast.showLongToastCenter(activity, "onNegative：取消");
super.onNegative(dialog);
}

@Override
public void onPositive(CustomDialog dialog) {
CustomToast.showLongToastWithIcon(activity, R.mipmap.icon_chat, "onPositive：确认");
super.onPositive(dialog);
}
});
 cDialog.show();
 * 2）加载类：
 CustomDialog cDialog = CustomDialog.newLoadingInstance(activity);
 cDialog.show();
 */
public final class CustomDialog extends Dialog{
    //变量区 start======================================
    /**上下文*/
    private Context mContext;
    /**弹出框类型：0加载类；1确认类*/
    private int mType;
    /**弹出框是否含有输入框：true有，false无*/
    private boolean mHasInput;
    /**弹出框的输入框是否密文 true是，false否*/
    private boolean mIsCiphertext;
    /**设置所有输入框的输入类型：-1表示不设置，值如：InputType.TYPE_CLASS_TEXT，来自android.text.InputType*/
    private int mInputType = -1;

    /**按钮监听器*/
    private ButtonCallback mCallback;
    /**是否可点击它处取消*/
    private boolean mCancelable = false;

    /**标题内容（主要内容），若为null则表示不显示*/
    private String mTitleText = null;
    /**提示内容（辅助说明），若为null，则表示不显示*/
    private String mContent = null;
    /**提示内容（辅助说明），若为null，则表示不显示，支持设置字体颜色、链接、大小的等*/
    private SpannableString mSpannableContent = null;

    /**取消类按钮名称（左侧），若为null则表示不显示*/
    private String mCancelText = null;
    /**确认类按钮名称（右侧），若为null则表示不显示*/
    private String mConfirmText = null;

    /**第一个输入框hint内容*/
    private String mHintText = null;
    /**第一个输入框默认内容*/
    private String mInputDefaultText = null;
    /**第一个输入框view*/
    private EditText mEditText = null;

    /**确认类按钮view*/
    private Button mConfirmBtn = null;
    /**确认类按钮是否需要根据输入框的内容来设置可用性：true是，false否*/
    private boolean mControlEnable = false;

    /**单位（右侧），若为null则表示不显示*/
    private String mConfirmTextView = null;

    /**弹出框含有的输入框个数：目前最多为2*/
    private int mMultiInputSize = 1;

    /**第二个输入框view*/
    private EditText mSecondEditText = null;
    /**第二个输入框hint内容*/
    private String mSecondHintText = null;
    /**第二个输入框默认内容*/
    private String mSecondInputDefaultText = null;

    // 执行区
    //-----------------------------------------------
    /**
     * 初始化加载类弹出框(默认提示内容为：加载中...)
     * @param context 上下文
     * @return
     */
    public static CustomDialog newLoadingInstance(Context context){
        return new CustomDialog(context);
    }

    /**
     * 初始化确认类弹出框
     * @param context 上下文
     * @return
     */
    public static CustomDialog newConfirmInstance(Context context){
        return new CustomDialog(context ,null);
    }

    /**
     * 初始化确认类弹出框（含单个输入框）
     * @param context 上下文
     * @param isCiphertext 输入框是否密文 true是，false否
     * @return
     */
    public static CustomDialog newConfirmInstance(Context context, boolean isCiphertext){
        return new CustomDialog(context, isCiphertext);
    }

    /**
     * 初始化确认类弹出框（含两个输入框）
     * @param context 上下文
     * @return
     */
    public static CustomDialog newMultiInputInstance(Context context){
        return new CustomDialog(context, 2);
    }

    /**
     * 初始构造方法-加载类弹出框
     * @param context 上下文
     * <br/>加载类弹出框-默认提示内容为：加载中...)
     */
    private CustomDialog(Context context) {
        //设置dialog的显示风格
        super(context, R.style.loading_dialog);
        this.mContext = context;
        this.mType = 0;
    }

    /**
     * 初始构造方法-确认类弹出框
     * @param context 上下文
     * @param content 显示的内容（主要内容）
     */
    private CustomDialog(Context context, String content) {
        //设置dialog的显示风格
        super(context, R.style.confirm_dialog);
        this.mContext = context;
        this.mContent = content;
        this.mType = 1;
        this.mHasInput = false;
    }

    /**
     * 初始构造方法-确认类弹出框（含输入框）
     * @param context 上下文
     * @param isCiphertext 输入框是否密文 true是，false否
     */
    private CustomDialog(Context context, boolean isCiphertext) {
        //设置dialog的显示风格
        super(context, R.style.confirm_dialog);
        this.mContext = context;
        this.mType = 1;
        this.mHasInput = true;
        this.mIsCiphertext = isCiphertext;
        this.mMultiInputSize = 1;
    }

    /**
     * 初始构造方法-确认类弹出框（含两个输入框）
     * @param context 上下文
     * @param inputSize 含输入框个数，目前固定为2
     */
    private CustomDialog(Context context, int inputSize) {
        //设置dialog的显示风格
        super(context, R.style.confirm_dialog);
        this.mContext = context;
        this.mType = 1;
        this.mHasInput = true;
        this.mIsCiphertext = false;
        this.mMultiInputSize = inputSize;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newInstance();
    }

    /**
     * 初始化
     * @return
     */
    private CustomDialog newInstance(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (mType == 0){
            initLoadingView(inflater, mContext.getString(R.string.ht_common_loading_data));
        }else if (mType == 1){
            initConfirmView(inflater);
        }
        initConfig();
        return this;
    }

    // 方法区
    //-----------------------------------------------

    /**
     * 加载配置
     */
    private void initConfig(){
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，
        //则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCancelable);
        //若setCancelable(false)， 则需要监听返回键手动处理dialog的消失(确认类的)


        if (mType == 1) {
            String deviceType = DeviceUtils.getPhoneModel();
            if (TextUtils.isEmpty(deviceType) || !deviceType.equals("GT-I9100")) {
                //设置该值，会导致在某些机型（I9100 4.4.4）下，宽度撑满屏幕
                this.getWindow().setBackgroundDrawableResource(R.drawable.bg_t99_custom_dialog);
            }
        }
    }

    /**
     * 加载确认类自定义布局
     * @param inflater 布局处理器
     */
    private void initConfirmView(LayoutInflater inflater){
        //加载自定义布局
        View rootView;
        if (mHasInput){
            rootView = inflater.inflate(R.layout.custom_dialog_confirm_input, null);
        }else {
            rootView = inflater.inflate(R.layout.custom_dialog_confirm, null);
        }
        //根视图
        LinearLayout confirmDialogView = (LinearLayout)rootView.findViewById(R.id.confirm_dialog_view);
        //宽度占屏幕宽80%
        int mWidth = (LocalDisplay.screenWidthPixels * 8 / 10);
        //设置布局
        confirmDialogView.setLayoutParams(new LinearLayout.LayoutParams(
                mWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
        //处理生成自定义布局的具体布局
        creatConfirmSubView(rootView);
        //设置布局
        this.setContentView(rootView, new LinearLayout.LayoutParams(
                mWidth, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 处理生成确认类自定义布局的具体布局
     * @param rootView 自定义布局根视图
     */
    private void creatConfirmSubView(View rootView){
        //标题
        TextView confirmTip = (TextView) rootView.findViewById(R.id.confirm_tip);
        if (!StringUtils.isBlank(mTitleText)) {
            confirmTip.setVisibility(View.VISIBLE);
            confirmTip.setText(mTitleText);
        }else{
            confirmTip.setVisibility(View.GONE);
        }
        //内容
        TextView confirmTextView = (TextView)rootView.findViewById(R.id.confirm_textView);
        if (!StringUtils.isBlank(mContent)) {
            confirmTextView.setText(mContent);
            confirmTextView.setVisibility(View.VISIBLE);
        }else{
            if (null != mSpannableContent){
                confirmTextView.setText(mSpannableContent);
                confirmTextView.setVisibility(View.VISIBLE);
            }else {
                confirmTextView.setVisibility(View.GONE);
            }
        }

        //取消按钮
        Button negativeButton = (Button) rootView.findViewById(R.id.negative_button);
        if (!StringUtils.isBlank(mCancelText)) {
            negativeButton.setText(mCancelText);
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mCallback) {
                        mCallback.onNegative(CustomDialog.this);
                    } else {
                        CustomDialog.this.dismiss();
                    }
                }
            });
        }else{
            rootView.findViewById(R.id.v_line).setVisibility(View.GONE);
            negativeButton.setVisibility(View.GONE);
        }
        //确认按钮
        Button positiveButton = (Button) rootView.findViewById(R.id.positive_button);
        if (!StringUtils.isBlank(mConfirmText)) {
            if (!StringUtils.isBlank(mCancelText)){
                positiveButton.setTypeface(positiveButton.getTypeface(), Typeface.BOLD);
            }else{
                positiveButton.setTypeface(positiveButton.getTypeface(), Typeface.NORMAL);
            }
            positiveButton.setText(mConfirmText);
            positiveButton.setVisibility(View.VISIBLE);
            if (mControlEnable){
                positiveButton.setEnabled(false);
            }else{
                positiveButton.setEnabled(true);
            }
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mCallback) {
                        if (null != getEditText()) {
                            if (null != getSecondEditText()){
                                mCallback.onPositive(CustomDialog.this, getEditText(), getSecondEditText());
                            }else {
                                mCallback.onPositive(CustomDialog.this, getEditText());
                            }
                        } else {
                            mCallback.onPositive(CustomDialog.this);
                        }
                    } else {
                        CustomDialog.this.dismiss();
                    }
                }
            });
        }else{
            rootView.findViewById(R.id.v_line).setVisibility(View.GONE);
            positiveButton.setVisibility(View.GONE);
        }

        this.setConfirmBtn(positiveButton);

        //输入框的处理
        creatConfirmSubInputView(rootView);
    }

    /**
     * 处理生成确认类自定义布局的输入框布局
     * <p>由于输入框的监听会用到positiveButton，因此必须放置于按钮的初始化后面</p>
     * @param rootView 自定义布局根视图
     */
    private void creatConfirmSubInputView(View rootView){
        if (mHasInput) {
            //第一个输入框
            EditText confirmEdittext = (EditText) rootView.findViewById(R.id.confirm_edittext);
            confirmEdittext.setVisibility(View.VISIBLE);
            confirmEdittext.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (checkInput()){
                        getConfirmBtn().setEnabled(true);
                    }else{
                        if (mControlEnable){
                            getConfirmBtn().setEnabled(false);
                        }
                    }
                }
                @Override public void afterTextChanged(Editable s) {}
            });

            //设置输入框输入类型
            if (mInputType != -1){
                confirmEdittext.setInputType(mInputType);
            }else if (mMultiInputSize>1) {//含有多个输入框，则默认输入类型为数字
                confirmEdittext.setInputType(InputType.TYPE_CLASS_NUMBER); //调用数字键盘
            }

            if (mIsCiphertext) {//密文显示
                confirmEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {//明文显示
                confirmEdittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            if (!StringUtils.isBlank(mHintText)){
                confirmEdittext.setHint(mHintText);
            }
            if (!StringUtils.isBlank(mInputDefaultText)) {
                confirmEdittext.setText(mInputDefaultText);
            }
            this.setEditText(confirmEdittext);

            //含有多个输入框
            if (mMultiInputSize>1) {
                //两个输入框间的斜线
                rootView.findViewById(R.id.confirm_textview_line).setVisibility(View.VISIBLE);
                //第二个输入框
                EditText secondConfirmEdittext = (EditText) rootView.findViewById(R.id.second_confirm_edittext);
                secondConfirmEdittext.setVisibility(View.VISIBLE);
                secondConfirmEdittext.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (checkInput()){
                            getConfirmBtn().setEnabled(true);
                        }else{
                            if (mControlEnable){
                                getConfirmBtn().setEnabled(false);
                            }
                        }
                    }
                    @Override public void afterTextChanged(Editable s) {}
                });
                //设置输入框输入类型
                if (mInputType != -1){
                    secondConfirmEdittext.setInputType(mInputType);
                }else if (mMultiInputSize>1) {//含有多个输入框，则默认输入类型为数字
                    secondConfirmEdittext.setInputType(InputType.TYPE_CLASS_NUMBER); //调用数字键盘
                }

                if (mIsCiphertext) {//密文显示
                    secondConfirmEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {//明文显示
                    secondConfirmEdittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                if (!StringUtils.isBlank(mSecondHintText)) {
                    secondConfirmEdittext.setHint(mSecondHintText);
                }
                if (!StringUtils.isBlank(mSecondInputDefaultText)) {
                    secondConfirmEdittext.setText(mSecondInputDefaultText);
                }
                this.setSecondEditText(secondConfirmEdittext);
            }

            //单位
            if (!StringUtils.isBlank(mConfirmTextView)){
                TextView confirmTextview = (TextView) rootView.findViewById(R.id.confirm_textview);
                confirmTextview.setVisibility(View.VISIBLE);
                confirmTextview.setText(mConfirmTextView);
            }
        }
    }

    /**
     * 加载加载类自定义布局
     * @param inflater 布局处理器
     * @param dContent 默认提示标题内容
     * @return
     */
    private void initLoadingView(LayoutInflater inflater, String dContent){
        //加载自定义布局
        View rootView = inflater.inflate(R.layout.custom_dialog_loading, null);
        //根视图
        LinearLayout loagdingDialogView = (LinearLayout)rootView.findViewById(R.id.loagding_dialog_view);
        //宽度占屏幕宽80%
        int mWidth = (LocalDisplay.screenWidthPixels * 6 / 10);
        //设置布局
        loagdingDialogView.setLayoutParams(new LinearLayout.LayoutParams(
                mWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

        //进度条

        //提示内容
        TextView loadingTip = (TextView) rootView.findViewById(R.id.loading_tip);
        loadingTip.setVisibility(View.VISIBLE);
        if (!StringUtils.isBlank(mContent)){
            loadingTip.setText(mContent);
        }else {
            if (null != mSpannableContent){
                loadingTip.setText(mSpannableContent);
            }else {
                loadingTip.setText(dContent);
            }
        }
        //设置布局
        this.setContentView(rootView, new LinearLayout.LayoutParams(
                mWidth, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置确认类按钮对象
     * @param confirmBtn 确认类按钮对象
     */
    private void setConfirmBtn(Button confirmBtn) {
        this.mConfirmBtn = confirmBtn;
    }

    /**
     * 获取确认类按钮对象
     * @return
     */
    private Button getConfirmBtn() {
        return mConfirmBtn;
    }

    /**
     * 设置第一个输入框对象
     * @param editText 输入框对象
     */
    private void setEditText(EditText editText) {
        this.mEditText = editText;
    }

    /**
     * 设置第二个输入框对象
     * @param secondEditText 输入框对象
     */
    private void setSecondEditText(EditText secondEditText) {
        this.mSecondEditText = secondEditText;
    }

    /**
     * 获取第一个输入框对象
     * @return
     */
    private EditText getEditText() {
        return mEditText;
    }

    /**
     * 设置第二个输入框对象
     */
    private EditText getSecondEditText() {
        return mSecondEditText;
    }

    /**
     * 检查输入内容是否满足启用按钮
     * @return true满足，false不满足
     */
    private boolean checkInput(){
        boolean isOK;
        EditText firstEditText = getEditText();
        EditText secondEditText = getSecondEditText();
        String firstInpuText ="";
        String secondInpuText = "";
        if (null != firstEditText){
            firstInpuText = firstEditText.getText().toString().trim();
        }
        if (null != secondEditText){
            secondInpuText = secondEditText.getText().toString().trim();
        }

        if (mMultiInputSize>1){
            if (!StringUtils.isBlank(firstInpuText) && !StringUtils.isBlank(secondInpuText)) {
                isOK = true;
            } else {
                isOK = false;
            }
        }else {
            if (!StringUtils.isBlank(firstInpuText) || !StringUtils.isBlank(secondInpuText)) {
                isOK = true;
            } else {
                isOK = false;
            }
        }
        return isOK;
    }

    //可配置属性方法
    //==============================================================================================
    /**
     * 设置Dialog是否可点击取消（点击dialog外空白处或者按返回键取消）
     * @param cancelable If true, the dialog is cancelable.  The default
     * is true.
     */
    public CustomDialog setIsCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    /**
     * 设置按钮监听回调方法
     * @param bCallback 回调方法
     */
    public CustomDialog setBtnCallback(ButtonCallback bCallback){
        this.mCallback = bCallback;
        return this;
    }

    /**
     * 设置标题内容，若为null则表示不显示
     * @param title 标题内容
     * */
    public CustomDialog setTitle(String title) {
        this.mTitleText = title;
        return this;
    }

    /**
     * 设置提示内容，若为null则显示默认内容
     * @param content 提示内容
     * @return
     */
    public CustomDialog setContent(String content) {
        this.mContent = content;
        return this;
    }

    /**
     * 设置提示内容，若为null则显示默认内容
     * @param spannableContent 提示内容(与Content互斥)，可设置字体颜色、链接、大小等
     * @return
     */
    public CustomDialog setSpannableContent(SpannableString spannableContent) {
        this.mSpannableContent = spannableContent;
        return this;
    }

    /**
     * 设置取消类按钮名称（左侧），若为null则表示不显示
     * @param cancelStr 取消类按钮名称
     * */
    public CustomDialog setCancelBtnText(String cancelStr) {
        this.mCancelText = cancelStr;
        return this;
    }

    /**
     * 确认类按钮名称（右侧），若为null则表示不显示
     * @param confirmStr 确认类按钮名称
     *
     * */
    public CustomDialog setConfirmBtnText(String confirmStr) {
        this.mConfirmText = confirmStr;
        return this;
    }

    /**
     * 若有输入框，则设置输入框是否密文显示
     * @param isCiphertext 是否密文显示 true是，false否
     * @return
     */
    public CustomDialog setIsCiphertext(boolean isCiphertext) {
        this.mIsCiphertext = isCiphertext;
        return this;
    }

    /**
     * (第一个输入框)设置输入框hint提示内容（若有输入框，才生效）
     * @param hintText 提示内容
     * @return
     */
    public CustomDialog setHintText(String hintText) {
        this.mHintText = hintText;
        return this;
    }

    /**
     * (第一个输入框)设置输入框默认内容（若有输入框，才生效）
     * @param inputDefaultText 默认内容
     * @return
     */
    public CustomDialog setInputDefaultText(String inputDefaultText) {
        this.mInputDefaultText = inputDefaultText;
        return this;
    }

    /**
     * 设置Dialog的确认类按钮是否需要根据输入框的内容来设置可用性：true是，false否
     * @param controlEnable true是（则初始化时，该按钮不可用），false否(默认值，则初始化时该按钮可用)
     */
    public CustomDialog setControlEnable(boolean controlEnable) {
        this.mControlEnable = controlEnable;
        return this;
    }

    /**
     * (第二个输入框)设置输入框hint提示内容（若有输入框，才生效）
     * @param hintText 提示内容
     * @return
     */
    public CustomDialog setSecondHintText(String hintText) {
        this.mSecondHintText = hintText;
        return this;
    }

    /**
     * (第二个输入框)设置输入框默认内容（若有输入框，才生效）
     * @param inputDefaultText 默认内容
     * @return
     */
    public CustomDialog setSecondInputDefaultText(String inputDefaultText) {
        this.mSecondInputDefaultText = inputDefaultText;
        return this;
    }

    /**
     * 设置单位（右侧），若为null则表示不显示
     * @param unitText 单位名称
     * @return
     */
    public CustomDialog setConfirmTextView(String unitText) {
        this.mConfirmTextView = unitText;
        return this;
    }

    /**
     * 设置所有输入框的输入类型，若为-1则表示不设置(值如：InputType.TYPE_CLASS_TEXT，来自android.text.InputType)
     * @param inputType 输入类型
     * @return
     */
    public CustomDialog setInputType(int inputType) {
        this.mInputType = inputType;
        return this;
    }

    //自定义方法类
    //==============================================================================================
    /**
     * confirmDialog 按钮回调方法类
     */
    public abstract static class ButtonCallback {
        /**
         * 取消按钮的事件
         * @param dialog 按钮本身
         */
        public void onNegative(CustomDialog dialog) {
            //默认关闭
            if (null != dialog) {
                dialog.dismiss();
            }
        }
        /**
         * 确认按钮的事件
         * @param dialog 按钮本身
         */
        public void onPositive(CustomDialog dialog) {
            //默认关闭
            if (null != dialog) {
                dialog.dismiss();
            }
        }

        /**
         * 确认按钮的事件
         * @param dialog 按钮本身
         * @param editText 文本输入框
         */
        public void onPositive(CustomDialog dialog, EditText editText) {
            this.onPositive(dialog);
        }

        /**
         * 确认按钮的事件
         * @param dialog 按钮本身
         * @param editText 第一个文本输入框
         * @param secondEditText 第二个文本输入框
         */
        public void onPositive(CustomDialog dialog, EditText editText, EditText secondEditText) {
            this.onPositive(dialog);
        }

        /**按钮回调方法默认构造方法*/
        public ButtonCallback() {
        }

        /**
         * 克隆方法
         * @return
         * @throws CloneNotSupportedException 找不到类异常
         */
        protected final Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        /**
         * equals 方法
         * @param o 被比较对象
         * @return
         */
        public final boolean equals(Object o) {
            return super.equals(o);
        }

        /**
         * finalize方法
         * @throws Throwable 抛异常
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
