package com.ht.baselib.views.bannerview.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ht.baselib.manager.ImageLoaderManager;

/**
 * 网络图片Holder实例
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    /**
     * 加载图  和    默认图 资源Id
     */
    private int loadingId = -1, defaultId = -1;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    /**
     * 设置 加载中 资源Id
     *
     * @param loadingId id
     */
    public void setLoadingId(int loadingId) {
        this.loadingId = loadingId;
    }

    /**
     * 设置 默认图 资源Id
     *
     * @param defaultId id
     */
    public void setDefaultId(int defaultId) {
        this.defaultId = defaultId;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        if (loadingId == -1 || defaultId == -1) {
            ImageLoaderManager.getInstance().displayImage(imageView, data);
        } else {
            ImageLoaderManager.getInstance().displayImage(imageView, data, loadingId, defaultId);
        }
    }
}
