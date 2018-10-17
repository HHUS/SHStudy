package asp.citic.ptframework.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import asp.citic.ptframework.BuildConfig;
import asp.citic.ptframework.common.constants.PTConfig;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.signtools.PTSignTool;
import asp.citic.ptframework.common.tools.PTFileOperator;
import asp.citic.ptframework.common.tools.PTStreamOperator;
import asp.citic.ptframework.communication.PTCommunicationHelper;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.message.PTMessageCenter;
import asp.citic.ptframework.security.PT3Des;
import asp.citic.ptframework.security.PTClientKeyStore;
import asp.citic.ptframework.security.PTLicenseUtil;

/**
 * 解密HTML
 * Created by dora on 2016-12-27.
 */
public final class PTHtmlManager {

    private final static String TAG = "PTHtmlManager";

    /**
     * 私有构造方法
     */
    private PTHtmlManager() {
        //To be a Utility class
    }

    /**
     * 读取HTML内容
     *
     * @param relativeUrl
     * @return
     */
    public static byte[] getHtmlContentByUrl(String relativeUrl) {

        if (relativeUrl == null || relativeUrl.equals("")) {
            return new byte[1];
        }
        String relativePath = PTSignTool.confuseTemplateUrl(relativeUrl);
        String filePath = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE + relativePath;
        if (BuildConfig.PUB_TYPE != PTConfig.PUB_RELEASE) {
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_CHECK_FILELIST, PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS);
            return decryptHtml(filePath);
        }
        if (checkLocalSingleHtml(filePath)) {
            return decryptHtml(filePath);
        }
        //本地html验签失败，需要从服务器下载
        if (!updateSingleHtml(relativePath)) {
            return new byte[1];
        }
        //服务器Html文件下载成功，需要对服务器刚下载的文件进行验签
        if (checkServerSingleHtml(filePath)) {
            return decryptHtml(filePath);
        }
        PTLogger.info(TAG, "网络传输被窃取");
        return new byte[1];
    }

    /**
     * 本地html验签
     *
     * @return
     */
    private static boolean checkLocalSingleHtml(String filePath) {
        int signLen = PTClientKeyStore.getResourceCertifiedPublicKey().getModulus().bitLength() / 8;
        int signBase64Len = PTSignTool.getSignBase64Len(signLen);
        return PTSignTool.checkFileSelf(filePath, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_3, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
    }

    /**
     * 从服务器下载html文件
     *
     * @param relativePath
     * @return
     */
    private static boolean updateSingleHtml(String relativePath) {
        String fileName = PTSignTool.confuseTemplateUrl(relativePath);
        String filePath = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE + fileName;
        return PTCommunicationHelper.fileDownload(fileName, filePath);
    }

    /**
     * 服务器html验签
     *
     * @param filePath
     * @return
     */
    private static boolean checkServerSingleHtml(String filePath) {
        return checkLocalSingleHtml(filePath);
    }

    /**
     * HTML内容解密
     *
     * @return
     */
    private static byte[] decryptHtml(String filePath) {
        byte[] encryContent = PTFileOperator.getFileContent(filePath);
        if (encryContent == null || encryContent.length <= 0) {
            return new byte[1];
        }
        int signLen = PTClientKeyStore.getResourceCertifiedPublicKey().getModulus().bitLength() / 8;
        int signBase64Len = PTSignTool.getSignBase64Len(signLen);
        int rushLen = signBase64Len + PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_3 + PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_4 + PTSignTool.getSignBase64Len(PTFrameworkConstants.PTResourceConstant.VERSION_CODE_LENGTH);
        int contentLen = encryContent.length - rushLen;
        InputStream contentIn = new ByteArrayInputStream(encryContent, 0, contentLen);
        byte[] contentBytes = PTStreamOperator.getInputStreamBytes(contentIn);
        byte[] desKey = PTLicenseUtil.getLicenseCertifiedPublicKey(PTFrameworkConstants.PTFormateConstants.URL_PREFIX_HTML);
        return PT3Des.decrypt(contentBytes, desKey);
    }

}
