package com.csii.sh.net;

/**
 * Created by on 2018/1/8.
 */

public interface ResultCallBack<T> {

    void success(T t);

    void failed();


}
