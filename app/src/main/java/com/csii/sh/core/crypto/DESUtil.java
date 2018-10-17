//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.csii.sh.core.crypto;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {
    private static final String CHARSET = "UTF-8";
    private static final String DES = "DES";
    private static String KEY = "CSII-DES";
    private static SecretKey secretkey = null;

    public DESUtil() {
    }

    private static Key getKey() {
        if(secretkey == null) {
            Object bb = null;

            try {
                byte[] bb1 = KEY.getBytes("UTF-8");
                secretkey = new SecretKeySpec(bb1, "DES");
            } catch (Exception var2) {
            }
        }

        return secretkey;
    }

    public static String encrypt(String encryptKey, String source) {
        if(encryptKey != null && !"".equals(encryptKey)) {
            KEY = encryptKey;
        }

        if(source != null && !"".equals(source)) {
            String s = null;
            Object target = null;

            try {
                byte[] e = source.getBytes("UTF-8");
                Key key = getKey();
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(1, key);
                byte[] target1 = cipher.doFinal(e);
                s = Base64.encodeToString(target1, 0);
                return s;
            } catch (Exception var7) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String decrypt(String decryptKey, String source) {
        if(decryptKey != null && !"".equals(decryptKey)) {
            KEY = decryptKey;
        }

        if(source != null && !"".equals(source)) {
            String s = null;
            Object dissect = null;

            try {
                byte[] e = Base64.decode(source.getBytes("UTF-8"), 0);
                Key key = getKey();
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(2, key);
                byte[] dissect1 = cipher.doFinal(e);
                s = new String(dissect1, "UTF-8");
                return s;
            } catch (Exception var7) {
                return "";
            }
        } else {
            return "";
        }
    }
}
