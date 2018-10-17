package asp.citic.ptframework.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import asp.citic.ptframework.common.algorithm.PTBase64;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @author dora
 * @ingroup encryptionDecryptModuleClass
 * @function RSA加密算法
 * @time 2016-11-29
 */
public final class PTRSA {
    /**
     * 类标记
     */
    private static final String TAG = "PTRSA";
    /**
     * RSA算法标识.
     */
    public static final String RSA = "RSA/ECB/NoPadding";
    /**
     * MD5withRSA签名算法.
     */
    public static final String MD5WITHRSA = "MD5withRSA";
    /**
     * SHA1withRSA签名算法.
     */
    public static final String SHA1WITHRSA = "SHA1withRSA";
    /**
     * RSA字符串
     */
    private static final String RSASTR = "RSA";

    static {
        try {
            Security.insertProviderAt(
                    (Provider) Class
                            .forName(
                                    "org.bouncycastle.jce.provider.BouncyCastleProvider")
                            .newInstance(), 1);
        } catch (InstantiationException e) {
            PTLogger.error(TAG,e.getMessage());
        } catch (IllegalAccessException e) {
            PTLogger.error(TAG,e.getMessage());
        } catch (ClassNotFoundException e) {
            PTLogger.error(TAG,e.getMessage());
        }
    }

    /**
     * 私有构造方法
     */
    private PTRSA() {
        //To be a Utility class
    }

    /**
     * 生成密钥对
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSASTR);
        keyPairGen.initialize(keySize, new SecureRandom());
        return keyPairGen.generateKeyPair();
    }

    /**
     * 生成公钥.
     */
    public static RSAPublicKey generateRSAPublicKey(
            BigInteger modulus,
            BigInteger publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFac = KeyFactory.getInstance(RSASTR);
        RSAPublicKeySpec pubKeySpec =
                new RSAPublicKeySpec(modulus, publicExponent);

        return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
    }

    /**
     * 从X509的base64串中获取公钥对象
     */
    public static RSAPublicKey getRSAPublicKey(String urlB64Key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFac = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec pubKeySpec =
                new X509EncodedKeySpec(PTBase64.decode(urlB64Key.getBytes()));
        return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
    }

    /**
     * 从私钥对象中获取公钥对象.
     */
    public static RSAPublicKey getRSAPublicKey(RSAPrivateKey prk) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return generateRSAPublicKey(
                new BigInteger(prk.getModulus().toByteArray()),
                new BigInteger(((RSAPrivateCrtKey) prk).getPublicExponent().toByteArray()));
    }

    /**
     * 生成私钥
     */
    public static RSAPrivateKey generateRSAPrivateKey(
            BigInteger modulus,
            BigInteger privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFac = KeyFactory.getInstance(RSASTR);
        RSAPrivateKeySpec priKeySpec =
                new RSAPrivateKeySpec(modulus, privateExponent);
        return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
    }

    /**
     * 从PKCS8的base64串中获取私钥对象.
     */
    public static RSAPrivateKey getRSAPrivateKey(String strB64Key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFac = KeyFactory.getInstance(RSASTR);

        PKCS8EncodedKeySpec priKeySpec =
                new PKCS8EncodedKeySpec(PTBase64.decode(strB64Key
                        .getBytes()));

        return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
    }

    /**
     * 加密数据.
     */
    public static byte[] encrypt(RSAKey key, byte[] data) {
        //TODO 零字节开头数据的处理
        try {

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, (Key) key);

            int step;
            if (RSA.contains("PKCS1Padding")) {
                //PKCS1Padding
                step = key.getModulus().bitLength() / 8 - 11;
            }
            if (RSA.contains("OAEPPadding")) {
                //PKCS1Padding
                step = key.getModulus().bitLength() / 8 - 42;
            } else {
                //NoPadding
                step = key.getModulus().bitLength() / 8;
            }

            int delt = data.length / step;
            if (delt > 0) {
                //分片加密
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int i = 0; i < delt; i++) {
                    baos.write(cipher.doFinal(data, i * step, step));
                }
                delt = data.length % step;
                if (delt != 0) {
                    baos.write(cipher.doFinal(data, data.length - delt, delt));
                }
                return baos.toByteArray();
            } else {
                return cipher.doFinal(data);
            }
        } catch (Exception exception) {
            PTLogger.error(TAG,exception.getMessage());
        }
        return new byte[1];
    }

    /**
     * 解密数据.
     */
    public static byte[] decrypt(RSAKey key, byte[] raw) {
        //TODO 零字节开头数据的处理
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, (Key) key);
            int step = key.getModulus().bitLength() / 8;
            int delt = raw.length / step;
            if (delt > 0) {
                //分片解密
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int i = 0; i < delt; i++) {
                    baos.write(cipher.doFinal(raw, i * step, step));
                }
                return baos.toByteArray();
            } else {
                return cipher.doFinal(raw);
            }
        } catch (Exception exception) {
            PTLogger.error(TAG,exception.getMessage());
        }
        return new byte[1];
    }

    /**
     * 对数字节数组进行签名.
     */
    public static byte[] signature(String signtype, byte[] data, PrivateKey prik) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(signtype);
        sign.initSign(prik);
        sign.update(data);
        return sign.sign();
    }

    /**
     * 对输入流进行签名.
     */
    public static byte[] signatureIs(String signtype,
                                     InputStream ins,
                                     PrivateKey prik) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {
        Signature sign = Signature.getInstance(signtype);
        sign.initSign(prik);
        byte[] buf = new byte[1024];
        while (ins.read(buf) != -1) {
            sign.update(buf, 0, buf.length);
        }
        return sign.sign();
    }

    /**
     * 对字节数组验签.
     */
    public static boolean verify(String signtype,
                                 byte[] data,
                                 byte[] sign,
                                 PublicKey pubk) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signtype);
        signature.initVerify(pubk);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 对输入流验签.
     */
    public static boolean verifyIs(String signtype,
                                   InputStream ins,
                                   byte[] sign,
                                   PublicKey pubk) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        Signature signature = Signature.getInstance(signtype);
        signature.initVerify(pubk);

        byte[] buf = new byte[1024];
        while (ins.read(buf) != -1) {
            signature.update(buf, 0, ins.read(buf));
        }
        return signature.verify(sign);
    }
}
