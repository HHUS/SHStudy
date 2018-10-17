package asp.citic.ptframework.security;

import android.content.res.Resources;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.hash.PTHash;
import asp.citic.ptframework.common.tools.PTStreamOperator;

/**
 * @author dora
 * @ingroup keyManagerModuleClass
 * @function 本地3Des秘钥生成器
 * @time 2016-11-29
 */
public final class PTLicenseUtil {
    /**
     * 用于生成3DES的公钥
     */
    private final static String LICENSECERTIFIED_PUBLICKEY = "ptframework_license";

    /**
     * 私有构造方法
     */
    private PTLicenseUtil() {
        //To be a Utility class
    }

    /**
     * 用于生成3DES秘钥
     *
     * @return byte[]
     */
    public static byte[] getLicenseCertifiedPublicKey(String key) {
        byte[] fileHash = PTLicenseUtil.getLicenseKey(LICENSECERTIFIED_PUBLICKEY);
        return PTLicenseUtil.getKey(fileHash, key);
    }

    /**
     * 读取客户端本地证书文件
     *
     * @param cerName
     * @return
     */
    private static byte[] getLicenseKey(String cerName) {
        Resources activityRes = PTFramework.getContext().getResources();
        int rawCertId = activityRes.getIdentifier(cerName, "raw", PTFramework.getContext().getPackageName());
        byte[] fileHash = PTHash.getHashByBytes(PTFrameworkConstants.PTHashEnumConstants.SHA1
                , PTStreamOperator.getInputStreamBytes(PTFramework.getContext()
                .getResources().openRawResource(rawCertId), true));
        return fileHash;
    }

    /**
     * 获取license中的秘钥
     *
     * @param license 授权文件数据
     * @param keyName 秘钥名称
     * @return 秘钥
     */
    private static byte[] getKey(byte[] license, String keyName) {
        int len = 24;
        int index = keyName.lastIndexOf('|');
        String tempKeyName = keyName;
        if (index != -1) {
            len = Integer.parseInt(keyName.substring(index + 1));
            tempKeyName = keyName.substring(0, index);
        }
        byte[] name = PTHash.getHashByString(PTFrameworkConstants.PTHashEnumConstants.SHA1, tempKeyName);
        byte[] mask = PTHash.getHashByString(PTFrameworkConstants.PTHashEnumConstants.SHA1, String.valueOf(len));
        byte[] data = license;

        byte[] ret = new byte[len];
        byte maskByte;
        byte nameByte;
        byte dataByte;
        int length = 0;
        while (length < len) {
            maskByte = mask[length % mask.length];
            nameByte = name[length % name.length];
            dataByte = data[length % name.length];

            ret[length] = (byte) (maskByte ^ nameByte ^ dataByte);

            length++;

            if (length % name.length == 0) {
                name = PTHash.getHashByBytes(PTFrameworkConstants.PTHashEnumConstants.SHA1, name);
            }
            if (length % mask.length == 0) {
                mask = PTHash.getHashByBytes(PTFrameworkConstants.PTHashEnumConstants.SHA1, mask);
            }
            if (length % data.length == 0) {
                data = PTHash.getHashByBytes(PTFrameworkConstants.PTHashEnumConstants.SHA1, data);
            }
        }
        return ret;
    }
}
