package com.ht.baselib.views.slideswitch;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ht.baselib.R;

/**
 * <p>自定义滑动开关组件
 * <br/>注意：layout_width和layout_height和实际显示有偏差，不能精确设置
 * </p>
 *
 * @author 7heaven (https://github.com/7heaven/SHSwitchView)
 * @author zmingchun (Updated by)
 * @version: 1.0(2015/9/25)
 * <p/>
 * 增加reverseEnabled 属性，控制开关滑片方向：true反iOS7风格(滑片方向和iOS7相反的)，false iOS7风格
 * 相关属性：
 * declare-styleable name="ShSwitchView"
 * attr name="tintColor" format="reference|color" 开状态颜色
 * attr name="outerStrokeWidth" format="reference|dimension"外边框宽度
 * attr name="shadowSpace" format="reference|dimension"阴影间距
 * attr name="reverseEnabled" format="reference|boolean"滑片方向是否和ios7原生开关控件方向相反
 */
public class ShSwitchView extends View {
    //默认的持续时间300毫秒
    private static final long COMMON_DURATION = 300L;

    // 属性动画定义相关
    //============================================
    //里层内容变换动画
    private ObjectAnimator innerContentAnimator;
    //属性定义
    private Property<ShSwitchView, Float> innerContentProperty = new Property<ShSwitchView, Float>(Float.class, "innerBound") {
        @Override
        public void set(ShSwitchView sv, Float innerContentRate) {
            sv.setInnerContentRate(innerContentRate);
        }

        @Override
        public Float get(ShSwitchView sv) {
            return sv.getInnerContentRate();
        }
    };

    //收缩扩展动画
    private ObjectAnimator knobExpandAnimator;
    //属性定义
    private Property<ShSwitchView, Float> knobExpandProperty = new Property<ShSwitchView, Float>(Float.class, "knobExpand") {
        @Override
        public void set(ShSwitchView sv, Float knobExpandRate) {
            sv.setKnobExpandRate(knobExpandRate);
        }

        @Override
        public Float get(ShSwitchView sv) {
            return sv.getKnobExpandRate();
        }
    };

    //滑片移动动画
    private ObjectAnimator knobMoveAnimator;
    //属性定义
    private Property<ShSwitchView, Float> knobMoveProperty = new Property<ShSwitchView, Float>(Float.class, "knobMove") {
        @Override
        public void set(ShSwitchView sv, Float knobMoveRate) {
            sv.setKnobMoveRate(knobMoveRate);
        }

        @Override
        public Float get(ShSwitchView sv) {
            return sv.getKnobMoveRate();
        }
    };

    // 手势定义相关
    //============================================
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        //
        @Override
        public boolean onDown(MotionEvent event) {
            //若view不可用，则不响应手势动作
            if (!isEnabled()) {
                return false;
            }
            preIsOn = isOn;

            innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
            innerContentAnimator.start();

            knobExpandAnimator.setFloatValues(knobExpandRate, 1.0F);
            knobExpandAnimator.start();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent event) {

        }

        //一次点击up事件
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            isOn = knobState;

            if (preIsOn == isOn) {
                isOn = !isOn;
                knobState = !knobState;
            }

            // 切换动画属性设置
            //-------------------------------
            if (knobState) {
                if (reverseEnabled) {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                } else {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                }
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            } else {
                if (reverseEnabled) {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                } else {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                }
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }

            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();

            if (ShSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
                ShSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(ShSwitchView.this, isOn);
            }

