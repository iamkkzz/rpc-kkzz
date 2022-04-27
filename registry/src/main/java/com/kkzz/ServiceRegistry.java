package com.kkzz;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
