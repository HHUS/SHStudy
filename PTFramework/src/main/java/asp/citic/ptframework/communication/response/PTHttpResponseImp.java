package asp.citic.ptframework.communication.response;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import asp.citic.ptframework.common.tools.PTStreamOperator;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.logger.PTLogger;


/**
 * @ingroup httpRequestModuleClass
 * http响应实例
 */
public class PTHttpResponseImp implements PTHttpResponseInterface {
    /**
     * 类标记
     */
    private static final String TAG = "PTHttpResponseImp";
    /**
     * gzip
     */
    private final static String GZIP = "gzip";
    /**
     * 内部的HttpResponse对象.
     */
    private final transient HttpResponse response;
    /**
     * 内部的HttpClient对象.
     */
    private final transient DefaultHttpClient httpClient;
    /**
     * 响应的字节数组.
     */
    private transient byte[] responseData;
    /**
     * 响应的字节数组是否读取标记
     */
    private transient boolean responseDataHaveRead;

    public PTHttpResponseImp(HttpResponse response,
                             DefaultHttpClient httpClient) {
        this.response = response;
        this.httpClient = httpClient;
    }

    @Override
    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public byte[] getResponseData() {
        if (!responseDataHaveRead) {
            InputStream ins = null;
            try {
                ins = response.getEntity().getContent();
                Header header = response.getFirstHeader("Content-Encoding");
                if (header != null && GZIP.equals(header.getValue())) {
                    ins = new GZIPInputStream(ins);
                }
                responseData = PTStreamOperator.getInputStreamBytes(ins);
            } catch (IllegalStateException e) {
                PTLogger.error(TAG,e.getMessage());
            } catch (IOException e) {
                PTLogger.error(TAG,e.getMessage());
            } finally {
                PTStreamOperator.close(ins);
                responseDataHaveRead = true;
            }
        }
        if (responseData == null) {
            return new byte[1];
        } else {
            return Arrays.copyOf(responseData, responseData.length);
        }
    }

    @Override
    public Header[] getAllHeaders() {
        return response.getAllHeaders();
    }

    @Override
    public String getHeader(String key) {
        Header header = response.getFirstHeader(key);
        if (header != null) {
            return header.getValue();
        }
        return null;
    }

    @Override
    public String getSessionId() {
        CookieStore cookieStore = httpClient.getCookieStore();
        List<Cookie> cookieList = cookieStore.getCookies();
        for (Cookie c : cookieList) {
            if (c.getName().indexOf("SESSIONID") != -1 && c.getValue() != null) {
                try {
                    return URLDecoder.decode(c.getValue(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    PTLogger.error(TAG,e.getMessage());
                }
            }
        }
        return null;
    }
}
