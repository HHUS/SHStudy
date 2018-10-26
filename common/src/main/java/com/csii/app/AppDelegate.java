package com.csii.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
/**
 * 该类代理Application的生命周期，在对应的生命周期处理对应的逻辑
 * Java是单继承的，则遇到某些第三方库需要继承Application时，就只能自定义Application并继承第三方库Application
 * 这时候，只能在自定义的Applicatio中对应的生命周期调用AppDelegate对应的方法
 */

public class AppDelegate implements AppLifecycles {

    private Application mApplication;
    private List<ConfigModule> configModules;
    private List<AppLifecycles> mAppLifecycles = new ArrayList<>();

    public AppDelegate(Context context) {
        //通过反射，将AndroidManifest.xml中带有ConfigModule标签的Class转成对象集合（List<ConfigModule>）
        configModules = new ManifestParser(context).parse();
        //遍历执行每一个ConfigModule实现类的方法
        for (ConfigModule configModule : configModules) {
            //框架外部，开发将Application的生命周期回调(Applifecycles) 存入mAppLifecycles集合(此时未注册回调)
            configModule.injectAppLifecycles(context, mAppLifecycles);
        }
    }

    @Override
    public void attachBaseContext(@NonNull Application application,Context context) {
        for (AppLifecycles lifecycles : mAppLifecycles) {
            lifecycles.attachBaseContext(application,context);
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        //执行框架外部，onCreate逻辑实现
        for (AppLifecycles lifecycles : mAppLifecycles) {
            lifecycles.onCreate(application);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(application);
            }
        }

        this.mAppLifecycles = null;
        this.mApplication = null;
    }
}
