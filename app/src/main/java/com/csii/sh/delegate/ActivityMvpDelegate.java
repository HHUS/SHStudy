package com.csii.sh.delegate;

import android.os.Bundle;

import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.view.IView;

/**
 * Created by on 2018/1/7.
 */

interface ActivityMvpDelegate<P extends BasePresenter<V>, V extends IView> {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onStop();

    void onPause();

    void onRestart();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);
}
