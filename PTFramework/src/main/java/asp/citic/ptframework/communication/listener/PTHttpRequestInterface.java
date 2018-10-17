package asp.citic.ptframework.communication.listener;

import org.json.JSONException;

import java.io.IOException;


/**
 * @ingroup httpRequestModuleProtocol
 * http请求接口
 */
public interface PTHttpRequestInterface {

	/**
	 * 发送同步请求.
	 */
	PTHttpResponseInterface dataRequest() throws IOException, JSONException;

	/**
	 * 发送同步请求并将返回的数据写入文件.
	 */
	boolean fileRequest(String filePath) throws IOException;

}
