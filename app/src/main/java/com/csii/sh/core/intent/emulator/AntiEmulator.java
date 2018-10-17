//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.csii.sh.core.intent.emulator;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.csii.sh.util.Logger;

import java.io.File;
import java.io.FileInputStream;

public class AntiEmulator {
    private static final String TAG = "AntiEmulator";
    private static String[] known_qemu_drivers = new String[]{"goldfish"};
    private static String[] known_files = new String[]{"/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace", "/system/bin/qemu-props"};
    private static String[] known_numbers = new String[]{"15555215554", "15555215556", "15555215558", "15555215560", "15555215562", "15555215564", "15555215566", "15555215568", "15555215570", "15555215572", "15555215574", "15555215576", "15555215578", "15555215580", "15555215582", "15555215584"};
    private static String[] known_imsi_ids = new String[]{"310260000000000"};

    public AntiEmulator() {
    }

    private static Boolean checkQEmuDriverFile() {
        File driver_file = new File("/proc/tty/drivers");
        if(driver_file.exists() && driver_file.canRead()) {
            byte[] data = new byte[1024];

            try {
                FileInputStream driver_data = new FileInputStream(driver_file);
                driver_data.read(data);
                driver_data.close();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            String var8 = new String(data);
            String[] var6 = known_qemu_drivers;
            int var5 = known_qemu_drivers.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                String known_qemu_driver = var6[var4];
                if(var8.indexOf(known_qemu_driver) != -1) {
                    Log.i("Result:", "Find know_qemu_drivers!");
                    return Boolean.valueOf(true);
                }
            }
        }

        Log.i("Result:", "Not Find known_qemu_drivers!");
        return Boolean.valueOf(false);
    }

    private static Boolean CheckEmulatorFiles() {
        for(int i = 0; i < known_files.length; ++i) {
            String file_name = known_files[i];
            File qemu_file = new File(file_name);
            if(qemu_file.exists()) {
                Log.v("Result:", "Find Emulator Files!");
                return Boolean.valueOf(true);
            }
        }

        Log.v("Result:", "Not Find Emulator Files!");
        return Boolean.valueOf(false);
    }

    private static Boolean CheckPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        String phonenumber = telephonyManager.getLine1Number();
        String[] var6 = known_numbers;
        int var5 = known_numbers.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            String number = var6[var4];
            if(number.equalsIgnoreCase(phonenumber)) {
                Log.v("Result:", "Find PhoneNumber!");
                return Boolean.valueOf(true);
            }
        }

        Log.v("Result:", "Not Find PhoneNumber!");
        return Boolean.valueOf(false);
    }

    private static Boolean CheckImsiIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi_ids = telephonyManager.getSubscriberId();
        String[] var6 = known_imsi_ids;
        int var5 = known_imsi_ids.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            String know_imsi = var6[var4];
            if(know_imsi.equalsIgnoreCase(imsi_ids)) {
                Log.v("Result:", "Find imsi ids: 310260000000000!");
                return Boolean.valueOf(true);
            }
        }

        Log.v("Result:", "Not Find imsi ids: 310260000000000!");
        return Boolean.valueOf(false);
    }

    private static Boolean CheckEmulatorBuild(Context context) {
        String BOARD = Build.BOARD;
        String BOOTLOADER = Build.BOOTLOADER;
        String BRAND = Build.BRAND;
        String DEVICE = Build.DEVICE;
        String HARDWARE = Build.HARDWARE;
        String MODEL = Build.MODEL;
        String PRODUCT = Build.PRODUCT;
        if(BOARD != "unknown" && BOOTLOADER != "unknown" && BRAND != "generic" && DEVICE != "generic" && MODEL != "sdk" && PRODUCT != "sdk" && HARDWARE != "goldfish") {
            Log.v("Result:", "Not Find Emulator by EmulatorBuild!");
            return Boolean.valueOf(false);
        } else {
            Log.v("Result:", "Find Emulator by EmulatorBuild!");
            return Boolean.valueOf(true);
        }
    }

    private static boolean CheckOperatorNameAndroid(Context context) {
        String szOperatorName = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
        if(szOperatorName.toLowerCase().equals("android")) {
            Log.v("Result:", "Find Emulator by OperatorName!");
            return true;
        } else {
            Log.v("Result:", "Not Find Emulator by OperatorName!");
            return false;
        }
    }

    private static boolean CheckImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            return imei != null && imei.equals("000000000000000")?true: Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
        } catch (Exception var3) {
            return false;
        }
    }

    public static boolean isEmulator(Context context) {
        Logger.d( "检测imei和设备型号--CheckEmulatorBuild：" + CheckImei(context));
        Logger.d( "检测硬件信息--CheckEmulatorBuild：" + CheckEmulatorBuild(context));
        Logger.d( "检测驱动列表--checkQEmuDriverFile：" + checkQEmuDriverFile());
        Logger.d( "检测imsi--CheckImsiIDS：" + CheckImsiIDS(context));
        Logger.d( "检测运营商--CheckOperatorNameAndroid：" + CheckOperatorNameAndroid(context));
        boolean imei = CheckImei(context);
        boolean EmulatorBuild = CheckEmulatorBuild(context).booleanValue();
        boolean QEmuDriverFile = checkQEmuDriverFile().booleanValue();
        boolean ImsiIDS = CheckImsiIDS(context).booleanValue();
        boolean OperatorNameAndroid = CheckOperatorNameAndroid(context);
        return imei || EmulatorBuild || QEmuDriverFile || ImsiIDS || OperatorNameAndroid;
    }
}
