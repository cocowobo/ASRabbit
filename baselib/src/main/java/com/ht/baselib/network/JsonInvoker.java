package com.ht.baselib.network;

/**
 * <p>API方法代理对象</p>
 *
 * @param <S> 返回数据泛型
 * @author wuzuweng<br/>
 * @version 1.0 (16/4/18 下午5:11)<br/>
 */

public abstract class JsonInvoker<S> {

    /**
     * 执行API方法
     *
     * @param result 请求结果
     * @return
     * @throws Exception 抛出异常
     */
    public abstract S perform(String result) throws Exception;
}