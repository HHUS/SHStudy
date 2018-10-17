package com.csii.sh.net.rx;

import android.util.Log;

import com.csii.sh.exception.ApiException;
import com.csii.sh.exception.ExceptionUtil;
import com.csii.sh.net.BaseResponse;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * description: 网络请求订阅者
 * 处理Retrofit网络加载结果(网络错误等)的Transformer.
 * autour:
 * date: 2018/1/7 下午9:00
 */

public class NetworkTransformer<T extends BaseResponse>implements FlowableTransformer<T, T> {

    @Override
    public Publisher<T> apply(@NonNull Flowable<T> flowable) {

        // 和onErrorResumeNext是有顺序关系的
        // 如果发生了业务错误,ExceptionUtils仍然能够处理
        // 业务错误直接抛给Subscriber.onError方法
        return flowable.doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(@NonNull T response) throws Exception {
                        int status = Integer.parseInt((response).getStatus());
                        // 在网络访问成功后，首先对Response进行Code检查，以判断接口是否访问成功
//                        if (status != 1) {
//                            throw new ApiException(status, response.getMessage());
//                        }
                    }
                })
                // 调用链只要有错误发生时，就会调用该方法
                // 不需要再做其他的错误处理
                .onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() {
                    @Override
                    public Publisher<? extends T> apply(@NonNull Throwable throwable) throws Exception {
                        // 对错误进行封装处理
                        // 最终会触发Subscriber的onError方法
                        Log.e("SN",throwable.getMessage());
                        return Flowable.error(ExceptionUtil.wrapperException(throwable));
                    }
                });
        //                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
        //                    @Override
        //                    public Observable<?> call(Observable<? extends Throwable> observable) {
        //                        return null;
        //                    }
        //                });
    }
}
