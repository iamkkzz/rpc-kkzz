package com.kkzz.hook;

import com.kkzz.utils.NacosUtils;
import com.kkzz.utils.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class ShutDownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutDownHook.class);

    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");

    private static final ShutDownHook shutDownHook = new ShutDownHook();

    public static ShutDownHook getShutDownHook() {
        return shutDownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtils.clearRegistry();
            threadPool.shutdown();
        }));
    }
}
