package com.csii.sh.home;

import com.csii.sh.model.IModel;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by on 2018/1/8.
 */

public class HomeInfoModel implements IModel {


//    void request

    private void requestAllHomeInfo(CompositeDisposable disposable, String cityId, String cityName, String latitude, String longitude) {

//        disposable.add(ApiClient.getApiService(APIService.class).getHomeInfo(cityId, cityName, latitude, longitude)
//                .compose(new SchedulersTransformer<BaseResponse>())
//                .compose(new HttpSubscriber<BaseResponse>())
//                .subscribeWith(new NetworkSubscriber<BaseResponse>() {
//                    @Override
//                    public void onNext(BaseResponse baseResponse) {
//                        if (getMvpView() != null) {
//                            getMvpView().getHomeAllInfo(baseResponse);
//                        }
//
//                    }
//
//                }));
    }

    @Override
    public void onDestroy() {

    }
}
