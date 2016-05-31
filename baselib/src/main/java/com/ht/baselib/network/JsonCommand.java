package com.ht.baselib.network;

/**
 * <p>密蜜平台json请求数据描述</p>
 *
 * @author wuzuweng<br/>
 * @version 1.0 (16/4/13 上午10:17)<br/>
 */
public class JsonCommand extends Command {
    /**
     * 数据模型类
     */
    private Class modelClazz;

    /**
     * json操作代理
     */
    private JsonInvoker<?> jsonInvoker;

    /**
     * 构造方法
     *
     * @param id          标识
     * @param url         请求地址
     * @param rspListener 响应接口
     */
    public JsonCommand(int id, String url, RspListener rspListener) {
        this(id, url, null, rspListener);
    }

    /**
     * 构造方法
     *
     * @param id          标识
     * @param url         请求地址
     * @param jsonInvoker json操作代理
     * @param rspListener 响应接口
     */
    public JsonCommand(int id, String url, JsonInvoker<?> jsonInvoker, RspListener rspListener) {
        this.id = id;
        this.url = url;
        this.jsonInvoker = jsonInvoker;
        this.rspListener = rspListener;
        this.operation = JsonOperation.class.getName();
    }

    /**
     * 获取数据模型类
     *
     * @return
     */
    public Class getModelClazz() {
        return modelClazz;
    }

    /**
     * 设置数据模型类
     *
     * @param modelClazz 数据模型类
     */
    public void setModelClazz(Class modelClazz) {
        this.modelClazz = modelClazz;
    }

    /**
     * 获取json操作代理
     *
     * @return
     */
    public JsonInvoker<?> getJsonInvoker() {
        return jsonInvoker;
    }

    /**
     * 设置json操作代理
     *
     * @param jsonInvoker json操作代理
     */
    public void setJsonInvoker(JsonInvoker<?> jsonInvoker) {
        this.jsonInvoker = jsonInvoker;
    }

}
