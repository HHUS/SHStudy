package com.csii.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.csii.imageloader.loader.ImageLoader;
import com.csii.imageloader.strategy.GlideImageLoaderStrategy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * autor : sunhao
 * time  : 2018/10/26  21:51
 * desc  :
 */

public class GlobalConfiguration implements ConfigModule {
    @Override
    public void injectAppLifecycles(Context context, List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecycles() {
            @Override
            public void attachBaseContext(@NonNull Application application, @NonNull Context context) {

            }

            @Override
            public void onCreate(@NonNull Application application) {
                //初始化图片加载框架为GLide
                ImageLoader.setLoadImgStrategy(new GlideImageLoaderStrategy());
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }
}
