package asp.citic.ptframework.common.algorithm;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.UrlBase64;


/**
 * @ingroup codeEncodeModuleClass
 * @author dora
 * @function Base64转码
 * @time 2016-11-29
 */
public final class PTBase64 {

	/**
	 * 对输入的数据进行Base64编码.
	 */
	public static byte[] encode(byte[] data){
		return encode(data,false);
	}

	/**
	 * 私有构造方法
	 */
	private PTBase64(){
		//To be a Utility class
	}
	/**
	 * 对输入的数据进行Base64编码.
	 */
	public static byte[] encode(byte[] data,boolean urlSafe){
		if (data == null){
			return new byte[1];
		}
		if (urlSafe) {
			return UrlBase64.encode(data);
		}else {
			return Base64.encode(data);	
		}
	}
	
	/**
	 * 对输入的Base 64数据进行解码，返回解码后的数据.
	 */
	public static byte[] decode(byte[] data) {
		return decode(data, false);
	}
	
	/**
	 * 对输入的Base 64数据进行解码，返回解码后的数据.
	 */
	public static byte[] decode(byte[] data,boolean urlSafe){
		if (data == null){
			return new byte[1];
		}
		if (urlSafe) {
			return UrlBase64.decode(data);
		}else {
			return Base64.decode(data);
		}
	}
}
