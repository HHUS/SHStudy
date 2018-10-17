package com.csii.sh.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.csii.sh.core.crypto.AESUtils;


public class SecureStorage {
    private static String SPConfig = "app_config";
    private SharedPreferences preferences;
    private static String secretKey = "";

    public SecureStorage(Context context, String xmlFileName, String secretKey) {
        secretKey = secretKey;
        if(xmlFileName != null && !"".equals(xmlFileName)) {
            SPConfig = xmlFileName;
        }

        this.preferences = context.getSharedPreferences(SPConfig, 0);
    }

    public boolean getBoolean(String key) {
        return this.preferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean state) {
        this.preferences.edit().putBoolean(key, state).commit();
    }

    public String getString(String key) {
        String info = this.preferences.getString(key, "");
        if(!"".equals(info)) {
            info = AESUtils.decrypt(secretKey, info);
        }

        return info;
    }

    public void putString(String key, String value) {
        if(value != null && !"".equals(value)) {
            value = AESUtils.encrypt(secretKey, value);
        }

        this.preferences.edit().putString(key, value).commit();
    }

    public int getInt(String key) {
        return this.preferences.getInt(key, -1);
    }

    public void putInt(String key, int value) {
        this.preferences.edit().putInt(key, value).commit();
    }

    public float getFloat(String key) {
        return this.preferences.getFloat(key, 0.0F);
    }

    public void putFloat(String key, float value) {
        this.preferences.edit().putFloat(key, value).commit();
    }

    public long getLong(String key) {
        return this.preferences.getLong(key, 0L);
    }

    public void putLong(String key, long value) {
        this.preferences.edit().putLong(key, value).commit();
    }

    public Editor getEdit() {
        return this.preferences.edit();
    }
}
