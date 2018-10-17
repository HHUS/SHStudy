package com.csii.sh.presenter;

import com.csii.sh.view.IView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by on 2017/10/31.
 */

public abstract class BasePresenter<V extends IView> implements IPresenter{

    protected WeakReference<V> mWeakRef;
    protected CompositeDisposable mCompositeDisposable;

    /**
     * 绑定V层
     *
     * @param view
     */

    public void attachView(V view) {
        mWeakRef = new WeakReference<>(view);
        onStart();
    }

    @Override
    public void onStart() {

        if(useEventBus()){
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onDestroy() {
        if (useEventBus()){//如果要使用 Eventbus 请将此方法返回 true
            EventBus.getDefault().unregister(this);
        }
        unDispose();
    }

    public boolean useEventBus(){
        return true;
    }

    /**
     * 解除绑定V层
     */
    public void decthView() {
        mWeakRef.clear();
        onDestroy();
    }

    /**
     * 获取V层
     */
    public V getMvpView() {
        return mWeakRef.get();
    }


    /**
     * @param disposable
     */
    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //将所有 Disposable 放入集中处理
        mCompositeDisposable.add(disposable);
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            //保证 Activity 结束时取消所有正在执行的订阅
            mCompositeDisposable.clear();
        }
    }
}
