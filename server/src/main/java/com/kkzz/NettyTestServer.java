package com.kkzz;

import com.kkzz.registry.DefaultServiceRegistry;
import com.kkzz.rpcInterface.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;
import com.kkzz.transport.netty.NettyRpcServer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyRpcServer server = new NettyRpcServer(registry);
        server.start(9090);
    }
}
