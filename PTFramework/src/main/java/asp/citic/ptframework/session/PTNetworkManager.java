package asp.citic.ptframework.session;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @ingroup httpStateModuleClass
 * 网络状态管理类
 */
public final class PTNetworkManager {
    /**
     * 私有构造方法
     */
    private PTNetworkManager() {
        //To be a Utility class
    }

    /**
     * 返回当前手机接入的网络类型,
     *
     * @return 返回值: 0:无网络, 1:2G3G, 2:wifi
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!isNetworkAvailable(connMgr)) {
            return 0;
        }
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo == null || !activeInfo.isConnected()) {
            return 0;
        }
        int status = 0;
        if (activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            status = 2;
        } else if (activeInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            status = 1;
        }
        return status;
    }

    /**
     * 网络是否可用
     *
     * @return true可用 </br> false不可用
     */
    private static boolean isNetworkAvailable(ConnectivityManager connectivity) {
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
