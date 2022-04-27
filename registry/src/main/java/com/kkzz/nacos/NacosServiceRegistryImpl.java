package com.kkzz.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kkzz.ServiceRegistry;
import com.kkzz.enums.RpcErrorMessageEnum;
import com.kkzz.exception.RpcException;
import com.kkzz.nacos.utils.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NacosServiceRegistryImpl implements ServiceRegistry {

    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtils.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务 {} 时有错误发生", serviceName, e);
            throw new RpcException(RpcErrorMessageEnum.SERVICE_REGISTER_FAILURE);
        }
    }
}
