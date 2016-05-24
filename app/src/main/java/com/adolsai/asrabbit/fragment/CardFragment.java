package com.adolsai.asrabbit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolsai.asrabbit.R;
import com.adolsai.asrabbit.base.AsRabbitBaseFragment;
import com.adolsai.asrabbit.model.Card;
import com.adolsai.asrabbit.utils.LocalAppUtil;
import com.adolsai.asrabbit.views.HtmlTextView;
import com.bumptech.glide.Glide;


public class CardFragment extends AsRabbitBaseFragment implements View.OnClickListener {
    protected Card mCard;
    protected TextView mAuthorText;
    protected ImageView mBottomEdgeImageView;
    protected TextView mBravoNumText;
    protected RelativeLayout mCardLayout;
    protected ImageView mCoverImageView;
    protected HtmlTextView mDigestText;
    protected TextView mSubTitleText;
    protected TextView mTitleText;

    /**
     * 构造方法
     *
     * @param card card
     * @return CardFragment
     */
    public static CardFragment getInstance(Card card) {
        CardFragment localCardFragment = new CardFragment();
        Bundle localBundle = new Bundle();
        localBundle.putSerializable("card", card);
        localCardFragment.setArguments(localBundle);
        return localCardFragment;
    }

    //*****************生命周期**********************************************************************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFragment(inflater, R.layout.fragment_card);
        return mMainView;
    }

    @Override
    public void onDestroy() {
        this.mCoverImageView.setImageBitmap(null);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        this.mCoverImageView.setImageBitmap(null);
        super.onDestroyView();
    }


    @Override
    protected void initData() {
        this.mCard = (Card) getArguments().getSerializable("card");
        mTitleText.setText(this.mCard.getTitle());
        mSubTitleText.setText(this.mCard.getSubTitle());
        this.mBravoNumText.setText("No." + this.mCard.getUpNum());
        this.mDigestText.setTextViewHtml(mCard.getDigest());
        this.mAuthorText.setText(Html.fromHtml("<B>" + this.mCard.getAuthorName() + "</B>"));
        initAndDisplayCoverImage();
    }

    @Override
    protected void initViews() {
        mCardLayout = ((RelativeLayout) mMainView.findViewById(R.id.rl_box_card));
        mCardLayout.setOnClickListener(this);
        mBottomEdgeImageView = ((ImageView) mMainView.findViewById(R.id.iv_bottom_edge));
        mCoverImageView = ((ImageView) mMainView.findViewById(R.id.image_cover));
        mTitleText = ((TextView) mMainView.findViewById(R.id.tv_title));
        mSubTitleText = ((TextView) mMainView.findViewById(R.id.tv_subtitle));
        mDigestText = ((HtmlTextView) mMainView.findViewById(R.id.text_digest));
        mAuthorText = ((TextView) mMainView.findViewById(R.id.text_author));
        mBravoNumText = ((TextView) mMainView.findViewById(R.id.text_bravos));
    }

    //*****************事件区**********************************************************************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_box_card:
                //TODO 点击事件

                break;
            default:
                break;
        }
    }

    //****************自定义方法区********************************************************************

    /**
     * 加载图片
     */
    protected void initAndDisplayCoverImage() {
        int coverWidth = LocalAppUtil.getScreenDisplayMetrics(getActivity()).widthPixels - 2 * 5;
        int coverHeight = (int) (180.0F * (coverWidth / 320.0F));
        ViewGroup.LayoutParams localLayoutParams = this.mCoverImageView.getLayoutParams();
        localLayoutParams.height = Float.valueOf(coverHeight).intValue();

        Glide.with(getActivity()).load(mCard.getCoverImgerUrl()).centerCrop().into(mCoverImageView);
    }

}