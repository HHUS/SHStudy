package asp.citic.ptframework.common.device;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import asp.citic.ptframework.common.tools.PTStringUtil;

/**
 * @ingroup utilModuleClass
 * 设备信息
 */
public final class PTDeviceUtil {

    /**
     * 上下文对象
     */
    private static Context context;
    /**
     * 手机管理类
     */
    private static TelephonyManager telm;
    /**
     * WiFi管理类
     */
    private static WifiManager wifim;
    /**
     * 屏幕大小
     */
    private static double screenSize;
    /**
     * 屏幕密度
     */
    private static float screenDensity;
    /**
     * 屏幕宽度
     */
    private static int screenWidth;
    /**
     * 屏幕高度
     */
    private static int screenHeight;
    /**
     * 浏览器内核类型
     */
    private static String browserCoreType;
    /**
     * 浏览器内核版本
     */
    private static String browserCoreVersion;
    /**
     * 长度常量
     */
    private static final int LENGTH = 6;
    /**
     * 私有构造方法
     */
    private PTDeviceUtil(){
        //To be a Utility class
    }
    /**
     * 呼叫号码.
     *
     * @param numble 待呼叫的号码
     */
    public static void call(String numble) {
        final Uri uri = Uri.parse("tel:" + numble);
        context.startActivity(new Intent(Intent.ACTION_CALL, uri));
    }

    /**
     * 发送短信.
     *
     * @param numble  收信人号码
     * @param msg 短信内容
     */
    public static void sendMessage(String numble, String msg) {
        final SmsManager smsManager = SmsManager.getDefault();
        final PendingIntent intent = PendingIntent.getBroadcast(
                context,
                0,
                new Intent(),
                PendingIntent.FLAG_ONE_SHOT);

        smsManager.sendTextMessage(numble, null, msg, intent, null);
    }

