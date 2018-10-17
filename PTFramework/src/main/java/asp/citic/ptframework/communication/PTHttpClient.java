package asp.citic.ptframework.communication;

import android.content.res.Resources;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @ingroup httpRequestModuleClass
 * HttpClient类
 */
public final class PTHttpClient {
    /**
     * 类标记
     */
    private static final String TAG = "PTHttpClient";
    /**
     * http请求类
     */
    public static DefaultHttpClient httpClient;

    static {
        // HttpClient对象初始化
        HttpParams params = new BasicHttpParams();
        // 设置Socket超时
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, PTFrameworkConstants.PTHttpTaskConstants.TIMEOUT);
        // 设置连接超时
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, PTFrameworkConstants.PTHttpTaskConstants.TIMEOUT);
        // 设置Socket缓冲区大小
        params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, PTFrameworkConstants.PTHttpTaskConstants.SOCKET_BUFFER_SIZE);
        params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, true);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        // 配置https协议证书
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance("BKS");
            Resources activityRes = PTFramework.getContext().getResources();
            int rawCertsId = activityRes.getIdentifier("ptframework_trustcerts", "raw", PTFramework.getContext().getPackageName());
            InputStream ksinstream = PTFramework.getContext().getResources().openRawResource(rawCertsId);
            try {
                trustStore.load(ksinstream, "".toCharArray());
                ksinstream.close();
                SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
                socketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                schReg.register(new Scheme("https", socketFactory, 443));

                ConnManagerParams.setMaxConnectionsPerRoute(params,
                        new ConnPerRouteBean(10));
                ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                        params, schReg);
                httpClient = new DefaultHttpClient(conMgr, params);
                httpClient.setRedirectHandler(new RedirectHandler() {

                    @Override
                    public boolean isRedirectRequested(HttpResponse response,
                                                       HttpContext context) {
                        // 禁止重定向
                        return false;
                    }

                    @Override
                    public URI getLocationURI(HttpResponse response, HttpContext context)
                            throws ProtocolException {
                        return null;
                    }
                });
            } catch (IOException e) {
                PTLogger.error(TAG, e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                PTLogger.error(TAG, e.getMessage());
            } catch (CertificateException e) {
                PTLogger.error(TAG, e.getMessage());
            } catch (UnrecoverableKeyException e) {
                PTLogger.error(TAG, e.getMessage());
            } catch (KeyManagementException e) {
                PTLogger.error(TAG, e.getMessage());
            }

        } catch (KeyStoreException e) {
            PTLogger.error(TAG, e.getMessage());
        }
    }

    /**
     * 私有构造方法
     */
    private PTHttpClient() {
        //To be a Utility class
    }
}
