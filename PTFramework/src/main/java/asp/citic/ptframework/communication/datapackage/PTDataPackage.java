package asp.citic.ptframework.communication.datapackage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.interfaces.RSAKey;
import java.util.Arrays;

import asp.citic.ptframework.common.algorithm.PTConverter;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.hash.PTHash;
import asp.citic.ptframework.common.tools.Charsets;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.secretkeys.PTSecretKeyManager;
import asp.citic.ptframework.security.PT3Des;
import asp.citic.ptframework.security.PTClientKeyStore;
import asp.citic.ptframework.security.PTRSA;

/**@ingroup httpSecurityModuleClass
 * 内层数据包
 */
public class PTDataPackage {
    /**
     * 类标记
     */
    private static final String TAG = "PTDataPackage";
    /** RSAKey*/
    private static RSAKey ptKey = PTClientKeyStore.getConsultPasswordPublicKey();
    /** 加密类型*/
    private transient int encryptFlag = PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_3DES;
    /** hash类型*/
    private transient int hashFlag = PTFrameworkConstants.PTHashConstant.FLAG_HASH_SHA1;
    /** 签名标记*/
    private transient int signatureFlag = PTFrameworkConstants.PTSignatureConstant.FLAG_SIGNATURE_NONE;
    /** hash数据*/
    private transient byte[] hash;
    /** 业务逻辑数据*/
    private transient byte[] business;
    /**sessionData */
    private transient JSONObject sessionData;
    /** 内层数据包解密是否成功*/
    public static boolean isDataPkgDecrypt;
    /**
     * 构造函数，用于构造请求数据包
     */
    public PTDataPackage(int encryptFlag, int hashFlag, int signatureFlag, byte[] business) {
        this.encryptFlag = encryptFlag;
        this.hashFlag = hashFlag;
        this.signatureFlag = signatureFlag;
        if (business == null) {
            this.business = new byte[1];
        } else {
            this.business = Arrays.copyOf(business, business.length);
        }
    }
    /**
     * 构造函数，用于构造响应数据包
     */
    public PTDataPackage() {
        //空构造方法
    }
    /**
     * 数据包加密
     * @todo 此方法未实现报文hash值签名的功能
     */
    public JSONObject encryDataPkg() {
         JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cryptFlag", encryptFlag);
            jsonObject.put("hashFlag", hashFlag);
            jsonObject.put("signatureFlag", signatureFlag);
            jsonObject.put("sessionData", sessionData);

            if (business != null) {
                //业务数据加密
                 String businessStr = encrypt(business);
                //计算散列值
                switch (hashFlag) {
                    case PTFrameworkConstants.PTHashConstant.FLAG_HASH_NONE:
                        hash = new byte[1];
                        break;
                    case PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5:
                        hash = PTHash.getHashByString(PTFrameworkConstants.PTHashEnumConstants.MD5, businessStr);
                        break;
                    case PTFrameworkConstants.PTHashConstant.FLAG_HASH_SHA1:
                        hash = PTHash.getHashByString(PTFrameworkConstants.PTHashEnumConstants.SHA1, businessStr);
                        break;
                    default:
                        hash = new byte[1];
                        break;
                }

                if (hash != null) {
                    jsonObject.put("hash", PTConverter.bytesToBase64(hash));
                }
//                //计算签名
//                switch (signatureFlag) {
//                    case FLAG_SIGNATURE_NONE:
//                        signature = null;
//                        break;
//                    case FLAG_SIGNATURE_MD5withRSA:
//                        signature = ("FLAG_SIGNATURE_MD5withRSA:" + businessStr).getBytes();
//                        break;
//                    case FLAG_SIGNATURE_SHA1withRSA:
//                        signature = ("FLAG_SIGNATURE_SHA1withRSA:" + businessStr).getBytes();
//                        break;
//
//                    default:
//                        //$ERROR:MP
//                        throw new PTInvalidTypeException("");
//                }
//
//                if (signature != null) {
//                    jsonObject.put("signature", PTConverter.bytesToBase64(signature));
//                }

                jsonObject.put("business", businessStr);
            }
        } catch (JSONException e) {
            PTLogger.error(TAG,e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 执行加密数据包操作
     * @param data
     * @return
     * @throws Exception
     */
    private String encrypt(byte[] data){
        String ret = "";
        switch (encryptFlag) {
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_NONE:
                ret = new String(data, Charsets.UTF_8);
                break;
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_3DES:
                ret = PTConverter.bytesToBase64(PT3Des.encrypt(business, PTSecretKeyManager.getEncryptKey()));
                break;
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_PTRSA:
                //服务器私钥加密,客户端公钥加密
                ret = PTConverter.bytesToBase64(PTRSA.encrypt(ptKey, business));
                break;
            default:
                break;
        }
        return ret;
    }
    /**
     * 解析JSON对象
     *
     * @todo 此方法未实现服务器返回报文hash值认证的功能
     */
    public void decryDataPkg(JSONObject jsonData) {
        try {
            encryptFlag = jsonData.getInt("cryptFlag");
            hashFlag = jsonData.getInt("hashFlag");
            signatureFlag = jsonData.getInt("signatureFlag");
            sessionData = jsonData.optJSONObject("sessionData");
             String businessStr = jsonData.getString("business");
            if (businessStr != null) {
                //验证签名
                switch (signatureFlag) {
                    case PTFrameworkConstants.PTSignatureConstant.FLAG_SIGNATURE_NONE:
//                        signature = null;
                        break;
                    case PTFrameworkConstants.PTSignatureConstant.FLAG_SIGNATURE_MD5WITHRSA:
                        //验签
                        //$ERROR:MP==验签失败==请联系客服
                        break;
                        //break;
                    case PTFrameworkConstants.PTSignatureConstant.FLAG_SIGNATURE_SHA1WITHRSA:
                        //验签
                        //$ERROR:MP==验签失败==请联系客服
                        break;
                    default:
                        break;
                }
                //验证散列值
                int hashType = 0;
                switch (hashFlag) {
                    case PTFrameworkConstants.PTHashConstant.FLAG_HASH_NONE:
                        hashType = PTFrameworkConstants.PTHashEnumConstants.NONE;
                        hash = new byte[1];
                        break;
                    case PTFrameworkConstants.PTHashConstant.FLAG_HASH_MD5:
                        hashType = PTFrameworkConstants.PTHashEnumConstants.MD5;
                        break;
                    case PTFrameworkConstants.PTHashConstant.FLAG_HASH_SHA1:
                        hashType = PTFrameworkConstants.PTHashEnumConstants.SHA1;
                        break;
                    default:
                        break;
                }
                if (hashType != PTFrameworkConstants.PTHashEnumConstants.NONE) {
                    //校验散列值
                     String hashStr = jsonData.getString("hash");
                    if (!PTConverter.bytesToBase64(
                            PTHash.getHashByString(hashType, businessStr))
                            .equals(hashStr)) {
                        //$ERROR:MP==校验散列值失败==请联系客服
                        return;
                    }
                }
                //解析业务数据
                decrypt(businessStr);
            }
        } catch (JSONException e) {
           PTLogger.error(TAG,e.getMessage());
        }
    }

    /**
     * 执行解密数据包操作
     */
    private void decrypt(String businessStr) {
        switch (encryptFlag) {
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_NONE:
                business = businessStr.getBytes(Charsets.UTF_8);
                break;
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_3DES:
                business = PT3Des.decrypt(PTConverter.base64ToBytes(businessStr), PTSecretKeyManager.getEncryptKey());
                break;
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_AES:
                business = businessStr.getBytes(Charsets.UTF_8);
                break;
            case PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_PTRSA:
                //服务器私钥解密，客户端公钥解密
                business = PTRSA.decrypt(ptKey, PTConverter.base64ToBytes(businessStr));
                break;
            default:
                break;
        }
        if (business != null){
            isDataPkgDecrypt = true;
        }
    }

    /**
     * 获取business.
     */
    public byte[] getBusiness(){
        if (business == null) {
            return new byte[1];
        } else {
            return Arrays.copyOf(business, business.length);
        }
    }
    /**
     * 组装JSON格式的内层数据包
     */
    public JSONObject dataPkgToJson() {
        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("cryptFlag", encryptFlag);
            dataJson.put("hashFlag", hashFlag);
            dataJson.put("signatureFlag", signatureFlag);
            dataJson.put("sessionData", sessionData);
            dataJson.put("business", new String(getBusiness(), Charsets.UTF_8));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataJson;

    }
}
