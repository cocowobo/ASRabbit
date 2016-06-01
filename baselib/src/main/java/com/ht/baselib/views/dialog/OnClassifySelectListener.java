package com.ht.baselib.views.dialog;

/**
 * Msg: 二级分类选择监听器
 * Version: 1.1
 * Created by chenchao on 2015/11/26
 */
public interface OnClassifySelectListener {
    /**
     * call on selected
     *
     * @param firstCategoryIndex 一级类目
     * @param secondCategoryIndex 二级类目
     */
    void onSelected(int firstCategoryIndex, int secondCategoryIndex);

    /**
     * call on cancel
     */
    void onUnSelect();
}
