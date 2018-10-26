package com.csii.sh.delegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.csii.sh.R;
import com.csii.sh.base.BaseActivity;
import com.csii.sh.navigation.DefaultNavigationBar;
import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.view.IView;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;

/**
 * description: 基类
 * autour: sunhao
 * date: 2018/1/7 上午11:50
 */
public abstract class BaseMVPActivity<P extends BasePresenter<V>, V extends IView> extends BaseActivity implements MvpCallback {

    protected String TAG = this.getClass().getSimpleName();

    private ActivityMvpDelegate<P, V> mvpDelegate;

    private P mPresenter;

    private View mBaseContentView;
    private LinearLayout mRootlayout;


    public ActivityMvpDelegate<P, V> getMvpDelegate() {
        if (mvpDelegate == null) {
            this.mvpDelegate = new ActivityMvpDelegateImpl<P, V>(this);
        }

        return mvpDelegate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseContentView = LayoutInflater.from(this).inflate(R.layout.activity_base, null);

        setContentView(mBaseContentView);

        /** 真实页面布局 */
        View contentView = LayoutInflater.from(this).inflate(initContentView(), null);

        RelativeLayout mContainer = mBaseContentView.findViewById(R.id.container);
        mRootlayout =  mBaseContentView.findViewById(R.id.ll_root);

        mContainer.addView(contentView);

        initNaviagationBar();

        ButterKnife.bind(this);

        ActivityManager.getAppManager().addActivity(this);
        getMvpDelegate().onCreate(savedInstanceState);

        initStatus();

    }

    protected void initNaviagationBar() {

        new DefaultNavigationBar.Builder(this, mRootlayout).setTitle("投稿")
                .setLeftIcon(R.mipmap.ic_launcher)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).create();


        ImmersionBar.with(this).keyboardEnable(true)
                .statusBarView(R.id.status_bar_place_hold)
                .init();

    }

    protected abstract int initContentView();


    private void initStatus() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 屏幕竖屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();

    }

    @Override
    protected void onDestroy() {

        ActivityManager.getAppManager().finishActivity(this);
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getMvpDelegate().onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void attachView() {
        mPresenter.attachView((V) this);
    }

    @Override
    public void deatchView() {
        mPresenter.decthView();
    }

    @Override
    public P getMvpPresenter() {
        return mPresenter;
    }

    @Override
    public void setSvpPresenter(BasePresenter presenter) {
        this.mPresenter = (P) presenter;
    }

    @Override
    public void saveInstanceState(Bundle outState) {

    }

    @Override
    public void restoreInstanceState(Bundle outState) {

    }

    /**
     * 跳转一个界面不传递数据
     *
     * @param clazz 要启动的Activity
     */
    protected void startActivity(Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }


}
