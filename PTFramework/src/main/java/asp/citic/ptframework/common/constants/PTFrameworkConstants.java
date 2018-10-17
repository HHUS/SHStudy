package asp.citic.ptframework.common.constants;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @author dora
 * @time 2016-12-02.
 */

public final class PTFrameworkConstants {
    /**
     * 私有构造方法
     */
    private PTFrameworkConstants() {
        //To be a Utility class
    }

    /**
     * @ingroup httpStateModuleEnum
     * 网络状态枚举类
     */
    public enum PTNetworkStatus {
        INSTANCE;
        /**
         * 未联网
         */
        public static final int STATE_NET_NULL = 0;
        /**
         * 已连接手机网络
         */
        public static final int STATE_NET_MOBILE = 1;
        /**
         * 已连接wifi网络
         */
        public static final int STATE_NET_WIFI = 2;
    }

    /**
     * @ingroup httpStateModuleEnum
     * 会话状态枚举类
     */
    public enum PTSessionStatus {
        INSTANCE;
        /**
         * 未建立会话
         */
        public static final int STATE_SESSION_NULL = 0;
        /**
         * 正在握手
         */
        public static final int STATE_HANDSHAKEING = 1;
        /**
         * 握手失败
         */
        public static final int STATE_HANDSHAKE_FAILED = 2;
        /**
         * 会话超时，需要重新握手
         */
        public static final int STATE_SESSION_TIMEOUT = 3;
        /**
         * 握手成功
         */
        public static final int STATE_HANDSHAKE_SUCCESS = 4;
    }

    /**
     * @ingroup httpStateModuleEnum
     * 通用错误码
     */
    public enum PTErrorCode {
        INSTANCE;
        /**
         * 无错误
         */
        public static final int NOERROR = 0;
        /**
         * 未知错误
         */
        public static final int UNKNOWN = Integer.MAX_VALUE;
        //文件类错误
        /**
         * 文件不存在
         */
        public static final int FILE_NOT_FOUND = 3001;
        /**
         * 文件太大
         */
        public static final int FILE_TOO_LARGE = 3002;
        /**
         * 签名错误
         */
        public static final int INVALID_SIGN = 3004;
        /**
         * 版本号错误
         */
        public static final int INVALID_VERSION = 3005;
        /**
         * URL错误
         */
        public static final int INVALID_URL = 4001;
        /**
         * 更新状态错误
         */
        public static final int INVALID_UPDATESTATE = 4002;
    }

    /**
     * @ingroup resourceFileModuleEnum
     *
     */
    public enum PTConstant {
        INSTANCE;
        /**
         * 通用布尔常量：空值
         */
        public static final int NULL = -1;
        /**
         * 通用状态常量：已完成
         */
        public static final int STATE_COMPLETE = 1;
        /**
         * 通用状态常量：已失败
         */
        public static final int STATE_FAILED = 2;
        /**
         * 属性命名空间
         */
        public static final String NS_RESOURCE = "http://schemas.android.com/apk/res/android";
    }

    /**
     * @ingroup resourceFileModuleEnum
     * 解压状态标识
     */
    public enum PTDataZipConstant {
        INSTANCE;
        /**
         * 用户首次安装程序
         */
        public static final int NEW_LOAD = 0;
        /**
         * 老用户升级新版本
         */
        public static final int OLD_TO_NEWLOAD = 1;
        /**
         * 上次解压data.zip失败
         */
        public static final int BEFORE_LOAD_FAILED = 2;
        /**
         * 资源为最新
         */
        public static final int NO_LOAD = 3;
    }

    /**
     * @ingroup resourceFileModuleEnum
     * 通用常量
     */
    public enum PTResourceConstant {
        INSTANCE;
        /**
         * 服务器基地址
         */
        public static final String TAG = "PTFrameworkConstants.PTResourceConstant";
        /**
         * 服务器基地址
         */
        public static final String URL_BASE;
        /**
         * 安装目录基地址
         */
        public static final String PATH_BASE;
        /**
         * 安装目录release基地址
         */
        public static final String PATH_RELEASE;
        /**
         * 安装目录backup基地址
         */
        public static final String PATH_BACKUP;
        /**
         * 安装目录下压缩包路径
         */
        public static final String FILE_ZIP;
        /**
         * 工程assets下压缩包路径
         */
        public static final String ASSETS_ZIP = "file:///android_asset/data.zip";
        /**
         * 文件清单地址
         */
        public static final String RESOURCE = "resource.json";
        /**
         * 服务器模板数据基地址
         */
        public static final String URL_DATA;
        /**
         * 框架请求地址
         */
        public static final String FRAMEWORK_SERVICE = "ptframework.do";
        /**
         * 框架握手请求地址
         */
        public static final String TRANSCODE_HANDSHAKE = "handshake";
        /**
         * 框架握手请求地址
         */
        public final static String URL_MANIFESTUPDATE = "update";
        /**
         * 注释符号无后缀
         */
        public final static int SUFFIXLEN_0 = 0;
        /**
         * 注释符号后缀为2位,例如 ＊/
         */
        public final static int SUFFIXLEN_2 = 2;
        /**
         * 注释符号后缀为3位,例如 -->
         */
        public final static int SUFFIXLEN_3 = 3;
        /**
         * 注释符号后缀为4位,例如 <!--
         */
        public final static int SUFFIXLEN_4 = 4;
        /**
         * 模版版本号长度!<
         */
        public final static int VERSION_CODE_LENGTH = 6;
        /**
         * aPP获取本地资源对象
         */
        static Resources activityRes;

