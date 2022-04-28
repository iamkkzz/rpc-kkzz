package com.kkzz.config;

import com.kkzz.nacos.utils.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 当服务提供方关闭时,将提供的服务全部下线
 */
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll(InetSocketAddress inetSocketAddress) {
        log.info("执行 shutdownHook 用以注销服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                NacosUtils.clearRegistry(inetSocketAddress);
                log.info("完成服务[{}]下所有服务的注销", inetSocketAddress.toString());
            } catch (Exception e) {
                log.error("注销全部服务失败");
            }

        }));
    }
}
