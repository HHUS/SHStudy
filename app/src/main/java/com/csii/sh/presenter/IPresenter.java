package com.csii.sh.presenter;

import android.app.Activity;

/**
 * autor : sunhao
 * time  : 2018/06/07  17:36
 * desc  :
 */

public interface IPresenter {

    /**
     * 做一些初始化的工作
     */
    void onStart();


    /**
     *在框架中 {@link Activity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();

}
