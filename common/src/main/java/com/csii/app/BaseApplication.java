package com.csii.app;

import android.app.Application;
import android.content.Context;

import com.csii.util.ProcessUtil;


/**
 * 所有的module独立运行时使用BaseApplication
 */

public class BaseApplication extends Application {

    private AppDelegate mAppDelegate;
    private static BaseApplication mBaseApplication;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        if(mAppDelegate == null) {
            mAppDelegate = new AppDelegate(base);
        }
        if (ProcessUtil.getCurrentProcessName(this).equals(base.getPackageName())){
            mAppDelegate.attachBaseContext(BaseApplication.this,base);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (ProcessUtil.getCurrentProcessName(this).equals(getApplicationContext().getPackageName())){
            if(mAppDelegate != null) {
                mAppDelegate.onCreate(this);
            }
            mBaseApplication = this;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (ProcessUtil.getCurrentProcessName(this).equals(getApplicationContext().getPackageName())){
            if(mAppDelegate != null) {
                mAppDelegate.onTerminate(this);
            }
        }
    }

    public static BaseApplication getInstance() {
        return mBaseApplication;
    }
}
