package com.csii.sh.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.csii.sh.R;
import com.csii.sh.base.BaseActivity;

/**
 * autor : sunhao
 * time  : 2018/01/21  14:31
 * desc  :
 */

public class ActionActivity extends BaseActivity {

    private ActionFragment actionFg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.actionFg = ActionFragment.newInstance();
        this.actionFg.setArguments(this.getIntent().getExtras());
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, this.actionFg);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_action;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.actionFg.onKeyDown(keyCode, event) ? true : super.onKeyDown(keyCode, event);


    }
}
