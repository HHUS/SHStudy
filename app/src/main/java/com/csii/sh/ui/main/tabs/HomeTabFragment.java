package com.csii.sh.ui.main.tabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csii.sh.R;
import com.csii.sh.adapter.FragmentPagerAdapter;
import com.csii.sh.base.BaseFragment;
import com.csii.sh.entity.SubTab;
import com.csii.sh.ui.main.nav.OnTabReselectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * autor : sunhao
 * time  : 2018/01/22  14:34
 * desc  :
 */

public class HomeTabFragment extends BaseFragment implements OnTabReselectListener {


    @BindView(R.id.layout_tab)
    TabLayout mLayoutTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;


    private View view;
    private Fragment mCurFragment;

    private Activity activity;
    List<SubTab> tabs;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        initView();
        return view;
    }

    private void initView() {

        tabs = new ArrayList<>();
        SubTab subTab = new SubTab();
        subTab.setName("中原");
        tabs.add(subTab);

        for (SubTab tab : tabs) {
            mLayoutTab.addTab(mLayoutTab.newTab().setText(tab.getName()));
        }


        mViewPager.setAdapter(mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TabSubFragment.newInstance(getContext(), tabs.get(position));
            }

            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position).getName();
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
                if (mCurFragment == null) {
                    commitUpdate();
                }
                mCurFragment = (Fragment) object;
            }

            //this is called when notifyDataSetChanged() is called
            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }

        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mAdapter.commitUpdate();
                }
            }
        });
        mLayoutTab.setupWithViewPager(mViewPager);
        mLayoutTab.setSmoothScrollingEnabled(true);
    }

    @Override
    public void onTabReselect() {

    }
}
