package asp.citic.ptframework;

import android.app.Application;
import android.content.Context;

import asp.citic.ptframework.common.device.PTDeviceUtil;
import asp.citic.ptframework.common.tools.Charsets;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.resource.PTDataZipManager;
import asp.citic.ptframework.resource.PTHtmlManager;
import asp.citic.ptframework.resource.PTResourceJsonManager;
import asp.citic.ptframework.secretkeys.PTConsultPassword;

/**
 * @ingroup frameModuleClass
 * 框架入口
 */
public final class PTFramework {
    private static final String TAG = "PTFramework";

    /**
     * Application对象
     */
    private static Context context;

    /**
     * 私有构造方法
     */
    private PTFramework() {
        //To be a Utility class
    }

    /**
     * 初始化框架环境.
     */
    public static void initialization(Application app) {
        PTLogger.info(TAG, "开始调用初始化框架环境模块");
        context = app.getApplicationContext();
    }

    /**
     * 供外部调用通信安全模块的接口
     */
    public static void communicationSecurityModel() {
        PTLogger.info(TAG, "开始调用通信安全模块");
        new Thread() {
            @Override
            public void run() {
                super.run();
                PTConsultPassword.sendConsultPasswordRequest();
            }
        }.start();
    }

    /**
     * 供外部调用解压模块的接口
     */
    public static void unzipModel() {
        PTLogger.info(TAG, "开始调用解压模块");
        PTDataZipManager.unpackResource();
    }

    /**
     * 供外部调用资源安全模块的接口
     */
    public static void resourceSecurityModule() {
        PTLogger.info(TAG, "开始调用资源安全模块");
        PTResourceJsonManager.initialization();
    }

    /**
     * 供外部调用增量更新模块的接口
     */
    public static boolean incrementalUpdateModule() {
        PTLogger.info(TAG, "开始调用增量更新模块");
        return PTResourceJsonManager.updateResourceFile();
    }

    /**
     * 供外部调用解密HTML模块的接口
     */
    public static String readHtmlModule(String url) {
        PTLogger.info(TAG, "开始调用解密HTML模块");
        byte[] htmlBytes = PTHtmlManager.getHtmlContentByUrl(url);
        return new String(htmlBytes, Charsets.UTF_8);
    }

    /**
     * 供外部调用设备信息模块的接口
     */
    public static void deviceModel() {
        PTLogger.info(TAG, "开始调用设备信息模块");
        PTDeviceUtil.initialization(context);
    }

    public static Context getContext() {
        return context;
    }

}
