package com.csii.sh.presenter;

import com.csii.basecomponent.SHModuleApi;
import com.csii.sh.model.LanModel;
import com.csii.sh.view.LanView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2017/10/31.
 */

public class LanPresenter extends BasePresenter<LanView> implements SHPublicApi.RecordApi{

    //Model 层的引用

    LanModel mMode = new LanModel();

    public LanPresenter() {
    }

    public void fetch(){

        if(getMvpView() != null){
            getMvpView().showLoading();

            if(mMode != null){
                mMode.getTranferData(new LanModel.OnLoadCallBack() {
                    @Override
                    public void onSuccess(String str) {
                        getMvpView().returnData(str);
                    }
                });
            }
        }
    }

//    @Override
    public Map<Class<? extends SHModuleApi>, SHModuleApi> getModuleApi() {
        Map<Class<? extends SHModuleApi>,SHModuleApi> map = new HashMap<>();
        map.put(SHPublicApi.RecordApi.class,this);
        return map;
    }

    @Override
    public void updateRecordView() {

    }
}