    /**
     * 取得手机屏幕的密度
     *
     * @return 手机屏幕的密度
     */
    public static float getDensity() {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取客户端屏幕宽度
     *
     * @return 客户端屏幕尺寸
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * 获取客户端屏幕高度
     *
     * @return 客户端屏幕尺寸
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * 获取客户端屏幕尺寸，单位英寸
     *
     * @return 客户端屏幕尺寸
     */
    public static double getScreenSize() {
        return screenSize;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dip 转换前的dp值
     * @return 转换后的像素值
     */
    public static int dip2px(float dip) {
        return (int) (dip * screenDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param scrp 转换前的sp值
     * @return 返回像素值
     */
    public static int sp2px(float scrp) {
        return (int) (scrp * screenDensity + 0.5f);
    }

    /**
     * 获取IMSI号
     *
     * @return IMSI号
     */
    public static String getIMSI() {
        String result = "";
        if (telm != null) {
            result = telm.getSubscriberId();
        }
        return result;
    }

    /**
     * 获取客户端类型
     *
     * @return 客户端类型，Android或aPad
     */
    public static String getDeviceType() {
        if (screenSize > LENGTH) {
            //屏幕尺寸大于6寸的被识别为aPad
            return "aPad";
        } else {
            //否则识别为Android手机
            return "Android";
        }
    }

    /**
     * 获取IMEI
     *
     * @return IMEI号
     */
    public static String getIMEI() {
        String result = "";
        if (telm != null) {
            result = telm.getDeviceId();
        }
        return result;
    }

    /**
     * 获取ICCID
     *
     * @return ICCID号
     */
    public static String getICCID() {
        String result = "";
        if (telm != null) {
            result = telm.getSimSerialNumber();
        }
        return result;
    }

    /**
     * 获取手机系统版本
     *
     * @return 手机系统版本
     */
    public static String getPhoneOSVersion() {
        String osVersion = "";
        osVersion = Build.VERSION.RELEASE;
        osVersion = PTStringUtil.trim(osVersion);
        return osVersion;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号 eg:ME860 10 2.3.4
     */
    public static String getPhoneModel() {
        String deviceModel = "";
        //ME860							//10		API等级							//2.3.4 系统版本
        deviceModel = Build.MODEL;
        deviceModel = PTStringUtil.trim(deviceModel);
        return deviceModel;
    }

    /**
     * 获取手机产品代号
     *
     * @return 手机产品代号 eg:MOTO ME860_HKTW
     */
    public static String getPhoneProduct() {
        String phoneType = "";
        // eg：MOTO					//eg:ME860_HKTW
        phoneType = Build.PRODUCT;
        phoneType = PTStringUtil.trim(phoneType);
        return phoneType;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商 eg:motorola
     */
    public static String getPhonePhoneManuFacturer() {
        String phoneManufactuer = "";
        phoneManufactuer = Build.MANUFACTURER;//eg:motorola
        phoneManufactuer = PTStringUtil.trim(phoneManufactuer);
        return phoneManufactuer;
    }

    /**
     * 获取手机网络运营商类型
     *
     * @return 手机网络运营商类型
     */
    public static String getPhoneISP() {
        String teleCompany = "";
        /*
         * MCC+MNC(mobile country code + mobile network code) 注意：仅当用户已在网络注册时有效。
		 * 在CDMA网络中结果也许不可靠。
		 */
        final String networkOperator = telm.getNetworkOperator();
        if (networkOperator != null) {
            if (networkOperator.startsWith("46000") || networkOperator.startsWith("46002")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                // 中国移动
                teleCompany = "y";
            } else if (networkOperator.startsWith("46001")) {
                // 中国联通
                teleCompany = "l";
            } else if (networkOperator.startsWith("46003")) {
                // 中国电信
                teleCompany = "d";
            }
        }
        teleCompany = PTStringUtil.trim(teleCompany);
        return teleCompany;
    }

    /**
     * 获取mac地址
     *
     * @return mac地址
     */
    public static String getMac() {
        if (wifim != null && wifim.getConnectionInfo() != null) {
            final WifiInfo wifiInfo = wifim.getConnectionInfo();
            if (wifiInfo != null) {

                return wifiInfo.getMacAddress();
            }
        }
        return null;
    }

    /**
     * 获取ip地址
     *
     * @return ip地址
     */
    public static String getIpAddress() {
        if (wifim != null && wifim.getConnectionInfo() != null) {
            final WifiInfo wifiInfo = wifim.getConnectionInfo();
            if (wifiInfo != null) {
                return intToIp(wifiInfo.getIpAddress());
            }
        }
        return null;
    }

    /**
     * 格式化IP
     *
     * @return 格式化后的IP
     */
    private static String intToIp(int ipNum) {
        final StringBuffer sbuffer = new StringBuffer();
        sbuffer.append(ipNum & 0xFF).append('.').append((ipNum >> 8) & 0xFF).append('.').append((ipNum >> 16) & 0xFF).append('.').append((ipNum >> 24) & 0xFF).append('.');
        return sbuffer.toString();
    }

    /**
     * 获取存储路径
     *
     * @return 存储路径
     */
    public static String getContentStoragePath() {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "/";
    }

    /**
     * 获取浏览器内核类型
     *
     * @return 浏览器内核类型
     */
    public static String getBrowserCoreType() {
        return browserCoreType;
    }

    /**
     * 获取浏览器内核版本号
     *
     * @return 浏览器内核版本号
     */
    public static String getBrowserCoreVersion() {
        return browserCoreVersion;
    }

    /**
     * 检查照相机是否可用
     *
     * @return 可用返回true，否则返回false
     */
    @SuppressLint("NewApi")
    public static boolean checkCameraHardware() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    /**
     * 工具类初始化
     */
    public static void initialization(Context con) {
        if (context != null) {
            //已完成初始化，无需再次初始化
            return;
        }

        context = con;
        telm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        wifim = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //计算客户端屏幕尺寸
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics dism = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dism);
        screenSize = Math.sqrt(dism.widthPixels * dism.widthPixels + dism.heightPixels * dism.heightPixels) / (dism.density * 160);

        screenDensity = dism.density;
        screenWidth = dism.widthPixels;
        screenHeight = dism.heightPixels;

        final String userAgent = new WebView(context).getSettings().getUserAgentString();

        browserCoreType = "AppleWebKit";
        int start = userAgent.indexOf(browserCoreType);
        if (start == -1) {
            browserCoreType = "Unknown";
        } else {
            start += browserCoreType.length();
            int end = userAgent.indexOf(' ', start);
            if (end == -1) {
                end = userAgent.length();
            }
            browserCoreVersion = userAgent.substring(start, end);
            if (browserCoreVersion.charAt(0) == '/') {
                browserCoreVersion = browserCoreVersion.substring(1);
            }
        }
    }
}
