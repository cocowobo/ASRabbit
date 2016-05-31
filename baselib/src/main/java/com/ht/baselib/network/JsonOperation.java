package com.ht.baselib.network;


import com.ht.baselib.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>密蜜服务操作类</p>
 *
 * @author wuzuweng<br/>
 * @version 1.0 (16/3/1 上午10:54)<br/>
 */
public class JsonOperation implements Operation {

    @Override
    public Object doOperate(Command cmd) throws Exception {
        JsonCommand jsonCmd = (JsonCommand) cmd;
        try {
            HttpUtils httpUtils = new HttpUtils();
            String rspBody = httpUtils.get(jsonCmd.getUrl());
            LogUtils.v("------------------ >>>> 响应报文(原始):" + rspBody);
//            jsonCmd.getJsonInvoker().perform(result)
            return rspBody;
        } catch (Exception e) {
            throw e;
        }
    }


    //*******************缓存区***********************//
    private static final int POOL_SIZE = 16;
    private static final List<JsonOperation> POOLS = new ArrayList<>(POOL_SIZE);

    /**
     * 释放资源
     *
     * @param jsonOperation jsonOperation
     */
    public static void release(JsonOperation jsonOperation) {
        if (jsonOperation != null) {
            synchronized (POOLS) {
                int size = POOLS.size();
                if (size < POOL_SIZE) {
                    POOLS.add(jsonOperation);
                }
            }
        }
    }

    /**
     * 获取对象
     *
     * @return
     */
    public static JsonOperation obtain() {
        JsonOperation JsonOperation = null;
        synchronized (POOLS) {
            int size = POOLS.size();
            if (size > 0) {
                JsonOperation = POOLS.remove(size - 1);
            }
        }
        if (JsonOperation != null) {
            return JsonOperation;
        }
        return new JsonOperation();
    }
}
