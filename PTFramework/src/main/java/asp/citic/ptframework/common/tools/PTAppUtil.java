package asp.citic.ptframework.common.tools;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import java.io.File;
import java.util.List;

import asp.citic.ptframework.PTFramework;

/**
 * @author dora
 * @function App基本信息
 * @time 2016-11-29
 */
public final class PTAppUtil {
    /**
     * 包管理器
     */
    private static PackageManager pkgm;
    /**
     * 上下文对象
     */
    private static Context context;
    /**
     * 包信息类
     */
    private static PackageInfo pkgi;

    static {
        context = PTFramework.getContext();
        pkgm = context.getPackageManager();
        try {
            pkgi = pkgm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            pkgi = new PackageInfo();
        }
    }

    /**
     * 私有构造方法
     */
    private PTAppUtil() {
        //To be a Utility class
    }

    /**
     * 获取目前软件版本
     *
     * @return 软件版本号
     */
    public static int getAppVersionCode() {
        return pkgi.versionCode;
    }

    /**
     * 获取目前软件版本名
     *
     * @return 软件版本名
     */
    public static String getAppVersionName() {
        return pkgi.versionName;
    }

    /**
     * 获取manifest中下载渠道信息
     *
     * @return 下载渠道
     */
    public static String getChanel() throws NameNotFoundException {
        String channelId = "0";
        ApplicationInfo appi = pkgm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        Object value = appi.metaData.get("CHANNEL");
        if (value != null) {
            channelId = value.toString();
        }
        return channelId;
    }

    /**
     * 获取数字签名
     *
     * @return 应用数字签名
     */
    public static String getAppSign() {
        return new String(pkgi.signatures[0].toChars());
    }

    /**
     * 检查apk是否存在
     *
     * @param packageName
     * @return 相应的apk是否存在
     */
    public static boolean isApkExist(String packageName) {

        List<PackageInfo> packageInfos = pkgm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 安装apk
     *
     * @param file
     */
    public static void installApk(File file) {
        if (file != null && file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 重启应用
     */
    public static void restartApp() {
        Intent intent = pkgm.getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 启动应用
     *
     * @param packageName 应用的包名
     */
    public static void startApp(String packageName) {
        Intent intent = pkgm.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * app是在前台还是后台
     *
     * @return 应用处于前台返回true, 否则返回false
     */
    public static boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName()) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
