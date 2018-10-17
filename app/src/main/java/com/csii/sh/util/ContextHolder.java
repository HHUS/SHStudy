package com.csii.sh.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/7/3.
 */

public class ContextHolder {
    private static Context context;

    public static void initContext(Context applicationContext) {
        context = applicationContext;
    }

    public static Context getContext() {
        return context;
    }
}
