package com.kkzz.proxy.jdk;

import cn.hutool.core.util.IdUtil;
import com.kkzz.config.RpcServiceConfig;
import com.kkzz.enums.RpcErrorMessageEnum;
import com.kkzz.enums.RpcResponseCodeEnum;
import com.kkzz.exception.RpcException;
import com.kkzz.remoting.dto.RpcRequest;
import com.kkzz.remoting.dto.RpcResponse;
import com.kkzz.remoting.transport.RpcRequestTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private final RpcRequestTransport rpcRequestTransport;
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        rpcServiceConfig = new RpcServiceConfig();
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        log.info("代理对象调用方法:[{}]",method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(IdUtil.randomUUID())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        RpcResponse<Object> rpcResponse=null;
        //todo 这里可以进行优化
        CompletableFuture<RpcResponse<Object>> future= (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRequest(rpcRequest);
        rpcResponse=future.get();
        check(rpcRequest,rpcResponse);
        return rpcResponse.getData();
    }

    private void check(RpcRequest rpcRequest, RpcResponse<Object> rpcResponse) {
        if (rpcResponse==null){

        }
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){

        }
        if (rpcResponse.getCode()==null||!rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())){

        }
    }
}
