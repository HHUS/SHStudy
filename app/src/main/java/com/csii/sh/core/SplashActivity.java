package com.csii.sh.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.csii.sh.R;
import com.csii.sh.base.BaseActivity;
import com.csii.sh.core.receiver.HomeKeyEventReceiver;

/**
 * autor : sunhao
 * time  : 2018/01/21  13:26
 * desc  : 启动页面
 */

public class SplashActivity extends BaseActivity {


    private HomeKeyEventReceiver receiver = new HomeKeyEventReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!this.isTaskRoot()) {
            Intent mainIntent = this.getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory("android.intent.category.LAUNCHER") && action.equals("android.intent.action.MAIN")) {
                this.finish();
                return;
            }
        }

        init();


    }

    /**
     * 初始化检测版本，启动logo等
     */
    private void init() {

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_splash;
    }
}
