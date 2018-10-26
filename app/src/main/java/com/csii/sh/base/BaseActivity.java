package com.csii.sh.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.csii.sh.core.callback.CallBackIntent;
import com.csii.sh.core.callback.IntentCallBack;
import com.csii.sh.core.secure.SecureManager;
import com.csii.sh.util.Constant;
import com.csii.util.Logger;
import com.csii.sh.util.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by on 2017/10/31.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity baseAt;
    private CallBackIntent callBackIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((this.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            this.finish();
        } else {

            PermissionUtil.readPhonestate(new PermissionUtil.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    if (Constant.IsPreventScreenShot) {
                        SecureManager.preventScreenShot(BaseActivity.this);
                    }

                    if (Constant.IsEmulatorCheck && SecureManager.isEmulator(BaseActivity.this)) {
//                Controller.sendNotification(this.baseAt, "Secure|IsEmulator", "", (ObserverCallback)null);
                    }
                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {

                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                }
            },new RxPermissions(this));



            setContentView(initContentView());
            this.baseAt = this;
            ActivityManager.getAppManager().addActivity(this);

            ButterKnife.bind(this);

            initWidget();
        }

    }
    protected void initWidget() {
    }

    protected abstract int initContentView();

    public void StartActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this.baseAt, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        this.startActivity(intent);
    }

    public void startAcForResult(Intent intent, final IntentCallBack callBack) {
        this.startActivityForResult(intent, new CallBackIntent() {
            public void onResult(Intent data) {
                if (callBack != null) {
                    callBack.onIntent(data);
                }

            }
        });
    }

    public void startAcForResult(Class<?> cls, Bundle bundle, IntentCallBack callBack) {
        Intent intent = new Intent(this.baseAt, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        this.startAcForResult(intent, callBack);
    }

    public void startActivityForResult(Intent intent, CallBackIntent callBackIntent) {
        this.callBackIntent = callBackIntent;
        this.startActivityForResult(intent, 100);
    }

    public void forResult(Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }

        this.setActivityResultCallback(intent);
    }

    public void setActivityResultCallback(Intent intent) {
        if (intent == null) {
            this.setResult(101, new Intent());
        } else {
            this.setResult(101, intent);
        }

        this.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (101 == resultCode) {
            if (this.callBackIntent != null) {
                this.callBackIntent.onResult(data);
            } else {
                Logger.d("自定义的页面onActivityResult--callback==null");
            }
        } else if (100 == requestCode && resultCode != 0) {
            if (this.callBackIntent != null) {
                this.callBackIntent.onResult(data);
            } else {
                Logger.d("系统页面onActivityResult--callback==null");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
