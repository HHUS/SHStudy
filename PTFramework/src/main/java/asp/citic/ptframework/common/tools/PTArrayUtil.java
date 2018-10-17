package asp.citic.ptframework.common.tools;

/**
 * @author dora
 * @function 数组比较
 * @time 2016-11-29
 */

public final class PTArrayUtil {
    /**
     * 私有构造方法
     */
    private PTArrayUtil() {
        //To be a Utility class
    }

    /**
     * 比较两个字节数组是否相等.
     *
     * @param array1 第一个字节数组
     * @param array2 第二个字节数组
     * @return 相等返回0，a大返回正数，b大返回负数
     */
    public static boolean isEqual(byte[] array1, byte[] array2) {
        return isEqual(array1, array2, true);
    }

    /**
     * 比较两个字节数组是否相等.
     *
     * @param array1 第一个字节数组
     * @param array2 第二个字节数组
     * @param strict 是否严格比较
     * @return 相等返回0，a大返回正数，b大返回负数
     */
    public static boolean isEqual(byte[] array1, byte[] array2, boolean strict) {
        if (strict && array1.length != array2.length) {
            return false;
        }

        int min = Math.min(array1.length, array2.length);
        boolean ret = true;

        //从后向前比
        for (int i = 0; i < min; i++) {
            if (array1[array1.length - i - 1] != array2[array2.length - i - 1]) {
                ret = false;
                break;
            }
        }
        if (strict) {
            //严格比较
            if (ret) {
                return array1.length == array2.length;
            }
        } else {
            //非严格相等
            if (!ret) {
                ret = true;
                //从前向后比
                for (int i = 0; i < min; i++) {
                    if (array1[i] != array2[i]) {
                        ret = false;
                        break;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 比较两个字节数组.
     *
     * @param array1 第一个字节数组
     * @param array2 第二个字节数组
     * @return 相等返回0，a大返回正数，b大返回负数
     */
    public static int compare(byte[] array1, byte[] array2) {
        return compare(array1, array2, true);
    }

    /**
     * 比较两个字节数组.
     *
     * @param array1 第一个字节数组
     * @param array2 第二个字节数组
     * @param strict 是否严格比较
     * @return 相等返回0，a大返回正数，b大返回负数
     */
    public static int compare(byte[] array1, byte[] array2, boolean strict) {
        if (array1 == null && array2 == null) {
            return 0;
        } else if (array2 == null) {
            return Integer.MAX_VALUE;
        } else if (array1 == null) {
            return Integer.MIN_VALUE;
        }

        int min = Math.min(array1.length, array2.length);
        int ret = 0;
        //从前向后比
        for (int i = 0; i < min; i++) {
            if (array1[i] != array2[i]) {
                ret = array1[i] - array2[i];
                break;
            }
        }
        if (strict && ret == 0) {
            //严格相等
            return array1.length - array2.length;
        }
        return ret;
    }
}
