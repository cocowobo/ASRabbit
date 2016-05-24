package com.adolsai.asrabbit.model;

import java.io.Serializable;

/**
 * <p>UserInfo类 1、提供用户信息</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-24 9:36)<br/>
 */
public class UserInfo implements Serializable {
    private String account;
    private String password;

    public UserInfo() {
    }

    public UserInfo(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
