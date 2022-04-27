package com.kkzz.remoting.transport.netty.server;

import com.kkzz.config.CustomShutdownHook;
import com.kkzz.config.RpcServiceConfig;
import com.kkzz.factory.SingletonFactory;
import com.kkzz.provider.ServiceProvider;
import com.kkzz.provider.impl.NacosServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyRpcServer {
    private final String host;
    private final int port;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(NacosServiceProviderImpl.class);

    public NettyRpcServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(new InetSocketAddress(host, port), rpcServiceConfig);
    }

    public void  start(){
        CustomShutdownHook.getCustomShutdownHook().clearAll(new InetSocketAddress(host,port));

    }
}
