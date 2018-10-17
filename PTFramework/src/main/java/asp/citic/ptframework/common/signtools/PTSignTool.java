package asp.citic.ptframework.common.signtools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import asp.citic.ptframework.common.algorithm.PTConverter;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.hash.PTHash;
import asp.citic.ptframework.common.tools.PTArrayUtil;
import asp.citic.ptframework.common.tools.PTFileOperator;
import asp.citic.ptframework.common.tools.PTStreamOperator;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.security.PTClientKeyStore;
import asp.citic.ptframework.security.PTLicenseUtil;
import asp.citic.ptframework.security.PTRSA;

/**
 * 资源文件验签工具
 * Created by dora on 2016-12-21.
 */
public final class PTSignTool {
    /**
     * 类标记
     */
    private static final String TAG = "PTSignTool";
    /**
     * 私有构造方法
     */
    private PTSignTool() {
        //To be a Utility class
    }

    /**
     * 对json文件自身验签(resource.json)
     */
    public static boolean checkJsonFileSelf(String filePath, int signLen, int suffixLen, int hashType) {
        return checkFileSelf(filePath, signLen, suffixLen, hashType);
    }

    /**
     * 对json文件验签
     */
    public static boolean checkJsonFile(String filePath, String fileHash, int signLen, int suffixLen, int hashType) {
        return checkFileCompare(filePath, fileHash, signLen, suffixLen, hashType);
    }

    /**
     * 对js文件验签
     */
    public static boolean checkJsFile(String filePath, String fileHash, int signLen, int suffixLen, int hashType) {
        return checkFileCompare(filePath, fileHash, signLen, suffixLen, hashType);
    }

    /**
     * 对css文件验签
     */
    public static boolean checkCssFile(String filePath, String fileHash, int signLen, int suffixLen, int hashType) {
        return checkFileCompare(filePath, fileHash, signLen, suffixLen, hashType);
    }

    /**
     * 对xml文件验签
     *
     * @return true:验签成功, false:验签失败
     */
    public static boolean checkXmlFile(String filePath, String fileHash, int signLen, int suffixLen, int hashType) {
        return checkFileCompare(filePath, fileHash, signLen, suffixLen, hashType);
    }

    /**
     * 对pic文件验签
     */
    public static boolean checkPicFile(String filePath, String fileHash, int signLen, int suffixLen, int hashType) {
        return checkFileCompare(filePath, fileHash, signLen, suffixLen, hashType);
    }

    /**
     * 对html文件验签
     *
     * @return true:验签成功, false:验签失败
     */
    public static boolean checkHtmlFile(String relativePath, String fileHash, int signLen, int suffixLen, int hashType) {
        //获取filePath的hash值
        String filePath = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE + relativePath;
        return checkFileCompare(filePath, fileHash, signLen, suffixLen, hashType);
    }
    /**
     * 对未知文件验签
     *
     * @return true:验签成功, false:验签失败
     */
    public static boolean checkUnknownFile(String filePath, String fileHash, int hashType) {
        PTLogger.info(TAG, "开始未知文件验签: " + filePath);
        //文件读取为二进制数组
        byte[] contentBytes = PTFileOperator.getFileContent(filePath);
        if (contentBytes == null || contentBytes.length <= 0) {
            PTLogger.info(TAG, "未知文件内容流读取异常: " + filePath);
            return false;
        }
        InputStream ins = new ByteArrayInputStream(contentBytes, 0, contentBytes.length);
        byte[] contentData = PTHash.getHashByInputStream(hashType, ins);
        PTStreamOperator.close(ins);
        String contentHash = PTConverter.bytesToHex(contentData);
        if (fileHash == null || contentHash == null || !fileHash.equals(contentHash)) {
            PTLogger.info(TAG, "开始未知文件Hash值对比: " + fileHash + " && " + contentHash);
            return false;
        }
        return true;
    }

    /**
     * 文件自身验签
     *
     * @param filePath  文件路径
     * @param signLen   签名长度
     * @param suffixLen 注释后缀长度
     * @param hashType  hash类型
     * @return true:验签成功, false:验签失败
     */
    public static boolean checkFileSelf(String filePath, int signLen, int suffixLen, int hashType) {
        PTLogger.info(TAG, "开始自身验签: " + filePath);
        //文件读取为二进制数组
        byte[] contentBytes = PTFileOperator.getFileContent(filePath);
        if (contentBytes == null || contentBytes.length <= 0) {
            return false;
        }
        byte[] sign = new byte[signLen];
        int dataLen = contentBytes.length - signLen - suffixLen;
        //截取文件流后几位存入签名数组
        System.arraycopy(contentBytes, dataLen, sign, 0, signLen);
        //签名数组base64解码
        byte[] signBytes = PTConverter.base64ToBytes(new String(sign));
        //用本地验签公钥解密签名数组
        byte[] deSignBytes = PTRSA.decrypt(PTClientKeyStore.getResourceCertifiedPublicKey(), signBytes);
        InputStream ins = new ByteArrayInputStream(contentBytes, 0, dataLen);
        //获取文件内容流(去除签名信息)的hash数组
        byte[] contentData = PTHash.getHashByInputStream(hashType, ins);
        PTStreamOperator.close(ins);
        return PTArrayUtil.isEqual(contentData, deSignBytes, false);
    }

