package com.kkzz.registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    /**
     * 将服务注册至注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 从注册中获取服务的ip地址
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
