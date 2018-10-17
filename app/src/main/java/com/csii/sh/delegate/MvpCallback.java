package com.csii.sh.delegate;

import android.os.Bundle;

import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.view.IView;

/**
 * description:
 * autour:
 * date: 2018/1/7 上午11:58
 * <p>
 * 第二个代理->目标接口->绑定和解绑
 */
interface MvpCallback<P extends BasePresenter<V>, V extends IView> {

    void attachView();

    P createPresenter();

    void deatchView();

    void setSvpPresenter(P presenter);

    P getMvpPresenter();


    void saveInstanceState(Bundle outState);

    void restoreInstanceState(Bundle outState);


}
