package asp.citic.ptframework.communication.datapackage;

import org.json.JSONException;
import org.json.JSONObject;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @ingroup httpSecurityModuleClass
 * 外层数据包
 */
public class PTComPackage {
    /**
     * 类标记
     */
    private static final String TAG = "PTComPackage";
    /**
     * 通信包传输方向标志.
     */
    private transient int pkgFlag;
    /**
     * 通信包错误标志.
     */
    private transient int errCode;
    /**
     * 通信包错误信息.
     */
    private transient String errMsg;
    /**
     * 数据包对象.
     */
    private transient final PTDataPackage dataPackage;

    /**
     * 构造函数,用于构造请求数据包
     */
    public PTComPackage(int errCode, String errMsg, PTDataPackage dataPackage) {
        this.pkgFlag = PTFrameworkConstants.PTPkgFlag.CONTEXT_TYPE_CLIENT;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.dataPackage = dataPackage;
    }

    /**
     * 构造函数，用于构造响应数据包
     */
    public PTComPackage() {
        this.pkgFlag = PTFrameworkConstants.PTPkgFlag.CONTEXT_TYPE_SERVER;
        this.dataPackage = new PTDataPackage();
    }

    /**
     * 加密外层数据包
     */
    public String encryComPkg() {
         JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pkgFlag", pkgFlag);
            jsonObject.put("errCode", errCode);
            jsonObject.put("errMsg", errMsg);
            if (dataPackage != null) {
                jsonObject.put("dataPackage", dataPackage.encryDataPkg());
            }
        } catch (JSONException e) {
            PTLogger.error(TAG,e.getMessage());
        }
        return jsonObject.toString();
    }

    /**
     * 解密外层数据包.
     */
    public void decryComPkg(String jsonPkgStr) {
        JSONObject jsonPkg = null;
        try {
            jsonPkg = new JSONObject(jsonPkgStr);
             JSONObject jsonData = jsonPkg.getJSONObject("dataPackage");
            pkgFlag = jsonPkg.optInt("pkgFlag");
            errCode = jsonPkg.optInt("errCode");
            if (jsonPkg.has("errMsg")) {
                errMsg = jsonPkg.getString("errMsg");
            }
            if (dataPackage != null) {
                dataPackage.decryDataPkg(jsonData);
            }
        } catch (JSONException e) {
            PTLogger.error(TAG,e.getMessage());
        }
    }

    /**
     * 获取PTDataPackage对象
     */
    public PTDataPackage getPTDataPackage() {
        return dataPackage;
    }

    /**
     * 获取错误码
     */
    public int getErrCode() {
        return errCode;
    }

    /**
     * 组装JSON格式的外层数据包
     */
    public JSONObject comPkgToJson() {
        JSONObject comJson = new JSONObject();
        try {
            comJson.put("pkgFlag", pkgFlag);
            comJson.put("errCode", errCode);
            comJson.put("errMsg", errMsg);
            if (dataPackage != null){
                JSONObject dataJson = dataPackage.dataPkgToJson();
                comJson.put("dataPackage",dataJson);
            }else{
                comJson.put("dataPackage","");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comJson;
    }
}
