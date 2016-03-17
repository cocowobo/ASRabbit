package com.adolsai.asrabbit.model;

import java.io.Serializable;

/**
 * <p>Post类 1、帖子对象</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-14 16:29)<br/>
 */
public class Post implements Serializable {
    private String plate;//板块
    private String readTime;//阅读时间
    private String readPosition;//阅读者位置
    private String invitationType;//帖子类型
    private String content;//内容

    public Post(String plate, String readTime, String readPosition, String invitationType, String content) {

        this.readTime = readTime;
        this.readPosition = readPosition;
        this.plate = plate;
        this.invitationType = invitationType;
        this.content = content;
    }

    public String getPlate() {
        return plate;
    }

    public String getInvitationType() {
        return invitationType;
    }

    public String getReadTime() {
        return readTime;
    }

    public String getReadPosition() {
        return readPosition;
    }

    public String getContent() {
        return content;
    }
}
