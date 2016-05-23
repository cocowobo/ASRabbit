package com.adolsai.asrabbit.model;

/**
 * <p>FavouritePost类 1、提供XXX功能；2、提供XXX方法</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016-3-31 9:57)<br/>
 */
public class FavouritePost extends Post {
    private String id;
    private String frontCoverUrl;


    public FavouritePost(String plate, String readTime, String readPosition, String invitationType, String content) {
        super(plate, readTime, readPosition, invitationType, content);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrontCoverUrl() {
        return frontCoverUrl;
    }

    public void setFrontCoverUrl(String frontCoverUrl) {
        this.frontCoverUrl = frontCoverUrl;
    }
}
