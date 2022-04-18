package com.kkzz;

import com.kkzz.registry.DefaultServiceRegistry;

import com.kkzz.rpcInterface.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;
import com.kkzz.transport.socket.SocketRpcServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        SocketRpcServer socketRpcServer = new SocketRpcServer(registry);
        socketRpcServer.start(9090);
    }
}