            return true;
        }

        //在屏幕上拖动事件-手指移动就会执行
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e2.getX() > centerX) {
                if (!knobState) {
                    knobState = !knobState;
                    if (reverseEnabled) {
                        knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                    } else {
                        knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                    }
                    knobMoveAnimator.start();

                    innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                    innerContentAnimator.start();
                }
            } else {
                if (knobState) {
                    knobState = !knobState;
                    if (reverseEnabled) {
                        knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                    } else {
                        knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                    }
                    knobMoveAnimator.start();
                }
            }
            return true;
        }
    };

    // 自定义Switch相关
    //============================================
    private boolean reverseEnabled;
    private int width;
    private int height;

    private int centerX;
    private int centerY;

    private float cornerRadius;

    private int shadowSpace;
    private int outerStrokeWidth;

    private RectF knobBound;
    private float knobMaxExpandWidth;
    private float intrinsicKnobWidth;
    private float knobExpandRate;
    private float knobMoveRate;

    private boolean knobState;//开关状态：true开，false关
    private boolean isOn;     //是否开：true是，false否
    private boolean preIsOn;

    private RectF innerContentBound;
    private float innerContentRate = 1.0F;

    private float intrinsicInnerWidth;
    private float intrinsicInnerHeight;

    private int tintColor;

    private int tempTintColor;

    private static final int BACKGROUND_COLOR = 0xFFCCCCCC;//默认背景色-浅灰色
    private int colorStep = BACKGROUND_COLOR;
    //前景色-扩展收缩遮罩层颜色-白色
    private static final int FOREGROUND_COLOR = 0xFFFFFFFF;
    private Paint paint;

    //临时绘图区域
    private RectF tempForRoundRect;

    private boolean dirtyAnimation = false;
    private boolean isAttachedToWindow = false;

    /**
     * 状态切换监听
     */
    public interface OnSwitchStateChangeListener {
        /**
         * 状态切换监听方法
         *
         * @param v    对应的view
         * @param isOn true开，false关
         */
        void onSwitchStateChange(View v, boolean isOn);
    }

    private OnSwitchStateChangeListener onSwitchStateChangeListener;

    /**
     * 默认构造器
     *
     * @param context 上下文
     */
    public ShSwitchView(Context context) {
        this(context, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性值集
     */
    public ShSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造器
     *
     * @param context  上下文
     * @param attrs    属性值集
     * @param defStyle 样式资源id
     */
    public ShSwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShSwitchView);

        //滑片方向是否和ios7原生开关控件方向相反，默认为false
        reverseEnabled = ta.getBoolean(R.styleable.ShSwitchView_reverseEnabled, false);

        //默认颜色为0xFF9CE949
        tintColor = ta.getColor(R.styleable.ShSwitchView_tintColor, 0xFF9CE949);
        tempTintColor = tintColor;

        int defaultOuterStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5F, context.getResources().getDisplayMetrics());
        int defaultShadowSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());

        outerStrokeWidth = ta.getDimensionPixelOffset(R.styleable.ShSwitchView_outerStrokeWidth, defaultOuterStrokeWidth);
        shadowSpace = ta.getDimensionPixelOffset(R.styleable.ShSwitchView_shadowSpace, defaultShadowSpace);

        ta.recycle();

        knobBound = new RectF();
        innerContentBound = new RectF();
        tempForRoundRect = new RectF();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (reverseEnabled) {
            knobMoveRate = 1.0F;
        } else {
            knobMoveRate = 0.0F;
        }

        //开始值innerContentRate，终止值1.0F
        innerContentAnimator = ObjectAnimator.ofFloat(ShSwitchView.this, innerContentProperty, innerContentRate, 1.0F);
        innerContentAnimator.setDuration(COMMON_DURATION);
        innerContentAnimator.setInterpolator(new DecelerateInterpolator());

        //开始值knobExpandRate，终止值1.0F
        knobExpandAnimator = ObjectAnimator.ofFloat(ShSwitchView.this, knobExpandProperty, knobExpandRate, 1.0F);
        knobExpandAnimator.setDuration(COMMON_DURATION);
        knobExpandAnimator.setInterpolator(new DecelerateInterpolator());

        //开始值knobMoveRate，终止值1.0F
        knobMoveAnimator = ObjectAnimator.ofFloat(ShSwitchView.this, knobMoveProperty, knobMoveRate, 1.0F);
        knobMoveAnimator.setDuration(COMMON_DURATION);
        knobMoveAnimator.setInterpolator(new DecelerateInterpolator());
    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener) {
        this.onSwitchStateChangeListener = onSwitchStateChangeListener;
    }

    public OnSwitchStateChangeListener getOnSwitchStateChangeListener() {
        return this.onSwitchStateChangeListener;
    }

    /**
     * 设置内域刷新速率
     *
     * @param rate 速率
     */
    void setInnerContentRate(float rate) {
        this.innerContentRate = rate;
        //刷新Canvas-该方法的作用是告诉系统刷新当前控件的 “Canvas”，也就是触发一次：“onDraw(Canvas canvas)” 方法。
        invalidate();
    }

    float getInnerContentRate() {
        return this.innerContentRate;
    }

    /**
     * 设置扩展伸缩速率
     *
     * @param rate 速率
     */
    void setKnobExpandRate(float rate) {
        this.knobExpandRate = rate;
        //刷新Canvas
        invalidate();
    }

    float getKnobExpandRate() {
        return this.knobExpandRate;
    }

    /**
     * 设置滑片滑动速率
     *
     * @param rate 速率
     */
    void setKnobMoveRate(float rate) {
        this.knobMoveRate = rate;
        //刷新Canvas
        invalidate();
    }

    float getKnobMoveRate() {
        return knobMoveRate;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        //是否重写-手动设置开关状态setOn
        if (dirtyAnimation) {
            knobState = this.isOn;
            if (knobState) {
                if (reverseEnabled) {
                    //滑片移动-移动至左边
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                } else {
                    //滑片移动-移动至右边
                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                }
                knobMoveAnimator.start();

                //遮罩收缩
                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            } else {
                if (reverseEnabled) {
                    //滑片移动-移动至右边
                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                } else {
                    //滑片移动-移动至左边
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                }
                knobMoveAnimator.start();

                //遮罩扩展
                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }

            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();

            if (ShSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
                ShSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(ShSwitchView.this, isOn);
            }

            dirtyAnimation = false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        //make sure widget remain in a good appearance
        if ((float) height / (float) width < 0.33333F) {
            height = (int) ((float) width * 0.33333F);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));

            super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }

        centerX = width / 2;
        centerY = height / 2;
        //半径
        cornerRadius = centerY - shadowSpace;

        innerContentBound.left = outerStrokeWidth + shadowSpace;
        innerContentBound.top = outerStrokeWidth + shadowSpace;
        innerContentBound.right = width - outerStrokeWidth - shadowSpace;
        innerContentBound.bottom = height - outerStrokeWidth - shadowSpace;

        intrinsicInnerWidth = innerContentBound.width();
        intrinsicInnerHeight = innerContentBound.height();

        knobBound.left = outerStrokeWidth + shadowSpace;
        knobBound.top = outerStrokeWidth + shadowSpace;
        knobBound.right = height - outerStrokeWidth - shadowSpace;
        knobBound.bottom = height - outerStrokeWidth - shadowSpace;

        intrinsicKnobWidth = knobBound.height();
        knobMaxExpandWidth = (float) width * 0.7F;
        if (knobMaxExpandWidth > knobBound.width() * 1.25F) {
            knobMaxExpandWidth = knobBound.width() * 1.25F;
        }
    }

    public boolean isOn() {
        return this.isOn;
    }

    /**
     * 设置开关状态
     *
     * @param on true开，false关
     */
    public void setOn(boolean on) {
        setOn(on, false);
    }

    /**
     * 设置开关状态
     *
     * @param on       true开，false关
     * @param animated 是否带动画效果：true是，false否
     */
    public void setOn(boolean on, boolean animated) {

        if (this.isOn == on) {
            return;
        }

        if (!isAttachedToWindow && animated) {
            dirtyAnimation = true;
            this.isOn = on;

            return;
        }

        this.isOn = on;
        knobState = this.isOn;

        if (!animated) {
            if (on) {
                if (reverseEnabled) {
                    setKnobMoveRate(0.0F);
                } else {
                    setKnobMoveRate(1.0F);
                }
                setInnerContentRate(0.0F);
            } else {
                if (reverseEnabled) {
                    setKnobMoveRate(1.0F);
                } else {
                    setKnobMoveRate(0.0F);
                }
                setInnerContentRate(1.0F);
            }
            setKnobExpandRate(0.0F);
        } else {
            if (knobState) {
                if (reverseEnabled) {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                } else {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                }
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            } else {
                if (reverseEnabled) {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                } else {
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                }
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }
            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();
        }

        if (ShSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
            ShSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(ShSwitchView.this, isOn);
        }
    }

    /**
     * 设置开状态颜色
     *
     * @param tintColor 颜色资源值
     */
    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        tempTintColor = this.tintColor;
    }

    public int getTintColor() {
        return this.tintColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!knobState) {
                    //变化
                    innerContentAnimator = ObjectAnimator.ofFloat(ShSwitchView.this, innerContentProperty, innerContentRate, 1.0F);
                    //设置时间
                    innerContentAnimator.setDuration(300L);
                    //先快后慢
                    innerContentAnimator.setInterpolator(new DecelerateInterpolator());

                    innerContentAnimator.start();
                }

                knobExpandAnimator = ObjectAnimator.ofFloat(ShSwitchView.this, knobExpandProperty, knobExpandRate, 0.0F);
                knobExpandAnimator.setDuration(300L);
                knobExpandAnimator.setInterpolator(new DecelerateInterpolator());
                knobExpandAnimator.start();

                isOn = knobState;

                if (ShSwitchView.this.onSwitchStateChangeListener != null && isOn != preIsOn) {
                    ShSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(ShSwitchView.this, isOn);
                }

                break;
            default:
                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            this.tintColor = tempTintColor;
        } else {
            this.tintColor = this.rGBColorTransform(0.5F, tempTintColor, 0xFFFFFFFF);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //innerContentCalculation begin - 扩展收缩遮罩
        float w = intrinsicInnerWidth / 2.0F * innerContentRate;
        float h = intrinsicInnerHeight / 2.0F * innerContentRate;
        this.innerContentBound.left = centerX - w;
        this.innerContentBound.top = centerY - h;
        this.innerContentBound.right = centerX + w;
        this.innerContentBound.bottom = centerY + h;
        //innerContentCalculation end

        //knobExpandCalculation begin - 滑片扩展
        w = intrinsicKnobWidth + (knobMaxExpandWidth - intrinsicKnobWidth) * knobExpandRate;
        //判断滑片是否在左边
        boolean left = knobBound.left + knobBound.width() / 2 > centerX;

        if (left) {
            knobBound.left = knobBound.right - w;
        } else {
            knobBound.right = knobBound.left + w;
        }
        //knobExpandCalculation end

        //knobMoveCalculation begin - 滑片移动
        float kw = knobBound.width();
        w = (width - kw - ((shadowSpace + outerStrokeWidth) * 2)) * knobMoveRate;

        //颜色变换
        if (reverseEnabled) {
            this.colorStep = rGBColorTransform(knobMoveRate, tintColor, BACKGROUND_COLOR);
        } else {
            this.colorStep = rGBColorTransform(knobMoveRate, BACKGROUND_COLOR, tintColor);
        }

        knobBound.left = shadowSpace + outerStrokeWidth + w;
        knobBound.right = knobBound.left + kw;
        //knobMoveCalculation end

        //background
        paint.setColor(colorStep);
        paint.setStyle(Paint.Style.FILL);
        //绘制圆角矩形-底层域（背景，含阴影）
        drawRoundRect(shadowSpace, shadowSpace, width - shadowSpace, height - shadowSpace, cornerRadius, canvas, paint);

        //innerContent
        paint.setColor(FOREGROUND_COLOR);
        //绘制圆角矩形-扩展收缩遮罩：第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径
        canvas.drawRoundRect(innerContentBound, innerContentBound.height() / 2, innerContentBound.height() / 2, paint);

        //knob
        paint.setShadowLayer(2, 0, shadowSpace / 2, isEnabled() ? 0x20000000 : 0x10000000);
        //绘制圆角矩形-滑片
        canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);

        paint.setShadowLayer(0, 0, 0, 0);
        paint.setColor(BACKGROUND_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        //绘制圆角矩形-滑片边框
        canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
    }

    private void drawRoundRect(float left, float top, float right, float bottom, float radius, Canvas canvas, Paint paint) {
        tempForRoundRect.left = left;
        tempForRoundRect.top = top;
        tempForRoundRect.right = right;
        tempForRoundRect.bottom = bottom;

        canvas.drawRoundRect(tempForRoundRect, radius, radius, paint);
    }

    //seperate RGB channels and calculate new value for each channel
    //ignore alpha channel
    private int rGBColorTransform(float progress, int fromColor, int toColor) {
        int or = (fromColor >> 16) & 0xFF;
        int og = (fromColor >> 8) & 0xFF;
        int ob = fromColor & 0xFF;

        int nr = (toColor >> 16) & 0xFF;
        int ng = (toColor >> 8) & 0xFF;
        int nb = toColor & 0xFF;

        int rGap = (int) ((float) (nr - or) * progress);
        int gGap = (int) ((float) (ng - og) * progress);
        int bGap = (int) ((float) (nb - ob) * progress);

        return 0xFF000000 | ((or + rGap) << 16) | ((og + gGap) << 8) | (ob + bGap);

    }
}