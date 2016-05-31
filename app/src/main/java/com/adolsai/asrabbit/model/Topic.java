package com.adolsai.asrabbit.model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Topic类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-30 19:05)<br/>
 */
public class Topic implements Serializable {
    private String title;
    private String boardId;
    private String topicId;
    private String author;
    private int totalReplyCount;
    private int totalPage;
    private Date leastReplyDate;
    private String type;
    private int unreadCount;

    public Topic() {
    }

    public Topic(String title, String boardId, String topicId, String author, int totalReplyCount,
                 int totalPage, Date leastReplyDate, String type, int unreadCount) {
        this.title = title;
        this.boardId = boardId;
        this.topicId = topicId;
        this.author = author;
        this.totalReplyCount = totalReplyCount;
        this.totalPage = totalPage;
        this.leastReplyDate = leastReplyDate;
        this.type = type;
        this.unreadCount = unreadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(int totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Date getLeastReplyDate() {
        return leastReplyDate;
    }

    public void setLeastReplyDate(Date leastReplyDate) {
        this.leastReplyDate = leastReplyDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
