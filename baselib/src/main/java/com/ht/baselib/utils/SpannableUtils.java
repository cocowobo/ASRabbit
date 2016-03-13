package com.ht.baselib.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *     Spannable工具类，用于设置文字的前景色、背景色、Typeface、粗体、斜体、字号、超链接、删除线、下划线、上下标等
 * </p>
 * @author zmingchun
 * @version 1.0(2015-05-22)
 */
public final class SpannableUtils {
    /**富文本属性-字符串*/
    public static final String RICHTEXT_STRING = "string";
    /**富文本属性-颜色*/
    public static final String RICHTEXT_COLOR = "color";
    /**富文本属性-字体大小*/
    public static final String RICHTEXT_SIZE = "size";
    /**富文本属性-超链接*/
    public static final String RICHTEXT_RSIZE = "relativesize";
    /**富文本属性-删除线*/
    public static final String RICHTEXT_DELETE = "delete";

    private SpannableUtils( ){

    }

    /**
     * 设置指定范围字符串为可点击超链接
     * @param content 内容
     * @param startIndex 可点击范围起点（中英文一样）
     * @param endIndex 可点击范围终点（中英文一样）
     * @param mColor
     *        <p>颜色值集：
     *        <br/>* 第一个为mTextNormalColor 超链接默认文本颜色（为16进制颜色值，默认为Color.BLACK）
     *        <br/>* 第二个为mTextPressedColor 超链接点击时文本颜色（默认为Color.BLUE）
     *        <br/>* 第三个为mBackgroundNormalColor 超链接默认背景颜色（默认为Color.WHITE）
     *        <br/>* 第四个为mBackgroundPressedColor 超链接点击时背景颜色（默认为Color.WHITE）
     *        </p>
     * @param clickListener 点击事件监听
     * @return
     */
    public static SpannableString setTextClick(String content, int startIndex, int endIndex
            ,int[] mColor,View.OnClickListener clickListener) {
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }
        final LinkTextHolder linkTextHolder = new LinkTextHolder();
        linkTextHolder.mLinkTextConfig = new LinkTextConfig();
        //Set custom color
        linkTextHolder.mLinkTextConfig.mTextNormalColor = mColor[0];
        linkTextHolder.mLinkTextConfig.mTextPressedColor = mColor[1];
        linkTextHolder.mLinkTextConfig.mBackgroundNormalColor = mColor[3];
        linkTextHolder.mLinkTextConfig.mBackgroundPressedColor = mColor[4];

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new TouchableSpan(clickListener,linkTextHolder), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(mTextNormalColor, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * <p>改变字符串中某一段文字的字号
     * setTextSize("",24,0,2) = null;
     * setTextSize(null,24,0,2) = null;
     * setTextSize("abc",-2,0,2) = null;
     * setTextSize("abc",24,0,4) = null;
     * setTextSize("abc",24,-2,2) = null;
     * setTextSize("abc",24,0,2) = normal string
     * <p/>
     * @param content 内容
     * @param startIndex 开始位置索引
     * @param endIndex  结束位置索引
     * @param fontSize 字体大小
     * */
    public static SpannableString setTextSize( String content, int startIndex, int endIndex, int fontSize ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan( new AbsoluteSizeSpan( fontSize ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        return spannableString;
    }

    /**
     * 在某位置之间处理内容
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextSub( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan( new SubscriptSpan( ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextSuper( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan( new SuperscriptSpan( ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextStrikethrough( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextUnderline( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextBold( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextItalic( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static SpannableString setTextBoldItalic( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @param foregroundColor 前景色
     * @return
     */
    public static SpannableString setTextForeground( String content, int startIndex, int endIndex, int foregroundColor ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new ForegroundColorSpan( foregroundColor ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     *
     * @param content 处理内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @param backgroundColor 背景色
     * @return
     */
    public static SpannableString setTextBackground( String content, int startIndex, int endIndex, int backgroundColor ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new BackgroundColorSpan( backgroundColor ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 设置文本的超链接
     * @param content 需要处理的文本
     * @param startIndex 开始位置索引
     * @param endIndex 被处理文本中需要处理字串的开始和结束索引
     * @param url 文本对应的链接地址，需要注意格式：
     * （1）电话以"tel:"打头，比如"tel:02355692427"
     * （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     * （3）短信以"sms:"打头，比如"sms:02355692427"
     * （4）彩信以"mms:"打头，比如"mms:02355692427"
     * （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     * （6）网络以"http://"打头，比如"http://www.google.com"
     * */
    public static SpannableString setTextURL( String content, int startIndex, int endIndex, String url ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new URLSpan( url ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 设置图片的显示
     * @param content 内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @param drawable 图片资源
     * @return
     */
    public static SpannableString setTextImg( String content, int startIndex, int endIndex, Drawable drawable ){
        if( TextUtils.isEmpty( content ) || startIndex < 0 || endIndex > content.length( ) || startIndex >= endIndex ){
            return null;
        }

        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new ImageSpan(drawable), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 设置图片的显示
     * @param mContext 上下文
     * @param content 内容
     * @param startIndex 开始位置
     * @param endIndex 结束位置
     * @param drawableId 图片资源ID
     * @return
     */
    public static SpannableString setTextImg(Context mContext, String content, int startIndex, int endIndex, int drawableId ){
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return setTextImg(content,startIndex,endIndex,drawable);
    }


    /**
     * 将富文本转成CharSequence
     * @param mContext 上下文
     * @param commonStr 普通内容
     * @param drawableId  表情图片
     * @return
     */
    public static CharSequence transferImageText(final Context mContext, String commonStr,int drawableId) {
        /**
         * 用于富文本显示-获取本地图片资源
         */
        final Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                // 根据id从资源文件中获取图片对象
                Drawable d = mContext.getApplicationContext().getResources().getDrawable(id);
                if (d != null) {
                    // 以此作为标志位，方便外部取出对应的资源id
                    d.setState(new int[] { id });
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                }
                return d;
            }
        };
        return Html.fromHtml(commonStr + "<img src=\"" + drawableId + "\">", imageGetter, null);
    }

    /**
     * 根据传入的hashmaplist组成富文本返回,key在SpannableUtils里
     * @param list 文本集
     * @return
     */
    public static SpannableStringBuilder getSpannableStringFromList(List<HashMap<String,Object>> list){
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        int position = 0;
        for (int i=0;i<list.size();i++){
            HashMap<String,Object> map = list.get(i);
            try{
                String st = (String)map.get(RICHTEXT_STRING);
                ssb.append(st);
                int len = st.length();

                if (map.containsKey(RICHTEXT_COLOR)){
                    int color = ((Integer)map.get(RICHTEXT_COLOR)).intValue();
                    ssb.setSpan(new ForegroundColorSpan(color), position, position+len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                if (map.containsKey(RICHTEXT_SIZE)){
                    int size = ((Integer)map.get(RICHTEXT_SIZE)).intValue();
                    ssb.setSpan(new AbsoluteSizeSpan(size), position, position+len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (map.containsKey(RICHTEXT_RSIZE)){
                    float size = ((Float)map.get(RICHTEXT_RSIZE)).floatValue();
                    ssb.setSpan(new RelativeSizeSpan(size), position, position+len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (map.containsKey(RICHTEXT_DELETE)){
                    ssb.setSpan(new StrikethroughSpan(),position, position+len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

//              android.text.style.RelativeSizeSpan
                position = position+len;

            } catch(Exception e){
                return null;
            }
        }
        return ssb;
        /**
         * 示例
         *
             List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
             HashMap<String, Object> map;
             map = new HashMap<String, Object>();
             map.put(SpannableUtils.RICHTEXT_STRING, "第1部分正常");
             list.add(map);

             map = new HashMap<String, Object>();
             map.put(SpannableUtils.RICHTEXT_STRING, "第2部分红色");
             map.put(SpannableUtils.RICHTEXT_COLOR, new Integer(Color.RED));
             list.add(map);

             map = new HashMap<String, Object>();
             map.put(SpannableUtils.RICHTEXT_STRING, "第3部分30号字");
             map.put(SpannableUtils.RICHTEXT_SIZE, new Integer(30));
             list.add(map);

             map = new HashMap<String, Object>();
             map.put(SpannableUtils.RICHTEXT_STRING, "第4部分蓝色加大35");
             map.put(SpannableUtils.RICHTEXT_COLOR, new Integer(Color.BLUE));
             map.put(SpannableUtils.RICHTEXT_SIZE, new Integer(35));
             list.add(map);

             tvBottom.setText(SpannableUtils.getSpannableStringFromList(list));
         *
         */
    }

    /**
     * 设置textView 的富文本内容的图片点击事件
     * @param mContext 上下文
     * @param textView 文本域
     */
    public void setSpanClickable(final Context mContext,TextView textView) {
        Spanned s = new SpannableString(textView.getText());
        //setMovementMethod很重要，不然ClickableSpan无法获取点击事件。
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
        for (ImageSpan span : imageSpans) {
            final String imageSrc = span.getSource();
            final int start = s.getSpanStart(span);
            final int end = s.getSpanEnd(span);
            ClickableSpan clickSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(mContext, "Image Clicked " + imageSrc, Toast.LENGTH_SHORT).show();
                }
            };

            ClickableSpan[] clickSpans = s.getSpans(start, end, ClickableSpan.class);
            if (clickSpans.length != 0) {
                // remove all click spans
                for (ClickableSpan cSpan : clickSpans) {
                    ((Spannable) s).removeSpan(cSpan);
                }
            }

            ((Spannable) s).setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }




// 处理超链接点击前后颜色变化
//===============================
    private static class LinkTextHolder {
        //Color config.
        private LinkTextConfig mLinkTextConfig;
    }

    /**
     * 初始化默认配置
     */
    public static class LinkTextConfig {
        /**是否有下划线*/
        public boolean mIsLinkUnderLine = false;
        /**文本默认颜色*/
        public int mTextNormalColor = Color.BLACK;
        /**点击超链接时文本颜色*/
        public int mTextPressedColor = Color.BLUE;
        /**默认背景颜色*/
        public int mBackgroundNormalColor = Color.WHITE;
        /**点击超链接时背景颜色*/
        public int mBackgroundPressedColor = Color.WHITE;
    }

    /**
     * 扩展LinkMovementMethod
     */
    public static class LinkTouchMovementMethod extends LinkMovementMethod {

        private TouchableSpan mPressedSpan;
        private static LinkTouchMovementMethod mInstance;
        /**
         * Override static method
         * @return
         */
        public static LinkTouchMovementMethod getInstance() {
            if (mInstance == null) {
                mInstance = new LinkTouchMovementMethod();
            }
            return mInstance;
        }

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPressedSpan = getPressedSpan(textView, spannable, event);
                    if (mPressedSpan != null) {
                        mPressedSpan.setPressed(true);
                        Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                                spannable.getSpanEnd(mPressedSpan));
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                    if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                        mPressedSpan.setPressed(false);
                        mPressedSpan = null;
                        Selection.removeSelection(spannable);
                    }
                    break;

                default:
                    if (mPressedSpan != null) {
                        mPressedSpan.setPressed(false);
                        super.onTouchEvent(textView, spannable, event);
                    }
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                    break;
            }

            return true;
        }

        /**
         * 获取点击的文字范围
         * @param textView
         * @param spannable
         * @param event
         * @return
         */
        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
    }

    /**
     * 内部类，用于截获点击富文本后的事件
     */
    public static class TouchableSpan extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;
        private boolean mIsPressed;
        private LinkTextHolder linkTextHolder;

        /**
         * 构造方法
         * @param l 点击监听回调方法
         * @param h LinkTextHolder
         */
        public TouchableSpan(View.OnClickListener l, LinkTextHolder h) {
            mListener = l;
            linkTextHolder = h;
        }

        //For LinkTouchMovementMethod call
        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(mIsPressed ? linkTextHolder.mLinkTextConfig.mTextPressedColor :
                    linkTextHolder.mLinkTextConfig.mTextNormalColor);
            textPaint.bgColor = mIsPressed ? linkTextHolder.mLinkTextConfig.mBackgroundPressedColor :
                    linkTextHolder.mLinkTextConfig.mBackgroundNormalColor;
            //是否去除超链接的下划线
            textPaint.setUnderlineText(linkTextHolder.mLinkTextConfig.mIsLinkUnderLine);
        }
    }
}
