package com.kkzz;

import com.kkzz.proxy.RpcClientProxy;
import com.kkzz.rpcInterface.HelloService;
import com.kkzz.to.HelloObject;
import com.kkzz.transport.RpcClient;
import com.kkzz.transport.netty.NettyRpcClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyRpcClient("127.0.0.1", 9090);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String res = helloService.hello(new HelloObject(11, "我是消息"));
        System.out.println(res);
    }
}
