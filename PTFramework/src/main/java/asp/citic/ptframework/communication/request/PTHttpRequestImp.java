package asp.citic.ptframework.communication.request;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import asp.citic.ptframework.communication.PTHttpClient;
import asp.citic.ptframework.communication.listener.PTHttpRequestInterface;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.communication.response.PTHttpResponseImp;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.session.PTSessionManager;

/**
 * @ingroup httpRequestModuleClass
 * http请求实例
 */
public class PTHttpRequestImp implements PTHttpRequestInterface {
    /**
     * 类标记
     */
    private static final String TAG = "PTHttpRequestImp";
    /**
     * 缓冲长度，1K.
     */
    private static final int BUFFSIZE = 1024;
    /**
     * gzip
     */
    private final static String GZIP = "gzip";
    /**
     * 被封装的请求对象.
     */
    private final transient HttpUriRequest request;

    /**
     * 构造函数.
     */
    public PTHttpRequestImp(HttpUriRequest request) {
        request.addHeader("Accept-Encoding", GZIP);
        this.request = request;
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     */
    public static void setTimeout(int timeout) {
        HttpParams params = PTHttpClient.httpClient.getParams();
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        ConnManagerParams.setTimeout(params, timeout);
        PTHttpClient.httpClient.setParams(params);
    }

    @Override
    public PTHttpResponseInterface dataRequest() {
        PTHttpResponseInterface ptResponse = null;
        request.setHeader("content-type", "text/plain");
        try {
            HttpResponse response = PTHttpClient.httpClient.execute(request);
            if (response != null) {
                PTLogger.debug(TAG, "HttpResponse不为空, URL:" + request.getURI());
                ptResponse = new PTHttpResponseImp(response, PTHttpClient.httpClient);
                int code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_OK) {
                    PTSessionManager.updateSession(ptResponse);
                }
            } else {
                PTLogger.debug(TAG, "HttpResponse为空, URL:" + request.getURI());
            }
        } catch (IOException e) {
            PTLogger.debug(TAG, "execute request发生异常, URL:" + request.getURI());
        }
        return ptResponse;
    }

    @Override
    public boolean fileRequest(String filePath) {
        makeDir(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            try {
                HttpResponse response = PTHttpClient.httpClient.execute(request);
                if (response == null) {
                    return false;
                }
                int code = response.getStatusLine().getStatusCode();
                if (code != HttpStatus.SC_OK) {
                    return false;
                }
                long total = response.getEntity().getContentLength();
                InputStream ins = response.getEntity().getContent();
                Header header = response.getFirstHeader("Content-Encoding");
                if (header != null && GZIP.equals(header.getValue())) {
                    ins = new GZIPInputStream(ins);
                }
                byte[] buf = new byte[BUFFSIZE];
                int size;
                while ((size = ins.read(buf)) != -1) {
                    fos.write(buf, 0, size);
                    if (total == -1) {
                        continue;
                    }
                }
            } catch (IOException e) {
                PTLogger.error(TAG, e.getMessage());
                return false;
            }
        } catch (FileNotFoundException e) {
            PTLogger.error(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 创建文件夹
     */
    private void makeDir(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            String tempPath = "";
            File file = new File(filePath);
            if (!file.isDirectory() && filePath.lastIndexOf('/') != -1) {
                tempPath = filePath.substring(0, filePath.lastIndexOf('/'));
            }
            file = new File(tempPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}
