package com.csii.sh.util;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class ActivityManager {

  private static Stack<Activity> activityStack;
  private static ActivityManager instance;

  private ActivityManager() {
  }

  /**
   * 单一实例
   */
  public static ActivityManager getAppManager() {
    if (instance == null) {
      instance = new ActivityManager();
    }
    return instance;
  }

  /**
   * 添加Activity到堆栈
   */
  public void addActivity(Activity activity) {
    if (activityStack == null) {
      activityStack = new Stack<>();
    }
    activityStack.add(activity);
  }

  /**
   * 获取当前Activity（堆栈中最后一个压入的）
   */
  public Activity currentActivity() {
    return activityStack.lastElement();
  }

  /**
   * 结束当前Activity（堆栈中最后一个压入的）
   */
  public void finishActivity() {
    Activity activity = activityStack.lastElement();
    finishActivity(activity);
  }

  /**
   * 结束指定的Activity
   */
  public void finishActivity(Activity activity) {
    if (activity != null&&activityStack!=null) {
      activityStack.remove(activity);
      activity.finish();
    }
  }

  /**
   * 结束指定类名的Activity
   */
  public void finishActivity(Class<?> cls) {
    for (Activity activity : activityStack) {
      if (activity.getClass().equals(cls)) {
        finishActivity(activity);
      }
    }
  }

  /**
   * 结束所有Activity
   */
  public void finishAllActivity() {
    for (int i = 0, size = activityStack.size(); i < size; i++) {
      if (null != activityStack.get(i)) {
        activityStack.get(i).finish();
      }
    }
    activityStack.clear();
  }

  /**
   * 退出应用程序
   */
  public void AppExit(Context context) {
    try {
      finishAllActivity();
      android.app.ActivityManager activityMgr =
          (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      activityMgr.killBackgroundProcesses(context.getPackageName());
      System.exit(0);
    } catch (Exception ignored) {}
  }

  public boolean isAppExit() {
    return activityStack == null || activityStack.isEmpty();
  }
}