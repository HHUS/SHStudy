package com.csii.sh.base;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csii.sh.R;
import com.csii.sh.adapter.ViewPagerFragmentAdapter;
import com.csii.sh.widget.LoadingEmptyLayout;
import com.csii.sh.widget.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * autor : sunhao
 * time  : 2018/01/19  15:23
 * desc  :
 */

public abstract class BaseViewPagerFragment extends BaseFragment {

    private static final String TAG = "BaseViewPagerFragment";
    @BindView(R.id.pager_tabstrip)
    PagerSlidingTabStrip mTabStrip;
    @BindView(R.id.pager)
    protected ViewPager mViewPager;
    protected ViewPagerFragmentAdapter mTabsAdapter;
    @BindView(R.id.error_layout)
    protected LoadingEmptyLayout mErrorLayout;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = null;

        if (mRoot == null) {
            View root = inflater.inflate(R.layout.base_viewpage_fragment, null);

            ButterKnife.bind(this, root);
            mTabsAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), mTabStrip, mViewPager);

            setScreenPageLimit();
            mRoot = root;
            onSetupTabAdapter(mTabsAdapter);
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("position");
            mViewPager.setCurrentItem(pos, true);
        }
    }

    protected void setScreenPageLimit() {
    }

    protected abstract void onSetupTabAdapter(ViewPagerFragmentAdapter adapter);
}
