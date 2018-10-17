package asp.citic.ptframework.secretkeys;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.device.PTDeviceUtil;
import asp.citic.ptframework.common.tools.PTAppUtil;
import asp.citic.ptframework.communication.PTCommunicationHelper;
import asp.citic.ptframework.communication.datapackage.PTComPackage;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.message.PTMessageCenter;
import asp.citic.ptframework.session.PTNetworkManager;
import asp.citic.ptframework.session.PTSessionManager;

/**
 * @ingroup httpConsultModuleClass
 * Created by dora on 2016-11-30.
 */
public final class PTConsultPassword {
    /**
     * 标记
     */
    private static final String TAG = "PTConsultPassword";

    /**
     * 私有构造方法
     */
    private PTConsultPassword() {
        //To be a Utility class
    }

    /**
     * 发送密钥协商请求
     */
    public static boolean sendConsultPasswordRequest() {
        PTLogger.info(TAG, "发送密钥协商请求");
        PTSecretKeyManager.initialization();
        try {
            if (PTNetworkManager.getNetworkType(PTFramework.getContext()) <= PTFrameworkConstants.PTNetworkStatus.STATE_NET_NULL) {
                PTLogger.info(TAG, "网络连接异常");
                return false;
            }
            //有网络
            JSONObject handshakeData = new JSONObject();
            //客户端随机数
            handshakeData.put("cr", PTSecretKeyManager.clientR);
            //客户端类型
            handshakeData.put("sys-client", PTDeviceUtil.getDeviceType());
            //客户端版本号
            handshakeData.put("sys-version", PTAppUtil.getAppVersionName());
            //客户端设备标识
            handshakeData.put("deviceId", "");
            String serverName = String.format("%s?act=%s", PTFrameworkConstants.PTResourceConstant.FRAMEWORK_SERVICE, PTFrameworkConstants.PTResourceConstant.TRANSCODE_HANDSHAKE);
            HttpPost request = PTCommunicationHelper.createHttpPost(serverName,
                    handshakeData,
                    PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_PTRSA);
            PTHttpResponseInterface response = PTCommunicationHelper.execHttpPost(request);
            PTSecretKeyManager.consultSecretKey();
            return parseResponse(response);
        } catch (JSONException e) {
            PTLogger.error(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * 解析密钥协商请求报文
     *
     * @param response
     */
    private static boolean parseResponse(PTHttpResponseInterface response) {
        boolean isSucc = false;
        JSONObject parseJson = new JSONObject();
        if (response != null){
            if (PTSessionManager.getSessionState() >= PTFrameworkConstants.PTSessionStatus.STATE_HANDSHAKE_SUCCESS) {
                //握手会话状态成功,读取外层数据包
                PTComPackage pkgCom = PTCommunicationHelper.parseComPackage(response);
                if (pkgCom != null && pkgCom.getErrCode() == 0 && pkgCom.getPTDataPackage().isDataPkgDecrypt) {
                    //密钥协商成功
                    isSucc = true;
                    String parseJsonStr = new String(pkgCom.getPTDataPackage().getBusiness());
                    try {
                        parseJson = new JSONObject(parseJsonStr);
                        PTLogger.info(TAG, "协商秘钥响应报文解析：" + parseJson.toString());
                    } catch (JSONException e) {
                        PTLogger.error(TAG, e.getMessage());
                    }
                }
            }
        }
        //发送 秘钥协商状态变更事件
        try {
            parseJson.put("result", isSucc);
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_CONSULTKEY_CHANGE, parseJson);
        } catch (JSONException e) {
            isSucc = false;
            PTLogger.error(TAG, e.getMessage());
        }
        return isSucc;
    }

}
