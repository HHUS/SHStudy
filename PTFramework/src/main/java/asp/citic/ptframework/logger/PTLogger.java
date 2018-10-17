package asp.citic.ptframework.logger;

import asp.citic.ptframework.BuildConfig;
import android.util.Log;

/**
 * @ingroup loggerModuleClass
 * @author dora
 * @function 日志
 */
public final class PTLogger {

	/**LOG输出开关，true=输出，false=禁止*/
	// private final static boolean ISDEBUG = true;
	/**
	 * 私有构造方法
	 */
	private PTLogger(){
		//To be a Utility class
	}
	/**
	 * info类日志
     */
	public static void info(String tag, String msg) {
		if (BuildConfig.DEBUG && isLogEnable(Log.INFO)&& msg != null) {
			Log.i(tag, msg);
		}
	}
	/**
	 * debug类日志
	 */
	public static void debug(String tag, String msg) {
		if (BuildConfig.DEBUG && isLogEnable(Log.DEBUG)&& msg != null) {
			Log.d(tag, msg);
		}
	}
	/**
	 * error类日志
	 */
	public static void error(String tag, String msg) {
		if (BuildConfig.DEBUG && isLogEnable(Log.ERROR) && msg != null) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 当前日志级别是否可用
     */
	private static boolean isLogEnable(int lever) {
		boolean isEnable = false;
		switch (lever){
			case Log.INFO:
				isEnable = true;
				break;
			case Log.DEBUG:
				isEnable = true;
				break;
			case Log.ERROR:
				isEnable = true;
				break;
			default:
				break;
		}
		return isEnable;
	}


}
