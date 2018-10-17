package com.csii.sh.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.csii.sh.util.ContextHolder;

import asp.citic.ptframework.PTFramework;

/**
 * APPLICATION
 */
public class AppContext extends Application{


    private ApplicationHelper mHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.initContext(this);

        PTFramework.initialization(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 分包
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
