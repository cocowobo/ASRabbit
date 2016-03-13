package com.ht.baselib.views.pulltorefresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ht.baselib.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Msg: 下拉刷新，上拉加载更多控件,还有listview滑动触发的加载,如果不要上拉到底刷新的，请使用PullToRefreshListView，或者调用removeFooterView() ，如果上拉到底的方法已经加载了所有数据，也可以调用removeFooterView() 方法，防止重复加载；
 * Update:  2015/9/13
 * Version: 1.0
 * Created by hxm on 2015/9/13 16:40.
 */
public class PullAndUpToRefreshView extends PullToRefreshListView {
    public static final int DELAY_TIMER = 10000;//延时10秒关闭底部Footer时间
    public static final int AUTO_LOADING = 0;//自动加载
    public static final int CLICK_LOADING = 1;//点击加载
    public static final int LAZY_LOADING = 2;//预加载
    public static final int LOADING_AUTO_COMPLETE = 5;//一定时间后自动结束loading
    public static final int LOADING_AUTO_COMPLETE_TIME = 10000;//一定时间后自动结束loading
    public static final String FOOTVIEW_TAG = "footview_tag";//footview的tag
    private Context context;
    private ListView currListView;//当前的listview
    private ProgressBar proLoadMore;//加载更多的缓冲bar
    private TextView tvRefreshMore;//加载更多的提示
    private View rlFootRefresh;//上拉加载的layout
    private OnFooterViewClick onfooterviewclick;//点击加载更多按钮回调
    private boolean refreshing = false;//是否刷新中
    private View foot;//底部加载更多的view
    private int loadType = CLICK_LOADING;//加载的类型

    /**
     * 滑动监听
     */
    private AbsListView.OnScrollListener onExtendScrollListener;
    /**
     * 是否所有数据都加载完毕
     */
    private boolean isAllDataHasLoaded = false;

    /**
     * 点击加载的文案、加载中的文案
     */
    private String clickLoadMoreText, loadingText;

    /**
     * 加载前的总条数,用于防止在自动加载模式情况下，加载数据失败时，不断地加载数据
     */
    private int preTotalItemCount;

    public PullAndUpToRefreshView(Context context) {
        super(context);
        init(context);
    }

    public PullAndUpToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullAndUpToRefreshView(Context context, Mode mode) {
        super(context, mode);
        init(context);
    }

