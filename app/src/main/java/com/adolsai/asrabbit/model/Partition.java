package com.adolsai.asrabbit.model;

import java.io.Serializable;

/**
 * <p>Partition类 1、分区实体类</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/3 17:06)<br/>
 */
public class Partition implements Serializable {
    private String number;//区域编号
    private String title;//区域标题
    private boolean isFavourite;//是否喜欢


    public Partition(String number, String title, boolean isFavourite) {
        this.number = number;
        this.title = title;
        this.isFavourite = isFavourite;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }
}