    /**
     * 文件自身验签 + 对比resource.json验签
     *
     * @param filePath  文件路径
     * @param fileHash  recourse.json中文件的hash值
     * @param signLen   签名长度
     * @param suffixLen 注释后缀长度
     * @param hashType  hash类型
     * @return true:验签成功, false:验签失败
     */
    public static boolean checkFileCompare(String filePath, String fileHash, int signLen, int suffixLen, int hashType) {
        PTLogger.info(TAG, "开始对比验签: " + filePath);
        //文件读取为二进制数组
        byte[] contentBytes = PTFileOperator.getFileContent(filePath);
        if (contentBytes == null || contentBytes.length <= 0) {
            return false;
        }
        byte[] sign = new byte[signLen];
        int dataLen = contentBytes.length - signLen - suffixLen;
        //截取文件流后几位存入签名数组
        System.arraycopy(contentBytes, dataLen, sign, 0, signLen);
        //获取内容数组的hex值和resource.json中的hash值对比
        InputStream ins = new ByteArrayInputStream(contentBytes, 0, dataLen);
        byte[] contentData = PTHash.getHashByInputStream(hashType, ins);
        PTStreamOperator.close(ins);
        String contentHash = PTConverter.bytesToHex(contentData);
        if (fileHash == null || contentHash == null || !fileHash.equals(contentHash)) {
            return false;
        }
        //签名数组base64解码
        byte[] signBytes = PTConverter.base64ToBytes(new String(sign));
        //用本地验签公钥解密签名数组和文件内容的hash数组对比
        byte[] deSignBytes = PTRSA.decrypt(PTClientKeyStore.getResourceCertifiedPublicKey(), signBytes);
        return PTArrayUtil.isEqual(contentData, deSignBytes, false);
    }

    /**
     * 获取混淆过的文件路径
     *
     * @param relativeUrl 文件的相对路径
     * @return 文件Key
     */
    public static String confuseTemplateUrl(String relativeUrl) {
        if (relativeUrl == null || relativeUrl.length() == 0) {
            return "";
        }
        String reUrl = getUniformUrl(relativeUrl, PTFrameworkConstants.PTFormateConstants.URL_PREFIX_HTML, false);
        int last = reUrl.lastIndexOf('/');
        String subPath = (last == -1) ? "" : reUrl.substring(0, last + 1);
        String name = (last == -1) ? reUrl : reUrl.substring(last + 1);
        byte[] key = PTLicenseUtil.getLicenseCertifiedPublicKey(PTFrameworkConstants.PTFormateConstants.URL_PREFIX_HTML);
        try {
            return subPath + PTConverter.bytesToStr(name, key);
        } catch (IOException e) {
            PTLogger.error(TAG,e.getMessage());
        }
        return relativeUrl;
    }

    /**
     * 获取标准url
     *
     * @param url        业务链接
     * @param preFix     业务前缀类型
     * @param keepPreFix 是否保持前缀
     * @return 标准url
     */
    public static String getUniformUrl(String url, String preFix, boolean keepPreFix) {
        if (url != null) {
            String urlTemp = url.trim();
            String urlForm = urlTemp;
            String urls[] = urlTemp.split(PTFrameworkConstants.PTFormateConstants.URL_PREFIX_SEPARATOR_PATTERN);
            if (urls.length == 2 && preFix != null) {
                if (preFix.equals(urls[0])) {
                    urlForm = urls[1];
                } else {
                    return null;
                }
            }
            try {
                URI uri = new URI(urlForm);
                if (preFix != null && keepPreFix) {
                    return preFix + PTFrameworkConstants.PTFormateConstants.URL_PREFIX_SEPARATOR + uri.getPath();
                } else {
                    return uri.getPath();
                }
            } catch (URISyntaxException e) {
                PTLogger.error(TAG,e.getMessage());
            }
        }
        return null;
    }

    /**
     * 获取base64转码的签名长度
     *
     * @param signLen
     * @return
     */
    public static int getSignBase64Len(int signLen) {
        int mod = signLen % 3;
        int div = signLen / 3;

        if (mod == 0) {
            return div * 4;
        }
        return (div + 1) * 4;
    }
}
