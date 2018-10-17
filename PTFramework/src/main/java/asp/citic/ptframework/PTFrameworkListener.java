package asp.citic.ptframework;

import org.json.JSONException;


/**@ingroup httpAssistModuleProtocol
 * 框架接口
 */
public interface PTFrameworkListener {

	/**
	 * 会话事件监听器
	 *
	 */
	interface SessionEventListener extends PTFrameworkListener{
		/**
		 * 会话事件处理回调
		 */
		void onSessionEvent(int event, Object data) throws JSONException;
	}

	/**
	 * 自定义事件监听器
	 *
	 */
	interface CustomEventListener extends PTFrameworkListener{
		/**
		 * 自定义事件的回调
		 */
		void onCustomEvent(int event,Object obj) throws JSONException;
	}
}
