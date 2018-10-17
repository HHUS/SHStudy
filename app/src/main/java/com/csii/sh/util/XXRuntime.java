package com.csii.sh.util;

import android.content.Context;

/**
 * autor : sunhao
 * time  : 2018/06/15  15:32
 * desc  :
 */

public class XXRuntime {
    protected final Context context;

    public XXRuntime(Context context) {
        if (null == context) {
            throw new NullPointerException("XXRuntime context con not be null!");
        }
        this.context = context;
    }

    public Context getContext() {
        return context;
    }


}
