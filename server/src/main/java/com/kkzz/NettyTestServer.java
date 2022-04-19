package com.kkzz;

import com.kkzz.rpcInterface.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;
import com.kkzz.transport.netty.NettyRpcServer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 9090);
        server.publishService(helloService,HelloService.class);
        server.start();
    }
}
