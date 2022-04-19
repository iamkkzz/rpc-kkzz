package com.kkzz.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kkzz.enums.RpcErrorMessageEnum;
import com.kkzz.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceRegistry implements ServiceRegistry{
    private static final Logger logger= LoggerFactory.getLogger(NacosServiceRegistry.class);
    private static final String SERVER_ADDRESS="127.0.0.1:8848";
    private static final NamingService  namingService;
    static {
        try {
            namingService= NamingFactory.createNamingService(SERVER_ADDRESS);
        }catch (NacosException e){
            logger.error("连接到Nacos时出错:",e);
            throw new RpcException(RpcErrorMessageEnum.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName,inetSocketAddress.getHostName(),inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:",e);
            throw new RpcException(RpcErrorMessageEnum.REGISTER_SERVICE_FAILED);
        }
    }
}
