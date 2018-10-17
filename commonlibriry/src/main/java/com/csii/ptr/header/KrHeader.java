package com.csii.ptr.header;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.csii.commonlibriry.R;
import com.csii.ptr.PtrFrameLayout;
import com.csii.ptr.PtrUIHandler;
import com.csii.ptr.indicator.PtrIndicator;

/**
 * autor : sunhao
 * time  : 2018/10/14  16:48
 * desc  :
 */

public class KrHeader extends FrameLayout implements PtrUIHandler {

    private ImageView mScaleImageView;


    private LottieAnimationView mLoadingLottieView;
    private TextView mRefreshInfoTextView;

    private boolean isShowRefreshInfo;

    public KrHeader(@NonNull Context context) {
        this(context, null);
    }

    public KrHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public KrHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }


    private void init() {
        this.mScaleImageView.setVisibility(GONE);
        this.mLoadingLottieView.setVisibility(VISIBLE);
        this.mRefreshInfoTextView.setVisibility(GONE);
        this.mLoadingLottieView.playAnimation();
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.header_kr, this);
        this.mScaleImageView = view.findViewById(R.id.pre);
        this.mLoadingLottieView = view.findViewById(R.id.loading);
        this.mRefreshInfoTextView = view.findViewById(R.id.tv_refresh_info);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        this.onUIRefreshPrepare(frame);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        this.mScaleImageView.setVisibility(VISIBLE);
        this.mLoadingLottieView.setVisibility(GONE);
        this.mRefreshInfoTextView.setVisibility(GONE);
        this.mLoadingLottieView.setProgress(0f);
        this.mLoadingLottieView.cancelAnimation();
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        this.init();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        this.onUIRefreshComplete();
    }

    /**
     * 根据手势上下拉缩放imageview
     * @param frame
     * @param isUnderTouch
     * @param status
     * @param ptrIndicator
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        int offset = frame.getOffsetToRefresh();
        int currentPosY = ptrIndicator.getCurrentPosY();
        if (currentPosY >= offset) {
            this.mScaleImageView.setScaleX(1.0F);
            this.mScaleImageView.setScaleY(1.0F);
        } else if (status == 2) {
            //根据偏移量计算缩放比例
            float scale = (float)(offset - currentPosY) / (float)offset;
            this.mScaleImageView.setScaleX(1.0F - scale);
            this.mScaleImageView.setScaleY(1.0F - scale);
        }

    }

    private void onUIRefreshComplete() {
        if (this.isShowRefreshInfo) {
            this.mScaleImageView.setVisibility(GONE);
            this.mLoadingLottieView.setVisibility(GONE);
//            this.mRefreshInfoTextView.setVisibility(VISIBLE);
        }

    }

    public void setShowRefreshInfo(boolean showRefreshInfo) {
        this.isShowRefreshInfo = showRefreshInfo;
    }

    public TextView getCompleteView() {
        return this.mRefreshInfoTextView;
    }
}
