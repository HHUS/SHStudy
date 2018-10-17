package com.csii.sh.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.csii.sh.core.BaseActionFragment;
import com.csii.sh.function.TestInterfaceActivity;

/**
 * Created by on 2017/10/15.
 */

public class BaseFragment extends Fragment {

    private BaseActionFragment baseActionFragment;

    public BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TestInterfaceActivity){
            ((TestInterfaceActivity) context).setFunctionForFragment(getTag());
        }

        if(activity instanceof BaseActivity) {
            this.activity = (BaseActivity)activity;
        }

        if(this.getParentFragment() instanceof BaseActionFragment) {
            this.baseActionFragment = (BaseActionFragment)this.getParentFragment();
        }
    }

    public void startFragment(BaseFragment fragment, boolean animation) {
        if(this.baseActionFragment != null) {
            this.baseActionFragment.startFragment(fragment, animation);
        }

    }

    public void finishFragment() {
        if(this.baseActionFragment != null) {
            this.baseActionFragment.finishFragment();
        }

    }

    public void backToFragment(BaseFragment fragment) {
        if(this.baseActionFragment != null) {
            this.baseActionFragment.backToFragment(fragment);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.finishFragment();
            return true;
        } else {
            return false;
        }
    }

}
