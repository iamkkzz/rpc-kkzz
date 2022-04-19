package com.kkzz;

import com.kkzz.annotation.ServiceScan;
import com.kkzz.rpcInterface.HelloService;
import com.kkzz.service.impl.HelloServiceImpl;
import com.kkzz.transport.netty.NettyRpcServer;

@ServiceScan
public class NettyTestServer {
    public static void main(String[] args){
        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 9090, 0);
        server.start();
    }
}
