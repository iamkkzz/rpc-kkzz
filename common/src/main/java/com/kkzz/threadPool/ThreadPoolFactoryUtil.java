package com.kkzz.threadPool;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 用于统一管理线程池
 */
@Slf4j
public class ThreadPoolFactoryUtil {
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadPoolName) {
        CustomThreadPoolConfig config = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(config, threadPoolName, false);
    }

    private static ExecutorService createCustomThreadPoolIfAbsent(CustomThreadPoolConfig config, String threadPoolName, boolean daemon) {
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadPoolName, v -> createThreadPool(config, threadPoolName, daemon));
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadPoolName);
            threadPool = createThreadPool(config, threadPoolName, daemon);
            THREAD_POOLS.put(threadPoolName, threadPool);
        }
        return threadPool;
    }

    private static ExecutorService createThreadPool(CustomThreadPoolConfig config, String threadPoolName, boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadPoolName, daemon);
        return new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaximumPoolSize(), config.getKeepAliveTime(),
                config.getUnit(), config.getWorkQueue(), threadFactory);
    }

    public static ThreadFactory createThreadFactory(String threadPoolName, Boolean daemon) {
        if (threadPoolName != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder().setNamePrefix(threadPoolName + "-%d").setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder().setNamePrefix(threadPoolName + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    public static void shutDownAllThreadPool() {
        log.info("调用 shutDownAllThreadPool 方法");
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            try {
                executorService.shutdown();
                log.info("关闭了线程池 [{}] [{}]", entry.getKey(), entry.getValue());
                //等待线程池中的线程执行完毕
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("线程池中仍然有线程未执行完毕,强行关闭线程池!");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                e.printStackTrace();
            }
        });

    }
}
