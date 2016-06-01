package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.CardPagerAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.control.IRhythmItemListener;
import com.adolsai.asrabbit.control.RhythmAdapter;
import com.adolsai.asrabbit.control.RhythmLayout;
import com.adolsai.asrabbit.control.ViewPagerScroller;
import com.adolsai.asrabbit.listener.RequestListener;
import com.adolsai.asrabbit.manager.DataManager;
import com.adolsai.asrabbit.model.Card;
import com.adolsai.asrabbit.model.FavouritePost;
import com.adolsai.asrabbit.utils.AnimatorUtils;
import com.adolsai.asrabbit.utils.HexUtils;
import com.ht.baselib.views.dialog.CustomDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shine
 * Date: 2014-12-13
 * Time: 19:45
 * Description:
 */
public class CardViewPagerFragment extends AsRabbitBaseFragment {

    private View mCardMainView;

    private RhythmLayout mRhythmLayout;

    private ViewPager mViewPager;

    private CardPagerAdapter mCardPagerAdapter;

    private int mPreColor;

    private int mCurrentViewPagerPage;

    private List<Card> mCardList;


    private RhythmAdapter mRhythmAdapter;

    private CustomDialog customDialog;

    /**
     * 构造方法
     *
     * @return CardViewPagerFragment
     */
    public static CardViewPagerFragment getInstance() {
        CardViewPagerFragment FavFragment = new CardViewPagerFragment();

        return FavFragment;
    }

    //********生命周期*******************************************************************************


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_niceapp);
        mCardMainView = mMainView.findViewById(R.id.main_view);
        mRhythmLayout = (RhythmLayout) mCardMainView.findViewById(R.id.box_rhythm);
        mViewPager = (ViewPager) mCardMainView.findViewById(R.id.pager);

        setViewPagerScrollSpeed(mViewPager, 400);
        mRhythmLayout.setScrollRhythmStartDelayTime(400);
        int height = (int) mRhythmLayout.getRhythmItemWidth() + (int) TypedValue.applyDimension(1, 10.0F, getResources().getDisplayMetrics());
        mRhythmLayout.getLayoutParams().height = height;
        ((RelativeLayout.LayoutParams) mViewPager.getLayoutParams()).bottomMargin = height;
        mRhythmLayout.setRhythmListener(rhythmItemListener);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
        return mCardMainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchData();
    }


    @Override
    protected void initData() {
        mCardList = new ArrayList<>();

    }

    @Override
    protected void initViews() {
        customDialog = CustomDialog.newLoadingInstance(activity);

    }

    @Override
    public void backToFragment() {

    }

    //**************事件区****************************************************************************

    private IRhythmItemListener rhythmItemListener = new IRhythmItemListener() {
        public void onRhythmItemChanged(int paramInt) {
        }

        public void onSelected(final int paramInt) {
            mViewPager.setCurrentItem(paramInt);

        }

        public void onStartSwipe() {
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrollStateChanged(int paramInt) {
        }

        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {

        }

        public void onPageSelected(int position) {
            onAppPagerChange(position);
        }
    };


    //*************自定义方法区***********************************************************************

    /**
     * 获取数据
     */
    private void fetchData() {
        if (customDialog != null) {
            customDialog.show();
        }
        DataManager.getFavouritePost(new RequestListener() {
            @Override
            public void getResult(Object result) {
                handData(result);
            }
        });

    }

    /**
     * 处理数据
     *
     * @param result result
     */
    private void handData(Object result) {
        if (result != null && result instanceof List) {
            ArrayList<FavouritePost> list = (ArrayList<FavouritePost>) result;
            ArrayList<Card> cardList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Card card = new Card();
                card.setId("id");
                card.setTitle("title");
                card.setSubTitle("SubTitle");
                card.setDigest("Digest");
                card.setAuthorName("AuthorName");
                card.setUpNum(Integer.valueOf("1"));
                card.setBackgroundColor("#795548");
                card.setCoverImgerUrl(list.get(i).getFrontCoverUrl());
                card.setIconUrl(list.get(i).getFrontCoverUrl());
                cardList.add(card);
            }

            updateUI(cardList);

        }
    }

    /**
     * 更新UI
     *
     * @param cardList cardList
     */
    private void updateUI(final List<Card> cardList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAppAdapter(cardList);
                onAppPagerChange(0);
                if (customDialog != null) {
                    customDialog.dismiss();
                }

            }
        });

    }


    /**
     * 更新适配器
     *
     * @param cardList cardList
     */
    private void updateAppAdapter(List<Card> cardList) {
        if ((getActivity() == null) || (getActivity().isFinishing())) {
            return;
        }
        if (cardList.isEmpty()) {
            this.mCardMainView.setBackgroundColor(this.mPreColor);
            return;
        }
        int size = mCardList.size();
        if (mCardPagerAdapter == null) {
            mCurrentViewPagerPage = 0;
            mCardPagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager(), cardList);
            mViewPager.setAdapter(mCardPagerAdapter);
        } else {
            mCardPagerAdapter.addCardList(cardList);
            mCardPagerAdapter.notifyDataSetChanged();
        }
        addCardIconsToDock(cardList);

        this.mCardList = mCardPagerAdapter.getCardList();

        if (mViewPager.getCurrentItem() == size - 1)
            mViewPager.setCurrentItem(1 + mViewPager.getCurrentItem(), true);
    }

    /**
     * 添加下面的图标的数据
     *
     * @param cardList cardList
     */
    private void addCardIconsToDock(final List<Card> cardList) {
        if (mRhythmAdapter == null) {
            resetRhythmLayout(cardList);
            return;
        }
        mRhythmAdapter.addCardList(cardList);
        mRhythmAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新layout
     *
     * @param cardList
     */
    private void resetRhythmLayout(List<Card> cardList) {
        if (getActivity() == null)
            return;
        if (cardList == null)
            cardList = new ArrayList<>();
        mRhythmAdapter = new RhythmAdapter(getActivity(), mRhythmLayout, cardList);
        mRhythmLayout.setAdapter(mRhythmAdapter);
    }

    /**
     * 设置滚动速度
     *
     * @param viewPager viewPager
     * @param speed     speed
     */
    private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(viewPager.getContext(), new OvershootInterpolator(0.6F));
            field.set(viewPager, viewPagerScroller);
            viewPagerScroller.setDuration(speed);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 改变页数
     *
     * @param position position
     */
    private void onAppPagerChange(int position) {
        mRhythmLayout.showRhythmAtPosition(position);
        Card post = this.mCardList.get(position);
        int currColor = HexUtils.getHexColor(post.getBackgroundColor());
        AnimatorUtils.showBackgroundColorAnimation(this.mCardMainView, mPreColor, currColor, 400);
        mPreColor = currColor;

    }

}
