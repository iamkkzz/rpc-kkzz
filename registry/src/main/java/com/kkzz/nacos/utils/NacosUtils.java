package com.kkzz.nacos.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.kkzz.enums.RpcErrorMessageEnum;
import com.kkzz.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Slf4j
public class NacosUtils {
    private static final String REGISTRY_NAME = "nacos";
    private static final NamingService namingService;
    private static final Map<String, List<InetSocketAddress>> SERVICE_MAP = new ConcurrentHashMap<>();
    private static final String SERVICE_ADDRESS = "127.0.0.1:8848";

    static {
        namingService = getNamingService();
    }

    public static NamingService getNamingService() {
        try {
            return NamingFactory.createNamingService(SERVICE_ADDRESS);
        } catch (NacosException e) {
            throw new RpcException(RpcErrorMessageEnum.CLIENT_CONNECT_SERVER_FAILURE, REGISTRY_NAME);
        }
    }

    public static void registerService(String serviceName, InetSocketAddress inetSocketAddress) throws NacosException {
        namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        List<InetSocketAddress> addressList = SERVICE_MAP.get(serviceName);
        if (addressList==null) {
            addressList = new ArrayList<>();
        }
        addressList.add(inetSocketAddress);
        SERVICE_MAP.put(serviceName, addressList);
    }

    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    //关闭这个地址下的所有服务
    public static void clearRegistry(InetSocketAddress address) {
        //遍历所有的ip
        Set<String> deleteSet = new HashSet<>();
        SERVICE_MAP.forEach((serviceName, addressList) -> {
            if (addressList.contains(address)) {
                try {
                    namingService.deregisterInstance(serviceName, address.getHostName(), address.getPort());
                } catch (NacosException e) {
                    log.error("注销机器 {} 上的服务 {} 失败", address, serviceName, e);
                }
                addressList.remove(address);
                if (addressList.size() == 0) {
                    deleteSet.add(serviceName);
                }
            }
        });
        deleteSet.forEach(SERVICE_MAP::remove);
    }

}
