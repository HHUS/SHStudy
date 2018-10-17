package com.csii.sh.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.csii.ptr.BaseRefreshAdapter;
import com.csii.ptr.PullToRefreshView;
import com.csii.sh.R;
import com.csii.sh.base.BaseActivity;
import com.csii.sh.core.ControllerAction;
import com.csii.sh.core.ObserverCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor : sunhao
 * time  : 2018/01/21  11:06
 * desc  : 刷新demo
 */

public class PtrRefreshActivity extends BaseActivity {

    @BindView(R.id.pull_view)
    PullToRefreshView mPullView;

    private SimpleAdapter mSimpleAdapter;

    private List<Integer> mIntegerList = new ArrayList<>();

    private int mCursor = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_ptr_refresh;
    }

    private void initView() {
        mSimpleAdapter = new SimpleAdapter(this, getData(), mPullView);
        mPullView.setMode(PullToRefreshView.REFRESH_BOTH);
        mPullView.setLayoutManager(new LinearLayoutManager(this));
        mPullView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mPullView.setAdapter(mSimpleAdapter);

    }



    private void initData() {
        for (int i = 1; i <= 50; i++) {
            mIntegerList.add(i);
        }
    }

    private List<Integer> getData() {
        List<Integer> list = new ArrayList<>(mIntegerList);
        if (hasMore()) {
            list = list.subList((mCursor - 1) * 10, mCursor * 10);
        }
        mCursor += 1;
        return list;
    }


    private boolean hasMore() {
        return mIntegerList.size() > (mCursor - 1) * 10;
    }

    private void initListener(){

        /** 只含有上拉加载的处理*/
        mPullView.setOnLoadMoreListener(new PullToRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRefresh() {
                Toast.makeText(PtrRefreshActivity.this, "上拉加载", Toast.LENGTH_SHORT).show();

            }
        });

        /** 只含有上拉加载-下拉刷新的处理*/
        mPullView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                Toast.makeText(PtrRefreshActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLoadMoreRefresh() {
                Toast.makeText(PtrRefreshActivity.this, "上拉加载", Toast.LENGTH_SHORT).show();

            }
        });

        /** 只含有下拉刷新的处理*/
        mPullView.setPullRefreshListener(new PullToRefreshView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                Toast.makeText(PtrRefreshActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();

            }
        });

    }



    public class SimpleAdapter extends BaseRefreshAdapter<SimpleAdapter.SimpleViewHolder, Integer> {

        public SimpleAdapter(Context context, List<Integer> list, PullToRefreshView refreshView) {
            super(context, list, refreshView);
        }


        public void setData(List<Integer> list, boolean isRefresh) {
            if (list != null) {
                if (isRefresh) {
                    mDataList.clear();
                }
                mDataList.addAll(list);
                notifyDataSetChanged();
            }
        }


        @Override
        protected SimpleViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new SimpleViewHolder(mLayoutInflater.inflate(R.layout.item_simple, parent, false));
        }


        @Override
        protected void onBindHolder(SimpleViewHolder holder, int position) {
            holder.mTvContent.setText(String.valueOf(mDataList.get(position)));
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_content)
            TextView mTvContent;

            SimpleViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
