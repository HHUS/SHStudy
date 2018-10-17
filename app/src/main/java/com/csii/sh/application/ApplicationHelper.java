package com.csii.sh.application;

import android.content.Context;

/**
 * autor : sunhao
 * time  : 2018/01/24  16:28
 * desc  :
 */

public class ApplicationHelper implements IInitMethods {

    ApplicationHelper mInstance;

    public ApplicationHelper(Context context) {

    }

    @Override
    public IInitMethods initNetWork() {

        return mInstance;
    }

    @Override
    public IInitMethods initImageLoader() {
        return null;
    }

    @Override
    public IInitMethods initPush() {
        return null;
    }
}
