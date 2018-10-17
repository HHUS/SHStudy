package com.csii.sh.core.crypto;

import android.os.Build.VERSION;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    public static final String TAG = "AESUtils";

    public AESUtils() {
    }

    public static String encrypt(String key, String clearText) {
        byte[] result = null;

        try {
            byte[] content = getRawKey(key.getBytes());
            result = encrypt(content, clearText.getBytes());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        String content1 = toHex(result);
        return content1;
    }

    public static String decrypt(String key, String encrypted) {
        try {
            byte[] rawKey = getRawKey(key.getBytes());
            byte[] e = toByte(encrypted);
            byte[] result = decrypt(rawKey, e);
            String coentn = new String(result);
            return coentn;
        } catch (Exception var6) {
            Log.e("secure", var6.getMessage());
            return null;
        }
    }
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        if(VERSION.SDK_INT < 17) {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        }

        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey sKey = kgen.generateKey();
        byte[] raw = sKey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    private static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];

        for(int i = 0; i < len; ++i) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }

        return result;
    }

    private static String toHex(byte[] buf) {
        if(buf == null) {
            return "";
        } else {
            StringBuffer result = new StringBuffer(2 * buf.length);

            for(int i = 0; i < buf.length; ++i) {
                appendHex(result, buf[i]);
            }

            return result.toString();
        }
    }

    private static void appendHex(StringBuffer sb, byte b) {
        String HEX = "0123456789ABCDEF";
        sb.append("0123456789ABCDEF".charAt(b >> 4 & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }
}
