

package com.csii.sh.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.cert.CertPathValidatorException;

import retrofit2.HttpException;


/**
 * 处理网络异常,对所有的网络异常进行封装
 */
public class ExceptionUtil {

    // http 的 状态码
    interface HTTP {
        int UNAUTHORIZED = 401;
        int FORBIDDEN = 403;
        int NOT_FOUND = 404;
        int REQUEST_TIMEOUT = 408;
        int INTERNAL_SERVER_ERROR = 500;
        int BAD_GATEWAY = 502;
        int SERVICE_UNAVAILABLE = 503;
        int GATEWAY_TIMEOUT = 504;
        int ACCESS_DENIED = 302;
        int HANDEL_ERROR = 417;
    }

    /**
     * 自定义网络相关异常状态码
     */
    interface SERVER_ERROR {

        /**
         * 无网络连接
         */
        int NO_CONNECT = 1000;

        /**
         * 解析错误
         */
        int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        int TIMEOUT_ERROR = 1006;

        /**
         * 证书未找到
         */
        int SSL_NOT_FOUND = 1007;

        /**
         * 格式错误
         */
        int FORMAT_ERROR = 1008;

        /**
         * 未知错误
         */
        int UNKNOWN = 1009;

        /**
         * 出现空值
         */
        int NULL = -100;
    }

    /**
     * 对网络相关错误进行封装
     */
    public static NetException wrapperException(Throwable e) {

        NetException ex;
            //Todo
//        if (!NetworkUtils.isNetworkAvailable(UIUtils.getContext())) {
        if (true) {
            ex = new NetException(e, SERVER_ERROR.NO_CONNECT);
            ex.setMessage("暂无网络连接,请检查网络设置");
            return ex;
        } else if (e instanceof HttpException) {
            // retrofit 抛出的 网络错误
            HttpException httpException = (HttpException) e;
            ex = new NetException(e, SERVER_ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case HTTP.UNAUTHORIZED:
                    ex.setMessage("未授权的请求");
                case HTTP.FORBIDDEN:
                    ex.setMessage("禁止访问");
                case HTTP.NOT_FOUND:
                    ex.setMessage("服务器地址未找到");
                case HTTP.REQUEST_TIMEOUT:
                    ex.setMessage("请求超时");
                case HTTP.GATEWAY_TIMEOUT:
                    ex.setMessage("网关响应超时");
                case HTTP.INTERNAL_SERVER_ERROR:
                    ex.setMessage("服务器出错");
                case HTTP.BAD_GATEWAY:
                    ex.setMessage("无效的请求");
                case HTTP.SERVICE_UNAVAILABLE:
                    ex.setMessage("服务器不可用");
                case HTTP.ACCESS_DENIED:
                    ex.setMessage("网络错误");
                case HTTP.HANDEL_ERROR:
                    ex.setMessage("接口处理失败");
                default:
                    ex.setMessage(e.getMessage());
                    break;
            }
            ex.setCode(httpException.code());
            return ex;
        } else if (e instanceof ApiException) {
            // 业务错误
            ApiException apiException = (ApiException) e;
            ex = new NetException(apiException, apiException.getCode());
            ex.setMessage(apiException.getMessage());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            // throw after Parse exception
            ex = new NetException(e, SERVER_ERROR.PARSE_ERROR);
            ex.setMessage("解析错误");
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new NetException(e, SERVER_ERROR.NETWORK_ERROR);
            ex.setMessage("连接失败");
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new NetException(e, SERVER_ERROR.SSL_ERROR);
            ex.setMessage("证书验证失败");
            return ex;
        } else if (e instanceof CertPathValidatorException) {
            ex = new NetException(e, SERVER_ERROR.SSL_NOT_FOUND);
            ex.setMessage("证书路径没找到");
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new NetException(e, SERVER_ERROR.TIMEOUT_ERROR);
            ex.setMessage("连接超时");
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new NetException(e, SERVER_ERROR.TIMEOUT_ERROR);
            ex.setMessage("连接超时");
            return ex;
        } else if (e instanceof ClassCastException) {
            ex = new NetException(e, SERVER_ERROR.FORMAT_ERROR);
            ex.setMessage("类型转换出错");
            return ex;
        } else if (e instanceof NullPointerException) {
            ex = new NetException(e, SERVER_ERROR.NULL);
            ex.setMessage("数据有空");
            return ex;
        } else {
            ex = new NetException(e, SERVER_ERROR.UNKNOWN);
            ex.setMessage(e.getLocalizedMessage());
            return ex;
        }
    }
}
