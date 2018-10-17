package asp.citic.ptframework.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import asp.citic.ptframework.PTFrameworkListener;
import asp.citic.ptframework.PTFrameworkListener.CustomEventListener;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @ingroup httpAssistModuleClass
 * 消息中心，发送通知给监听类
 */
public final class PTMessageCenter {
    /**
     * 类标记
     */
    private static final String TAG = "PTMessageCenter";
    //通信安全模块
    /**
     * 会话状态变更事件
     */
    public final static int EVENT_SESSION_CHANGED = 1000;
    /**
     * 密钥协商状态变更事件
     */
    public final static int EVENT_CONSULTKEY_CHANGE = 1001;
    //资源安全模块
    /**
     * 资源压缩包为空
     */
    public final static int EVENT_EMPTY_ZIPPACK = 1002;
    /**
     * 不去解压标识
     */
    public final static int EVENT_NO_UNZIP = 1003;
    /**
     * 开始解压标识
     */
    public final static int EVENT_UNZIP_START = 1004;
    /**
     * 解压data.zip进度
     */
    public final static int EVENT_DATAZIP_PROGRESS = 1005;
    /**
     * 验签本地resource.json
     */
    public final static int EVENT_RESOURCE_CHECK_LOCAL = 1006;
    /**
     * 验签服务端resource.json
     */
    public final static int EVENT_RESOURCE_CHECK_SERVER = 1007;
    /**
     * resource.json验签完成
     */
    public final static int EVENT_RESOURCE_COMPLETE = 1008;
    //增量更新模块
    /**
     * 对文件列表验签
     */
    public final static int EVENT_CHECK_FILELIST = 1009;


    /**
     * 监听器列表
     */
    // ##0001# 修改list实现为CopyOnWriteArrayList，解决多线程问题。注意，该List不适宜反复进行写操作。
    private static List<PTFrameworkListener> listenerList = new CopyOnWriteArrayList<PTFrameworkListener>();
    /**
     * Handler处理器
     */
    private static Handler mainMessageHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            //将消息发送给消息监听者
            for (PTFrameworkListener listener : listenerList) {
                try {
                    if (listener instanceof CustomEventListener) {
                        CustomEventListener customListener = (CustomEventListener) listener;
                        customListener.onCustomEvent(msg.what, msg.obj);
                    }
                } catch (JSONException e) {
                    PTLogger.error(TAG, e.getMessage());
                }
            }
        }
    };

    /**
     * 私有构造方法
     */
    private PTMessageCenter() {
        //To be a Utility class
    }

    /**
     * 添加监听器
     *
     * @param listener 待添加的监听器
     */
    public static void addListener(PTFrameworkListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    /**
     * 移除监听器
     *
     * @param listener 待移除的监听器
     */
    public static void removeListener(PTFrameworkListener listener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }

    /**
     * 发送事件
     */
    public static void riseEvent(int eventId, Object obj) {
        Message message = Message.obtain();
        message.what = eventId;
        message.obj = obj;
        mainMessageHandler.handleMessage(message);
    }

}
