package com.csii.sh.core;

import android.content.Intent;
import android.os.Bundle;

import com.csii.sh.base.BaseActivity;
import com.csii.sh.core.callback.IntentCallBack;
import com.csii.sh.util.Constant;
import com.csii.util.Logger;

/**
 * autor : sunhao
 * time  : 2018/01/19  16:19
 * desc  : 所有的Fragment控制
 */
public class ControllerAction {

    public ControllerAction() {

    }

    public static void startAction(final BaseActivity activity, String LoginType, final String ActionDisplayId, final String Params, final ObserverCallback callBack) {
        if (!Constant.isLogin && !"F".equals(LoginType)) {
            
            displayAction(activity, ActionConstant.ACTION_AUTH_LOGIN, "", new ObserverCallback() {
                public void setResult(Object obj) {
                    if (Constant.isLogin) {
                        ControllerAction.displayAction(activity, ActionDisplayId, Params, callBack);
                    }

                }
            });
        } else {
            displayAction(activity, ActionDisplayId, Params, callBack);
        }

    }

    /**
     * @param activity
     * @param actionDisplayId "Loading_SplashScreen" "Auth_Login"; 包名+类名
     * @param params
     * @param callBack
     */
    private static void displayAction(BaseActivity activity, String actionDisplayId, String params, final ObserverCallback callBack) {
        Logger.d("ActionDisplayId----" + actionDisplayId);
        String className = "";
        String[] strs;
        if (actionDisplayId.contains("_")) {
            strs = actionDisplayId.split("_");
            if (strs.length == 2) {
                className = "com.csii.sh." + strs[0].toLowerCase() + "." + strs[1] + "Fragment";
                Logger.d("ClassName: " + className);
                Logger.d("Params: " + params);
                Bundle bundle = new Bundle();
                bundle.putString(ActionConstant.KEY_GRAGMENRT_CLASSNAME, className);
                bundle.putString(ActionConstant.KEY_GRAGMENRT_PARAMS, params);
                if (callBack != null) {
                    activity.startAcForResult(ActionActivity.class, bundle, new IntentCallBack() {
                        public void onIntent(Intent intent) {
                            String result = "";
                            if (intent != null && intent.getExtras() != null) {
                                result = intent.getExtras().getString(ActionConstant.KEY_ACTIVITY_RESULT);
                            }
                            callBack.setResult(result);
                        }
                    });
                } else {
                    activity.StartActivity(ActionActivity.class, bundle);
                }
            } else {
                Logger.e("startAction:ActionDisplayId格式不正确--------" + actionDisplayId);
            }
        } else {
            Logger.e("startAction:ActionDisplayId格式不正确--------" + actionDisplayId);
        }

    }
}
