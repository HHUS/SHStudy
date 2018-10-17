package asp.citic.ptframework.common.tools;

/**
 * 字符串操作助手
 */
public final class PTStringUtil {
    /**
     * 私有构造方法
     */
    private PTStringUtil() {
        //To be a Utility class
    }

    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null && str.length() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 字符串是否为空
     */
    public static boolean hasEmpty(String... strs) {
        for (String str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去掉字符串前后空格，如果是null，则返回空字符串
     */
    public static String trim(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.trim();
    }

    /**
     * 检查是否是数字
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否是字母
     */
    public static boolean isChar(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 补足字符串
     */
    public static String fillStr(String str, int len, String tag) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = str.length();
        int appendCount = length < len ? len - length : 0;

        for (int i = 0; i < appendCount; i++) {
            stringBuilder.append(tag);
        }
        return str + stringBuilder.toString();
    }

}
