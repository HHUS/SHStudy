package com.csii.sh.net.rx;


import android.util.Log;

import com.csii.sh.exception.ApiException;
import com.csii.sh.exception.NetException;
import com.csii.sh.net.BaseResponse;

import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 网络结果处理的Subscriber.
 */
public abstract class NetworkSubscriber<T extends BaseResponse> extends DisposableSubscriber<T> {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onError(Throwable e) {
        // 打印错误信息
        //公告处理错误信息
        if (e instanceof NetException) {
            NetException netException = (NetException) e;

            Log.e("SN","错误: " + netException.getMessage());
            if (onError(netException.getCode())){

            }
//                UIUtils.showToast(e.getMessage());
        }else if(e instanceof ApiException){
            Log.e("SN","错误: " + e.getMessage());

        }else{
            Log.e("SN","错误: " + e.getMessage());
        }
    }

    /**
     * 网络加载完成回掉
     */
    @Override
    public void onComplete() {

    }

    /**
     * 网络访问成功的回掉
     */
    @Override
    public abstract void onNext(T t);


    /**
     * 网络错误的回掉
     *
     * @param errorCode 错误码
     * @return True 错误时给出Toast提示
     */
    protected boolean onError(int errorCode) {

        return true;
    }



}
