package com.csii.sh.ui.main.nav;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csii.sh.R;
import com.csii.sh.base.BaseFragment;
import com.csii.sh.ui.main.tabs.TabSubFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * autor : sunhao
 * time  : 2018/01/22  10:49
 * desc  :
 */

public class NavFragment extends BaseFragment {

    private View view;

    @BindView(R.id.nav_item_one)
    NavigationButton mNavNews;
    @BindView(R.id.nav_item_two)
    NavigationButton mNavTweet;
    @BindView(R.id.nav_item_three)
    NavigationButton mNavExplore;
    @BindView(R.id.nav_item_four)
    NavigationButton mNavMe;

    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;

    private OnNavigationReselectListener mOnNavigationReselectListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_nav, container, false);
        ButterKnife.bind(this, view);
        initWidget();

        return view;
    }

    private void initWidget() {
        mNavNews.init(R.mipmap.ic_launcher,
                R.string.tab_home,
                TabSubFragment.class);

        mNavTweet.init(R.mipmap.ic_launcher,
                R.string.tab_mine,
                TabSubFragment.class);

        mNavExplore.init(R.mipmap.ic_launcher,
                R.string.tab_mine,
                TabSubFragment.class);

        mNavMe.init(R.mipmap.ic_launcher,
                R.string.tab_home,
                TabSubFragment.class);

    }

    @OnClick({R.id.nav_item_one, R.id.nav_item_two, R.id.nav_item_three, R.id.nav_item_four})

    public void onClick(View v) {
        if (v instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) v;
            doSelect(nav);
        }
    }

    private void doSelect(NavigationButton newNavButton) {

        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
            }
            oldNavButton.setSelected(false);
        }

        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavNews);
    }

    private void clearOldFragment() {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0) {
            return;
        }

        boolean doCommit = false;

        for (Fragment fragment : fragments) {
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit) {
            transaction.commitNow();
        }

    }

    public void select(int index) {
        if (mNavMe != null)
            doSelect(mNavMe);
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext, newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();

    }


    private void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }


    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }
}
