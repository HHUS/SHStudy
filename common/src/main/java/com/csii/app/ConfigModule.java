package com.csii.app;

import android.content.Context;

import java.util.List;

/**
 * 可以给框架配置一些参数，
 * 需要在AndroidManifest.xml中配置实现
 */

public interface ConfigModule {

    /**
     * 在Application的生命周期中注入一些操作(第三方库初始化)
     * @param context
     * @param lifecycles
     */
    void injectAppLifecycles(Context context, List<AppLifecycles> lifecycles);

}
