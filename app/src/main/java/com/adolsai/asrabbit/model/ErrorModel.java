package com.adolsai.asrabbit.model;

/**
 * <p>ErrorModel类 1、提供异常，错误的属性类</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-29 8:45)<br/>
 */
public class ErrorModel {
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 错误提醒
     */
    private String errorMsg;

    public ErrorModel(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
