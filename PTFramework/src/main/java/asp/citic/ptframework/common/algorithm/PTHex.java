package asp.citic.ptframework.common.algorithm;

import java.util.Locale;


/**
 * @author dora
 * @ingroup codeEncodeModuleClass
 * @function Hex转码
 * @time 2016-11-29
 */
public final class PTHex {
    /**
     * 长度常量
     */
    private static final int LENGTH = 1;

    /**
     * 私有构造方法
     */
    private PTHex() {
        //To be a Utility class
    }

    /**
     * 将字节数组进行HEX编码.
     */
    public static String encode(byte[] data) {
        if (data == null) {
            return "";
        }

        StringBuilder sbuilder = new StringBuilder();
        String stmp;
        for (int n = 0; n < data.length; n++) {
            stmp = Integer.toHexString(data[n] & 0xFF);
            if (stmp.length() == LENGTH) {
                sbuilder.append('0');
            }
            sbuilder.append(stmp);
        }
        return sbuilder.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 将HEX编码的字符串转换为字节数组.
     */
    public static byte[] decode(String data) {
        if (data == null) {
            return new byte[1];
        }
        int length = data.length();
        String tempData = "";
        if (length % 2 != 0) {
            tempData = "0" + data;
            length++;
        }
        int tempLength = length / 2;
        byte[] bytes = new byte[tempLength];
        for (int i = 0; i < tempLength; i++) {
            bytes[i] = (byte) Integer
                    .parseInt(tempData.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

}
