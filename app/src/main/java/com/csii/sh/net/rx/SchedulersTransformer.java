package com.csii.sh.net.rx;

import com.csii.sh.net.BaseResponse;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * description: 线程Rx封装
 * autour:
 * date: 2018/1/9 下午4:24
*/

public class SchedulersTransformer<T extends BaseResponse> implements FlowableTransformer<T, T> {
    @Override
    public Publisher<T> apply(Flowable<T> upstream) {

        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
