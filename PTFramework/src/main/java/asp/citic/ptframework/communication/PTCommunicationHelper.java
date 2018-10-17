package asp.citic.ptframework.communication;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.tools.Charsets;
import asp.citic.ptframework.communication.datapackage.PTComPackage;
import asp.citic.ptframework.communication.datapackage.PTDataPackage;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.communication.request.PTHttpRequestImp;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.taskexecutor.PTHttpTask;
import asp.citic.ptframework.taskexecutor.PTTaskListener;

/**
 * @ingroup httpAssistModuleClass
 * 网络通信助手
 */
public final class PTCommunicationHelper {
    /**
     * 终端类型
     */
    private static String TAG = "PTCommunicationHelper";
    /**
     * 终端类型
     */
    private static String terminalType = "Android".toLowerCase(Locale.CHINA);
    /**
     * 终端POST类型
     */
    public final static int POST = 0;
    /**
     * 终端GET类型
     */
    public final static int GET = 1;

    /**
     * 私有构造方法
     */
    private PTCommunicationHelper() {
        //To be a Utility class
    }

    /**
     * 客户端发送同步POST请求
     * 主线程中进行
     */
    public static void sendRequest(String serverName,int method, JSONObject params, PTTaskListener listener){
        PTHttpTask task = new PTHttpTask();
        task.setTaskDate(serverName,params);
        task.setListener(listener);
        task.setMethod(method);
        task.excuteTask();
    }
    /**
     * 客户端发送同步POST请求
     * 主线程中进行
     */
    public static void sendRequest(String serverName,int method, JSONObject params, int timeout, PTTaskListener listener){
        PTHttpRequestImp.setTimeout(timeout);
        PTHttpTask task = new PTHttpTask();
        task.setTaskDate(serverName,params);
        task.setListener(listener);
        task.setMethod(method);
        task.excuteTask();
    }
    /**
     * 生成一个httpPost
     */
    public static HttpPost createHttpPost(String serverName, JSONObject jsonBusiness, int encryptFlag) {
        String url = PTFrameworkConstants.PTResourceConstant.URL_BASE + serverName;
        PTLogger.debug(TAG,"创建HttpPost, URL:" + url);
        byte[] business = jsonBusiness.toString().getBytes();
        PTDataPackage dataPackage = new PTDataPackage(encryptFlag, PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5, PTFrameworkConstants.PTSignatureConstant.FLAG_SIGNATURE_NONE, business);
        PTComPackage pkg = new PTComPackage(0, "", dataPackage);
        HttpPost request = new HttpPost(url);
        ByteArrayEntity entity = new ByteArrayEntity(pkg.encryComPkg().getBytes(Charsets.UTF_8));
        request.setEntity(entity);
        return request;
    }

    /**
     * 执行httppost
     */
    public static PTHttpResponseInterface execHttpPost(HttpPost request) {
        return new PTHttpRequestImp(request).dataRequest();
    }

    /**
     * 执行httpGet
     */
    public static PTHttpResponseInterface execHttpGet(HttpGet request) {
        return new PTHttpRequestImp(request).dataRequest();
    }

    /**
     * 解析外层数据包
     */
    public static PTComPackage parseComPackage(PTHttpResponseInterface response) {
        PTComPackage pkg = new PTComPackage();
        byte[] resData = response.getResponseData();
        if (resData != null && resData.length > 0){
            pkg.decryComPkg(new String(resData));
        }
        return pkg;
    }

    /**
     * 带监听器的文件下载，先下载到*.tmp~文件
     */
    public static boolean fileDownload(String fileName, String filePath) {
        if (fileName == null || fileName.equals("")) {
            return false;
        }
        String url = PTFrameworkConstants.PTResourceConstant.URL_DATA + terminalType + "/" + fileName;
        HttpGet request = new HttpGet(url);
        File tmpFile = new File(filePath + ".tmp~");
        //删除临时文件
        tmpFile.delete();
        boolean isSuc = new PTHttpRequestImp(request).fileRequest(tmpFile.getPath());
        if (isSuc) {
            PTLogger.info(TAG, "资源文件下载成功: " + fileName);
            //删除源文件
            File destFile = new File(filePath);
            destFile.delete();
            //重命名新文件
            tmpFile.renameTo(destFile);
        }
        return isSuc;
    }
}
