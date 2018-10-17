package com.csii.sh.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.csii.sh.base.BaseActivity;
import com.csii.sh.base.BaseFragment;
import com.csii.sh.base.FragmentInterface;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActionFragment extends Fragment implements FragmentInterface {

    private View view;
    private static int CONTENT_ID = 4353;
    private List<BaseFragment> fgList = new ArrayList<>();
    private BaseActivity activity;

    public BaseActionFragment() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof BaseActivity) {
            this.activity = (BaseActivity)activity;
        }

    }

    protected abstract void initContentView(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(this.activity);
        frameLayout.setId(CONTENT_ID);
        this.view = frameLayout;
        this.initContentView(this.view);
        return this.view;
    }

    public void startFragment(BaseFragment fragment, boolean animation) {
        this.fgList.add(fragment);
        FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
        if(animation) {
            fragmentTransaction.setTransition(4097);
        }

        fragmentTransaction.replace(CONTENT_ID, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void finishFragment() {
        if(this.fgList.size() > 1) {
            this.fgList.remove(this.fgList.size() - 1);
            FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.replace(CONTENT_ID, this.fgList.get(this.fgList.size() - 1));
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            this.activity.finish();
        }

    }

    public void backToFragment(BaseFragment fragment) {
    }

    public List<BaseFragment> getFragments() {
        return this.fgList;
    }
}
