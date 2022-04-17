package com.kkzz;

import com.kkzz.proxy.RpcClientProxy;
import com.kkzz.service.rpcInterface.HelloService;
import com.kkzz.to.HelloObject;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9090);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(1, "这是消息");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
