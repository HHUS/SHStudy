package com.csii.sh.core.secure;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.csii.sh.core.ActivityPrevent;
import com.csii.sh.core.intent.emulator.AntiEmulator;
import com.csii.sh.core.storage.SecureStorage;


public class SecureManager {

    public SecureManager() {
    }

    /**
     * 防止截屏
     * @param activity
     */
    public static void preventScreenShot(Activity activity) {
        if(activity != null) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    public static boolean isEmulator(Context context) {
        return AntiEmulator.isEmulator(context);
    }

    public static void monitorRunningTask(Context context, boolean allowHint, ActivityPrevent.TaskStatusListener listener) {
        ActivityPrevent.monitorRunningTask(context, allowHint, listener);
    }

    public static SecureStorage instanceSecureStorage(Context context, String xmlFileName, String secretKey) {
        return new SecureStorage(context, xmlFileName, secretKey);
    }
}
