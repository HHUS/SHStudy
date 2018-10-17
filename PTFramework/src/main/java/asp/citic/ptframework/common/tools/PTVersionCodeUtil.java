package asp.citic.ptframework.common.tools;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 版本号操作助手
 */
public final class PTVersionCodeUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd.HH.mm.ss", Locale.CHINA);

    /**
     * 私有构造方法
     */
    private PTVersionCodeUtil() {
        //To be a Utility class
    }

    /**
     * 获取版本号的字节数组.
     */
    public static byte[] getVersionCode(String version) {
        byte[] versionCode = new byte[]{0, 0, 0, 0, 0, 0};
        String[] vers = version.split("\\.");
        for (int i = 0; i < versionCode.length && i < vers.length; i++) {
            versionCode[i] = Byte.parseByte(vers[i]);
        }
        return versionCode;
    }

    /**
     * 获取时间生成的版本号的字节数组.
     */
    public static byte[] getVersionCode(long time) {
        return getVersionCode(getVersionString(time));
    }

    /**
     * 获取时间生成的版本号.
     */
    public static String getVersionString(long time) {
        synchronized (PTVersionCodeUtil.class) {
            return format.format(time);
        }
    }

    /**
     * 获取时间生成的版本号.
     */
    public static String getVersionString(byte[] code) {
        if (code == null || code.length == 0) {
            return "0";
        }
        StringBuilder sbuilder = new StringBuilder();
        int min = Math.min(code.length, 6);
        for (int i = 0; i < min; i++) {
            sbuilder.append(code[i] & 0xff);
            sbuilder.append('.');
        }
        sbuilder.setLength(sbuilder.length() - 1);
        return sbuilder.toString();
    }

    /**
     * 比较版本号.
     */
    public static int compareVersionCode(byte[] array1, String array2) {
        return PTArrayUtil.compare(array1, getVersionCode(array2));
    }

    /**
     * 比较版本号.
     */
    public static int compareVersionCode(String array1, byte[] array2) {
        return PTArrayUtil.compare(getVersionCode(array1), array2);
    }

    /**
     * 比较版本号.
     *
     * @param array1 第一个版本号
     * @param array2 第二个版本号
     * @return =0：相等, >0: a>b，<0: b>a
     */
    public static int compareVersionCode(String array1, String array2) {
        return PTArrayUtil.compare(getVersionCode(array1), getVersionCode(array2));
    }
}