    public PullAndUpToRefreshView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        init(context);
    }

    private void init(Context ctx) {
        context = ctx;
        foot = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pull_up_to_load_footer_view, null, false);
        rlFootRefresh = foot.findViewById(R.id.rl_foot_refresh);
        foot.setTag(FOOTVIEW_TAG);
        proLoadMore = (ProgressBar) foot.findViewById(R.id.pro_load_more_data);
        tvRefreshMore = (TextView) foot.findViewById(R.id.tv_refresh_more);
        currListView = getRefreshableView();
        currListView.addFooterView(foot);
        clickLoadMoreText = getContext().getString(R.string.click_load_more);
        loadingText = getContext().getString(R.string.footer_view_loading);
        //是点击加载的才默认显示footerView
        setFooterVisible(loadType == CLICK_LOADING ? true : false);
        rlFootRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshClickEvent(CLICK_LOADING);
            }
        });

        currListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onExtendScrollListener != null) {
                    onExtendScrollListener.onScrollStateChanged(view, scrollState);
                }
                if (loadType == AUTO_LOADING && SCROLL_STATE_IDLE == scrollState) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1) &&
                            currListView.getCount() - 1 > currListView.getLastVisiblePosition() - currListView.getFirstVisiblePosition() + 1) {
                        //最后一个可见，
                        refreshClickEvent(AUTO_LOADING);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onExtendScrollListener != null) {
                    onExtendScrollListener.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                //当目前listview的最低可见item大于总数的0.75，且少于10时（防止数目多的时候，加载很多次），预加载
                if (!isRefreshing() && loadType == LAZY_LOADING && firstVisibleItem + visibleItemCount > 0.75 * totalItemCount
                        && (totalItemCount - firstVisibleItem - visibleItemCount < 10) && totalItemCount - 1 > visibleItemCount && totalItemCount != preTotalItemCount) {
                    preTotalItemCount = totalItemCount;
                    refreshClickEvent(LAZY_LOADING);
                }
            }
        });

    }

    //加载数据操作
    private void refreshClickEvent(int type) {
        if (onfooterviewclick != null && foot.getVisibility() == View.VISIBLE && !isAllDataHasLoaded && currListView.findViewWithTag(FOOTVIEW_TAG) != null) {
            if (!isRefreshing()) {
                setFootViewState(true);
                refreshing = true;
                sendMsg(type);
            }
        }
    }


    /**
     * @param isStartRefresh true=正在刷新，false=刷新完毕
     */
    private void setFootViewState(boolean isStartRefresh) {
        if (isStartRefresh) {
            tvRefreshMore.setText(loadingText);
            proLoadMore.setVisibility(VISIBLE);
        } else {
            tvRefreshMore.setText(clickLoadMoreText);
            proLoadMore.setVisibility(GONE);
        }
    }


    private void handleMessage(Message msg) {
        switch (msg.what) {
            case AUTO_LOADING:
                if (onfooterviewclick != null)
                    onfooterviewclick.onFooterRefresh();
                PullAndUpToRefreshHandler.sendEmptyMessageDelayed(LOADING_AUTO_COMPLETE, LOADING_AUTO_COMPLETE_TIME);
                break;
            case LOADING_AUTO_COMPLETE:
                footerLoadComplete();
                break;
            case CLICK_LOADING:
                if (onfooterviewclick != null)
                    onfooterviewclick.onFooterRefresh();
                break;
            case LAZY_LOADING:
                if (onfooterviewclick != null)
                    onfooterviewclick.onFooterRefresh();
                break;
        }
    }

    ;

    /**
     * 给handler发送消息，防止频繁发送
     *
     * @param msg
     */
    private void sendMsg(int msg) {
        if (PullAndUpToRefreshHandler.hasMessages(msg)) {
            PullAndUpToRefreshHandler.removeMessages(msg);
        }
        PullAndUpToRefreshHandler.sendEmptyMessage(msg);
    }

    /**
     * 给handler发送延时消息，防止频繁发送
     *
     * @param msg
     * @param delay
     */
    private void sendDelayMsg(int msg, long delay) {
        if (PullAndUpToRefreshHandler.hasMessages(msg)) {
            PullAndUpToRefreshHandler.removeMessages(msg);
        }
        PullAndUpToRefreshHandler.sendEmptyMessageDelayed(msg, delay);
    }


    /**
     * ui线程的handler
     */
    protected Handler PullAndUpToRefreshHandler = new UiHandler(this);

    private static class UiHandler extends Handler {
        private final WeakReference<PullAndUpToRefreshView> payHelperWeakReference;

        public UiHandler(PullAndUpToRefreshView payHelper) {
            payHelperWeakReference = new WeakReference<PullAndUpToRefreshView>(payHelper);
        }

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (payHelperWeakReference.get() != null) {
                payHelperWeakReference.get().handleMessage(msg);
            }
        }
    }

    /**
     * @param loadType 加载类型，有（{@link PullAndUpToRefreshView#AUTO_LOADING},{@link PullAndUpToRefreshView#LAZY_LOADING},{@link PullAndUpToRefreshView#CLICK_LOADING}））
     */
    public void setLoadType(final int loadType) {
        this.loadType = loadType;

    }

    @Override
    public boolean isFooterRefreshing() {
        return refreshing;
    }


    /**
     * 设置新的footview,该方法存在问题，暂是不使用
     *
     * @param view 新的footview,当为null时，使用默认的footview
     */
    public void setFooterView(View view) {
        View v = currListView.findViewWithTag(FOOTVIEW_TAG);
        if (v != null && view == null) {
            return;
        }
        if (view != null) {
            foot = view;
            foot.setTag(FOOTVIEW_TAG);
            proLoadMore = (ProgressBar) foot.findViewById(R.id.pro_load_more_data);
            tvRefreshMore = (TextView) foot.findViewById(R.id.tv_refresh_more);
            rlFootRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshClickEvent(CLICK_LOADING);
                }
            });
        }
        if (v != null) {
            currListView.removeFooterView(v);
        }
        try {
            Field f = ListView.class.getDeclaredField("mFooterViewInfos");
            f.setAccessible(true); //设置些属性是可以访问的
            Object val = f.get(currListView);
            if (f.getType().toString().contains("ArrayList")) {
                //把数据取出来，因为list里边的数据类型不想限制，所以不作泛型处理
                ArrayList<ListView.FixedViewInfo> mFooterViewInfos = (ArrayList<ListView.FixedViewInfo>) val;
                ArrayList<ListView.FixedViewInfo> footViews = new ArrayList<ListView.FixedViewInfo>();
                footViews.addAll(mFooterViewInfos);
                mFooterViewInfos.clear();
                f.set(currListView, mFooterViewInfos);
                ListView.FixedViewInfo info = currListView.new FixedViewInfo();
                info.data = null;
                info.view = foot;
                info.isSelectable = true;
                mFooterViewInfos.add(info);
                if (footViews != null && footViews.size() > 0) {
                    mFooterViewInfos.addAll(footViews);
                }
                f.set(currListView, mFooterViewInfos);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 移除footerview
     */
    public void removeFooterView() {
        if (currListView.findViewWithTag(FOOTVIEW_TAG) != null) {
            currListView.removeFooterView(foot);
        }
    }

    /**
     * 设置footview是否可见
     *
     * @param visible 是否可见
     */
    public void setFooterVisible(boolean visible) {
        if (visible) {
            foot.setVisibility(VISIBLE);
        } else {
            foot.setVisibility(INVISIBLE);
        }
    }


    /**
     * @param loadingText 加载中的文案
     */
    public void setFootViewLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    /**
     * @param clickLoadMoreText 点击加载更多的文案
     */
    public void setClickLoadMoreText(String clickLoadMoreText) {
        this.clickLoadMoreText = clickLoadMoreText;
        if (tvRefreshMore != null) {
            tvRefreshMore.setText(clickLoadMoreText);
        }
    }


    /**
     * @param height 设置footview高度 px
     */
    public void setFootViewHeight(int height) {
        foot.setMinimumHeight(height);
    }

    //加载完毕必须需要调用
    public void footerLoadComplete() {
        if (refreshing) {
            setFootViewState(false);
            refreshing = false;
        }
    }

    /**
     * 是否所有的数据已经加载完毕
     */
    public boolean isAllDataHasLoaded() {
        return isAllDataHasLoaded;
    }

    /**
     * 设置所有的数据是否都加载完毕
     *
     * @param isAllDataHasLoaded 数据是否加载完毕
     */
    public void setIsAllDataHasLoaded(boolean isAllDataHasLoaded) {
        this.isAllDataHasLoaded = isAllDataHasLoaded;
    }

    /**
     * 因为这里的下拉到底刷新使用了AbsListView.OnScrollListener监听，所以要提供一个给子类要继续使用的监听
     *
     * @param onExtendScrollListener 监听器
     */
    public void setOnExtendScrollListener(AbsListView.OnScrollListener onExtendScrollListener) {
        this.onExtendScrollListener = onExtendScrollListener;
    }

    /**
     * @param onfooterviewclick 下拉到底加载更多，预加载、点击加载更多的监听
     */
    public void setOnFooterViewClick(OnFooterViewClick onfooterviewclick) {
        this.onfooterviewclick = onfooterviewclick;
    }

    public interface OnFooterViewClick {
        void onFooterRefresh();
    }
}
