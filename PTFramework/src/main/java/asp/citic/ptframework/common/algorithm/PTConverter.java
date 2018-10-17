package asp.citic.ptframework.common.algorithm;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.hash.PTHash;
import asp.citic.ptframework.common.tools.PTStreamOperator;

/**
 * @ingroup codeEncodeModuleClass
 * @author dora
 * @function 转码工具
 * @time 2016-11-29
 */
public final class PTConverter {
	/**
	 * 私有构造方法
	 */
	private PTConverter(){
		//To be a Utility class
	}
	/**
	 * To base64.
	 */
	public static String bytesToBase64(byte[] src,boolean urlSafe){
		return new String(PTBase64.encode(src,urlSafe));
	}
	
	/**
	 * To base64.
	 */
	public static String bytesToBase64(byte[] src){
		return bytesToBase64(src,false);
	}
	
	/**
	 * Base64 to bytes.
	 */
	public static byte[] base64ToBytes(String src,boolean urlSafe){
		return PTBase64.decode(src.getBytes(),urlSafe);
	}
	
	/**
	 * Base64 to bytes.
	 */
	public static byte[] base64ToBytes(String src){
		return base64ToBytes(src,false);
	}
	
	/**
	 * To hex.
	 */
	public static String bytesToHex(byte[] src){
		return PTHex.encode(src);
	}

	/**
	 * 生成散列串
	 * @param url 源数据
	 * @param key 秘钥
	 * @return 散列串
	 */
	public static String bytesToStr(String url,byte[] key) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			if (url!=null) {
				baos.write(url.getBytes());
			}
			if (key!=null) {
				baos.write(key);
			}
			return PTConverter.bytesToHex(PTHash.getHashByBytes(PTFrameworkConstants.PTHashEnumConstants.MD5, baos.toByteArray()));
		}finally{
			PTStreamOperator.close(baos);
		}
	}
}
