package com.kkzz;

import com.kkzz.config.RpcServiceConfig;
import com.kkzz.remoting.transport.netty.server.NettyRpcServer;
import com.kkzz.service.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;

import java.net.InetSocketAddress;

public class TcpNettyServer {
    public static void main(String[] args) {
        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 9001);
        HelloService helloService = new HelloServiceImpl();
        RpcServiceConfig serviceConfig = RpcServiceConfig.builder().group("test").version("version").service(helloService).build();
        server.registerService(serviceConfig);
        server.start();
    }
}
