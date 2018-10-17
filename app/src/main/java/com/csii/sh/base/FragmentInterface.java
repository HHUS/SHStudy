package com.csii.sh.base;

public abstract interface FragmentInterface {
  public abstract void startFragment(BaseFragment paramBaseFragment, boolean paramBoolean);
  
  public abstract void finishFragment();
  
  public abstract void backToFragment(BaseFragment paramBaseFragment);
}
