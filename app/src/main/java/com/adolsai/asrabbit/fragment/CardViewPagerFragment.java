package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.adapter.CardPagerAdapter;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.control.IRhythmItemListener;
import com.adolsai.asrabbit.control.RhythmAdapter;
import com.adolsai.asrabbit.control.RhythmLayout;
import com.adolsai.asrabbit.control.ViewPagerScroller;
import com.adolsai.asrabbit.model.Card;
import com.adolsai.asrabbit.model.FavouritePost;
import com.adolsai.asrabbit.utils.AnimatorUtils;
import com.adolsai.asrabbit.utils.HexUtils;
import com.ht.baselib.views.viewselector.ViewSelectorLayout;

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

    private View mMainView;
    private Button mSideMenuOrBackBtn;

    private RhythmLayout mRhythmLayout;

    private ViewPager mViewPager;

    private CardPagerAdapter mCardPagerAdapter;

    private int mPreColor;

    private boolean mHasNext = true;

    private boolean mIsRequesting;

    private boolean isAdapterUpdated;

    private int mCurrentViewPagerPage;

    private ViewSelectorLayout mViewSelectorLayout;
    private List<Card> mCardList;


    private RhythmAdapter mRhythmAdapter;

    private static CardViewPagerFragment mFragment;


    private IRhythmItemListener rhythmItemListener = new IRhythmItemListener() {
        public void onRhythmItemChanged(int paramInt) {
        }

        public void onSelected(final int paramInt) {
            CardViewPagerFragment.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    mViewPager.setCurrentItem(paramInt);
                }
            }, 100L);
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
//            if (mHasNext && (position > -10 + mCardList.size()) && !mIsRequesting) {
////                fetchData();
//            }
        }
    };

    public static CardViewPagerFragment getInstance() {
        if (mFragment == null) {
            mFragment = new CardViewPagerFragment();
        }
        return mFragment;
    }


    @Override
    protected View initViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_niceapp, null);

        mViewSelectorLayout = new ViewSelectorLayout(getActivity(), view);

        mMainView = view.findViewById(R.id.main_view);

        mRhythmLayout = (RhythmLayout) view.findViewById(R.id.box_rhythm);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        setViewPagerScrollSpeed(mViewPager, 400);
        mRhythmLayout.setScrollRhythmStartDelayTime(400);
        int height = (int) mRhythmLayout.getRhythmItemWidth() + (int) TypedValue.applyDimension(1, 10.0F, getResources().getDisplayMetrics());
        mRhythmLayout.getLayoutParams().height = height;
        ((RelativeLayout.LayoutParams) mViewPager.getLayoutParams()).bottomMargin = height;
        return mViewSelectorLayout;
    }


    @Override
    protected void initActions(View paramView) {
        mRhythmLayout.setRhythmListener(rhythmItemListener);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void initData() {
        mCardList = new ArrayList<>();
    }


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


    private void onAppPagerChange(int position) {
        mRhythmLayout.showRhythmAtPosition(position);
        Card post = this.mCardList.get(position);
        int currColor = HexUtils.getHexColor(post.getBackgroundColor());
        AnimatorUtils.showBackgroundColorAnimation(this.mMainView, mPreColor, currColor, 400);
        mPreColor = currColor;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewSelectorLayout.show_LoadingView();
        fetchData();

    }


    private void fetchData() {
//        AppDao.getInstance().subscribeByUser("0", new CallbackListener<AllBookModels>() {
//            @Override
//            public void onStringResult(String result) {
//                super.onStringResult(result);
//                Log.i("scirbe", "result---->" + result);
//            }
//
//            @Override
//            public void onSuccess(AllBookModels result) {
//                super.onSuccess(result);
//                mViewSelectorLayout.show_ContentView();
//                handData(result.Return.List);
//            }

//            @Override
//            public void onError(Exception e) {
//                super.onError(e);
//            }
//        });

//        ArrayList<Card> cardList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            int m = i % 10;
//            Card card = addData(m);
//            cardList.add(card);
//        }
//        mPreColor = HexUtils.getHexColor(cardList.get(0).getBackgroundColor());

    }

    private void handData(ArrayList<FavouritePost> list) {
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

        updateAppAdapter(cardList);
        onAppPagerChange(0);
    }


    private void updateAppAdapter(List<Card> cardList) {
        if ((getActivity() == null) || (getActivity().isFinishing())) {
            return;
        }

        if (cardList.isEmpty()) {
            this.mMainView.setBackgroundColor(this.mPreColor);
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

    private void addCardIconsToDock(final List<Card> cardList) {
        if (mRhythmAdapter == null) {
            resetRhythmLayout(cardList);
            return;
        }
        mRhythmAdapter.addCardList(cardList);
        mRhythmAdapter.notifyDataSetChanged();
    }

    private void resetRhythmLayout(List<Card> cardList) {
        if (getActivity() == null)
            return;
        if (cardList == null)
            cardList = new ArrayList<>();
        mRhythmAdapter = new RhythmAdapter(getActivity(), mRhythmLayout, cardList);
        mRhythmLayout.setAdapter(mRhythmAdapter);
    }


}
