package com.csii.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 用于代理Application的生命周期
 */

public interface AppLifecycles {

    void attachBaseContext(@NonNull Application application, @NonNull Context context);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);

}
