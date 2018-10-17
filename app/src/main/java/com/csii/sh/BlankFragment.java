package com.csii.sh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.csii.sh.base.BaseFragment;
import com.csii.sh.function.FunctionManager;

/**
 * Created by on 2017/10/15.
 */

public class BlankFragment extends BaseFragment {


    //定义接口
    public static final String INTERFACE = BlankFragment.class.getName() + "NPNR";


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FunctionManager.getInstance().invokeFunc(INTERFACE,"aaaa",String.class);
    }






}
