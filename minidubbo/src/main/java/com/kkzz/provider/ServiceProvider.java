package com.kkzz.provider;

import com.kkzz.config.RpcServiceConfig;

import java.net.InetSocketAddress;

public interface ServiceProvider {

    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    void publishService(InetSocketAddress inetSocketAddress,RpcServiceConfig rpcServiceConfig);
}
