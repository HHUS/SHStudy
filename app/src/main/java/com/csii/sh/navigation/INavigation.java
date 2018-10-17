package com.csii.sh.navigation;

/**
 * autor : sunhao
 * time  : 2018/01/18  22:28
 * desc  : 定义导航条规范
 */

public interface INavigation {

    //绑定布局的Id
    int bindLayoutId();

    void applyView();

}
