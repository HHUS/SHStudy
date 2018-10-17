package com.csii.sh.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.csii.sh.R;
import com.csii.sh.base.BaseActivity;
import com.csii.sh.ui.main.nav.NavFragment;
import com.csii.sh.ui.main.nav.NavigationButton;
import com.csii.sh.ui.main.nav.OnTabReselectListener;

/**
 * autor : sunhao
 * time  : 2018/01/19  16:19
 * desc  :
 */

public class MainTabActivity extends BaseActivity implements NavFragment.OnNavigationReselectListener {


    private NavFragment mNavBar;

    @Override
    protected int initContentView() {
        return R.layout.activity_tabmain;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        doNewIntent(getIntent(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doNewIntent(intent, false);
    }

    @SuppressWarnings("unused")
    private void doNewIntent(Intent intent, boolean isCreate) {
        if (intent == null || intent.getAction() == null)
            return;
        String action = intent.getAction();
//        if (action.equals(ACTION_NOTICE)) {
        NavFragment bar = mNavBar;
        if (bar != null) {
            bar.select(3);
        }
//        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        FragmentManager manager = getSupportFragmentManager();

        mNavBar = ((NavFragment) manager.findFragmentById(R.id.main_fag_nav));
        mNavBar.setup(this, manager, R.id.main_container, this);

    }

    @Override
    public void onReselect(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null && fragment instanceof OnTabReselectListener) {
            OnTabReselectListener listener = (OnTabReselectListener) fragment;
            listener.onTabReselect();
        }
    }
}
