package asp.citic.ptframework.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import asp.citic.ptframework.common.tools.Charsets;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @author dora
 * @ingroup encryptionDecryptModuleClass
 * @function 3Des加密算法
 * @time 2016-11-29
 */
public final class PT3Des {
    private static final String TAG = "PT3Des";
    private static final String ALGORITHM = "DESede/ECB/PKCS5Padding";
    /**
     * 长度常量
     */
    private static final int LENGTH = 8;

    /**
     * 私有构造方法
     */
    private PT3Des() {
        //To be a Utility class
    }

    /**
     * 函数功能: 对数据3DES加密
     */
    public static byte[] encrypt(byte[] src,
                                 String key1,
                                 String key2,
                                 String key3) {

        if (src == null || src.length <= 0
                || key1 == null || key1.length() < 1
                || key2 == null || key2.length() < 1
                || key3 == null || key3.length() < 1) {
            return new byte[1];
        }
        String tempTey1 = "";
        String tempTey2 = "";
        String tempTey3 = "";
        if (key1.length() < LENGTH) {
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < 8 - key1.length(); i++) {
                sbuffer.append('0');
            }
            tempTey1 = key1 + sbuffer.toString();
        }
        if (key2.length() < LENGTH) {
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < 8 - key2.length(); i++) {
                sbuffer.append('0');
            }
            tempTey2 = key2 + sbuffer.toString();
        }
        if (key3.length() < LENGTH) {
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < 8 - key3.length(); i++) {
                sbuffer.append('0');
            }
            tempTey3 = key3 + sbuffer.toString();
        }
        return encrypt(src, (tempTey1 + tempTey2 + tempTey3).getBytes(Charsets.UTF_8));
    }

    /**
     * 函数功能: 对数据3DES解密
     */
    public static byte[] decrypt(byte[] src,
                                 String key1,
                                 String key2,
                                 String key3) {
        if (src == null || src.length < 1
                || key1 == null || key1.length() < 1
                || key2 == null || key2.length() < 1
                || key3 == null || key3.length() < 1) {
            return new byte[1];
        }
        String tempTey1 = key1;
        String tempTey2 = key2;
        String tempTey3 = key3;
        if (key1.length() < LENGTH) {
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < 8 - key1.length(); i++) {
                sbuffer.append('0');
            }
            tempTey1 = key1 + sbuffer.toString();
        }
        if (key2.length() < LENGTH) {
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < 8 - key2.length(); i++) {
                sbuffer.append('0');
            }
            tempTey2 = key2 + sbuffer.toString();
        }
        if (key3.length() < LENGTH) {
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < 8 - key3.length(); i++) {
                sbuffer.append('0');
            }
            tempTey3 = key3 + sbuffer.toString();
        }
        return decrypt(src, (tempTey1 + tempTey2 + tempTey3).getBytes(Charsets.UTF_8));
    }

    /**
     * 函数功能: 对数据3DES加密
     */
    public static byte[] encrypt(byte[] src, byte[] key) {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey desKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            return cipher.doFinal(src);
        } catch (Exception exception) {
            PTLogger.error(TAG,exception.getMessage());
        }
        return new byte[1];
    }

    /**
     * 函数功能: 对数据3DES解
     */
    public static byte[] decrypt(byte[] src, byte[] key) {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey desKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            return cipher.doFinal(src);
        } catch (Exception exception) {
            PTLogger.error(TAG,exception.getMessage());
        }
        return new byte[1];
    }

}
