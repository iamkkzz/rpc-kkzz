package com.kkzz;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kkzz.extension.Spi;

import java.net.InetSocketAddress;

@Spi
public interface ServiceRegistry {
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
