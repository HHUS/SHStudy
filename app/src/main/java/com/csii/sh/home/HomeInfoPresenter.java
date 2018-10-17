package com.csii.sh.home;

import com.csii.basecomponent.SHModuleApi;
import com.csii.sh.data.LoginResponse;
import com.csii.sh.net.APIService;
import com.csii.sh.net.ApiClient;
import com.csii.sh.net.rx.NetworkTransformer;
import com.csii.sh.net.rx.NetworkSubscriber;
import com.csii.sh.net.rx.SchedulersTransformer;
import com.csii.sh.presenter.BasePresenter;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by on 2018/1/8.
 */

public class HomeInfoPresenter extends BasePresenter<HomeInfoView> {

    private HomeInfoModel model;

    public HomeInfoPresenter() {
//        super(map);
        model = new HomeInfoModel();
    }

    public void getHomeInfo(String cityId, String cityName, String latitude, String longitude) {


    }

    public void getLogin() {

        addDispose(ApiClient.getApiService(APIService.class).login("123456", "13665834640")
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(new SchedulersTransformer<LoginResponse>())
                .compose(new NetworkTransformer<LoginResponse>())
                .subscribeWith(new NetworkSubscriber<LoginResponse>() {

                    @Override
                    public void onNext(LoginResponse response) {
                        LoginResponse.InfoBean info = response.getInfo();
                    }

                    @Override
                    protected boolean onError(int errorCode) {
                        return super.onError(errorCode);
                    }
                }));

    }

}
