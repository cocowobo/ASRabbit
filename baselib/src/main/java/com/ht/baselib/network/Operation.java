package com.ht.baselib.network;


/**
 * Msg:任务处理类 1、任务处理方法
 * <p/>
 * Version: 1.0
 * Created by wuzuweng on 2015/10/12 14:33.
 */
public interface Operation {

    /**
     * 任务执行方法
     *
     * @param cmd 任务对象
     * @return
     * @throws Exception 任务处理时抛出的异常
     */
    public Object doOperate(Command cmd) throws Exception;
}
