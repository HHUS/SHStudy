package com.csii.sh.core;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

import com.csii.sh.util.Constant;
import com.csii.sh.util.ToastUtils;

import java.util.Iterator;
import java.util.List;

public class ActivityPrevent {
    private static final String TAG = "ActivityPrevent";

    public ActivityPrevent() {

    }

    public static void monitorRunningTask(Context context, boolean allowHint, ActivityPrevent.TaskStatusListener listener) {
        if (!isAppOnForeground(context)) {
            if (allowHint) {
                ToastUtils.showShort(Constant.ALarmText);
            }

            if (listener != null) {
                listener.taskStatusChanged(true);
            }
        } else if (listener != null) {
            listener.taskStatusChanged(false);
        }

    }

    private static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List appProcesses = activityManager.getRunningAppProcesses();
        Iterator iterator = appProcesses.iterator();

        while (iterator.hasNext()) {
            RunningAppProcessInfo appProcess = (RunningAppProcessInfo) iterator.next();
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == 400) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }

                Log.i("前台", appProcess.processName);
                return false;
            }
        }

        return false;
    }

    private static boolean isAppOnForeground(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = ctx.getApplicationContext().getPackageName();
        List appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        } else {
            Iterator iterator = appProcesses.iterator();

            RunningAppProcessInfo appProcess;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                appProcess = (RunningAppProcessInfo) iterator.next();
            } while (!appProcess.processName.equals(packageName) || appProcess.importance != 100);

            return true;
        }
    }

    public interface TaskStatusListener {
        void taskStatusChanged(boolean var1);
    }
}
