package com.kkzz;

import com.kkzz.proxy.RpcClientProxy;
import com.kkzz.service.HelloService;
import com.kkzz.to.GreetTo;

public class TcpNettyClient {
    public static void main(String[] args) {
        HelloService helloService = RpcClientProxy.getProxy(HelloService.class);
        String res = helloService.hello(new GreetTo("你好", "2001"));
        System.out.println(res);
    }
}
