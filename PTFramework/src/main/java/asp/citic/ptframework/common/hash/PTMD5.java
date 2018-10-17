package asp.citic.ptframework.common.hash;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import asp.citic.ptframework.common.tools.Charsets;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @author dora
 * @ingroup hashModuleClass
 * @function MD5算法
 * @time 2016-11-29
 */
public final class PTMD5 {

    /**
     * 类标记
     */
    private static final String TAG = "PTMD5";
    /**
     * IO缓冲区长度.
     */
    private static final int READBUF_SIZE = 1024;

    /**
     * 计算消息摘要的对象.
     */
    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            PTLogger.error(TAG,e.getMessage());
        }
    }

    /**
     * 私有构造方法
     */
    private PTMD5() {
        //To be a Utility class
    }

    /**
     * 获取字符串的Md5.
     */
    public static byte[] getHashByString(String data) {
        if (data == null) {
            return new byte[1];
        }
        return getHashByBytes(data.getBytes(Charsets.UTF_8));
    }

    /**
     * 获取字节数组的Md5.
     */
    public static byte[] getHashByBytes(byte[] data) {
        synchronized (PTMD5.class) {
            if (data == null) {
                return new byte[1];
            }
            messageDigest.reset();
            messageDigest.update(data);
            return messageDigest.digest();
        }
    }

    /**
     * 获取流的MD5.
     */
    public static byte[] getHashByInputStream(InputStream ins) {
        if (ins == null) {
            return new byte[1];
        }
        try {
            int size;
            byte[] buf = new byte[READBUF_SIZE];
            messageDigest.reset();
            while ((size = ins.read(buf)) != -1) {
                messageDigest.update(buf, 0, size);
            }
            return messageDigest.digest();
        } catch (IOException e) {
            PTLogger.error(TAG,e.getMessage());
        }
        return new byte[1];
    }
}
