package com.csii.sh.delegate;

import android.os.Bundle;

import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.view.IView;

/**
 * description:
 * autour:
 * date: 2018/1/7 下午12:03
 *
 * 第二个代理-> 代理对象 持有目标对象 实现目标接口
*/
public class ProxyMvpCallBack<P extends BasePresenter<V>,V extends IView> implements MvpCallback<P,V> {

    private MvpCallback<P,V> mvpCallback;

    public ProxyMvpCallBack(MvpCallback<P, V> mvpCallback) {
        this.mvpCallback = mvpCallback;
    }

    @Override
    public P createPresenter() {

        P presenter = this.mvpCallback.getMvpPresenter();
        if(presenter == null){
            presenter = this.mvpCallback.createPresenter();
        }

        if(presenter == null){
            throw new NullPointerException("presenter 不能为 null");
        }

        this.mvpCallback.setSvpPresenter(presenter);

        return presenter;
    }

    @Override
    public void attachView() {
        this.mvpCallback.attachView();
    }

    @Override
    public void deatchView() {
        this.mvpCallback.deatchView();
    }

    @Override
    public void setSvpPresenter(P presenter) {
        this.mvpCallback.setSvpPresenter(presenter);
    }

    @Override
    public P getMvpPresenter() {
        return this.mvpCallback.getMvpPresenter();
    }

    @Override
    public void saveInstanceState(Bundle outState) {
       mvpCallback.saveInstanceState(outState);
    }

    @Override
    public void restoreInstanceState(Bundle outState) {
        mvpCallback.restoreInstanceState(outState);

    }
}
