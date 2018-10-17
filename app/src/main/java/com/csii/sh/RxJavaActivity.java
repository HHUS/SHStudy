package com.csii.sh;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.csii.sh.net.APIService;
import com.csii.sh.net.ApiClient;
import com.csii.sh.presenter.LanPresenter;
import com.csii.sh.delegate.BaseMVPActivity;
import com.csii.sh.view.LanView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


public class RxJavaActivity extends BaseMVPActivity<LanPresenter, LanView> implements LanView {

    @BindView(R.id.button1)
    Button button1;

    public static final String theadFactory = "AA";

    @Override
    protected int initContentView() {
        return R.layout.layout_rxjava;
    }

    @Override
    public LanPresenter createPresenter() {
        return new LanPresenter();
    }

    private void initData() {
        getMvpPresenter().fetch();
        APIService mApiService;
        mApiService = ApiClient.getApiService(APIService.class);

//
//        addDisposable(mApiService.getAuthCode("Aa").compose(new SchedulersTransformer<>())
//                .compose(new HttpSubscriber<BaseResponse>()).subscribeWith(new NetworkSubscriber<BaseResponse>() {
//                    @Override
//                    public void onNext(BaseResponse baseResponse) {
//
//                    }
//                }));
//
//        RxView.clicks(button1).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(@NonNull Object o) throws Exception {
////                Toast.makeText(RxJavaActivity.this,"点击了",Toast.LENGTH_LONG).show();
//
//            }
//        });

//        EditText ed = null;
//
//        RxTextView.textChangeEvents(ed).debounce(300,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Consumer<TextViewTextChangeEvent>() {
//            @Override
//            public void accept(@NonNull TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
//
//            }
//        });


        Observable.just(1,2,23,4)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {

                        return false;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

            }
        });

//      每1s发送1个数据 = 从0开始，递增1，即0、1、2、3
        Observable.interval(1, TimeUnit.DAYS)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return false;
                    }
                });


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void returnData(String str) {

        Log.e("SNNN",str);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
