package com.ht.baselib.views.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ht.baselib.R;

/**
 * <p>自定义的actionSheet</p>
 *
 * @author wenwei.chen<br/>
 * @version 1.0 (2016-1-12)
 */
public class MulitActionSheet {

    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle   = false;
    private List<SheetItem> sheetItems;
    private Display display;
    OnSheetItemClickListener listener;

    /**
     * 构造
     *
     * @param context  上下文
     * @param listener 监听回调
     */
    public MulitActionSheet(Context context, OnSheetItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    /**
     * 构建自定义 ActionSheet
     *
     * @return ActionSheet
     */
    public MulitActionSheet builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null);
        view.setMinimumWidth(display.getWidth());
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(-1);
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.ActionSheet);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return ActionSheet
     */
    public MulitActionSheet setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    /**
     * 设置dialog是否能够按返回键取消
     *
     * @param cancel 是否可以
     * @return ActionSheet
     */
    public MulitActionSheet setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 设置dialog在屏幕外部是否能够取消
     *
     * @param cancel 是否可以
     * @return ActionSheet
     */
    public MulitActionSheet setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /***
     * 添加子项目
     *
     * @param strItem 子项目
     * @return ActionSheet
     */
    public MulitActionSheet addSheetItem(String strItem) {
        return addSheetItem(strItem, SheetItemColor.Blue);
    }

    /***
     * 添加子项目
     *
     * @param strItem 子项目
     * @param color   颜色
     * @return ActionSheet
     */
    public MulitActionSheet addSheetItem(String strItem, SheetItemColor color) {
        if (sheetItems == null) {
            sheetItems = new ArrayList<SheetItem>();
        }
        sheetItems.add(new SheetItem(strItem, color, listener));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItems == null || sheetItems.size() <= 0) {
            return;
        }
        int size = sheetItems.size();
        if (size >= 8) {//子项目大于8，可以滚动显示
            LinearLayout.LayoutParams params = (LayoutParams) sLayout_content
                    .getLayoutParams();
            params.height = display.getHeight() * 3 / 5;
            sLayout_content.setLayoutParams(params);
        }
        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItems.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;
            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
/*            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }*/
            textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }
            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (50 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, height));

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index-1);//返回从0开始
                    dialog.dismiss();
                }
            });
            lLayout_content.addView(textView);
        }
    }

    /**
     * 显示
     */
    public void show() {
        setSheetItems();
        dialog.show();
    }

    /**
     * 点击事件接口
     */
    public interface OnSheetItemClickListener {
        /**
         * 点击
         * @param which 位置
         */
        void onClick(int which);
    }

    /**
     * 子项目
     */
    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        /**
         * 构造
         * @param name 名字
         * @param color 颜色
         * @param itemClickListener 点击
         */
        public SheetItem(String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    /**
     * 子项目的颜色
     */
    public enum SheetItemColor {
        /**
         * 蓝色，黑色
         */
        Blue("#4A90E2"), Black("#222222");
        private String name;

        /**
         * set名字
         * @param name 名字
         */
        SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
