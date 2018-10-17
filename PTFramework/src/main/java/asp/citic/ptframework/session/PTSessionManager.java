package asp.citic.ptframework.session;

import android.text.TextUtils;

import org.apache.http.Header;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.message.PTMessageCenter;
import asp.citic.ptframework.secretkeys.PTSecretKeyManager;

/**
 * @ingroup httpStateModuleClass
 * 会话管理类
 */
public final class PTSessionManager {
    /**
     * 标记
     */
    private final static String TAG = "PTSessionManager";
    /**
     * session状态
     */
    private static int state = PTFrameworkConstants.PTSessionStatus.STATE_SESSION_NULL;

    /**
     * 私有构造方法
     */
    private PTSessionManager() {
        //To be a Utility class
    }

    /**
     * 更新会话状态.
     *
     * @param response 响应对象
     */
    public static int updateSession(PTHttpResponseInterface response) {
        synchronized (PTSessionManager.class) {
            PTLogger.info(TAG, "开始更新会话状态");
            if (response == null) {
                return 0;
            }
            String sessionId = response.getSessionId();
            if (TextUtils.isEmpty(sessionId)){
                //sessionId为空的异常处理
                state = PTFrameworkConstants.PTSessionStatus.STATE_HANDSHAKE_FAILED;
                PTMessageCenter.riseEvent(PTMessageCenter.EVENT_SESSION_CHANGED, Integer.valueOf(state));
                return state;
            }
            int lastindex = sessionId.lastIndexOf(':');
            if (lastindex == -1) {
                lastindex = sessionId.length();
                PTSecretKeyManager.sessionkey = sessionId.substring(lastindex - 9, lastindex - 1);
                PTSecretKeyManager.sessionId = sessionId.substring(0, lastindex);
            } else if (sessionId.startsWith("s:")) {
                int pointIndex = sessionId.indexOf('.');
                PTSecretKeyManager.sessionkey = sessionId.substring(pointIndex - 9, pointIndex - 1);
                PTSecretKeyManager.sessionId = sessionId.substring(2, pointIndex);
            } else {
                PTSecretKeyManager.sessionkey = sessionId.substring(lastindex - 9, lastindex - 1);
                PTSecretKeyManager.sessionId = sessionId.substring(0, lastindex);
            }
            Header headers[] = response.getAllHeaders();
            for (Header header : headers) {
                String name = header.getName();
                String value = header.getValue();
                if ("Service-Number".equals(name)) {
                    PTSecretKeyManager.serverR = value;
                    break;
                }
            }
            String strSessionState = response.getHeader("sessionState");
            if (strSessionState != null) {
                state = Integer.parseInt(strSessionState);
                switch (state) {
                    case 100:
                        PTLogger.info(TAG, "握手成功");
                        state = PTFrameworkConstants.PTSessionStatus.STATE_HANDSHAKE_SUCCESS;
                        break;
                    case 101:
                        PTLogger.info(TAG, "握手失败");
                        state = PTFrameworkConstants.PTSessionStatus.STATE_HANDSHAKE_FAILED;
                        break;
                    case 102:
                        PTLogger.info(TAG, "握手超时");
                        state = PTFrameworkConstants.PTSessionStatus.STATE_SESSION_TIMEOUT;
                        break;
                    default:
                        PTLogger.info(TAG, "握手超时");
                        state = PTFrameworkConstants.PTSessionStatus.STATE_SESSION_TIMEOUT;
                        break;
                }
            }
            //发送 会话状态变更事件
            PTLogger.info(TAG, "发送会话状态变更事件");
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_SESSION_CHANGED, Integer.valueOf(state));
            return state;
        }
    }

    /**
     * 获取握手会话状态
     *
     * @return 握手会话状态
     */
    public static Integer getSessionState() {
        return state;
    }

}
