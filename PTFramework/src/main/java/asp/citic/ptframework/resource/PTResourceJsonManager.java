package asp.citic.ptframework.resource;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import asp.citic.ptframework.BuildConfig;
import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.common.constants.PTConfig;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.signtools.PTSignTool;
import asp.citic.ptframework.common.tools.PTFileOperator;
import asp.citic.ptframework.common.tools.PTJSONFile;
import asp.citic.ptframework.common.tools.PTVersionCodeUtil;
import asp.citic.ptframework.communication.PTCommunicationHelper;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.message.PTMessageCenter;
import asp.citic.ptframework.security.PTClientKeyStore;
import asp.citic.ptframework.session.PTNetworkManager;


/**
 * 资源管理器
 */
public final class PTResourceJsonManager {

    private static final String TAG = "PTResourceJsonManager";

    private static final int HTTP_CACHE_TAG = 304;
    private static final int HTTP_CODE_SUCC = 200;

    private static final String RELEASEJSONPATH = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE + PTFrameworkConstants.PTResourceConstant.RESOURCE;
    private static final String BACKUPJSONPATH = PTFrameworkConstants.PTResourceConstant.PATH_BACKUP + PTFrameworkConstants.PTResourceConstant.RESOURCE;
    private static String terminalType = "Android".toLowerCase(Locale.CHINA);
    private static int signLen = PTClientKeyStore.getResourceCertifiedPublicKey().getModulus().bitLength() / 8;
    private static int signBase64Len = PTSignTool.getSignBase64Len(signLen);
    private static JSONArray keyArray;
    private static JSONArray valueArray;

    /**
     * 私有构造方法
     */
    private PTResourceJsonManager() {
        //To be a Utility class
    }

    /**
     * 分支处理resource.json的加载
     */
    public static void initialization() {
        switch (BuildConfig.PUB_TYPE) {
            case PTConfig.PUB_DEBUG:
                break;
            case PTConfig.PUB_REMOTE:
                break;
            case PTConfig.PUB_RELEASE:
                checkResourceJsonLocal();
                updateResourceJson();
                checkResourceJsonServer();
                break;
            default:
                break;
        }
        readResourceJson();
    }

    /**
     * 对本地resource.json文件验签
     *
     * @return true:验签成功，false：验签失败
     */
    public static boolean checkResourceJsonLocal() {
        boolean checkStatus = PTSignTool.checkJsonFileSelf(RELEASEJSONPATH, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_2, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
        //发送通知
        int result = checkStatus ? PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS : PTFrameworkConstants.PTCheckStatusConstant.CHECK_FAILED;
        PTLogger.info(TAG, "本地resource.json验签结果：" + (result == 0 ? "成功" : "失败"));
        PTMessageCenter.riseEvent(PTMessageCenter.EVENT_RESOURCE_CHECK_LOCAL, result);
        return checkStatus;
    }

    /**
     * 对服务器resource.json文件验签
     *
     * @return true:验签成功，false：验签失败
     */
    public static boolean checkResourceJsonServer() {
        boolean checkStatus = PTSignTool.checkJsonFileSelf(RELEASEJSONPATH, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_2, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
        //发送通知
        int result = checkStatus ? PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS : PTFrameworkConstants.PTCheckStatusConstant.CHECK_FAILED;
        PTLogger.info(TAG, "服务端resource.json验签结果：" + (result == 0 ? "成功" : "失败"));
        PTMessageCenter.riseEvent(PTMessageCenter.EVENT_RESOURCE_CHECK_SERVER, result);
        return checkStatus;
    }

    /**
     * 304方式获取服务端resource.json
     *
     * @return true:更新成功，false：更新失败
     */
    private static boolean updateResourceJson() {
        if (PTNetworkManager.getNetworkType(PTFramework.getContext()) == PTFrameworkConstants.PTNetworkStatus.STATE_NET_NULL) {
            return false;
        }
        File jsonFile = new File(RELEASEJSONPATH);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        String sendUrl = PTFrameworkConstants.PTResourceConstant.URL_DATA + terminalType + "/" + PTFrameworkConstants.PTResourceConstant.RESOURCE;
        HttpGet request = new HttpGet(sendUrl);
        //文件最后修改时间转换的字符串
        request.setHeader("If-Modified-Since", format.format(new Date(jsonFile.lastModified())));
        PTHttpResponseInterface response = PTCommunicationHelper.execHttpGet(request);
        if (response == null) {
            return false;
        }
        int status = response.getStatusCode();
        if (status == HTTP_CACHE_TAG) {
            PTLogger.info(TAG, String.valueOf(status));
            //当前最新
            return true;
        } else if (status == HTTP_CODE_SUCC) {
            try {
                JSONObject oldResJson = new PTJSONFile(RELEASEJSONPATH);
                String oldVersion = oldResJson.optString("VERSION");
                //有更新，下载文件
                boolean isSuc = PTCommunicationHelper.fileDownload(PTFrameworkConstants.PTResourceConstant.RESOURCE, RELEASEJSONPATH);
                try {
                    JSONObject newResJson = new PTJSONFile(RELEASEJSONPATH);
                    String newVersion = newResJson.optString("VERSION");
                    int compareRes;
                    if (oldVersion == null || oldVersion.equals("")) {
                        compareRes = -1;
                    } else {
                        compareRes = PTVersionCodeUtil.compareVersionCode(oldVersion, newVersion);
                    }
                    versionHandler(compareRes);
                } catch (JSONException e) {
                    PTLogger.error(TAG, e.getMessage());
                }
                return isSuc;
            } catch (JSONException e) {
                PTLogger.error(TAG, e.getMessage());
            }
        }
        return false;
    }


    /**
     * 根据新旧版本比较结果处理resource.json
     *
     * @param compareRes
     * @return =0：相等, >0: a>b，<0: b>a
     */
    private static void versionHandler(int compareRes) {
        if (compareRes > 0) {
            //版本回退,backup目录覆盖release目录
            PTFileOperator.deleteFile(RELEASEJSONPATH);
            PTFileOperator.copyDirectiory(BACKUPJSONPATH, RELEASEJSONPATH);
        } else if (compareRes < 0) {
            //版本升级,release目录覆盖backup目录
            PTFileOperator.deleteFile(BACKUPJSONPATH);
            PTFileOperator.copyDirectiory(RELEASEJSONPATH, BACKUPJSONPATH);
        } else {
            PTLogger.info(TAG, "无需做版本上的变动");
        }
    }

    /**
     * 读取清单文件resource.json的内容
     */
    private static boolean readResourceJson() {
        int resourceStatus = PTFrameworkConstants.PTCheckStatusConstant.CHECK_DOING;
        JSONObject resourceJson;
        try {
            resourceJson = new PTJSONFile(RELEASEJSONPATH);
            JSONObject jsonFileList = resourceJson.getJSONObject("FILELIST");
            keyArray = jsonFileList.getJSONArray("KEY");
            valueArray = jsonFileList.getJSONArray("VALUE");
            if (keyArray == null || keyArray.length() == 0) {
                resourceStatus = PTFrameworkConstants.PTCheckStatusConstant.CHECK_FAILED;
            } else {
                resourceStatus = PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS;
            }
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_RESOURCE_COMPLETE, resourceStatus);
        } catch (JSONException e) {
            PTLogger.error(TAG, e.getMessage());
        }
        return resourceStatus == PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS;
    }

    /**
     * 对单个文件验签，不通过则下载
     */
    public static boolean updateResourceFile() {
        if (BuildConfig.PUB_TYPE != PTConfig.PUB_RELEASE) {
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_CHECK_FILELIST, PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS);
            return true;
        }
        boolean isSuc = foreachCheckFile();
        PTLogger.info(TAG, "本地循环验签结果：" + isSuc);
        int result = isSuc ? PTFrameworkConstants.PTCheckStatusConstant.CHECK_SUCCESS : PTFrameworkConstants.PTCheckStatusConstant.CHECK_FAILED;
        PTMessageCenter.riseEvent(PTMessageCenter.EVENT_CHECK_FILELIST, result);
        return true;
    }

