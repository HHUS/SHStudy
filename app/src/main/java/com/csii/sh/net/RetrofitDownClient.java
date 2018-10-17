package com.csii.sh.net;

import com.csii.sh.net.impl.OnProgressResponseListener;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * <pre>
 *  autor :
 *  time  : 2018/01/17
 *  desc  : 使用Retrofit下载Apk或者文件
 * </pre>
 */

public class RetrofitDownClient {

    //所有的联网地址 统一成https
    public final static String mBaseUrl = "http://download.zybank.com.cn/";

    //static 静态成员 一旦被初始化就就保存在同一个地址
    //所以一旦 被addProgressClient 修饰添加拦截器 之后 整个系统唯一的httpClient 就被添加了拦截器
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    public static <S> S createService(Class<S> serviceClass, OnProgressResponseListener listener) {
        Retrofit retrofit = builder
                .client(ApiClient.addProgressClient(httpClient, listener).build())
                .build();
        return retrofit.create(serviceClass);
    }


}
