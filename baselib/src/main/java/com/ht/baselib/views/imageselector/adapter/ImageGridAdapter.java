package com.ht.baselib.views.imageselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.ht.baselib.R;
import com.ht.baselib.manager.ImageLoaderManager;
import com.ht.baselib.views.imageselector.bean.Image;

import java.util.ArrayList;

/**
 * <p>图片浏览适配器</p>
 *
 * @author chenchao<br/>
 * @version 1.0 (2015-11-09)
 */
public class ImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private ArrayList<Image> mImages = new ArrayList<Image>();
    private ArrayList<Image> mSelectedImages = new ArrayList<Image>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;
    private FrameLayout.LayoutParams mItemImageViewParams;
    private OnItemCheckListener listener;

    public ImageGridAdapter(Context context, boolean showCamera) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
        mItemImageViewParams = new FrameLayout.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }

    /**
     * 显示选择指示器
     *
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 重置已选图片集合
     *
     * @param resultList
     */
    public void setSelectedImages(ArrayList<Image> resultList) {
        mSelectedImages = resultList;
    }

    /**
     * 设置数据集
     *
     * @param images
     */
    public void setData(ArrayList<Image> images) {
        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
    }

    /**
     * 获取数据
     * @return 当前图片集合
     */
    public ArrayList<Image> getData() {
        return  mImages;
    }

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {
        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;
        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
        mItemImageViewParams = new FrameLayout.LayoutParams(mItemSize, mItemSize);
        notifyDataSetChanged();
    }

    /**
     * 设置多选模式选择监听器
     * @param listener
     */
    public void setOnItemCheckListener(OnItemCheckListener listener) {
        this.listener = listener;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

    @Override
    public Image getItem(int i) {
        if (showCamera) {
            if (i == 0) {
                return null;
            }
            return mImages.get(i - 1);
        } else {
            return mImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);
        if (type == TYPE_CAMERA) {
            view = mInflater.inflate(R.layout.imageselector_list_item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHolde holde;
            if (view == null) {
                view = mInflater.inflate(R.layout.imageselector_list_item_image, viewGroup, false);
                holde = new ViewHolde(view);
            } else {
                holde = (ViewHolde) view.getTag();
                if (holde == null) {
                    view = mInflater.inflate(R.layout.imageselector_list_item_image, viewGroup, false);
                    holde = new ViewHolde(view);
                }
            }
            if (holde != null) {
                holde.bindData(getItem(i));
            }
        }

        /** Fixed View Size */
       // GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (view.getLayoutParams().height != mItemSize) {
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHolde {
        ImageView image;
        ImageView indicator;
        View mask;

        ViewHolde(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;

            // 处理单选和多选状态
            if (showSelectIndicator) {
                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    indicator.setImageResource(R.mipmap.icon_check_active);
                    mask.setVisibility(View.VISIBLE);
                } else {
                    // 未选择
                    indicator.setImageResource(R.mipmap.icon_check_normal);
                    mask.setVisibility(View.GONE);
                }

                indicator.setVisibility(View.VISIBLE);
                indicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemchecked(data);
                        }
                    }
                });
            } else {
                indicator.setVisibility(View.GONE);
            }

            if (mItemSize > 0) {
                // 读取图片
                image.setLayoutParams(mItemImageViewParams);
                ImageLoaderManager.getInstance().displayImage(image, data.getUri());
            }
        }
    }

    /**
     * 多选选择回调
     */
    public interface OnItemCheckListener {
        void onItemchecked(Image image);
    }
}
