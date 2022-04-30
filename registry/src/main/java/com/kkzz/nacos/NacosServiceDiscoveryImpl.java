package com.kkzz.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kkzz.ServiceDiscovery;
import com.kkzz.enums.RpcErrorMessageEnum;
import com.kkzz.exception.RpcException;
import com.kkzz.extension.ExtensionLoader;
import com.kkzz.loadbalancer.LoadBalancer;
import com.kkzz.loadbalancer.RandomLoadBalance;
import com.kkzz.nacos.utils.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class NacosServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalancer loadBalancer=ExtensionLoader.getExtensionLoader(LoadBalancer.class).getExtension("loadBalance");

    public NacosServiceDiscoveryImpl() {

    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        Instance instance = null;
        try {
            List<Instance> instances = NacosUtils.getAllInstance(serviceName);
            if (instances.size() == 0) {
                log.error("找不到对应的服务:" + serviceName);
                throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
            }
            instance = loadBalancer.select(instances);
        } catch (NacosException e) {
            log.error("获取服务实例列表时发生错误");
        }
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }
}
