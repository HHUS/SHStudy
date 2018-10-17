package com.csii.sh.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.csii.sh.util.Logger;

/**
 * autor : sunhao
 * time  : 2018/01/21  13:23
 * desc  :
 */

public class HomeKeyEventReceiver extends BroadcastReceiver{

    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";
    String SYSTEM_HOME_KEY_LONG = "recentapps";

    public HomeKeyEventReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
            String reason = intent.getStringExtra(this.SYSTEM_REASON);
            if(TextUtils.equals(reason, this.SYSTEM_HOME_KEY)) {
                Logger.d("按了home键,程序到了后台 !");
            } else {
                TextUtils.equals(reason, this.SYSTEM_HOME_KEY_LONG);
            }
        }

    }
}