    /**
     * for循环遍历文件列表
     */
    private static boolean foreachCheckFile() {
        if (keyArray == null || keyArray.length() <= 0) {
            return false;
        }

        PTLogger.info(TAG, "resource.json列表个数：" + keyArray.length());
        for (int i = 0; i < keyArray.length(); i++) {
            try {
                String relativePath = keyArray.getString(i);
                String fileHash = valueArray.getString(i);
                if (checkLocalSingleFile(relativePath, fileHash)) {
                    //本地文件验签成功
                    PTLogger.info(TAG, "本地循环验签成功：" + relativePath);
                    continue;
                } else {
                    PTLogger.info(TAG, "本地循环验签失败：" + relativePath);
                }
                if (!updateSingleFile(relativePath)) {
                    //本地文件验签失败，且下载失败
                    PTLogger.info(TAG, "本地文件验签失败，且下载失败：" + relativePath);
                    return false;
                }
                if (!checkServerSingleFile(relativePath, fileHash)) {
                    //本地文件验签失败，下载成功，但下载的文件验签失败
                    PTLogger.info(TAG, "本地文件验签失败，下载成功，但下载的文件验签失败：" + relativePath);
                    return false;
                }
                //本地文件验签失败，下载成功，且下载的文件验签成功
                PTLogger.info(TAG, "本地文件验签失败，下载成功，且下载的文件验签成功：" + relativePath);
                continue;
            } catch (JSONException e) {
                PTLogger.error(TAG, e.getMessage());
            }
        }
        return true;
    }

    /**
     * 对本地的单个资源文件进行验签，检查文件是否被篡改
     *
     * @param relativePath
     * @param fileHash
     */
    private static boolean checkLocalSingleFile(String relativePath, String fileHash) {
        if (relativePath != null) {
            String filePath = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE + relativePath;
            if (filePath.endsWith(".js")) {
                return PTSignTool.checkJsFile(filePath, fileHash, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_0, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            } else if (filePath.endsWith(".json")) {
                return PTSignTool.checkJsonFile(filePath, fileHash, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_2, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            } else if (filePath.endsWith(".css")) {
                return PTSignTool.checkCssFile(filePath, fileHash, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_2, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            } else if (filePath.endsWith(".xml")) {
                return PTSignTool.checkXmlFile(filePath, fileHash, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_3, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            } else if (filePath.endsWith(".gif") || filePath.endsWith(".png")) {
                return PTSignTool.checkPicFile(filePath, fileHash, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_3, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            } else if (filePath.endsWith(".html")) {
                return PTSignTool.checkHtmlFile(relativePath, fileHash, signBase64Len, PTFrameworkConstants.PTResourceConstant.SUFFIXLEN_3, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            } else {
                return PTSignTool.checkUnknownFile(filePath, fileHash, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5);
            }
        }
        return false;
    }

    /**
     * 从服务器更新单个资源文件
     */
    private static boolean updateSingleFile(String relativePath) {
        String filePathTemp = relativePath;

        String filePath = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE + filePathTemp;
        return PTCommunicationHelper.fileDownload(filePathTemp, filePath);
    }

    /**
     * 对服务器下载的单个资源文件进行验签，检查文件是否合法
     *
     * @param relativePath
     * @param fileHash
     */
    private static boolean checkServerSingleFile(String relativePath, String fileHash) {
        return checkLocalSingleFile(relativePath, fileHash);
    }
}
