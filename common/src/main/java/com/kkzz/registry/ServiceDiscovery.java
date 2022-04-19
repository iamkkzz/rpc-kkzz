package com.kkzz.registry;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    /**
     * 从注册中获取服务的ip地址
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
