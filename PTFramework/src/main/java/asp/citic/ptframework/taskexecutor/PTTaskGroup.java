package asp.citic.ptframework.taskexecutor;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * @ingroup httpAssistModuleClass
 * 线程任务分组执行管理类
 */
public class PTTaskGroup {
	/** 执行器  */
	public transient ExecutorService executorService;

	/**
	 * 构造函数.
	 *
	 * @param executorService 执行器
	 */
	public PTTaskGroup(ExecutorService executorService) {
		this.executorService=executorService;
	}

	/**
	 * 执行任务.
	 *
	 * @param task 任务对象
	 * @return 分配的任务ID
	 */
	public UUID excute(PTAbstractTask<?> task){
		executorService.submit(task);
		return UUID.randomUUID();
	}

	/**
	 * 释放任务组.
	 */
	public void release() {
		executorService.shutdownNow();
	}
}
