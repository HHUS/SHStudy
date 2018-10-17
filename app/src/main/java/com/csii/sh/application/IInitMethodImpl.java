package com.csii.sh.application;

import android.content.Context;

/**
 * autor : sunhao
 * time  : 2018/01/24  16:22
 * desc  :
 */

public class IInitMethodImpl implements IInitWrapper {


    private static IInitMethodImpl mInstance;


    public IInitMethodImpl() {
        if(mInstance == null){
            mInstance = new IInitMethodImpl();
        }
    }
    public static IInitMethodImpl getInstance(){
        return mInstance;
    }


    @Override
    public void init(Context context) {

    }

    @Override
    public void onExecute() {

    }
}
