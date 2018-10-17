package com.csii.sh.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csii.ptr.BaseRefreshAdapter;
import com.csii.ptr.PullToRefreshView;
import com.csii.sh.R;
import com.csii.sh.widget.LoadingEmptyLayout;
import com.csii.util.DeviceInfoUtil;

import org.json.JSONException;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.PTFrameworkListener;
import asp.citic.ptframework.message.PTMessageCenter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor : sunhao
 * time  : 2018/01/22  15:11
 * desc  :
 */

public abstract class BaseRecycleFragment<T> extends BaseFragment {

    @BindView(R.id.base_pull_view)
    public PullToRefreshView mPullView;

    @BindView(R.id.base_error_layout)
    LoadingEmptyLayout mEmptyLayout;

    private BaseRefreshAdapter mAdapter;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_base_recycler_view, container, false);

        ButterKnife.bind(this, view);
        initWidget();
        initDatas();
        return view;
    }

    private void initWidget() {
        mAdapter = getRecyclerAdapter();
        mPullView.setAdapter(mAdapter);
        mPullView.setMode(PullToRefreshView.REFRESH_BOTH);

        initListener();
    }

    public abstract void initDatas();

    protected void initListener() {
        mPullView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState && getActivity() != null
                        && getActivity().getCurrentFocus() != null) {
                    DeviceInfoUtil.hideSoftKeyboard(getActivity().getCurrentFocus());
                }
            }
        });
    }

    protected abstract BaseRefreshAdapter getRecyclerAdapter();


}

