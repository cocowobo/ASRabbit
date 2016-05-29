package com.adolsai.asrabbit.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Partition类 1、分区实体类</p>
 *
 * @author hxm<br/>
 * @version 1.0 (2016/3/3 17:06)<br/>
 */
public class Partition implements Serializable {
    /**
     * 分区列表
     */
    private List<BoardListBean> BoardList;

    public List<BoardListBean> getBoardList() {
        return BoardList;
    }

    public void setBoardList(List<BoardListBean> BoardList) {
        this.BoardList = BoardList;
    }

    /**
     * 分区类
     */
    public static class BoardListBean implements Serializable {
        /**
         * 区域id
         */
        private String id;
        /**
         * 区域状态
         */
        private String state;
        /**
         * 区域名字
         */
        private String name;

        /**
         * 区域中话题分类
         */
        private List<CategoriesBean> categories;

        /**
         * 是否喜欢
         */
        private boolean isFavourite;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CategoriesBean> getCategories() {
            return categories;
        }

        public void setCategories(List<CategoriesBean> categories) {
            this.categories = categories;
        }

        public boolean isFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(boolean isFavourite) {
            this.isFavourite = isFavourite;
        }

        /**
         * 区域中话题分类对象
         */
        public static class CategoriesBean implements Serializable {
            /**
             * 分类话题id
             */
            private String categoryId;
            /**
             * 分类话题名称
             */
            private String categoryValue;

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryValue() {
                return categoryValue;
            }

            public void setCategoryValue(String categoryValue) {
                this.categoryValue = categoryValue;
            }

            @Override
            public String toString() {
                return "CategoriesBean{" +
                        "categoryId='" + categoryId + '\'' +
                        ", categoryValue='" + categoryValue + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "BoardListBean{" +
                    "id='" + id + '\'' +
                    ", state='" + state + '\'' +
                    ", name='" + name + '\'' +
                    ", categories=" + categories +
                    ", isFavourite=" + isFavourite +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Partition{" +
                "BoardList=" + BoardList +
                '}';
    }
}




