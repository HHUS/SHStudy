package com.csii.sh.delegate;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.view.IView;

/**
 * Created by on 2018/1/7.
 */

public class BaseMVPFragment<P extends BasePresenter<V>,V extends IView> extends FragmentActivity implements MvpCallback<P,V> {


    private ActivityMvpDelegateImpl<P,V> mvpDelegate;

    public ActivityMvpDelegate<P, V> getMvpDelegate() {
        if (mvpDelegate == null) {
            this.mvpDelegate = new ActivityMvpDelegateImpl<P, V>(this);
        }

        return mvpDelegate;
    }


    @Override
    public void attachView() {

    }

    @Override
    public P createPresenter() {
        return null;
    }

    @Override
    public void deatchView() {

    }

    @Override
    public void setSvpPresenter(P presenter) {

    }

    @Override
    public P getMvpPresenter() {
        return null;
    }

    @Override
    public void saveInstanceState(Bundle outState) {

    }

    @Override
    public void restoreInstanceState(Bundle outState) {

    }
}
