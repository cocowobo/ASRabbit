package com.ht.baselib.views.bannerview.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * 本地图片Holder实例
 */
public class LocalImageHolderView implements Holder<Integer> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
