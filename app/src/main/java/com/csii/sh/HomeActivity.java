package com.csii.sh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.csii.basecomponent.SHPublicApiHelper;
import com.csii.sh.data.LoginResponse;
import com.csii.sh.delegate.BaseMVPActivity;
import com.csii.sh.home.HomeInfoPresenter;
import com.csii.sh.home.HomeInfoView;
import com.csii.sh.navigation.DefaultNavigationBar;
import com.csii.sh.net.APIService;
import com.csii.sh.net.ApiClient;
import com.csii.sh.net.BaseResponse;
import com.csii.sh.net.rx.NetworkSubscriber;
import com.csii.sh.net.rx.NetworkTransformer;
import com.csii.sh.net.rx.SchedulersTransformer;
import com.csii.sh.presenter.BasePresenter;
import com.csii.sh.ui.PtrRefreshActivity;
import com.csii.sh.util.PermissionUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by on 2018/1/8.
 */

public class HomeActivity extends BaseMVPActivity<HomeInfoPresenter, HomeInfoView> implements HomeInfoView {

    @BindView(R.id.ll_root_container)
    LinearLayout rootLayout;
    @Override
    public BasePresenter createPresenter() {
        return new HomeInfoPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getMvpPresenter().getHomeInfo("412","郑州市","","");
//        getMvpPresenter().getLogin();

        RxPermissions mRxPermissions = new RxPermissions(this);
        //请求外部存储权限用于适配android6.0的权限管理机制

        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                Log.e("SN","graned");
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                Log.e("SN","shouldShowRequestPermissionRationale" + permissions.get(0));
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                Log.e("SN","shouldShowRequestPermissionRationale22222" + permissions.get(0));

            }
        },mRxPermissions);

//        String downUrl = "ZYMobileBankP.apk";
//        DownloadService.launch(this, downUrl, "ZYMobileBankP.apk");

    }

    @Override
    protected int initContentView() {
        return R.layout.layout_rxjava;
    }

    @Override
    public void getHomeAllInfo(BaseResponse response) {

    }

    @OnClick(R.id.button1)
    public void onClick(){
        Intent intent = new Intent(this, PtrRefreshActivity.class);
        startActivity(intent);
    }

    private void onlyRetrofitRequest() {
        APIService service = ApiClient.getApiService(APIService.class);

        HashMap<String, Object> params = new HashMap<>();
        Call<LoginResponse> call = service.loginRequest(params);

        try {
            //同步请求
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //异步请求
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


        Flowable<BaseResponse> flowable = service.getAuthCode("a");

        flowable.compose(new NetworkTransformer<BaseResponse>())
                .compose(new SchedulersTransformer<BaseResponse>())
                .subscribeWith(new NetworkSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {

                    }
                });

        //取消
        call.cancel();


        ApiClient.getApiService(APIService.class).getCode("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse response) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();

                    }
                });


        Flowable.create(new FlowableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(FlowableEmitter<BaseResponse> e) throws Exception {
                e.onNext(new BaseResponse());
            }
        }, BackpressureStrategy.BUFFER).subscribe(new Subscriber<BaseResponse>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(BaseResponse response) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        Flowable.create(new FlowableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(FlowableEmitter<BaseResponse> e) throws Exception {
                e.onNext(new BaseResponse());
            }
        }, BackpressureStrategy.BUFFER).subscribe(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {

            }
        });
    }

    @Override
    public Context getContext() {
        return null;
    }
}
