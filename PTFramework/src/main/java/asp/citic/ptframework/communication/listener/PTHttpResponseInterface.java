package asp.citic.ptframework.communication.listener;

import org.apache.http.Header;

/**
 * @ingroup httpRequestModuleProtocol
 * http响应接口
 */
public interface PTHttpResponseInterface {

	/**
	 * 获取响应的状态码.
	 */
	int getStatusCode();

	/**
	 * 获取响应的字节数组.
	 */
	byte[] getResponseData();

	/**
	 * 获取所有的Header.
	 */
	Header[] getAllHeaders();

	/**
	 * 获取SessionId.
	 */
	String getSessionId();

	/**
	 * 获取指定Http头.
	 */
	String getHeader(String key);
}
