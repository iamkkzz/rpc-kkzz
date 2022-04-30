package com.kkzz.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kkzz.extension.Spi;

import java.util.List;

@Spi
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
