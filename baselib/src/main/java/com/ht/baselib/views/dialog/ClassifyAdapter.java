package com.ht.baselib.views.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ht.baselib.views.wheelview.adapters.AbstractWheelTextAdapter;

import java.util.List;

/**
 * Msg: 二级分类滚轮适配器.
 * Version: 1.0
 * Created by chenchao on 2015/11/26
 */
public class ClassifyAdapter extends AbstractWheelTextAdapter {
    /**数据列表*/
    private List<String> dataList;

    /**
     * Constructor
     * @param context the current context
     */
    public ClassifyAdapter(Context context) {
        super(context);
    }

    /**
     * 设置数据列表
     * @param dataList 数据列表
     */
    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    /**
     * 获取数据列表
     * @return 数据列表
     */
    public List<String> getDataList() {
        return dataList;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            return dataList.get(index);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return dataList == null ? 0 : dataList.size();
    }
    
    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView = getView(itemResourceId, parent);
            }

            TextView textView = getTextView(convertView, itemTextResourceId);
            if (textView != null) {
                CharSequence text = getItemText(index);
                textView.setText(text);

    
                if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView);
                }
            }
            return convertView;
        }
        return null;
    }
}
