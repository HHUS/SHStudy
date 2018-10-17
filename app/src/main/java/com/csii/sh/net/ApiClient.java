package com.csii.sh.net;

import android.text.TextUtils;

import com.csii.sh.BuildConfig;
import com.csii.sh.net.impl.OnProgressResponseListener;
import com.csii.sh.util.ContextHolder;
import com.csii.sh.util.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by on 2018/1/7.
 */

public class ApiClient {


    public static final int DEFAULT_MILLISECONDS = 60;


    /*************************缓存设置*********************/
    /**
     1. noCache 不使用缓存，全部走网络

     2. noStore 不使用缓存，也不存储缓存

     3. onlyIfCached 只使用缓存

     4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

     5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

     6. minFresh 设置有效时间，依旧如上

     7. FORCE_NETWORK 只走网络

     8. FORCE_CACHE 只走缓存
     */

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";


    /**
     * 由于retrofit底层的实现是通过okhttp实现的，所以可以通过okHttp来设置一些连接参数
     * 如超时等
     */
    public static OkHttpClient provideOkHttpClient() {


        //缓存
        File cacheFile = new File(ContextHolder.getContext().getCacheDir(), "cache");
        int cacheSize = 1024 * 1024 * 100; //100Mb
        Cache cache = new Cache(cacheFile, cacheSize);
        //增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .addInterceptor(headerInterceptor)
                .build();

        return okhttpClient;
    }


    public static OkHttpClient.Builder addProgressClient(OkHttpClient.Builder builder, final OnProgressResponseListener listener) {

        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            }
        });

        return builder;
    }


    public static Retrofit provideRetrofit(OkHttpClient okhttpClient) {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okhttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 返回信息
     *
     * @param serviceClazz
     * @param <T>
     * @return
     */
    public static <T> T getApiService(Class<T> serviceClazz) {
        Retrofit retrofit = provideRetrofit(provideOkHttpClient());
        return retrofit.create(serviceClazz);
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (!NetWorkUtils.isNetConnected(ContextHolder.getContext())) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(ContextHolder.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置

                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
}

