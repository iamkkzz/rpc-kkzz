package com.kkzz.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kkzz.remoting.dto.RpcRequest;
import com.kkzz.serialize.Serializer;

import java.io.IOException;

public class FastJsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Object object = JSON.parseObject(bytes, clazz);
        return (T) handleRequest(object);
    }

    private Object handleRequest(Object obj){
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = JSON.toJSONBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = JSON.parseObject(bytes, clazz);
            }
        }
        return rpcRequest;
    }

}
