package asp.citic.ptframework.taskexecutor;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.constants.PTFrameworkConstants.PTHttpTaskConstants;


/**
 * @ingroup httpAssistModuleClass
 * 异步任务对象管理类
 */
public abstract class PTAbstractTask<T> implements Callable<Void> {
    /**
     * Handler
     */
    private static Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 监听列表
     */
    private final transient List<PTTaskListener<T>> listenerList = new CopyOnWriteArrayList<PTTaskListener<T>>();
    /**
     * 任务ID
     */
    private final String taskId = UUID.randomUUID().toString();
    /**
     * 响应状态
     */
    protected transient int state = PTFrameworkConstants.PTConstant.NULL;
    /**
     * 请求取消标记
     */
    protected boolean cancelable;
    /**
     * http请求方式：POST/GET
     */
    private transient int method;
    /**
     * 响应数据
     */
    private transient T result;
    /**
     * 任务执行状态回调给客户端，包括完成、失败、超时、取消
     */
    private final transient Runnable completeTask = new Runnable() {

        @Override
        public void run() {
            synchronized (this) {
                switch (state) {
                    case PTFrameworkConstants.PTConstant.STATE_COMPLETE:
                        //任务完成
                        for (PTTaskListener<T> listener : listenerList) {
                            listener.onComplete(state, result);
                        }
                        break;
                    case PTFrameworkConstants.PTConstant.STATE_FAILED:
                        //任务出错
                        for (PTTaskListener<T> listener : listenerList) {
                            listener.onError();
                        }
                        break;
                    default:
                        for (PTTaskListener<T> listener : listenerList) {
                            listener.onError();
                        }
                        break;
                }
                handler.removeCallbacks(completeTask);
                PTTaskExecuteService.removeTask(taskId);
            }
        }
    };

    /**
     * 构造函数,默认不自动执行
     */
    public PTAbstractTask() {
        initialization(null, null);
    }

    /**
     * 构造函数
     *
     * @param listener 任务监听器
     */
    public PTAbstractTask(PTTaskListener<T> listener) {
        initialization(listener, null);
    }

    protected void initialization(PTTaskListener<T> listener, String taskGroupName) {
        if (listener != null) {
            addTaskListener(listener);
            PTTaskExecuteService.excute(this);
        }
    }

    /**
     * 增加监听器
     */
    public void addTaskListener(PTTaskListener<T> listener) {
        listenerList.add(listener);
    }

    /**
     * 获取任务ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 移除监听器
     */
    public void removeTaskListener(PTTaskListener<T> listener) {
        listenerList.remove(listener);
    }

    /**
     * 返回是否可取消
     *
     * @return 是否可取消
     */
    public boolean isCancelable() {
        return cancelable;
    }

    /**
     * 设置是否可取消
     *
     * @param cancelable 是否可取消
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * 清空监听器
     */
    public void clearTaskListener() {
        listenerList.clear();
    }

    /**
     * 重写方法
     */
    public final Void call() {
        if (state == PTFrameworkConstants.PTConstant.NULL) {
            handler.postDelayed(completeTask, PTHttpTaskConstants.TIMEOUT);
            //执行任务
            T result = doTask(method);
            if (result == null) {
                //结果为空表示任务执行失败
                state = PTFrameworkConstants.PTConstant.STATE_FAILED;
            } else {
                //结果不为空表示任务执行完成
                state = PTFrameworkConstants.PTConstant.STATE_COMPLETE;
            }
            this.result = result;
            handler.post(completeTask);
        }
        return null;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    /**
     * 执行请求实际任务的函数
     * @return 任务执行结果，为null表示任务未完成
     */
    protected abstract T doTask(int method);
}
