package com.kkzz.provider.impl;

import com.kkzz.ServiceRegistry;
import com.kkzz.config.RpcServiceConfig;
import com.kkzz.enums.RpcErrorMessageEnum;
import com.kkzz.exception.RpcException;
import com.kkzz.factory.SingletonFactory;
import com.kkzz.nacos.NacosServiceRegistryImpl;
import com.kkzz.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NacosServiceProviderImpl implements ServiceProvider {
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public NacosServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        //todo 使用ExtensionLoader
        serviceRegistry = SingletonFactory.getInstance(NacosServiceRegistryImpl.class);
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("添加服务:{} 和接口: {} 成功", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(InetSocketAddress inetSocketAddress, RpcServiceConfig rpcServiceConfig) {
        this.addService(rpcServiceConfig);
        serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), inetSocketAddress);
    }
}
