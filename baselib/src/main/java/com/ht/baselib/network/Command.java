package com.ht.baselib.network;

/**
 * <p>请求任务</p>
 *
 * @author zengyaping<br>
 * @version 1.0 (2015/10/19)<br>
 */
public abstract class Command {
    /**
     * Command标识
     */
    protected int id;

    /**
     * 取消请求
     */
    protected boolean isCancel = false;

    /**
     * 处理类
     */
    protected String operation;
    /**
     * 请求结果状态
     */
    protected boolean isSuccess;

    /**
     * 响应接口
     */
    protected RspListener rspListener;

    /**
     * 请求地址
     */
    protected String url;


    /**
     * 请求结果响应对象
     */
    protected Object rspObject;

    /**
     * 默认构造函数
     */
    public Command() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public RspListener getRspListener() {
        return rspListener;
    }

    public void setRspListener(RspListener rspListener) {
        this.rspListener = rspListener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getRspObject() {
        return rspObject;
    }

    public void setRspObject(Object rspObject) {
        this.rspObject = rspObject;
    }
}
