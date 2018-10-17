package com.csii.sh.core.intent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.List;

public class IntentPrevent {
    public IntentPrevent() {
    }

    public boolean getIntentSafeStatus(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List activitis = packageManager.queryIntentActivities(intent, 0);
        return activitis.size() > 0;
    }
}
