package com.csii.sh.core;

import android.view.KeyEvent;
import android.view.View;

import com.csii.sh.base.BaseFragment;
import com.csii.sh.util.Logger;

/**
 * autor : sunhao
 * time  : 2018/01/19  16:19
 * desc  :
 */
public class ActionFragment extends BaseActionFragment {

    BaseFragment baseFragment;

    public ActionFragment() {
    }

    protected void initContentView(View view) {

        String className = this.getArguments().getString(ActionConstant.KEY_GRAGMENRT_CLASSNAME);
        if (className == null || "".equals(className)) {
            Logger.d("类名信息为空！");
            this.finishFragment();
        }
        Class cs;
        try {
            cs = Class.forName(className);
            if (this.isBaseFragment(cs)) {
                this.baseFragment = (BaseFragment) cs.newInstance();
                this.baseFragment.setArguments(this.getArguments());
                this.startFragment(baseFragment, false);
            } else {
                Logger.e("Exception:Fragment类型不是BaseFragment！类名信息解析失败，类型错误！");
                this.finishFragment();
            }
        } catch (Exception e) {
            Logger.e("Exception:类名信息解析失败，未找到目标！className:" + className);
            this.finishFragment();
        }

    }

    /**
     * isAssignableFrom 是用来判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口。
     * @param c
     * @return
     */
    public boolean isBaseFragment(Class<?> c) {
        return BaseFragment.class.isAssignableFrom(c);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.getFragments() != null
                && this.getFragments().size() > 0 ? (this.getFragments().get(this.getFragments().size() - 1)).onKeyDown(keyCode, event) : false;
    }

    public static final ActionFragment newInstance() {
        ActionFragment actionFragment = new ActionFragment();
        return actionFragment;
    }
}
