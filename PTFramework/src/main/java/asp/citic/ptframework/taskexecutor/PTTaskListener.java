package asp.citic.ptframework.taskexecutor;
/**
 * @ingroup httpAssistModuleClass
 * 线程任务回调管理类
 */
public interface PTTaskListener<T> {

	/**
	 * 任务执行完成的回调
	 */
	void onComplete(int state,T result);

	/**
	 * 任务执行错误的回调
	 */
	void onError();

}
