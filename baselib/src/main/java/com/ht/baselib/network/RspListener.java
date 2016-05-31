package com.ht.baselib.network;

/**
 * Msg: 请求响应类 1、提供请求成功的响应方法；2、提供请求失败的响应方法
 * Update:  2015/10/12
 * Version: 1.0
 * Created by hxm on 2015/10/12 14:33.
 */

public interface RspListener {

    /**
     * 请求成功
     *
     * @param cmd 任务对象
     * @param obj 响应数据
     */
    void onSuccess(Command cmd, Object obj);

    /**
     * 请求失败
     * 注：(1)处理服务端业务异常,异常信息将封装成ErrorModel对象,响应到前端
     * (2)处理APP本地异常,如:App端请求超时,App端网络链接异常
     *
     * @param cmd 任务对象
     */
    void onFailure(Command cmd);


}
