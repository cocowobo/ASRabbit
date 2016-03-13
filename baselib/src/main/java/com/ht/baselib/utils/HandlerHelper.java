package com.ht.baselib.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * <p>HandlerHelper类 主要提供一些通用的功能，比如弹起关闭键盘<br>
 *     如果在非UI线程里面调用，请调用者在调用线程里面准备Looper,否则会异常
 *     </p>
 *
 * @author zengyaping<br/>
 * @version 1.0 (2015-11-16 10:17)<br/>
 */
public class HandlerHelper extends Handler {
    private WeakReference<Activity> weakReference;
    private WeakReference<OnHandleMessage>listener;

    /**
     * 构造方法
     * @param activity 上下文
     * @param onHandleMessage 接收到消息时的回调
     */
    public HandlerHelper(Activity activity,OnHandleMessage onHandleMessage){
        weakReference = new WeakReference<Activity>(activity);
        listener = new WeakReference<OnHandleMessage>(onHandleMessage);
    }

    /**
     * 处理消息
     * @param msg 消息对象
     */
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what<MESSAGE_WHAT_START){
            Activity activity;
            switch (msg.what){
                case WHAT_SHOW_INPUT_METHOD:
                    activity = weakReference.get();
                    if(activity!=null && msg.obj!=null && msg.obj instanceof EditText){
                        SoftInputMethodUtils.showSoftInputMethod(activity,(EditText)msg.obj);
                    }
                    break;
                case WHAT_HIDE_INPUT_METHOD:
                    activity = weakReference.get();
                    if(activity!=null && msg.obj!=null && msg.obj instanceof EditText){
                        SoftInputMethodUtils.hideSoftInputMethod(activity,(EditText)msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }else{
            OnHandleMessage onHandleMessage = listener.get();
            if(onHandleMessage!=null){
                onHandleMessage.onHandleMessage(msg);
            }
        }
    }

    /**
     * 是否显示输入法
     * @param show 是否显示输入法
     * @param editText 编辑框
     */
    public void toggleInputMethod(boolean show,EditText editText){
        if(show){
            sendMessageDelayed(obtainMessage(WHAT_SHOW_INPUT_METHOD,editText),DELAY_IN_MILLISECOND);
        }else{
            sendMessage(obtainMessage(WHAT_HIDE_INPUT_METHOD,editText));
        }
    }
    private static final int DELAY_IN_MILLISECOND=500;
    private static final int WHAT_SHOW_INPUT_METHOD=1;
    private static final int WHAT_HIDE_INPUT_METHOD=2;
    /**
     * 用户自定义的msg.what的起始值，这个值以内的备用
     */
    public static final int MESSAGE_WHAT_START=100;

    /**
     * 接收到消息时候的回调
     */
    public interface OnHandleMessage{
        /**
         * 接收到消息时候的回调
         * @param message 消息对象
         */
        public void onHandleMessage(Message message);
    }

}
