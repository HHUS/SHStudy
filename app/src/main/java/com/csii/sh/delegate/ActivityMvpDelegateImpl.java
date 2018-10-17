package com.csii.sh.delegate;

import android.os.Bundle;

import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.view.IView;

/**
 * Created by on 2018/1/7.
 */

public class ActivityMvpDelegateImpl<P extends BasePresenter<V>,V extends IView> implements ActivityMvpDelegate {

    ProxyMvpCallBack<P,V> proxyMvpCallBack;

    public ActivityMvpDelegateImpl(MvpCallback<P, V> mvpCallBack) {
        this.proxyMvpCallBack = new ProxyMvpCallBack<>(mvpCallBack);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        this.proxyMvpCallBack.createPresenter();
        this.proxyMvpCallBack.attachView();

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onDestroy() {
        this.proxyMvpCallBack.deatchView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        this.proxyMvpCallBack.saveInstanceState(outState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.onRestoreInstanceState(savedInstanceState);
    }
}
