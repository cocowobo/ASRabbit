package com.adolsai.asrabbit.model;

import com.adolsai.asrabbit.app.GlobalStaticData;
import com.orhanobut.hawk.Hawk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Topic类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-5-30 19:05)<br/>
 */
public class Topic implements Serializable {
    private String title;//帖子标题
    private String boardId;//版区ID，左边显示
    private String topicId;//话题ID
    private String author;//作者
    private int totalReplyCount;//总回复数
    private int totalPage;//总页数
    private String leastReplyDate;//最后回复时间
    private String type;//类型ID，右边显示
    private int unreadCount;//未读数目

    /**
     * 以下两个属性不需要构建，根据ID自动生成
     **/
    private String boardName;//版区名
    private String typeName;//类型名

    private static List<Partition.BoardListBean> tempList;//首页版区数据缓存

    public Topic() {
        tempList = Hawk.get(GlobalStaticData.PARTITION_LIST_CACHE, new ArrayList<Partition.BoardListBean>());
    }

    public Topic(String title, String boardId, String topicId, String author, int totalReplyCount,
                 int totalPage, String leastReplyDate, String type, int unreadCount) {
        tempList = Hawk.get(GlobalStaticData.PARTITION_LIST_CACHE, new ArrayList<Partition.BoardListBean>());
        this.title = title;
        this.boardId = boardId;
        this.topicId = topicId;
        this.author = author;
        this.totalReplyCount = totalReplyCount;
        this.totalPage = totalPage;
        this.leastReplyDate = leastReplyDate;
        this.type = type;
        this.unreadCount = unreadCount;
        setBoardAndTypeNameById();
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

    public void setBoardIdAndType(String boardId, String type) {
        this.boardId = boardId;
        this.type = type;
        setBoardAndTypeNameById();
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

    public String getLeastReplyDate() {
        return leastReplyDate;
    }

    public void setLeastReplyDate(String leastReplyDate) {
        this.leastReplyDate = leastReplyDate;
    }

    public String getType() {
        return type;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setBoardAndTypeNameById() {
        for (int i = 0; i < tempList.size(); i++) {
            Partition.BoardListBean curr = tempList.get(i);
            if (curr.getId().equals(boardId)) {
                boardName = curr.getName();
                List<Partition.BoardListBean.CategoriesBean> currCategory = curr.getCategories();
                for (int j = 0; j < currCategory.size(); j++) {
                    if (currCategory.get(j).getCategoryId().equals(type)) {
                        typeName = currCategory.get(j).getCategoryValue();
                        return;
                    }
                }
            }
        }
    }


}
