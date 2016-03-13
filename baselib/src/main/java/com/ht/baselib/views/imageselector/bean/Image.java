package com.ht.baselib.views.imageselector.bean;

import java.io.Serializable;

/**
 * <p>图片实体</p>
 *
 * @author chenchao<br/>
 * @version 1.0 (2015-11-09)
 */
public class Image implements Serializable {
    private String uri;
    private String name;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.uri.equalsIgnoreCase(other.uri);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
