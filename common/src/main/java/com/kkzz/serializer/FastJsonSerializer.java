package com.kkzz.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kkzz.dto.RpcRequest;
import com.kkzz.dto.RpcResponse;
import com.kkzz.enums.SerializerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FastJsonSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(FastJsonSerializer.class);

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        Object o = JSON.parseObject(bytes, clazz);
        if (o instanceof RpcRequest) {
            try {
                o = handleRequest(o);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    private Object handleRequest(Object obj) throws IOException {
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

    @Override
    public int getCode() {
        return SerializerCode.valueOf("FASTJSON").getCode();
    }
}
