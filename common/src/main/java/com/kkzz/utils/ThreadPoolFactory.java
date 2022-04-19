package com.kkzz.utils;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

public class ThreadPoolFactory {
    private static final int corePoolSize = 5;
    private static final int maximumPoolSize = 20;
    private static final long keepAliveTime = 10;
    private static final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);

    public static ThreadPoolExecutor createDefaultThreadPool(String name) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue, new DefaultThreadFactory(name));
    }
}
