package com.csii.sh.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

/**
 * autor : sunhao
 * time  : 2018/06/06  21:35
 * desc  : 屏幕适配方案
 *
 * 今日头条适配方案
 */

public class ScreenSupport {

    private static float sNoncopatDensity;
    private static float sNoncopaScaledDensity;

    /**
     * 按照设计图360dpi的设计来适配
     * @param activity
     * @param application
     */


    /**
     * DisplayMetrics#density 就是上述的density
     * DisplayMetrics#densityDpi 就是上述的dpi
     * DisplayMetrics#scaledDensity 字体的缩放因子，正常情况下和density相等，但是调节系统字体大小后会改变这个值
     * ：px = dp * density
     * @param activity
     * @param application
     */
    private static float appDensity;
    private static float appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;

    /**
     * 该方法在Application中初始化
     * @param application
     */
    public static void setDensity(@NonNull final Application application) {
        //获取application的DisplayMetrics
        appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (appDensity == 0) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }
    }

    //此方法在BaseActivity中做初始化(如果不封装BaseActivity的话,直接用下面那个方法就好了)
    public static void setDefault(Activity activity) {
        setAppOrientation(activity, "width");
    }

    //此方法用于在某一个Activity里面更改适配的方向
    public static void setOrientation(Activity activity, String orientation) {
        setAppOrientation(activity, orientation);
    }

    /**
     * targetDensity
     * targetScaledDensity
     * targetDensityDpi
     * 这三个参数是统一修改过后的值
     * <p>
     * orientation:方向值,传入width或height
     */
    private static void setAppOrientation(@Nullable Activity activity, String orientation) {

        float targetDensity = 0;
        try {
            Double division;
            //根据带入参数选择不同的适配方向
            if (orientation.equals("height")) {
                division = division(appDisplayMetrics.heightPixels, 667);
            } else {
                division = division(appDisplayMetrics.widthPixels, 360);
            }
            //由于手机的长宽不尽相同,肯定会有除不尽的情况,有失精度,所以在这里把所得结果做了一个保留两位小数的操作
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(division);
            targetDensity = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * 只修改Activity的density值
         */

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    //除法
    public static final Double division(double a, double b) {
        double div = 0;
        if (b != 0) {
            div = a / b;
        } else {
            div = 0;
        }
        return div;
    }



}
