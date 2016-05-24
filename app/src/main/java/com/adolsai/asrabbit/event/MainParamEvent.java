package com.adolsai.asrabbit.event;

/**
 * <p>Event class，用于EventBus发布事件</p>
 * <p>介绍：
 * EventBus是一款针对Android 优化的发布/订阅事件总线。
 * 主要功能是替代Intent,Handler,BroadCast在Fragment,Activity,Service,线程之间传递消息。
 * 优点是开销小，代码更优雅，以及将发送者和接收者解耦。
 * 主要有onEvent,onEventMainThread,onEventBackgroundThread,onEventAsync四个约定好了的回调方法，约定大于配置的经典用例。
 * 并且这四个回调方法里面参数类型可以自定义，但只能传递一个参数对象。
 * </p>
 *
 * @author zmingchun
 * @version 1.0（2015-11-17）
 */
public class MainParamEvent {
    /**
     * 参数对象
     */
    public final ParamsBean paramsBean;

    /**
     * 处理方法
     * <br/>接收通知结果
     *
     * @param paramsBean 参数实体
     */
    public MainParamEvent(ParamsBean paramsBean) {
        this.paramsBean = paramsBean;
    }
}
