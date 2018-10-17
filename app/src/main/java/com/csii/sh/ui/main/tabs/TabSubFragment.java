package com.csii.sh.ui.main.tabs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.csii.ptr.BaseRefreshAdapter;
import com.csii.ptr.PullToRefreshView;
import com.csii.sh.base.BaseRecycleFragment;
import com.csii.sh.entity.SubTab;
import com.csii.sh.ui.main.nav.OnTabReselectListener;

import org.json.JSONException;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.PTFrameworkListener;
import asp.citic.ptframework.common.signtools.PTSignTool;
import asp.citic.ptframework.message.PTMessageCenter;
import asp.citic.ptframework.security.PTClientKeyStore;

/**
 * autor : sunhao
 * time  : 2018/01/22  15:09
 * desc  :
 */

public class TabSubFragment extends BaseRecycleFragment implements OnTabReselectListener {

    @Override
    public void initDatas() {
//
//        //发交易   获取数据
//        int signLen = PTClientKeyStore.getResourceCertifiedPublicKey().getModulus().bitLength() / 8;
//        int signBase64Len = PTSignTool.getSignBase64Len(signLen);
//        PTFramework.incrementalUpdateModule();
//        PTFramework.deviceModel();
//        PTFramework.resourceSecurityModule();
//        PTMessageCenter.addListener(new PTFrameworkListener.CustomEventListener() {
//            @Override
//            public void onCustomEvent(int event, Object obj) throws JSONException {
//                Log.e("sun",obj.toString() + " event " + event);
//            }
//        });


    }

    public static TabSubFragment newInstance(Context context, SubTab subTab) {
        TabSubFragment fragment = new TabSubFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sub_tab", subTab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected BaseRefreshAdapter getRecyclerAdapter() {
        return null;
    }

    @Override
    protected void initListener() {
        super.initListener();

        mPullView.setPullRefreshListener(new PullToRefreshView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullView.onLoadComplete(true);

                    }
                }, 1000);

            }
        });
    }

    @Override
    public void onTabReselect() {

    }
}
