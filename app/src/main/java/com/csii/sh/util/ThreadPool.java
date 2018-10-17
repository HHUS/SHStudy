package com.csii.sh.util;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * autor : sunhao
 * time  : 2018/06/15  15:23
 * desc  : 线程池管理
 */

public class ThreadPool {


    private static ThreadPool sInstance = new ThreadPool();

    private ExecutorService executorServiceImpl = null;


    private static class LocalThreadFactory implements ThreadFactory {


        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);


        private final static String NAME_PREFIX = "thread-session-thread-";


        LocalThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        /**
         * Constructs a new {@code Thread}.  Implementations may also initialize
         * priority, name, daemon status, {@code ThreadGroup}, etc.
         *
         * @param r A runnable to be executed by new thread instance
         * @return Constructed thread, or {@code null} if the request to
         * create a thread is rejected
         */
        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(this.group, r, NAME_PREFIX + this.threadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }

            if (thread.getPriority() != 5) {
                thread.setPriority(5);
            }

            return thread;
        }
    }


    /**
     * 默认一个core pool maximum number of threads is 6
     */
    private ThreadPool() {
        executorServiceImpl = new ThreadPoolExecutor(1, 6,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new LocalThreadFactory());
    }


    private boolean execute(Runnable task) {
        try {
            executorServiceImpl.execute(task);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    static boolean postTask(Runnable task) {
        return sInstance.execute(task);
    }
}
