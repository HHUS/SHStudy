package com.csii.sh.ui.main;

import android.os.Bundle;
import android.view.View;

import com.csii.sh.core.BaseActionFragment;
import com.csii.sh.util.Constant;

/**
 * autor : sunhao
 * time  : 2018/01/22  10:19
 * desc  :
 */

public class Fragment_TabList extends BaseActionFragment {
    int type = 0;
    @Override
    protected void initContentView(View view) {
        if (getArguments() != null) {
            type = getArguments().getInt(Constant.KEY_FGTYPE);
        }

        switch (type) {
//            case 1:
//                // 首页
//                Fragment_home fragment_home = new Fragment_home();
//                startFragment(fragment_home, false);
//                break;
//            case 2:
//                // 理财
//                Fragment_financial fragment_financial = new Fragment_financial();
//                startFragment(fragment_financial, false);
//                break;
//            case 3:
//                // 生活
//                Fragment_life fragment_life = new Fragment_life();
//                startFragment(fragment_life, true);
//                break;
//            case 4:
//                //助手
//                Fragment_assistant fragment_assistant = new Fragment_assistant();
//                startFragment(fragment_assistant, false);
//                break;

            default:
                break;
        }
    }
}
