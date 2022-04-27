package com.kkzz;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;
import java.util.List;

public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName);
}
