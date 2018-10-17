package asp.citic.ptframework.security;

import android.content.res.Resources;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @author dora
 * @ingroup keyManagerModuleClass
 * @function 证书管理
 * @time 2016-11-29
 */
public final class PTClientKeyStore {
    /**
     * 类标记
     */
    private static final String TAG = "PTClientKeyStore";
    /**
     * 密钥协商公钥证书
     */
    private final static String CONSULTPASSWORD_PUBLICKEY = "ptframework_cert";
    /**
     * 服务端公钥证书
     */
    private final static String CERTIFIED_PUBLICKEY = "ptframework_cert";
    /**
     * 客户端私钥证书
     */
    private final static String SIGNATURE_PRIVATEKEY = "ptframework_cert";
    /**
     * 验签公钥证书
     */
    private final static String RESOURCE_CERTIFIED_PUBLICKEY = "ptframework_check_sign";

    /**
     * 私有构造方法
     */
    private PTClientKeyStore() {
        //To be a Utility class
    }

    /**
     * 读取 加密协商秘钥请求的 公钥
     *
     * @return RSAPublicKey
     */
    public static RSAPublicKey getConsultPasswordPublicKey() {
        return getKeystoreByCer(CONSULTPASSWORD_PUBLICKEY);
    }

    /**
     * 读取 认证 服务器返回给客户端的数据包的 公钥
     *
     * @return RSAPublicKey
     */
    public static RSAPublicKey getCertifiedPublicKey() {
        return getKeystoreByCer(CERTIFIED_PUBLICKEY);
    }

    /**
     * 读取 签名 客户端发送给服务器的数据包的 私钥
     *
     * @return RSAPublicKey
     */
    public static RSAPublicKey getSignaturePrivateKey() {
        return getKeystoreByCer(SIGNATURE_PRIVATEKEY);
    }

    /**
     * 读取 认证 资源文件的 公钥
     *
     * @return RSAPublicKey
     */
    public static RSAPublicKey getResourceCertifiedPublicKey() {
        return getKeystoreByCer(RESOURCE_CERTIFIED_PUBLICKEY);
    }

    /**
     * 读取客户端本地证书文件
     *
     * @param cerName
     * @return
     */
    private static RSAPublicKey getKeystoreByCer(String cerName) {
        try {
            CertificateFactory cerf = CertificateFactory.getInstance("X.509");
            Resources activityRes = PTFramework.getContext().getResources();
            int rawCertId = activityRes.getIdentifier(cerName, "raw", PTFramework.getContext().getPackageName());
            InputStream ins = PTFramework.getContext().getResources().openRawResource(rawCertId);
            Certificate cer = cerf.generateCertificate(ins);
            return (RSAPublicKey) cer.getPublicKey();
        } catch (CertificateException e) {
            PTLogger.error(TAG,e.getMessage());
        }
        return null;
    }

}
