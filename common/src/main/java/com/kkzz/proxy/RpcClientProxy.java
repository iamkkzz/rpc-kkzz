package com.kkzz.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class RpcClientProxy implements InvocationHandler {

    public RpcClientProxy() {

    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("执行方法:{}", method.getName());
        //todo 判断是哪种请求,判断使用哪种nettyClient发送消息

        //返回结果
        return null;
    }
}
