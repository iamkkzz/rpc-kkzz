package com.kkzz;

import com.kkzz.proxy.RpcClientProxy;
import com.kkzz.rpcInterface.HelloService;
import com.kkzz.to.HelloObject;
import com.kkzz.transport.RpcClient;
import com.kkzz.transport.socket.SocketRpcClient;

public class TestClient {
    public static void main(String[] args) {
        RpcClient client = new SocketRpcClient();
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(1, "这是消息");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
