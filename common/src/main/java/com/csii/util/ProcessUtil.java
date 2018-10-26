package com.csii.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * app 进程
 *
 * @author
 * @date 2018/9/19
 */
public class ProcessUtil {

    public static String getCurrentProcessName(Context context){
        int pid = android.os.Process.myPid();
        String processName = "";
        if(context != null && context.getApplicationContext() != null) {
            ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()){
                if (process.pid == pid){
                    processName = process.processName;
                }
            }
        }

        return processName;
    }
}
