package com.kkzz;

import com.kkzz.service.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;
import com.kkzz.transport.socket.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9090);
    }
}