        static {
            Properties properties = new Properties();
            Context context = PTFramework.getContext();
            activityRes = context.getResources();
            InputStream ins = null;
            try {
                int rawConfigId = activityRes.getIdentifier("ptframework_config", "raw", context.getPackageName());
                ins = activityRes.openRawResource(rawConfigId);
                properties.load(ins);
            } catch (IOException e) {
                PTLogger.error(TAG,e.getMessage());
            }
            URL_BASE = properties.getProperty("server_base");
            URL_DATA = URL_BASE + "ptframework/data/";
            PATH_BASE = context.getFilesDir().getAbsolutePath();
            PATH_RELEASE = PATH_BASE + "/release/";
            PATH_BACKUP = PATH_BASE + "/backup/";
            FILE_ZIP = PATH_BASE + "/data.zip";
        }
    }

    /**
     * @ingroup httpSecurityModuleEnum
     * 各种资源检查的状态 xml json js css文件的检查结果枚举
     */
    public enum PTCheckStatusConstant {
        INSTANCE;
        /**
         * 检查成功.
         */
        public static final int CHECK_SUCCESS = 0;
        /**
         * 检查失败.
         */
        public static final int CHECK_FAILED = 1;
        /**
         * 检查进行中.
         */
        public static final int CHECK_DOING = 2;
    }

    /**
     * @ingroup httpSecurityModuleEnum
     * 加密类型
     */
    public enum PTEncrytTypeConstant {
        INSTANCE;
        /**
         * 加密标志常量：不加密
         */
        public static final int FLAG_ENCRYPT_NONE = 0;
        /**
         * 加密标志常量：3DES加密
         */
        public static final int FLAG_ENCRYPT_3DES = 1;
        /**
         * 加密标志常量：AES加密
         */
        public static final int FLAG_ENCRYPT_AES = 2;
        /**
         * 加密标志常量：RC5加密
         */
        public static final int FLAG_ENCRYPT_RC5 = 3;
        /**
         * 加密标志常量：框架RSA加密
         */
        public static final int FLAG_ENCRYPT_PTRSA = 4;
    }

    /**
     * @ingroup httpSecurityModuleEnum
     * Hash类型
     */
    public enum PTHashConstant {
        INSTANCE;
        /**
         * 散列算法标志常量：不计算散列值
         */
        public static final int FLAG_HASH_NONE = 0;
        /**
         * 散列算法标志常量：MD5
         */
        public static final int FLAG_HASH_MD5 = 1;
        /**
         * 散列算法标志常量：SHA1
         */
        public static final int FLAG_HASH_SHA1 = 2;
    }

    /**
     * @ingroup httpSecurityModuleEnum
     * 签名类型
     */
    public enum PTSignatureConstant {
        INSTANCE;
        /**
         * 数据签名算法标志常量：不签名
         */
        public static final int FLAG_SIGNATURE_NONE = 0;
        /**
         * 数据签名法标志常量： MD5withRSA
         */
        public static final int FLAG_SIGNATURE_MD5WITHRSA = 1;
        /**
         * 数据签名算法标志常量：SHA1withRSA
         */
        public static final int FLAG_SIGNATURE_SHA1WITHRSA = 2;
    }

    /**
     * @ingroup httpSecurityModuleEnum
     * 通讯包方向类型枚举
     */
    public enum PTPkgFlag {

        INSTANCE;

        /**
         * 由客户端向前置服务器端发送的请求包
         */
        public static final int CONTEXT_TYPE_CLIENT = 0;

        /**
         * 由前置服务器端向客户端反馈的响应包
         */
        public static final int CONTEXT_TYPE_SERVER = 1;
    }

    /**
     * @ingroup hashModuleEnum
     * Hash算法类型
     */
    public enum PTHashEnumConstants {

        INSTANCE;
        /**
         * 不使用散列算法.
         */
        public static final int NONE = 0;
        /**
         * MD5散列算法.
         */
        public static final int MD5 = 1;
        /**
         * SHA1散列算法.
         */
        public static final int SHA1 = 2;
    }

    /**
     * @ingroup hashModuleEnum
     * Hash算法类型
     */
    public enum PTHttpTaskConstants {

        INSTANCE;
        /**
         * 网络请求超时时间
         */
        public static final int TIMEOUT = 60 * 1000;
        /**
         * 缓冲区大小
         */
        public static final int SOCKET_BUFFER_SIZE = 8 * 1024;
    }

    /**
     * @ingroup hashModuleEnum
     * Hash算法类型
     */
    public enum PTFormateConstants {

        INSTANCE;
        /**
         * 模板业务url前缀
         */
        public static final String URL_PREFIX_HTML = "html";
        /**
         * url前缀分隔符转义
         */
        public static final String URL_PREFIX_SEPARATOR_PATTERN = "\\|";
        /**
         * url前缀分隔符
         */
        public static final String URL_PREFIX_SEPARATOR = "|";
    }
}
