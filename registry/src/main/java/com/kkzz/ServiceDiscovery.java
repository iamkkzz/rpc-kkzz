package com.kkzz;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kkzz.extension.Spi;

import java.net.InetSocketAddress;
import java.util.List;

@Spi
public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName);
}
