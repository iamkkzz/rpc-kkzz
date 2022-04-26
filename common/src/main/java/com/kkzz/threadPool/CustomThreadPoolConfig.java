package com.kkzz.threadPool;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.*;

@Setter
@Getter
public class CustomThreadPoolConfig {
    /**
     * 默认参数
     */
    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    /**
     * 可配置参数
     */
    private int corePoolSize=DEFAULT_CORE_POOL_SIZE;
    private int maximumPoolSize=DEFAULT_MAXIMUM_POOL_SIZE;
    private long keepAliveTime=DEFAULT_KEEP_ALIVE_TIME;
    private TimeUnit unit=DEFAULT_TIME_UNIT;
    private BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
}
