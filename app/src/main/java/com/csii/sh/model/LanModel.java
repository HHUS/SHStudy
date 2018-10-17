package com.csii.sh.model;

/**
 * Created by on 2017/10/31.
 */

public class LanModel implements IModel {


    public void getTranferData(OnLoadCallBack callBack){
        if(callBack != null){
            callBack.onSuccess("AAAAAA");
        }

    }

    @Override
    public void onDestroy() {

    }


    public interface OnLoadCallBack{

        void onSuccess(String str);

    }
}
