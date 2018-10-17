package asp.citic.ptframework.taskexecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @ingroup httpAssistModuleClass
 * 线程任务操作管理类
 */
public final class PTTaskExecuteService {

    /**
     * 系统默认任务组
     */
    public static final String TASKGROUP_DEFAULT = "default";
    /**
     * 任务组表.
     */
    private static Map<String, PTTaskGroup> taskGroupMap = new ConcurrentHashMap<String, PTTaskGroup>();
    /**
     * 任务表.
     */
    private static Map<String, PTAbstractTask<?>> taskMap = new ConcurrentHashMap<String, PTAbstractTask<?>>();

    /**
     * 私有构造函数
     */
    private PTTaskExecuteService() {

        //默认系统任务组
        innerAddTaskGroup(TASKGROUP_DEFAULT, new PTTaskGroup(Executors.newFixedThreadPool(3)));
    }

    private static void innerAddTaskGroup(String groupName, PTTaskGroup taskGroup) {
        taskGroupMap.put(groupName, taskGroup);
    }


    /**
     * 执行任务.
     *
     * @param groupName 任务组名
     * @param task      任务对象
     * @return 分配的任务ID
     */
    public static String excute(String groupName, PTAbstractTask<?> task) {
        PTTaskGroup group = taskGroupMap.get(groupName);
        if (group != null) {
            group.excute(task);
            taskMap.put(task.getTaskId(), task);
        }
        return task.getTaskId();
    }

    /**
     * 提交到系统任务组执行任务.
     *
     * @param task 任务对象
     * @return 分配的任务ID
     */
    public static String excute(PTAbstractTask<?> task) {
        excute(TASKGROUP_DEFAULT, task);
        return task.getTaskId();
    }

    /**
     * 提交到系统任务组执行任务.
     *
     * @param task 任务对象
     * @return 分配的任务ID
     */
    public static String excute(PTAbstractTask<?> task, String groupName) {
        excute(groupName, task);
        return task.getTaskId();
    }

    /**
     * 取消指定任务.
     *
     * @param taskId 待取消任务的ID
     */
    public static void cancel(String taskId) {
        PTAbstractTask<?> task = taskMap.get(taskId);
        if (task != null) {
//			task.cancel();
            removeTask(taskId);
        }
    }

    /**
     * 添加任务执行组.
     *
     * @param groupName 任务组名
     * @param taskGroup 任务组对象
     */
    public static void addTaskGroup(String groupName, PTTaskGroup taskGroup) {
        innerAddTaskGroup(groupName, taskGroup);
    }

    /**
     * 移除任务组.
     *
     * @param groupName 待移除任务组的名称
     */
    public static void removeTaskGroup(String groupName) {
        PTTaskGroup taskGroup = taskGroupMap.remove(groupName);
        if (taskGroup != null) {
            taskGroup.release();
        }
    }

    /**
     * 移除任务组.
     *
     * @param taskId 待移除任务的ID
     */
    public static void removeTask(String taskId) {
        taskMap.remove(taskId);
    }
}
