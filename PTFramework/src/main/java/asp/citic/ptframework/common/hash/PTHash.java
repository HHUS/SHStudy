package asp.citic.ptframework.common.hash;

import java.io.InputStream;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;

/**
 * @author dora
 * @ingroup hashModuleClass
 * @function Hash算法
 * @time 2016-11-29
 */
public final class PTHash {
    /**
     * 私有构造方法
     */
    private PTHash(){
        //To be a Utility class
    }
    /**
     * 获取字符串的散列值.
     */
    public static byte[] getHashByString(int type, String src){
        switch (type) {
            case PTFrameworkConstants.PTHashEnumConstants.MD5:
                return PTMD5.getHashByString(src);
            case PTFrameworkConstants.PTHashEnumConstants.SHA1:
                return PTSHA1.getHashByString(src);
            default:
                return new byte[1];
        }
    }

    /**
     * 获取输入流的散列值.
     */
    public static byte[] getHashByInputStream(int type, InputStream src) {
        switch (type) {
            case PTFrameworkConstants.PTHashEnumConstants.MD5:
                return PTMD5.getHashByInputStream(src);
            case PTFrameworkConstants.PTHashEnumConstants.SHA1:
                return PTSHA1.getHashByInputStream(src);
            default:
                return new byte[1];
        }
    }

    /**
     * 获取字节数组的散列值.
     */
    public static byte[] getHashByBytes(int type, byte[] src){
        switch (type) {
            case PTFrameworkConstants.PTHashEnumConstants.MD5:
                return PTMD5.getHashByBytes(src);
            case PTFrameworkConstants.PTHashEnumConstants.SHA1:
                return PTSHA1.getHashByBytes(src);
            default:
                return new byte[1];
        }
    }
}
