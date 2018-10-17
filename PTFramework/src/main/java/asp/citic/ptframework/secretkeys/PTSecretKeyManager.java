package asp.citic.ptframework.secretkeys;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.session.PTSessionManager;

/**
 * @ingroup httpConsultModuleClass
 * 随机数管理
 * 2016-11-29.
 */
public final class PTSecretKeyManager {

    private static final String TAG = "PTSecretKeyManager";
    /**
     * 客户端随机数
     */
    public static String clientR;
    /**
     * 服务端随机数
     */
    public static String serverR;
    /**
     * 当前会话的Cookie
     */
    public static String sessionId;
    /**
     * 3Des加密值
     */
    public static String sessionkey;
    /**
     * 协商出的秘钥值
     */
    private static byte[] encryptKey;

    /**
     * 私有构造方法
     */
    private PTSecretKeyManager() {
        //To be a Utility class
    }

    /**
     * 会话管理器初始化
     */
    public static void initialization() {
        PTLogger.info(TAG, "开始清空随机数,重置秘钥");
        //清空随机数
        resetRandomNum();
        resetKey();
    }

    /**
     * 重置随机数
     */
    public static void resetRandomNum() {
        clientR = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.CHINA);
        serverR = "";
        sessionId = "";
        sessionkey = "";
    }

    /**
     * 获取截取后的8位服务端随机数
     */
    public static String subSR() {
        if (serverR != null) {
            return serverR.substring(4, 12);
        }
        return "";
    }

    /**
     * 获取截取后的8位客户端随机数
     */
    public static String subCR() {
        if (clientR != null) {
            return clientR.substring(4, 12);
        }
        return "";
    }

    /**
     * 协商秘钥
     */
    public static void consultSecretKey() {
        PTLogger.info(TAG, "开始协商秘钥");
        if (PTSessionManager.getSessionState() == PTFrameworkConstants.PTSessionStatus.STATE_HANDSHAKE_SUCCESS) {
            //握手成功
            String key1 = PTSecretKeyManager.subSR();
            String key2 = PTSecretKeyManager.subCR();
            String key3 = PTSecretKeyManager.sessionkey;
            encryptKey = (key1 + key2 + key3).getBytes();
            PTLogger.info(TAG, "秘钥协商成功：" + String.valueOf(encryptKey));
        } else {
            PTLogger.info(TAG, "秘钥协商失败");
        }
    }

    /**
     * 重置秘钥
     */
    public static void resetKey() {
        encryptKey = new byte[1];
    }

    public static byte[] getEncryptKey() {
        return Arrays.copyOf(encryptKey, encryptKey.length);
    }
}