package com.kkzz;


import com.kkzz.config.RpcServiceConfig;
import com.kkzz.proxy.jdk.RpcClientProxy;
import com.kkzz.remoting.transport.netty.client.NettyRpcClient;
import com.kkzz.service.HelloService;
import com.kkzz.to.GreetTo;


public class TcpNettyClient {
    public static void main(String[] args) {
        NettyRpcClient client = new NettyRpcClient();
        RpcServiceConfig serviceConfig = RpcServiceConfig.builder().group("test").version("version").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client,serviceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String res = helloService.hello(new GreetTo("你好", "2001"));
        System.out.println(res);
    }
}
